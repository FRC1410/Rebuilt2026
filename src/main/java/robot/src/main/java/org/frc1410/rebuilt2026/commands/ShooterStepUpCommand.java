package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;

public class ShooterStepUpCommand extends Command {
    private final Shoot shooter;
    private final int multiplier;

    public ShooterStepUpCommand(Shoot shooter, int multiplier) {
        this.shooter = shooter;
        this.multiplier = multiplier;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        System.out.println("UP!");
        shooter.tickUp(multiplier);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
