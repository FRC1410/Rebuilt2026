package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import java.util.List;
import java.util.Optional;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonTrackedTarget;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;


import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
public class Vision {
    private Cam[] eyesOfCthulu;
    private boolean targetVisible = false;
    private double targetYaw = 0.0;
    private int tagName;
    private Drivetrain dt;
    private Matrix<N3, N1> curStdDevs;
    private final EstimateConsumer estConsumer;
    public Vision(Cam[] cams, Drivetrain dt, EstimateConsumer estConsumer){
        eyesOfCthulu = cams;
        this.dt = dt;
        this.estConsumer = estConsumer;
    }
    public void autoAlign(){
        for(Cam c : eyesOfCthulu){
                c.lookForTag(7);
            if(c.returnCamYaw() != 0){
                this.dt.drive(new ChassisSpeeds(
            0, 
            0, 
            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        ));
           }
        }
        System.out.println("Command Running");
    }
    public double returnCamYaw(){
        return targetYaw;
    }
    public boolean hasTarget(){
        return targetVisible;
    }
    public int returnTagID(){
        return tagName;
    }

    @FunctionalInterface
    public static interface EstimateConsumer {
        public void accept(Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs);
    }
}
