package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;


public class autoAlign extends Command {
    private final Drivetrain drivetrain;
    public autoAlign(Drivetrain dt){
        this.drivetrain = dt;
    }
    
   
        
    @Override
    public void initialize() {
        
    }
     @Override
    public void execute(){
        //1.0 * targetYaw * Tuning.VISION_TURN_kP * Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY;
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}