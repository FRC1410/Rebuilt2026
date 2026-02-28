package robot.src.main.java.org.frc1410.rebuilt2026;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
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
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.FrameTestCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands.IntakeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterCommands.HoodTestCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterCommands.MoveHoodCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterCommands.ShooterToggleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageCommands.StorageToggleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.StorageCommands.StorageTransferRun;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ReadyToRumbleCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands.DriveLooped;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands.ToggleGuardModeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands.ToggleSlowmodeCommand;
import robot.src.main.java.org.frc1410.rebuilt2026.commands.ResetCommand;
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
// import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC1_OFFSET;
// import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC2_OFFSET;

public final class Robot extends PhaseDrivenRobot {

    private final Controller driverController = new Controller(this.scheduler, DRIVER_CONTROLLER, 0.1);
    private final Controller operatorController = new Controller(this.scheduler, OPERATOR_CONTROLLER, 0.1);
    private final Drivetrain drivetrain = subsystems.track(new Drivetrain(this.subsystems));

    private final ControlScheme scheme = new ControlScheme(driverController, operatorController);

    private final Shoot shooter = subsystems.track(new Shoot());
    private final ShooterToggleCommand shootingToggleCommand = new ShooterToggleCommand(shooter, 0.6);
    private final ShooterToggleCommand passingToggleCommand = new ShooterToggleCommand(shooter, 0.5);
    private final MoveHoodCommand moveHoodLowLeftCommand = new MoveHoodCommand(shooter, HoodStates.LOW_LEFT);
    private final MoveHoodCommand moveHoodLowRightCommand = new MoveHoodCommand(shooter, HoodStates.LOW_RIGHT);
    private final MoveHoodCommand moveHoodHighLeftCommand = new MoveHoodCommand(shooter, HoodStates.HIGH_LEFT);

    // Cam[] eyesOfCthulu = new Cam[]{new Cam(CAM_NAME1, EoC1_OFFSET, drivetrain::addVisionMeasurement), new Cam(CAM_NAME2, EoC2_OFFSET, drivetrain::addVisionMeasurement)};
    // Vision kv = subsystems.track(new Vision(eyesOfCthulu, drivetrain));
    private final Storage storage = subsystems.track(new Storage());

    private final StorageToggleCommand storageIntake = new StorageToggleCommand(storage, Storage.StorageStates.INTAKE);
    private final StorageToggleCommand storageNeutral = new StorageToggleCommand(storage, Storage.StorageStates.NEUTRAL);
    private final StorageToggleCommand storageOuttake = new StorageToggleCommand(storage, Storage.StorageStates.OUTTAKE);

    private final Intake intake = subsystems.track(new Intake());

    private final IntakeCommand intakeCommand = new IntakeCommand(intake);

    private final StorageTransferRun transfer = new StorageTransferRun(storage);

    private final ResetCommand resetCommand = new ResetCommand(drivetrain, intake, shooter, storage);
    // private final ReadyToRumbleCommand readyToRumbleCommand = new ReadyToRumbleCommand(driverController);
    private final NetworkTableInstance nt = NetworkTableInstance.getDefault();
    private final NetworkTable table = this.nt.getTable("Auto");

    private final AutoSelector autoSelector = new AutoSelector()
            .add("Tst", () -> new PathPlannerAuto("Tst"))
            .add("RightStartAuto", () -> new PathPlannerAuto("RightStartAuto"))
            .add("LeftStartAuto", () -> new PathPlannerAuto("LeftStartAuto"))
            .add("SysCheckSafe", () -> new PathPlannerAuto("SysCheckSafe"));

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

                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                drivetrain
        );

        // NamedCommands.registerCommand("Intake", new intakeCommand(intake));
        NamedCommands.registerCommand("Shoot Toggle", shootingToggleCommand);
        NamedCommands.registerCommand("Storage Pass", storageIntake);
        NamedCommands.registerCommand("Storage Stop", storageNeutral);
        NamedCommands.registerCommand("Transfer", transfer);
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

        this.scheme.STORAGE_INTAKE.whileHeldOnce(storageIntake, TaskPersistence.GAMEPLAY);
        this.scheme.STORAGE_NEUTRAL.whileHeldOnce(storageNeutral, TaskPersistence.GAMEPLAY);
        this.scheme.STORAGE_OUTTAKE.whileHeldOnce(storageOuttake, TaskPersistence.GAMEPLAY);
        this.scheme.TRANSFER.whileHeld(transfer, TaskPersistence.GAMEPLAY);

        this.scheme.HOOD_RAISE.whileHeldOnce(new HoodTestCommand(shooter, .1), TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_LOWER.whileHeldOnce(new HoodTestCommand(shooter, -.1), TaskPersistence.GAMEPLAY);

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

        // this.scheme.FRAME_RAISE.whileHeld(FrameRaiseCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.FRAME_LOWER.whileHeld(FrameLowerCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.INTAKE_FORWARD.whileHeld(intakeCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.INTAKE_REVERSE.whileHeld(intakeReverseCommand, TaskPersistence.GAMEPLAY);
        // this.scheduler.scheduleDefaultCommand(readyToRumbleCommand, TaskPersistence.GAMEPLAY, LockPriority.HIGH);
        this.scheme.SHOOTING_TOGGLE.whileHeldOnce(shootingToggleCommand, TaskPersistence.GAMEPLAY);
        this.scheme.PASSING_TOGGLE.whileHeldOnce(passingToggleCommand, TaskPersistence.GAMEPLAY);

        this.scheme.HOOD_LOW_LEFT.whileHeldOnce(moveHoodLowLeftCommand, TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_LOW_RIGHT.whileHeldOnce(moveHoodLowRightCommand, TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_HIGH_LEFT.whileHeldOnce(moveHoodHighLeftCommand, TaskPersistence.GAMEPLAY);

        // this.scheme.AUTO_ALIGN.whileHeld(
        //         new AutoAlign(
        //                 drivetrain,
        //                 kv,
        //                 scheme.AUTO_ALIGN
        //         ),
        //         TaskPersistence.GAMEPLAY
        // );
    }

    @Override
    public void testSequence() {
        this.scheduler.scheduleDefaultCommand(resetCommand, TaskPersistence.GAMEPLAY);
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

        this.scheme.STORAGE_INTAKE.whileHeldOnce(storageIntake, TaskPersistence.GAMEPLAY);
        this.scheme.STORAGE_NEUTRAL.whileHeldOnce(storageNeutral, TaskPersistence.GAMEPLAY);
        this.scheme.STORAGE_OUTTAKE.whileHeldOnce(storageOuttake, TaskPersistence.GAMEPLAY);
        this.operatorController.Y.whileHeld(transfer, TaskPersistence.GAMEPLAY);

        this.scheme.HOOD_RAISE.whileHeldOnce(new HoodTestCommand(shooter, .1), TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_LOWER.whileHeldOnce(new HoodTestCommand(shooter, -.1), TaskPersistence.GAMEPLAY);

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

        // this.scheme.FRAME_RAISE.whileHeld(FrameRaiseCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.FRAME_LOWER.whileHeld(FrameLowerCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.INTAKE_FORWARD.whileHeld(intakeCommand, TaskPersistence.GAMEPLAY);
        // this.scheme.INTAKE_REVERSE.whileHeld(intakeReverseCommand, TaskPersistence.GAMEPLAY);
        // this.scheduler.scheduleDefaultCommand(readyToRumbleCommand, TaskPersistence.GAMEPLAY, LockPriority.HIGH);
        this.scheme.SHOOTING_TOGGLE.whileHeldOnce(shootingToggleCommand, TaskPersistence.GAMEPLAY);
        this.scheme.PASSING_TOGGLE.whileHeldOnce(passingToggleCommand, TaskPersistence.GAMEPLAY);

        this.scheme.HOOD_LOW_LEFT.whileHeldOnce(moveHoodLowLeftCommand, TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_LOW_RIGHT.whileHeldOnce(moveHoodLowRightCommand, TaskPersistence.GAMEPLAY);
        this.scheme.HOOD_HIGH_LEFT.whileHeldOnce(moveHoodHighLeftCommand, TaskPersistence.GAMEPLAY);

        // this.scheme.AUTO_ALIGN.whileHeld(
        //         new AutoAlign(
        //                 drivetrain,
        //                 kv,
        //                 scheme.AUTO_ALIGN
        //         ),
        //         TaskPersistence.GAMEPLAY
        // );
    }

    @Override
    protected void disabledSequence() {
    }
}
