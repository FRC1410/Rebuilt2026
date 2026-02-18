package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;

public class AutoMoveHoodCommand extends Command {

    private final Shoot shoot;

    public AutoMoveHoodCommand(Shoot shoot) {
        this.shoot = shoot;
    }

    @Override
    public void initialize() {
        // Default to a specific hood state for auto
        this.shoot.setHoodPos(HoodStates.LOW_RIGHT);
        System.out.println(HoodStates.LOW_RIGHT);
        System.out.println(this.shoot.getHoodPos());
        System.out.println(this.shoot.getHoodSetPos());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
