package robot.src.main.java.org.frc1410.rebuilt2026;

import framework.src.main.java.org.frc1410.framework.AutoSelector;

// HELP

import framework.src.main.java.org.frc1410.framework.PhaseDrivenRobot;
import framework.src.main.java.org.frc1410.framework.control.Controller;
import framework.src.main.java.org.frc1410.framework.scheduler.task.TaskPersistence;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveLooped;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ToggleSlowmodeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.StringSubscriber;
import edu.wpi.first.wpilibj.DriverStation;

public final class Robot extends PhaseDrivenRobot {


    private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
    private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER, 0.1);
    private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));

	private final NetworkTableInstance nt = NetworkTableInstance.getDefault();
	private final NetworkTable table = this.nt.getTable("Auto");


	private final AutoSelector autoSelector = new AutoSelector()
			// .add("Tst", () -> new PathPlannerAuto("Tst"))
			.add("RightStartAuto", () -> new PathPlannerAuto("RightStartAuto"));

			 {
				{
		var profiles = new String[this.autoSelector.getProfiles().size()];
		for (var i = 0; i < profiles.length; i++) {
			profiles[i] = this.autoSelector.getProfiles().get(i).name();
		}

		var autoChoicesPub = NetworkTables.PublisherFactory(this.table, "Choices", profiles);
		autoChoicesPub.accept(profiles);
		}
	}


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

	private final StringPublisher autoPublisher = NetworkTables.PublisherFactory(
		this.table, 
		"Profile",
		this.autoSelector.getProfiles().isEmpty() ? "" : this.autoSelector.getProfiles().
			get(0)
			.name()
	);

	private final StringSubscriber autoSubscriber = NetworkTables.SubscriberFactory(this.table, this.autoPublisher.getTopic());


	@Override
	public void autonomousSequence() {
		NetworkTables.SetPersistence(this.autoPublisher.getTopic(), true);
			String autoProfile = this.autoSubscriber.get();
			
			if (autoProfile == null || autoProfile.isEmpty()) {
				if (!this.autoSelector.getProfiles().isEmpty()) {
					autoProfile = this.autoSelector.getProfiles().get(0).name();
				}
			}
			
			var autoCommand = this.autoSelector.select(autoProfile);

			this.scheduler.scheduleAutoCommand(autoCommand);

	}

	@Override
	public void teleopSequence() {
		this.scheduler.scheduleDefaultCommand(
			new DriveLooped(
					this.drivetrain, 
					this.driverController.LEFT_Y_AXIS,
					this.driverController.LEFT_X_AXIS,  
					this.driverController.RIGHT_X_AXIS, 
					this.driverController.RIGHT_TRIGGER
				), 
			TaskPersistence.GAMEPLAY, 
			LockPriority.HIGH
		);
		
		// Add slowmode toggle on left bumper
		this.driverController.LEFT_BUMPER.whenPressed(
			new ToggleSlowmodeCommand(this.drivetrain), 
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
//Comment so i can push lol