package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.APRIL_TAG_FIELD_LAYOUT;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kMultiTagStdDevs;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kSingleTagStdDevs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import robot.src.main.java.org.frc1410.rebuilt2026.util.NetworkTables;

public class Cam {

    //The Eye of Cthulu knows all
    private final PhotonCamera cam;
    private final Transform3d offset;
    private final PhotonPoseEstimator poseEst;
    private List<PhotonPipelineResult> results = new ArrayList<>();
    private final NetworkTable table = NetworkTableInstance.getDefault().getTable("THE ORB");
    private final DoublePublisher camYaw = NetworkTables.PublisherFactory(this.table, "Current Cam Yaw", 0);
    boolean targetVisible = false;
    double targetYaw = 0.0;
    double targetDist = 0.0;
    int tagName;
    
    
    @SuppressWarnings("unused")
    private Matrix<N3, N1> curStdDevs; //unsued
    @SuppressWarnings("unused")
    private final EstimateConsumer estConsumer; //unused
    //

    public Cam(String name, Transform3d offset, EstimateConsumer e) {
        this.cam = new PhotonCamera(name);
        this.offset = offset;
        poseEst = new PhotonPoseEstimator(
            APRIL_TAG_FIELD_LAYOUT,
            this.offset
        );
        estConsumer = e;
    }

    public void update() {
        // RANDOM BS GOOOOOOOOOOOOOO
        this.tagName = 0;
        this.targetYaw = 0;
        this.targetDist = 0;
        this.targetVisible = true;
        camYaw.set(0);
        results = cam.getAllUnreadResults();
    }
    public void updateEstimator(){
        //HAHA BOOM
    }

    public void lookForTag(int id) {
        if (!results.isEmpty()) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                // At least one AprilTag was seen by the camera
                for (var target : result.getTargets()) {
                    if (target.getFiducialId() == id) {
                        // Found Tag, record its information
                        this.tagName = target.getFiducialId();
                        this.targetYaw = target.getYaw();
                        this.targetDist = (target.getBestCameraToTarget().getX() * 39.3701); //conversion from meters to inches
                        this.targetVisible = true;
                        camYaw.set(this.targetYaw);
                    }
                }
            }
        } else {
            this.tagName = 0;
            this.targetYaw = 0;
            this.targetDist = 0;
            this.targetVisible = true;
            camYaw.set(0);
        }
    }

    public List<PhotonPipelineResult> getUnreadResults() {
        return results;
    }

    /**
     * Returns the Cam yaw
     *
     * @return Target yaw in degrees
     */
    public double returnCamYaw() {
        return targetYaw;
    }

    /**
     * Returns the Cam distance
     *
     * @return Target distance in degrees
     */
    public double returnCamDist() {
        return targetYaw;
    }

    /**
     * Returns the whether the camera has the target
     *
     * @return If the target is visible
     */
    public boolean hasTarget() {
        return targetVisible;
    }

    /**
     * Returns the Tag ID
     *
     * @return ID of tag
     */
    public int returnTagID() {
        return tagName;
    }
    // public Optional<EstimatedRobotPose> poseEst(){
    //     return Optional;
    // }
    private void updateEstimationStdDevs(
            Optional<EstimatedRobotPose> estimatedPose, List<PhotonTrackedTarget> targets) {
        if (estimatedPose.isEmpty()) {
            // No pose input. Default to single-tag std devs
            curStdDevs = kSingleTagStdDevs;
        } else {
            // Pose present. Start running Heuristic
            var estStdDevs = kSingleTagStdDevs;
            int numTags = 0;
            double avgDist = 0;
            // Precalculation - see how many tags we found, and calculate an average-distance metric
            for (var tgt : targets) {
                var tagPose = poseEst.getFieldTags().getTagPose(tgt.getFiducialId());
                if (tagPose.isEmpty()) continue;
                numTags++;
                avgDist +=
                        tagPose
                                .get()
                                .toPose2d()
                                .getTranslation()
                                .getDistance(estimatedPose.get().estimatedPose.toPose2d().getTranslation());
            }
            if (numTags == 0) {
                // No tags visible. Default to single-tag std devs
                curStdDevs = kSingleTagStdDevs;
            } else {
                // One or more tags visible, run the full heuristic.
                avgDist /= numTags;
                // Decrease std devs if multiple targets are visible
                if (numTags > 1) estStdDevs = kMultiTagStdDevs;
                // Increase std devs based on (average) distance
                if (numTags == 1 && avgDist > 4)
                    estStdDevs = VecBuilder.fill(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
                else estStdDevs = estStdDevs.times(1 + (avgDist * avgDist / 30));
                curStdDevs = estStdDevs;
            }
        }
    }
    @FunctionalInterface
    public static interface EstimateConsumer {

        public void accept(Pose2d pose, double timestamp, Matrix<N3, N1> estimationStdDevs);
    }
}
