package robot.src.main.java.org.frc1410.rebuilt2026;

// HELP

import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageToggleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;

public final class Robot extends PhaseDrivenRobot {
	public Robot() {}

	private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
	private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER,  0.1);
	// private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));
	private final Storage storage = subsystems.track(new Storage());

	private final StorageToggleCommand intake = new StorageToggleCommand(storage, Storage.StorageStates.INTAKE);
	private final StorageToggleCommand neutral = new StorageToggleCommand(storage, Storage.StorageStates.NEUTRAL);
	private final StorageToggleCommand outtake = new StorageToggleCommand(storage, Storage.StorageStates.OUTTAKE);

	@Override
	public void autonomousSequence() {
	}

	@Override
	public void teleopSequence() {
		// this.scheduler.scheduleDefaultCommand(
		// 	new DriveLooped(
		// 			this.drivetrain, 
		// 			this.driverController.LEFT_X_AXIS, 
		// 			this.driverController.LEFT_Y_AXIS, 
		// 			this.driverController.RIGHT_X_AXIS, 
		// 			this.driverController.RIGHT_TRIGGER
		// 		), 
		// 	TaskPersistence.GAMEPLAY, 
		// 	LockPriority.HIGH
		// );
		this.operatorController.A.whileHeldOnce(intake, TaskPersistence.GAMEPLAY);
		this.operatorController.B.whileHeldOnce(neutral, TaskPersistence.GAMEPLAY);
		this.operatorController.X.whileHeldOnce(outtake, TaskPersistence.GAMEPLAY);
	}


	@Override
	public void testSequence() {
	}

	@Override
	protected void disabledSequence() {

	}
}