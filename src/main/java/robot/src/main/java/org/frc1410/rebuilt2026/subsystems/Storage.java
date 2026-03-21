package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.BELT_MOTOR;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TRANSFER_MOTOR;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INDEXER_D;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INDEXER_I;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.INDEXER_P;

public class Storage implements TickedSubsystem {

    public enum StorageStates {
        INTAKE,
        NEUTRAL,
        OUTTAKE
    }

    private final SparkMax indexerMotor1;
    private final SparkMax indexerMotor2;

    private final TalonFX transferMotor1;
    private final TalonFX transferMotor2;

    private double targetIndexerSpeed = 0;
    private double indexerSpeed = 0;
    private double transferSpeed = 0;

    private PIDController indexerController;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault()
            .getTable("Storage");

    private final DoublePublisher speedPub =  NetworkTables.PublisherFactory(networkTable,
            "Speed",
            targetIndexerSpeed
    );

    public Storage() {
        this.targetIndexerSpeed = 0;
        this.transferSpeed = 0;
        this.indexerMotor1 = new SparkMax(
                BELT_MOTOR,
                SparkLowLevel.MotorType.kBrushless
        );
        this.indexerMotor2 = new SparkMax(
                BELT_MOTOR,
                SparkLowLevel.MotorType.kBrushless
        );
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(true);
        this.indexerMotor1.configure(
                config,
                com.revrobotics.ResetMode.kNoResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters
        );
        this.indexerMotor2.configure(
                config,
                com.revrobotics.ResetMode.kNoResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters
        );
        this.transferMotor1 = new TalonFX(TRANSFER_MOTOR);
        

        this.indexerController = new PIDController(INDEXER_P, INDEXER_I, INDEXER_D);
    }

    public void setBeltSpeed(double speed) {
        this.targetIndexerSpeed = speed;
    }

    public void setTransferSpeed(double speed) {
        this.transferSpeed = speed;
    }
    public void setSpeedState(StorageStates storageState) {
        switch (storageState) {
            case INTAKE -> this.targetIndexerSpeed = 0.5;
            case NEUTRAL -> this.targetIndexerSpeed = 0;
            case OUTTAKE -> this.targetIndexerSpeed = -0.25;
        }
    }

    public double calcIndexerSpeed(double setpoint) {
        return indexerController.calculate((this.indexerMotor1.get() + this.indexerMotor2.get())/2, setpoint);
    }

    @Override
    public void periodic() {
        this.indexerSpeed += calcIndexerSpeed(this.targetIndexerSpeed);
        if (Math.abs(this.indexerSpeed) > 1) {
            if (this.indexerSpeed > 0) {this.indexerSpeed = 1;}
            else if (this.indexerSpeed < 0) {this.indexerSpeed = -1;}
            else {System.err.println("According to all known laws of aviation, there is no way a bee should be able to fly. It's wings are too small to get it's fat little body off the ground. The bee, of course, flies anyway because bees don't care what humans think is impossible.");} //TODO: Finish
        }
    }
    @Override
    public void telem(){
        this.indexerMotor1.set(this.indexerSpeed);
        this.indexerMotor2.set(this.indexerSpeed);
        this.speedPub.set(this.targetIndexerSpeed);
        this.transferMotor.set(transferSpeed);
    }
}
