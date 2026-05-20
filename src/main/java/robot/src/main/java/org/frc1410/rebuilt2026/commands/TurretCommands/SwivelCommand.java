package robot.src.main.java.org.frc1410.rebuilt2026.commands.TurretCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret;

public class SwivelCommand extends Command {

    private Turret turret;

    Axis a1;
    Axis a2;

    public SwivelCommand(Turret turret, Axis a1, Axis a2) {
        this.turret = turret;
        this.a1 = a1;
        this.a2 = a2;
    }

    @Override
    public void execute() {
        double angle = Math.acos(a1.get()/a2.get()/a2.get());

        this.turret.setSwivelAngle(angle);
    }

}
