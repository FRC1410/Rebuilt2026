package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;

public class AutoShooterStepDownCommand extends Command {
    private final Shoot shooter;

    public AutoShooterStepDownCommand(Shoot shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        System.out.println("DOWN!");
        // Default multiplier value for auto
        shooter.tickDown(1);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
