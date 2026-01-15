package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.*;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Shoot implements TickedSubsystem{
    private final SparkMax shooterMotor;

    private double currentTick = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Shooter Spark");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Shooter Level", currentTick);
    
    public Shoot() {
        this.shooterMotor = new SparkMax(SHOOTER_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig shooterMotorConfig = new SparkMaxConfig();
        shooterMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        shooterMotorConfig.smartCurrentLimit(30);

        this.shooterMotor.configure(shooterMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
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

        public void tickUp(int multiplier){
            currentTick += 0.1 * multiplier;

            if (currentTick > 1) {
                currentTick = 1;
            } else if (currentTick < -1) {
                currentTick = -1;
            }
        }

        public void tickDown(int multiplier){
            currentTick -= 0.1 * multiplier;

            if (currentTick > 1) {
                currentTick = 1;
            } else if (currentTick < -1) {
                currentTick = -1;
            }
        }

    
    
    public void periodic() {
        this.shooterMotor.set(currentTick);
        this.currentSpeedPublisher.set(currentTick);
    }
}



