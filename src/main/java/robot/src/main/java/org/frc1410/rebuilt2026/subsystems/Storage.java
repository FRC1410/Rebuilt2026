package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BELT_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TRANSFER_MOTOR;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Storage implements TickedSubsystem {

    public enum StorageStates {
        INTAKE,
        NEUTRAL,
        OUTTAKE
    }

    private final SparkMax beltMotor;

    private final SparkMax transferMotor;

    private double beltSpeed = 0;
    private double transferSpeed = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault()
            .getTable("Storage");

    private final DoublePublisher speedPub =  NetworkTables.PublisherFactory(
            networkTable,
            "Speed",
            beltSpeed
    );

    public Storage() {
        this.beltSpeed = 0;
        this.transferSpeed = 0;
        this.beltMotor = new SparkMax(
                BELT_MOTOR,
                SparkLowLevel.MotorType.kBrushless
        );
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(true);
        this.beltMotor.configure(
                config,
                com.revrobotics.ResetMode.kNoResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters
        );
        this.transferMotor = new SparkMax(
                TRANSFER_MOTOR,
                SparkLowLevel.MotorType.kBrushless
        );
        this.transferMotor.configure(
                config,
                com.revrobotics.ResetMode.kNoResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters
        );
    }

    public void setBeltSpeed(double speed) {
        this.beltSpeed = speed;
    }

    public void setTransferSpeed(double speed) {
        this.transferSpeed = speed;
    }
    public void setSpeedState(StorageStates storageState) {
        switch (storageState) {
            case INTAKE -> this.beltSpeed = 1;
            case NEUTRAL -> this.beltSpeed = 0;
            case OUTTAKE -> this.beltSpeed = -0.5;
        }
    }

    @Override
    public void periodic() {
        this.beltMotor.set(beltSpeed);
        this.speedPub.set(this.beltSpeed);
        this.transferMotor.set(transferSpeed);
    }
}
