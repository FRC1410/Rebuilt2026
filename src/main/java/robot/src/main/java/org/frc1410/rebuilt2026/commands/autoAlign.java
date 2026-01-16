package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;
import edu.wpi.first.math.kinematics.ChassisSpeeds;


public class autoAlign extends Command {
    private final Drivetrain drivetrain;
    private final double targetYaw;
    public autoAlign(Drivetrain dt, double yaw){
        this.drivetrain = dt;
        this.targetYaw = yaw;
    }
    
   
        
    @Override
    public void initialize() {
        
    }
     @Override
    public void execute(){
        this.drivetrain.drive(new ChassisSpeeds(
            0, 
            0, 
            1.0 * targetYaw * Tuning.VISION_TURN_kP * Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
        //drivetrain.
        //1.0 * targetYaw * Tuning.VISION_TURN_kP * Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY;
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}