package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;

import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BELT_MOTOR;

public class Storage implements TickedSubsystem {

    public enum StorageStates {
        INTAKE,
        NEUTRAL,
        OUTTAKE
    }

    private final SparkMax beltMotor;

    private double speed = 0;

    public Storage() {
        this.beltMotor = new SparkMax(BELT_MOTOR, SparkLowLevel.MotorType.kBrushless);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSpeedState(StorageStates storageState) {
        switch (storageState) {
            case INTAKE:
                this.speed = 1;
            case NEUTRAL:
                this.speed = 0;
            case OUTTAKE:
                this.speed = -0.5;
        }
    }

    @Override
    public void periodic() {
        this.beltMotor.set(speed);
    }
}
