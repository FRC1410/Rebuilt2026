package robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

public class ToggleGuardModeCommand extends Command {
    private final Drivetrain drivetrain;

    public ToggleGuardModeCommand(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        this.drivetrain.toggleGuardMode();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}