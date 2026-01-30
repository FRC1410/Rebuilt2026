package robot.src.main.java.org.frc1410.rebuilt2026;

// HELP
import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveLooped;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.MoveHoodCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;

public final class Robot extends PhaseDrivenRobot {

    public Robot() {
    }

    private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
    private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER, 0.1);
    // private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));
    private final Shoot shooter = new Shoot();
    // private final ShooterStepUpCommand shooterStepUpCommand = new ShooterStepUpCommand(shooter, 1);
    // private final ShooterStepDownCommand shooterStepDownCommand = new ShooterStepDownCommand(shooter, 1);
    private final MoveHoodCommand moveHoodLowLeftCommand = new MoveHoodCommand(shooter, HoodStates.LOW_LEFT);
    private final MoveHoodCommand moveHoodLowRightCommand = new MoveHoodCommand(shooter, HoodStates.LOW_RIGHT);
    private final MoveHoodCommand moveHoodHighLeftCommand = new MoveHoodCommand(shooter, HoodStates.HIGH_LEFT);

    @Override
    public void autonomousSequence() {
    }

    @Override
    public void teleopSequence() {
        // this.scheduler.scheduleDefaultCommand(new DriveLooped(this.drivetrain, this.driverController.LEFT_X_AXIS, this.driverController.LEFT_Y_AXIS, this.driverController.RIGHT_X_AXIS, this.driverController.RIGHT_TRIGGER), TaskPersistence.GAMEPLAY, LockPriority.HIGH);
        // this.operatorController.DPAD_UP.whenPressed(shooterStepUpCommand, TaskPersistence.GAMEPLAY);
        // this.operatorController.DPAD_DOWN.whenPressed(shooterStepDownCommand, TaskPersistence.GAMEPLAY);

        this.operatorController.A.whileHeldOnce(moveHoodLowLeftCommand, TaskPersistence.GAMEPLAY);
        this.operatorController.B.whileHeldOnce(moveHoodLowRightCommand, TaskPersistence.GAMEPLAY);
        this.operatorController.X.whileHeldOnce(moveHoodHighLeftCommand, TaskPersistence.GAMEPLAY);
    }

    @Override
    public void testSequence() {
    }

    @Override
    protected void disabledSequence() {

    }
}
//Comment so i can push lol
