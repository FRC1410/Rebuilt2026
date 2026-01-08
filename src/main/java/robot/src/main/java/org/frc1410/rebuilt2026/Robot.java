package robot.src.main.java.org.frc1410.rebuilt2026;

// HELP

import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;
import org.photonvision.PhotonCamera;

public final class Robot extends PhaseDrivenRobot {
	public Robot() {}

	private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
	private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER,  0.1);
	private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));

	@Override
	public void autonomousSequence() {
	}

	@Override
	public void teleopSequence() {}


	@Override
	public void testSequence() {
	}

	@Override
	protected void disabledSequence() {

	}
}
