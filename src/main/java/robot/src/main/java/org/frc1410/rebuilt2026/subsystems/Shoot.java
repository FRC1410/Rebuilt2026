package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_HIGH_LEFT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_LOW_LEFT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOOD_LOW_RIGHT_SETPOINT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.HOOD_ACTUATOR;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Shoot implements TickedSubsystem {

    public enum HoodStates {
        LOW_RIGHT,
        HIGH_LEFT,
        LOW_LEFT
    }

    // private final SparkMax shooterMotor;
    private final LinearServo hoodActuator;

    private double currentTick = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Shooter Spark");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Shooter Level", currentTick);

    public Shoot() {
        // this.shooterMotor = new SparkMax(SHOOTER_SPARK, SparkLowLevel.MotorType.kBrushless);
        // SparkMaxConfig shooterMotorConfig = new SparkMaxConfig();
        // shooterMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        // shooterMotorConfig.smartCurrentLimit(30);

        this.hoodActuator = new LinearServo(HOOD_ACTUATOR, 1, 1);
        // this.shooterMotor.configure(
        //     shooterMotorConfig, 
        //     com.revrobotics.ResetMode.kNoResetSafeParameters, 
        //     com.revrobotics.PersistMode.kPersistParameters
        // );
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
        // this.shooterMotor.set(currentTick);
        this.hoodActuator.updateCurPos();
        this.currentSpeedPublisher.set(currentTick);
    }
}
