package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Cam;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Vision;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class AutoAlign extends Command {
    private final Drivetrain drivetrain;
    private final Cam[] cams;
    public AutoAlign(Drivetrain dt, Cam [] eyes){
        this.drivetrain = dt;
        this.cams = eyes;
    }
        
       
            
        @Override
        public void initialize() {
            // for(Cam c : cams){
            //     c.lookForTag(7);
            // }
            SmartDashboard.putBoolean("AutoAlign Init Working", true);
        }
         @Override
        public void execute(){
            // Read in relevant data from the Camera
            SmartDashboard.putBoolean("AutoAlign EXEC", true);
        // for(Cam c : cams){
        //     if(c.returnCamYaw() != 0){
        //         this.drivetrain.drive(new ChassisSpeeds(
        //     0, 
        //     0, 
        //     (-1.0 * 0 * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        // ));
        //    }
        // }
        //drivetrain.
        //1.0 * targetYaw * Tuning.VISION_TURN_kP * Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY;
    }
    @Override
    public boolean isFinished() {
        return true;
}
}