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

    public enum TestStates {
        FWRD,
        NTRL,
        BACK
    }
    
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

    public void setState(TestStates state) {
        switch(state) {
            case FWRD -> currentSpeed = 1;
            case NTRL -> currentSpeed = 0;
            case BACK -> currentSpeed = -0.5;
        }
    }

    public void setSpeed(Double speed) {
        this.currentSpeed = speed;
    }

    @Override
    public void periodic() {
        this.testMotor.set(currentSpeed);
        this.currentSpeedPublisher.set(currentSpeed);
    }
}
