package robot.src.main.java.org.frc1410.rebuilt2026;

// HELP
import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.TestSparkFlexCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSparkFlex;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSparkFlex.TestStates;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;

public final class Robot extends PhaseDrivenRobot {

    public Robot() {
    }

    private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
    private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER, 0.1);
    // private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));
    // private final TestSpark testSpark = new TestSpark();
    // private final TestSparkCommand testSparkFrwdCommand = new TestSparkCommand(testSpark, TestStates.FWRD);
    // private final TestSparkCommand testSparkNtrlCommand = new TestSparkCommand(testSpark, TestStates.NTRL);
    // private final TestSparkCommand testSparkBackCommand = new TestSparkCommand(testSpark, TestStates.BACK);
    private final TestSparkFlex testSparkFlex = new TestSparkFlex();
    private final TestSparkFlexCommand testSparkFrwdCommand = new TestSparkFlexCommand(testSparkFlex, TestStates.FWRD);
    private final TestSparkFlexCommand testSparkNtrlCommand = new TestSparkFlexCommand(testSparkFlex, TestStates.NTRL);
    private final TestSparkFlexCommand testSparkBackCommand = new TestSparkFlexCommand(testSparkFlex, TestStates.BACK);

    @Override
    public void autonomousSequence() {
    }

    @Override
    public void teleopSequence() {
        // this.scheduler.scheduleDefaultCommand(new DriveLooped(this.drivetrain, this.driverController.LEFT_X_AXIS, this.driverController.LEFT_Y_AXIS, this.driverController.RIGHT_X_AXIS, this.driverController.RIGHT_TRIGGER), TaskPersistence.GAMEPLAY, LockPriority.HIGH);
        this.driverController.A.whileHeldOnce(testSparkFrwdCommand, TaskPersistence.GAMEPLAY);
        this.driverController.B.whileHeldOnce(testSparkNtrlCommand, TaskPersistence.GAMEPLAY);
        this.driverController.X.whileHeldOnce(testSparkBackCommand, TaskPersistence.GAMEPLAY);
    }

    @Override
    public void testSequence() {
    }

    @Override
    protected void disabledSequence() {

    }
}
