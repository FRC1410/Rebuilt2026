package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

public class AutoToggleSlowmodeCommand extends Command {
    private final Drivetrain drivetrain;

    public AutoToggleSlowmodeCommand(Drivetrain drivetrain) {
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
