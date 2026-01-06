package org.frc1410.rebuilt2026;
// PLEASE

import org.frc1410.framework.PhaseDrivenRobot;
import org.frc1410.framework.control.Controller;
import static org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;

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
