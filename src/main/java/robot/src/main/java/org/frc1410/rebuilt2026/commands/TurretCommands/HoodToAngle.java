package robot.src.main.java.org.frc1410.rebuilt2026.commands.TurretCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret.ShooterStates;
import robot.src.main.java.org.frc1410.rebuilt2026.util.AngleCalc;

public class HoodToAngle extends Command{
    private Turret t;
    private double d;

    /**Move the hood to a desired angle
     * @param turret feed in turret you're changing
     * @param dist meters away fronm target
     */
    public HoodToAngle(Turret turret, double dist){
        t = turret;
        d = dist;
    }
    @Override
    public void execute() {
        this.t.setHoodAngle(AngleCalc.calcAngle(d));
    }
}
