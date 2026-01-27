package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.TEST_FLEX;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class TestSparkFlex implements TickedSubsystem{

    public enum TestStates {
        FWRD,
        NTRL,
        BACK
    }
    
    private final SparkFlex testFlex;

    private double currentSpeed = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Test Spark Flex");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Motor Power", currentSpeed);


    public TestSparkFlex() {
        this.testFlex = new SparkFlex(TEST_FLEX, SparkLowLevel.MotorType.kBrushless);
        SparkFlexConfig testMotorConfig = new SparkFlexConfig();
        testMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        testMotorConfig.smartCurrentLimit(30);

        this.testFlex.configure(testMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

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
        this.testFlex.set(currentSpeed);
        this.currentSpeedPublisher.set(currentSpeed);
    }
}
