package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TEST_SPARK;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class TestSpark implements TickedSubsystem{
    private final SparkMax testMotor;

    private double currentSpeed = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Test Spark");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Motor Power", currentSpeed);


    public TestSpark() {
        this.testMotor = new SparkMax(TEST_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig testMotorConfig = new SparkMaxConfig();
        testMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        testMotorConfig.smartCurrentLimit(30);

        this.testMotor.configure(testMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
    }

    public void toggle() {
        if (currentSpeed == 0) {
            currentSpeed = 1;
        } else {
            currentSpeed = 0;
        }
    }

    @Override
    public void periodic() {
        this.testMotor.set(currentSpeed);
        this.currentSpeedPublisher.set(currentSpeed);
    }
}
