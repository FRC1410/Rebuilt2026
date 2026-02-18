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
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Cam;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Vision;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.AutoAlign;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveLooped;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.FrameLowerCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.FrameRaiseCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.FrameTestCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.IntakeForwardCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.IntakeReverseCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.MoveHoodCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ReadyToRumbleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterStepDownCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterStepUpCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageToggleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageTransferRun;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ToggleGuardModeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ToggleSlowmodeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.HOLONOMIC_AUTO_CONFIG;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.ROBOT_CONFIG;
import robot.src.main.java.org.frc1410.rebuilt2026.util.ControlScheme;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME1;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME2;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.DRIVER_CONTROLLER;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.OPERATOR_CONTROLLER;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC1_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC2_OFFSET;

public final class Robot extends PhaseDrivenRobot {


    private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
    private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER, 0.1);
    private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));

    private final ControlScheme scheme = new ControlScheme(driverController, operatorController);

	// private final Shoot shooter = subsystems.track(new Shoot());
    // private final ShooterStepUpCommand shooterStepUpCommand = new ShooterStepUpCommand(shooter, 1);
    // private final ShooterStepDownCommand shooterStepDownCommand = new ShooterStepDownCommand(shooter, 1);
    // private final MoveHoodCommand moveHoodLowLeftCommand = new MoveHoodCommand(shooter, HoodStates.LOW_LEFT);
    // private final MoveHoodCommand moveHoodLowRightCommand = new MoveHoodCommand(shooter, HoodStates.LOW_RIGHT);
    // private final MoveHoodCommand moveHoodHighLeftCommand = new MoveHoodCommand(shooter, HoodStates.HIGH_LEFT);


    // Cam[] eyesOfCthulu = new Cam[]{new Cam(CAM_NAME1, EoC1_OFFSET, drivetrain::addVisionMeasurement), new Cam(CAM_NAME2, EoC2_OFFSET, drivetrain::addVisionMeasurement)};
	// Vision kv = subsystems.track(new Vision(eyesOfCthulu, drivetrain));

	// private final Storage storage = new Storage();

	// private final StorageToggleCommand storageIntake = new StorageToggleCommand(storage, Storage.StorageStates.INTAKE);
	// private final StorageToggleCommand storageNeutral = new StorageToggleCommand(storage, Storage.StorageStates.NEUTRAL);
	// private final StorageToggleCommand storageOuttake = new StorageToggleCommand(storage, Storage.StorageStates.OUTTAKE);

    private final Intake intake = subsystems.track(new Intake());

    private final IntakeForwardCommand intakeForwardCommand = new IntakeForwardCommand(intake, this.scheme.INTAKE_FORWARD);
    private final IntakeReverseCommand intakeReverseCommand = new IntakeReverseCommand(intake, this.scheme.INTAKE_REVERSE);
    private final FrameTestCommand FrameTestCommand = new FrameTestCommand(intake, this.scheme.FRAME_TEST_1, this.scheme.FRAME_TEST_2);
	private final FrameRaiseCommand FrameRaiseCommand = new FrameRaiseCommand(intake, this.scheme.FRAME_RAISE);
	private final FrameLowerCommand FrameLowerCommand = new FrameLowerCommand(intake, this.scheme.FRAME_LOWER);

	// private final StorageTransferRun transfer = new StorageTransferRun(storage);

    // private final ReadyToRumbleCommand readyToRumbleCommand = new ReadyToRumbleCommand(driverController);


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
					this.scheme.DRIVE_SIDEWAYS, 
					this.scheme.DRIVE_FORWARD, 
					this.scheme.DRIVE_TURN, 
					this.scheme.ROBOT_RELATIVE_TOGGLE
				), 
			TaskPersistence.GAMEPLAY, 
			LockPriority.HIGH
		);

		// this.scheme.STORAGE_INTAKE.whileHeldOnce(storageIntake, TaskPersistence.GAMEPLAY);
		// this.scheme.STORAGE_NEUTRAL.whileHeldOnce(storageNeutral, TaskPersistence.GAMEPLAY);
		// this.scheme.STORAGE_OUTTAKE.whileHeldOnce(storageOuttake, TaskPersistence.GAMEPLAY);
		// this.scheme.TRANSFER.whileHeld(transfer, TaskPersistence.GAMEPLAY);

		// Add slowmode toggle on left bumper
		this.scheme.SLOWMODE_TOGGLE.whenPressed(
			new ToggleSlowmodeCommand(this.drivetrain), 
			TaskPersistence.GAMEPLAY
		);
		
		// Add guard mode toggle on right bumper
		this.scheme.GUARDMODE_TOGGLE.whenPressed(
			new ToggleGuardModeCommand(this.drivetrain), 
			TaskPersistence.GAMEPLAY

		);
        // this.scheduler.scheduleDefaultCommand(intakeForwardCommand, TaskPersistence.GAMEPLAY);
        // this.scheduler.scheduleDefaultCommand(intakeReverseCommand, TaskPersistence.GAMEPLAY);


        // this.scheduler.scheduleDefaultCommand(readyToRumbleCommand, TaskPersistence.GAMEPLAY, LockPriority.HIGH);

        // this.scheme.SHOOTER_UP.whileHeldOnce(shooterStepUpCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.SHOOTER_DOWN.whileHeldOnce(shooterStepDownCommand, TaskPersistence.GAMEPLAY);

        // this.scheme.HOOD_LOW_LEFT.whileHeldOnce(moveHoodLowLeftCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.HOOD_LOW_RIGHT.whileHeldOnce(moveHoodLowRightCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.HOOD_HIGH_LEFT.whileHeldOnce(moveHoodHighLeftCommand, TaskPersistence.GAMEPLAY);

		// this.scheduler.scheduleDefaultCommand(
		// 	new AutoAlign(
		// 		drivetrain, 
		// 		kv, 
		// 		scheme.AUTO_ALIGN
		// 	), 
		// 	TaskPersistence.GAMEPLAY
		// );
    }

    @Override
    public void testSequence() {
    }

    @Override
    protected void disabledSequence() {
    }
}