package robot.src.main.java.org.frc1410.rebuilt2026;

// HELP

import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveLooped;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME;

import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.*;

public final class Robot extends PhaseDrivenRobot {
	public Robot() {}
	Vision kv = new Vision(CAM_NAME);
	private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
	private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER,  0.1);
	private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));

	@Override
	public void autonomousSequence() {
	}

	@Override
	public void teleopSequence() {
		this.scheduler.scheduleDefaultCommand(new DriveLooped(this.drivetrain, this.driverController.LEFT_X_AXIS, this.driverController.LEFT_Y_AXIS, this.driverController.RIGHT_X_AXIS, this.driverController.RIGHT_TRIGGER), TaskPersistence.GAMEPLAY, LockPriority.HIGH);
	}


	@Override
	public void testSequence() {
	}

	@Override
	protected void disabledSequence() {

	}
}
//Comment so i can push lol