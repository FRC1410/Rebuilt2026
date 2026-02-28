package robot.src.main.java.org.frc1410.rebuilt2026.commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;

public class HoodTestCommand extends Command{
    private final Shoot shoot;
    private double inc;

    public HoodTestCommand(Shoot shoot, double increment) {
        this.shoot = shoot;
        this.inc = increment;
    }

    @Override
    public void initialize() {
        this.shoot.bumpHoodPos(inc);
        System.out.println(this.shoot.getHoodSetPos());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
