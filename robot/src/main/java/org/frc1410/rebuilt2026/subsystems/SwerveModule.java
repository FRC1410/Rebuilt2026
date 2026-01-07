package org.frc1410.reefscape2025.subsystems;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.jni.OrchestraJNI;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.pathplanner.lib.util.DriveFeedforwards;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.units.*;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

import java.io.IOException;
import java.util.Random;

import static org.frc1410.reefscape2025.util.Constants.*;
import static org.frc1410.reefscape2025.util.Tuning.*;

public class SwerveModule implements TickedSubsystem {
    private final TalonFX driveMotor;
    private final SparkMax steerMotor;

    private final CANcoder steerEncoder;

    public SwerveModuleState desiredState = new SwerveModuleState();

    private final PIDController steerPIDController = new PIDController(
            SWERVE_STEER_P,
            SWERVE_STEER_I,
            SWERVE_STEER_D
    );

    private final DoublePublisher desiredVelocity;
    private final DoublePublisher desiredAngle;

    private final DoublePublisher actualVelocity;
    private final DoublePublisher actualAngle;

    private final Orchestra orchestra;

    public SwerveModule(
            int driveMotorID,
            int steerMotorID,
            int steerEncoderID,
            boolean driveInverted,
            boolean steerInverted,
            Measure<AngleUnit> angleOffset,
            DoublePublisher desiredVelocity,
            DoublePublisher desiredAngle,
            DoublePublisher actualVelocity,
            DoublePublisher actualAngle
    ) {

        // Drive config
        this.driveMotor = new TalonFX(driveMotorID, "CTRE");
        var driveMotorConfig = new TalonFXConfiguration();

        driveMotorConfig.Slot0.kS = DRIVE_KS;
        driveMotorConfig.Slot0.kV = DRIVE_KV;

        driveMotorConfig.Slot0.kP = SWERVE_DRIVE_P;
        driveMotorConfig.Slot0.kI = SWERVE_DRIVE_I;
        driveMotorConfig.Slot0.kD = SWERVE_DRIVE_D;

        driveMotorConfig.CurrentLimits.SupplyCurrentLimit = DRIVE_MOTOR_CURRENT_LIMIT;
        driveMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        driveMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake; 
        driveMotorConfig.MotorOutput.Inverted =
                driveInverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;

        this.driveMotor.getConfigurator().apply(driveMotorConfig);

        // Steer config
        var sparkConfig = new SparkMaxConfig();

        sparkConfig.smartCurrentLimit(STEER_MOTOR_CURRENT_LIMIT);
        sparkConfig.idleMode(SparkBaseConfig.IdleMode.kBrake); 
        sparkConfig.inverted(steerInverted);

        this.steerMotor = new SparkMax(steerMotorID, MotorType.kBrushless);
        this.steerMotor.configure(
                sparkConfig,
                SparkBase.ResetMode.kResetSafeParameters,
                SparkBase.PersistMode.kNoPersistParameters
        );

        // Steer encoder config
        this.steerEncoder = new CANcoder(steerEncoderID, "CTRE");
        var configurator = this.steerEncoder.getConfigurator();

        var steerEncoderConfig = new CANcoderConfiguration();

        steerEncoderConfig.MagnetSensor.MagnetOffset = angleOffset.unaryMinus().in(Units.Rotation);
        steerEncoderConfig.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 0.5;

        configurator.apply(steerEncoderConfig);

        this.steerPIDController.enableContinuousInput(-Math.PI, Math.PI);

        // Networktables stuff
        this.desiredVelocity = desiredVelocity;
        this.desiredAngle = desiredAngle;

        this.actualVelocity = actualVelocity;
        this.actualAngle = actualAngle;

        this.orchestra = new Orchestra();
        this.orchestra.addInstrument(driveMotor);
    }

    public void randomMusic() {
//        Random rand = new Random();
//        var randomNum = rand.nextInt(3);
        StatusCode result = this.orchestra.loadMusic("Fox.chrp");
        if(!result.isOK()) {
            System.out.println("Error loading orchestra music: " + result);
        } else {
            System.out.println("playing");
            this.orchestra.play();
        }

//        switch (randomNum) {
//            case 0 -> result = this.orchestra.loadMusic("Rasputin.chirp");
//            case 1 -> result = this.orchestra.loadMusic("Baby.chirp");
//            case 2 -> result = this.orchestra.loadMusic("Fox.chirp");
//            case 3 -> result = this.orchestra.loadMusic("MURICA.chirp");
//        }
    }

    private Rotation2d getSteerPosition() {
        return Rotation2d.fromRotations(this.steerEncoder.getAbsolutePosition().getValue().magnitude());
    }

    public void setDesiredState(SwerveModuleState desiredState) {

        desiredState.optimize(this.getSteerPosition());
        this.desiredState = desiredState;

        var request = new VelocityVoltage(
                SwerveModule.moduleVelocityToMotorAngularVelocity(
                        Units.MetersPerSecond.of(desiredState.speedMetersPerSecond)
                ).in(Units.RotationsPerSecond)
        );

        this.driveMotor.setControl(request);
    }

    public void drive(Measure<VoltageUnit> voltage) {
        this.desiredState.angle = new Rotation2d();
        this.driveMotor.setVoltage(voltage.in(Units.Volt));
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(this.getDriveVelocity(), this.getSteerPosition());
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(this.getDrivePosition(), this.getSteerPosition());
    }

    public AngularVelocity getAngularVelocity() {
        return Units.RotationsPerSecond.of(this.driveMotor.getVelocity().getValue().magnitude());
    }

    private static AngularVelocity moduleVelocityToMotorAngularVelocity(Measure<LinearVelocityUnit> linearVelocity) {
        return Units.RotationsPerSecond.of(linearVelocity.in(Units.MetersPerSecond) / WHEEL_CIRCUMFERENCE.in(Units.Meters) * DRIVE_GEAR_RATIO);
    }

    private LinearVelocity getDriveVelocity() {
        return Units.MetersPerSecond.of(this.driveMotor.getVelocity().getValue().magnitude() * WHEEL_CIRCUMFERENCE.in(Units.Meters)).div(DRIVE_GEAR_RATIO);
    }

    private Distance getDrivePosition() {
        return Units.Meters.of(this.driveMotor.getPosition().getValue().magnitude() * WHEEL_CIRCUMFERENCE.in(Units.Meters)).div(DRIVE_GEAR_RATIO);
    }

    @Override
    public void periodic() {
        double steerPIDOutput = this.steerPIDController.calculate(
                this.getSteerPosition().getRadians(),
                MathUtil.angleModulus(this.desiredState.angle.getRadians())
        );

        this.steerMotor.setVoltage(steerPIDOutput);

        this.desiredVelocity.set(this.desiredState.speedMetersPerSecond);
        this.desiredAngle.set(this.desiredState.angle.getDegrees());

        this.actualVelocity.set(this.getDriveVelocity().in(Units.MetersPerSecond));
        this.actualAngle.set(this.getSteerPosition().getDegrees());
    }
}