package robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

public class ToggleSlowmodeCommand extends Command {
    private final Drivetrain drivetrain;

    public ToggleSlowmodeCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {
        drivetrain.switchSlowmode();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
