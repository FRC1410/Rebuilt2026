package robot.src.main.java.org.frc1410.rebuilt2026.commands.AutoCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

public class OrientationResetAutoCommand extends Command{
    private final Drivetrain drivetrain;


    public OrientationResetAutoCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        // this.drivetrain.resetPose(new Pose2d(0, 0, Rotation2d.kZero));
        // System.out.println("RESET\nRESET\nRESET");
    }

    @Override
    public void end(boolean isInteruppted) {
        // this.drivetrain.resetPose(new Pose2d(0, 0, Rotation2d.kZero));
    }

}
