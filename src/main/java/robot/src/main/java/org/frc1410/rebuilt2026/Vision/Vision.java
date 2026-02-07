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
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import static edu.wpi.first.units.Units.DegreesPerSecond;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kSingleTagStdDevs;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.IDs.CAM_NAME1;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning.EoC1_OFFSET;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kMultiTagStdDevs;

import java.util.List;
import java.util.Optional;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.targeting.PhotonTrackedTarget;

import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;


import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
public class Vision implements TickedSubsystem{
    private Cam[] eyesOfCthulu;
    private Drivetrain dt;
    private Matrix<N3, N1> curStdDevs;
    private final EstimateConsumer estConsumer;
    public Vision(Cam[] cams, Drivetrain dt, EstimateConsumer estConsumer){
        eyesOfCthulu = cams;
        this.dt = dt;
        this.estConsumer = estConsumer;

    }
    public void periodic(){
        for(Cam c : eyesOfCthulu){
            c.update();
        }
    }
    public void autoAlign(){
        for (Cam c : eyesOfCthulu) {
                c.lookForTag(7);
                if (c.returnCamYaw() != 0) {
                    this.dt.setTurnRate(
                            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP * (Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY.in(DegreesPerSecond)/360))//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
                    );
                }
            }
        //System.out.println("Command Running");
    }
    
    // public Optional<EstimatedRobotPose> poseEst(){
    //     return Optional;
    // }
    // private void updateEstimationStdDevs(
    //         Optional<EstimatedRobotPose> estimatedPose, List<PhotonTrackedTarget> targets) {
    //     if (estimatedPose.isEmpty()) {
    //         // No pose input. Default to single-tag std devs
    //         curStdDevs = kSingleTagStdDevs;

    //     } else {
    //         // Pose present. Start running Heuristic
    //         var estStdDevs = kSingleTagStdDevs;
    //         int numTags = 0;
    //         double avgDist = 0;

    //         // Precalculation - see how many tags we found, and calculate an average-distance metric
    //         for (var tgt : targets) {
    //             var tagPose = photonEstimator.getFieldTags().getTagPose(tgt.getFiducialId());
    //             if (tagPose.isEmpty()) continue;
    //             numTags++;
    //             avgDist +=
    //                     tagPose
    //                             .get()
    //                             .toPose2d()
    //                             .getTranslation()
    //                             .getDistance(estimatedPose.get().estimatedPose.toPose2d().getTranslation());
    //         }

    //         if (numTags == 0) {
    //             // No tags visible. Default to single-tag std devs
    //             curStdDevs = kSingleTagStdDevs;
    //         } else {
    //             // One or more tags visible, run the full heuristic.
    //             avgDist /= numTags;
    //             // Decrease std devs if multiple targets are visible
    //             if (numTags > 1) estStdDevs = kMultiTagStdDevs;
    //             // Increase std devs based on (average) distance
    //             if (numTags == 1 && avgDist > 4)
    //                 estStdDevs = VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
    //             else estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30));
    //             curStdDevs = estStdDevs;
    //         }
    //     }
    // }
    
    @FunctionalInterface
    public static interface EstimateConsumer {
        public void accept(Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs);
    }
}
