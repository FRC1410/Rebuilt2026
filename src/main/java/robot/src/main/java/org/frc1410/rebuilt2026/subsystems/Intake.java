package robot.src.main.java.org.frc1410.rebuilt2026.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.INTAKE_FRAME_SPARK_LEFT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.INTAKE_FRAME_SPARK_RIGHT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.INTAKE_SPARK;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.INTAKE_TEST_SPARK;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Intake implements TickedSubsystem {

    private final SparkMax intakeMotor;
    private final SparkMax intakeFrameLeft;
    private final SparkMax intakeFrameRight;
    private final SparkMax intakeTestMotor;
    
    private final RelativeEncoder frameLeftEncoder;
    private final RelativeEncoder frameRightEncoder;

    private double currentSpeed = 0;
    private double currentFrameSpeedLeft = 0;
    private double currentFrameSpeedRight = 0;
    private double currentTestSpeed = 0;

    private final NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("Intake Sparks");

    private final DoublePublisher currentSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Intake Power", currentSpeed);
    private final DoublePublisher currentFrameLeftPublisher = NetworkTables.PublisherFactory(networkTable, "Frame Left Power", currentFrameSpeedLeft);
    private final DoublePublisher currentFrameRightPublisher = NetworkTables.PublisherFactory(networkTable, "Frame Right Power", currentFrameSpeedRight);
    private final DoublePublisher currentTestSpeedPublisher = NetworkTables.PublisherFactory(networkTable, "Intake Test Power", currentTestSpeed);
    private final DoublePublisher frameLeftPositionPublisher = NetworkTables.PublisherFactory(networkTable, "Frame Left Position", 0.0);
    private final DoublePublisher frameRightPositionPublisher = NetworkTables.PublisherFactory(networkTable, "Frame Right Position", 0.0);

    public Intake() {
        this.intakeMotor = new SparkMax(INTAKE_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
        intakeMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        intakeMotorConfig.smartCurrentLimit(30);

        this.intakeMotor.configure(intakeMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

        this.intakeFrameLeft = new SparkMax(INTAKE_FRAME_SPARK_LEFT, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig intakeFrameLeftConfig = new SparkMaxConfig();
        intakeFrameLeftConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        intakeFrameLeftConfig.smartCurrentLimit(30);

        this.intakeFrameLeft.configure(intakeFrameLeftConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

        this.intakeFrameRight = new SparkMax(INTAKE_FRAME_SPARK_RIGHT, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig intakeFrameRightConfig = new SparkMaxConfig();
        intakeFrameRightConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        intakeFrameRightConfig.smartCurrentLimit(30);

        this.intakeFrameRight.configure(intakeFrameRightConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);
        
        this.intakeTestMotor = new SparkMax(INTAKE_TEST_SPARK, SparkLowLevel.MotorType.kBrushless);
        SparkMaxConfig intakeTestMotorConfig = new SparkMaxConfig();
        intakeTestMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
        intakeTestMotorConfig.smartCurrentLimit(30);

        this.intakeTestMotor.configure(intakeTestMotorConfig, com.revrobotics.ResetMode.kNoResetSafeParameters, com.revrobotics.PersistMode.kPersistParameters);

        this.frameLeftEncoder = this.intakeFrameLeft.getEncoder();
        this.frameRightEncoder = this.intakeFrameRight.getEncoder();
    }
    

    public void setSpeed(double speed) {
        currentSpeed = speed;
    }

    public double getSpeed() {
        return this.intakeMotor.get();
    }

    public double getLeftFrame() {
        return this.intakeFrameLeft.get();
    }

    public double getRightFrame() {
        return this.intakeFrameRight.get();
    }

    public void raiseLowerIntakeFrame(double leftSpeed, double rightSpeed){
        currentFrameSpeedLeft = leftSpeed;
        currentFrameSpeedRight = rightSpeed;
    }
    
    public double getTestSpeed() {
        return this.intakeTestMotor.get();
    }

    public void setTestSpeed(double speed) {
        currentTestSpeed = speed;
    }
    public double getFrameLeftPosition() {
        return this.frameLeftEncoder.getPosition();
    }
    public void setFrameLeftPosition(double leftSpeed) {
        currentFrameSpeedRight = leftSpeed;
    }
    
    public double getFrameRightPosition() {
        return this.frameRightEncoder.getPosition();
    }
    public void setFrameRightPosition(double rightSpeed) {
        currentFrameSpeedRight = rightSpeed;
    }
    
    public void resetFrameEncoders() {
        this.frameLeftEncoder.setPosition(0);
        this.frameRightEncoder.setPosition(0);
    }

    @Override
    public void periodic() {
        this.intakeMotor.set(currentSpeed);
        this.currentSpeedPublisher.set(currentSpeed);
        this.intakeFrameLeft.set(currentFrameSpeedLeft);
        this.currentFrameLeftPublisher.set(currentFrameSpeedLeft);
        this.intakeFrameRight.set(currentFrameSpeedRight);
        this.currentFrameRightPublisher.set(currentFrameSpeedRight);
        this.frameLeftPositionPublisher.set(this.frameLeftEncoder.getPosition());
        this.frameRightPositionPublisher.set(this.frameRightEncoder.getPosition());
        this.intakeTestMotor.set(currentTestSpeed);
        this.currentTestSpeedPublisher.set(currentTestSpeed);
    }
}
