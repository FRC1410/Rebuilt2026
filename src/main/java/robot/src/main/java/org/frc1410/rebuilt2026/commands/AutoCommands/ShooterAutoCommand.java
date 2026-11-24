package robot.src.main.java.org.frc1410.rebuilt2026.commands.AutoCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;

public class ShooterAutoCommand extends Command {

    private final Shoot shoot;

    public ShooterAutoCommand(Shoot shoot) {
        this.shoot = shoot;
    }

    @Override
    public void initialize() {
        this.shoot.toggle(.6);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
