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

    private final Shoot shooter = subsystems.track(new Shoot());
    private final ShooterStepUpCommand shooterStepUpCommand = new ShooterStepUpCommand(shooter, 1);
    private final ShooterStepDownCommand shooterStepDownCommand = new ShooterStepDownCommand(shooter, 1);
    private final MoveHoodCommand moveHoodLowLeftCommand = new MoveHoodCommand(shooter, HoodStates.LOW_LEFT);
    private final MoveHoodCommand moveHoodLowRightCommand = new MoveHoodCommand(shooter, HoodStates.LOW_RIGHT);
    private final MoveHoodCommand moveHoodHighLeftCommand = new MoveHoodCommand(shooter, HoodStates.HIGH_LEFT);


    Cam[] eyesOfCthulu = new Cam[]{new Cam(CAM_NAME1, EoC1_OFFSET, drivetrain::addVisionMeasurement), new Cam(CAM_NAME2, EoC2_OFFSET, drivetrain::addVisionMeasurement)};
	Vision kv = subsystems.track(new Vision(eyesOfCthulu, drivetrain));

	private final Storage storage = new Storage();

	private final StorageToggleCommand storageIntake = new StorageToggleCommand(storage, Storage.StorageStates.INTAKE);
	private final StorageToggleCommand storageNeutral = new StorageToggleCommand(storage, Storage.StorageStates.NEUTRAL);
	private final StorageToggleCommand storageOuttake = new StorageToggleCommand(storage, Storage.StorageStates.OUTTAKE);

    private final Intake intake = subsystems.track(new Intake());

    private final IntakeForwardCommand intakeForwardCommand = new IntakeForwardCommand(intake, this.driverController.LEFT_TRIGGER);
    private final IntakeReverseCommand intakeReverseCommand = new IntakeReverseCommand(intake, this.driverController.RIGHT_TRIGGER);
    private final FrameTestCommand FrameTestCommand = new FrameTestCommand(intake, this.operatorController.DPAD_LEFT, this.operatorController.DPAD_RIGHT);
	private final FrameRaiseCommand FrameRaiseCommand = new FrameRaiseCommand(intake, this.operatorController.DPAD_UP);
	private final FrameLowerCommand FrameLowerCommand = new FrameLowerCommand(intake, this.operatorController.DPAD_DOWN);

	private final StorageTransferRun transfer = new StorageTransferRun(storage);

    private ControlScheme scheme = new ControlScheme(driverController, operatorController);

    private final ReadyToRumbleCommand readyToRumbleCommand = new ReadyToRumbleCommand(driverController);


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
					this.driverController.LEFT_X_AXIS, 
					this.driverController.LEFT_Y_AXIS, 
					this.driverController.RIGHT_X_AXIS, 
					this.driverController.RIGHT_TRIGGER
				), 
			TaskPersistence.GAMEPLAY, 
			LockPriority.HIGH
		);
		this.operatorController.A.whileHeldOnce(storageIntake, TaskPersistence.GAMEPLAY);
        this.scheduler.scheduleDefaultCommand(FrameTestCommand, TaskPersistence.GAMEPLAY);
		this.scheduler.scheduleDefaultCommand(FrameRaiseCommand, TaskPersistence.GAMEPLAY);
		this.operatorController.B.whileHeldOnce(storageNeutral, TaskPersistence.GAMEPLAY);
		this.operatorController.X.whileHeldOnce(storageOuttake, TaskPersistence.GAMEPLAY);
		this.operatorController.Y.whileHeld(transfer, TaskPersistence.GAMEPLAY);

		// Add slowmode toggle on left bumper
		this.driverController.LEFT_BUMPER.whenPressed(
			new ToggleSlowmodeCommand(this.drivetrain), 
			TaskPersistence.GAMEPLAY
		);
		
		// Add guard mode toggle on right bumper
		this.driverController.RIGHT_BUMPER.whenPressed(
			new ToggleGuardModeCommand(this.drivetrain), 
			TaskPersistence.GAMEPLAY
		);
        this.scheduler.scheduleDefaultCommand(intakeForwardCommand, TaskPersistence.GAMEPLAY);
        this.scheduler.scheduleDefaultCommand(intakeReverseCommand, TaskPersistence.GAMEPLAY);


        this.scheduler.scheduleDefaultCommand(readyToRumbleCommand, TaskPersistence.GAMEPLAY, LockPriority.HIGH);
        this.scheduler.scheduleDefaultCommand(readyToRumbleCommand, TaskPersistence.GAMEPLAY, LockPriority.HIGH);

        this.operatorController.A.whileHeldOnce(shooterStepUpCommand, TaskPersistence.GAMEPLAY);
        this.operatorController.B.whileHeldOnce(shooterStepDownCommand, TaskPersistence.GAMEPLAY);

        this.operatorController.A.whileHeldOnce(moveHoodLowLeftCommand, TaskPersistence.GAMEPLAY);
        this.operatorController.B.whileHeldOnce(moveHoodLowRightCommand, TaskPersistence.GAMEPLAY);
        this.operatorController.X.whileHeldOnce(moveHoodHighLeftCommand, TaskPersistence.GAMEPLAY);

		this.scheduler.scheduleDefaultCommand(
			new AutoAlign(
				drivetrain, 
				kv, 
				driverController.LEFT_BUMPER
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