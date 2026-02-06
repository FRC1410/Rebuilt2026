package robot.src.main.java.org.frc1410.rebuilt2026;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;
import edu.wpi.first.wpilibj.DriverStation;
import framework.src.main.java.org.frc1410.framework.AutoSelector;
import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.*;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC1_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC2_OFFSET;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOLONOMIC_AUTO_CONFIG;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.ROBOT_CONFIG;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME1;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME2;

import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.*;

public final class Robot extends PhaseDrivenRobot {
	
	private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
	private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER,  0.1);
	private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));
	
	
	Cam[] eyesOfCthulu = new Cam[]{new Cam(CAM_NAME1, EoC1_OFFSET), new Cam(CAM_NAME2, EoC2_OFFSET)};
	Vision kv = subsystems.track(new Vision(eyesOfCthulu, drivetrain, null));
	
	
	public Robot() {
		AutoBuilder.configure(
			this.drivetrain::getEstimatedPosition,
			this.drivetrain::resetPose,
			this.drivetrain::getChassisSpeeds,
			this.drivetrain::drive,
			HOLONOMIC_AUTO_CONFIG,
			ROBOT_CONFIG,
			() -> {
				var alliance = DriverStation.getAlliance();

				if(alliance.isPresent()) {
					return alliance.get() == DriverStation.Alliance.Red;
				}
				return false;
			},
			drivetrain
		);
    }
	@Override
	public void autonomousSequence() {
		// NetworkTables.SetPersistence(this.autoPublisher.getTopic(), true);
		// 	String autoProfile = this.autoSubscriber.get();
			
		// 	if (autoProfile == null || autoProfile.isEmpty()) {
		// 		if (!this.autoSelector.getProfiles().isEmpty()) {
		// 			autoProfile = this.autoSelector.getProfiles().get(0).name();
		// 		}
		// 	}
			
		// 	var autoCommand = this.autoSelector.select(autoProfile);

		// 	this.scheduler.scheduleAutoCommand(autoCommand);
	}

	@Override
	public void teleopSequence() {
		this.scheduler.scheduleDefaultCommand(new DriveLooped(this.drivetrain, this.driverController.LEFT_X_AXIS, this.driverController.LEFT_Y_AXIS, this.driverController.RIGHT_X_AXIS, this.driverController.RIGHT_TRIGGER), TaskPersistence.GAMEPLAY, LockPriority.HIGH);
		
		//this.kv.autoAlignTest();
		
		//this.driverController.RIGHT_BUMPER.whileHeldOnce();
		this.scheduler.scheduleDefaultCommand(
			new AutoAlign(
				drivetrain, 
				kv, 
				driverController.RIGHT_BUMPER
			), 
			TaskPersistence.GAMEPLAY
		);
	}


	@Override
	public void testSequence() {
	}

	@Override
	protected void disabledSequence() {
	}
}
