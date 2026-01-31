package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Vision;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;
import edu.wpi.first.math.kinematics.ChassisSpeeds;


public class AutoAlign extends Command {
    private final Drivetrain drivetrain;
    private final Vision vision;
    private final double yaw;
    public AutoAlign(Drivetrain dt, Vision v){
        this.drivetrain = dt;
        this.vision = v;
    }
    
   
        
    @Override
    public void initialize() {
        
    }
     @Override
    public void execute(){
        public void autoAlignTest(){
        // Read in relevant data from the Camera
        
        var results = kv1.getAllUnreadResults();
        if (!results.isEmpty()) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                // At least one AprilTag was seen by the camera
                for (var target : result.getTargets()) {
                    if (target.getFiducialId() == 7) {
                        // Found Tag 7, record its information
                        //this.tagName = target.getFiducialId();
                        this.yaw = target.getYaw();
                        //this.targetVisible = true;
                    }
                }
            }
        }
        }
        this.drivetrain.drive(new ChassisSpeeds(
            0, 
            0, 
            (-1.0 * this.yaw * Tuning.VISION_TURN_kP )//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
        //drivetrain.
        //1.0 * targetYaw * Tuning.VISION_TURN_kP * Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY;
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}