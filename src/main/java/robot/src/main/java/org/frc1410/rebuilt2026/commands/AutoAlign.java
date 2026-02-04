package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Cam;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Vision;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Ratata
public class AutoAlign extends Command {
    private final Drivetrain drivetrain;
    private final Cam[] cams;
    public AutoAlign(Drivetrain dt, Cam [] eyes){
        this.drivetrain = dt;
        this.cams = eyes;
    }

        
       
            
        @Override
        public void initialize() {
            this.drivetrain.aligning = true;
            for(Cam c : cams){
                c.lookForTag(7);
            if(c.returnCamYaw() != 0){
                this.drivetrain.drive(new ChassisSpeeds(
            0, 
            0, 
            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
           }
        }
        System.out.println("Command Init");
        for(Cam c : cams){
                c.lookForTag(7);
            if(c.returnCamYaw() != 0){
                this.drivetrain.drive(new ChassisSpeeds(
            0, 
            0, 
            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
           }
        }
        System.out.println("Command Running");
            // SmartDashboard.putBoolean("AutoAlign Init Working", true);
        }
         @Override
        public void execute(){
            // Read in relevant data from the Camera
            // SmartDashboard.putBoolean("AutoAlign EXEC", true);
            for(Cam c : cams){
                c.lookForTag(7);
            if(c.returnCamYaw() != 0){
                this.drivetrain.drive(new ChassisSpeeds(
            0, 
            0, 
            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
           }
        }
        System.out.println("Command Running");
    }
    @Override
    public void end(boolean interrupted){
        this.drivetrain.aligning = false;
        System.out.println("Command Finish");
    }
    @Override
    public boolean isFinished() {
        return true;
}
}