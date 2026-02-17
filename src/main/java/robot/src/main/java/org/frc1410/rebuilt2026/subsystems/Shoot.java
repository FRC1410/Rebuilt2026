package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.DRIVE_MOTOR_CURRENT_LIMIT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_HIGH_LEFT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_LOW_LEFT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_LOW_RIGHT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.HOOD_ACTUATOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.SERVO_HUB;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.SHOOTER_KRAKEN;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig;

import com.revrobotics.servohub.ServoHub;
public class Shoot implements TickedSubsystem {

    public enum HoodStates {
        LOW_RIGHT,
        HIGH_LEFT,
        LOW_LEFT
    }

    private final TalonFX shooterMotor;
    // Initialize the servo hub
    ServoHub m_servoHub = new ServoHub(SERVO_HUB);

    // Obtain a servo channel controller
    private final LinearServo hoodActuator;

    private double currentTick = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Shooter Spark");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Shooter Level", currentTick);
    private final DoublePublisher hoodPosPublisher = NetworkTables.PublisherFactory(networkTable, "Hood Pose", 0);

    public Shoot() {
        this.shooterMotor = new TalonFX(SHOOTER_KRAKEN, CANBus.roboRIO());
        TalonFXConfiguration shooterMotorConfig = new TalonFXConfiguration();
        shooterMotorConfig.CurrentLimits.SupplyCurrentLimit = 40;
        shooterMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;

        shooterMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        shooterMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        this.shooterMotor.getConfigurator().apply(shooterMotorConfig);

        
        this.m_servoHub = new ServoHub(SERVO_HUB);

        this.hoodActuator = new LinearServo(SERVO_HUB, HOOD_ACTUATOR, 1, 1);
    }

    public void setSpeed(boolean up) {
        if (up) {
            currentTick += 0.1;
        } else {
            currentTick -= 0.1;
        }

        if (currentTick > 1) {
            currentTick = 1;
        } else if (currentTick < 0) {
            currentTick = 0;
        }
    }

    public void tickUp(int multiplier) {
        currentTick += 0.1 * multiplier;

        if (currentTick > 1) {
            currentTick = 1;
        } else if (currentTick < -1) {
            currentTick = -1;
        }
    }

    public void tickDown(int multiplier) {
        currentTick -= 0.1 * multiplier;

        if (currentTick > 1) {
            currentTick = 1;
        } else if (currentTick < -1) {
            currentTick = -1;
        }
    }

    public void setHoodPos(HoodStates hoodState) {
        switch (hoodState) {
            case LOW_LEFT ->
                this.hoodActuator.setPosition(HOOD_LOW_LEFT_SETPOINT);
            case LOW_RIGHT ->
                this.hoodActuator.setPosition(HOOD_LOW_RIGHT_SETPOINT);
            case HIGH_LEFT ->
                this.hoodActuator.setPosition(HOOD_HIGH_LEFT_SETPOINT);

        }
    }

    public double getHoodPos() {
        return this.hoodActuator.getPosition();
    }

    public double getHoodSetPos() {
        return this.hoodActuator.getSetPos();
    }

    @Override
    public void periodic() {
        this.shooterMotor.set(currentTick);
        this.shooterMotor.set(currentTick);
        this.hoodActuator.updateCurPos();
        this.hoodPosPublisher.set(this.hoodActuator.getPosition());
        this.currentSpeedPublisher.set(currentTick);
    }
}
