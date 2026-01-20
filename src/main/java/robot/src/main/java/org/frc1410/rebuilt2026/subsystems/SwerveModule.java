package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.*;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.DoublePublisher;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volt;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.units.measure.Voltage;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.DRIVE_GEAR_RATIO;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.DRIVE_MOTOR_CURRENT_LIMIT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.STEER_MOTOR_CURRENT_LIMIT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.WHEEL_CIRCUMFERENCE;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.DRIVE_KS;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.DRIVE_KV;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_DRIVE_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_DRIVE_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_DRIVE_P;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_STEER_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_STEER_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.SWERVE_STEER_P;

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
            Angle angleOffset,
            DoublePublisher desiredVelocity,
            DoublePublisher desiredAngle,
            DoublePublisher actualVelocity,
            DoublePublisher actualAngle
    ) {

        // Drive config
        this.driveMotor = new TalonFX(driveMotorID, CAN_BUS_NAME);
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
        this.steerMotor.configure(sparkConfig, com.revrobotics.ResetMode.kResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

        // Steer encoder config
        this.steerEncoder = new CANcoder(steerEncoderID, CAN_BUS_NAME);
        var configurator = this.steerEncoder.getConfigurator();

        var steerEncoderConfig = new CANcoderConfiguration();

        steerEncoderConfig.MagnetSensor.MagnetOffset = angleOffset.unaryMinus().in(Rotation);
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
        return Rotation2d.fromRotations(this.steerEncoder.getAbsolutePosition().getValue().in(Rotations));
    }

    public void setDesiredState(SwerveModuleState desiredState) {

        desiredState.optimize(this.getSteerPosition());
        this.desiredState = desiredState;

        var request = new VelocityVoltage(
                SwerveModule.moduleVelocityToMotorAngularVelocity(
                        MetersPerSecond.of(desiredState.speedMetersPerSecond)
                ).in(RotationsPerSecond)
        );

        this.driveMotor.setControl(request);
    }

    public void drive(Voltage voltage) {
        this.desiredState.angle = new Rotation2d();
        this.driveMotor.setVoltage(voltage.in(Volt));
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(this.getDriveVelocity(), this.getSteerPosition());
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(this.getDrivePosition(), this.getSteerPosition());
    }

    public AngularVelocity getAngularVelocity() {
        return RotationsPerSecond.of(this.driveMotor.getVelocity().getValue().in(RotationsPerSecond));
    }

    private static AngularVelocity moduleVelocityToMotorAngularVelocity(LinearVelocity linearVelocity) {
        return RotationsPerSecond.of(linearVelocity.in(MetersPerSecond) / WHEEL_CIRCUMFERENCE.in(Meters) * DRIVE_GEAR_RATIO);
    }

    private LinearVelocity getDriveVelocity() {
        return MetersPerSecond.of(this.driveMotor.getVelocity().getValue().in(RotationsPerSecond) * WHEEL_CIRCUMFERENCE.in(Meters) / DRIVE_GEAR_RATIO);
    }

    private Distance getDrivePosition() {
        return Meters.of(this.driveMotor.getPosition().getValue().in(Rotations) * WHEEL_CIRCUMFERENCE.in(Meters) / DRIVE_GEAR_RATIO);
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

        this.actualVelocity.set(this.getDriveVelocity().in(MetersPerSecond));
        this.actualAngle.set(this.getSteerPosition().getDegrees());
    }
}