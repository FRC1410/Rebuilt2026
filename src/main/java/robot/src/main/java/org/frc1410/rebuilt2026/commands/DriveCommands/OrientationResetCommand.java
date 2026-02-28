package robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

public class OrientationResetCommand extends Command{
    private final Drivetrain drivetrain;


    public OrientationResetCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        this.drivetrain.resetPose(new Pose2d(0, 0, Rotation2d.kZero));
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
