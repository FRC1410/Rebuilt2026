package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.APRIL_TAG_FIELD_LAYOUT;

import java.util.ArrayList;
import java.util.List;


import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Transform3d;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

public class Cam implements TickedSubsystem{
    //The Eye of Cthulu knows all
    private final PhotonCamera cam;
    private final Transform3d offset;
    private final PhotonPoseEstimator poseEst;
    private List<PhotonPipelineResult> results = new ArrayList<>();
    boolean targetVisible = false;
    double targetYaw = 0.0;
    int tagName;
            //
    
        public Cam(String name, Transform3d offset){
            this.cam = new PhotonCamera(name);
            this.offset = offset;
             poseEst = new PhotonPoseEstimator(
                    APRIL_TAG_FIELD_LAYOUT,
                    this.offset
            );
        }
    @Override
    public void periodic() {
        // RANDOM BS GOOOOOOOOOOOOOO
        results = cam.getAllUnreadResults();
    }
    public void lookForTag(int id){
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
                            this.targetVisible = true;
                        }
                    }
                }
            }
    }
    public List<PhotonPipelineResult> getUnreadResults(){
        return results;
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
}
