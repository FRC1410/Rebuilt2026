package robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Button;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;

public class ShooterToggleCommand extends Command {
    private final Shoot shooter;
    private final int multiplier;

    public ShooterToggleCommand(Shoot shooter, int multiplier) {
        this.shooter = shooter;
        this.multiplier = multiplier;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        System.out.println("TOGGLE");
        shooter.toggle();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
