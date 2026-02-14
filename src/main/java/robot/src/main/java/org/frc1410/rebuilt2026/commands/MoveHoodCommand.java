package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;

public class MoveHoodCommand extends Command {

    private final Shoot shoot;
    private final HoodStates hoodstate;

    public MoveHoodCommand(Shoot shoot, HoodStates hoodState) {
        this.shoot = shoot;
        this.hoodstate = hoodState;
    }

    @Override
    public void initialize() {
        this.shoot.setHoodPos(hoodstate);
        System.out.println(hoodstate);
        System.out.println(this.shoot.getHoodPos());
        System.out.println(this.shoot.getHoodSetPos());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
