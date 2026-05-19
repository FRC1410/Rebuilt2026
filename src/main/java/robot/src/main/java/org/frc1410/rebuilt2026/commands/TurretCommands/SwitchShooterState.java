package robot.src.main.java.org.frc1410.rebuilt2026.commands.TurretCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Turret.ShooterStates;

public class SwitchShooterState extends Command{
    
    //Declare necessary
    private final Turret t;
    private final ShooterStates s;
    

    /** Change the state of the shooter by passing in a {@link ShooterStates state}
    * @param turret feed in the turret here
    * @param state pass in the state you want to change to
    */
    public SwitchShooterState(Turret turret, ShooterStates state){
        t = turret;
        s = state;
    }

    @Override
    public void initialize(){
        t.setShooterSpeed(s);
    }
}
