package robot.src.main.java.org.frc1410.rebuilt2026.commands.TurretCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret.ShooterStates;

public class SwitchShooterState extends Command{
    private final Turret t;
    private final ShooterStates s;
    
    public SwitchShooterState(Turret tu, ShooterStates ss){
        t = tu;
        s= ss;
    }

    @Override
    public void initialize(){
        t.setShooterSpeed(s);
    }
}
