package robot.src.main.java.org.frc1410.rebuilt2026.commands.TurretCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret.ShooterStates;
import robot.src.main.java.org.frc1410.rebuilt2026.util.AngleCalc;

public class HoodTest extends Command{

    private Axis a1;
    private Turret t;
    /**Move the hood to a desired angle
     * @param angle uppies or downies
     * @param turret turret
     */
    public HoodTest(Turret turret, Axis angle){
        a1 = angle;
        t = turret;
    }
    @Override
    public void execute() {

        this.t.setHoodAngle((a1.get() * 45) + 45);
    }
}