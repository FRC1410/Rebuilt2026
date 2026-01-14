package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.INTAKE_SPARK;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Intake implements TickedSubsystem{
    private final SparkMax intakeMotor;

    private double currentSpeed = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Intake Spark");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Motor Power", currentSpeed);
    
    public Intake() {
        this.intakeMotor = new SparkMax(INTAKE_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
        intakeMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        intakeMotorConfig.smartCurrentLimit(30);

        this.intakeMotor.configure(intakeMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
    }
     public void setSpeed(double speed) {
        currentSpeed = speed;
    }
    
    
    public void periodic() {
        this.intakeMotor.set(currentSpeed);
        this.currentSpeedPublisher.set(currentSpeed);
    }
}



