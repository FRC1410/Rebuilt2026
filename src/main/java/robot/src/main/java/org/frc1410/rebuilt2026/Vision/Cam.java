package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.APRIL_TAG_FIELD_LAYOUT;

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
    private final List<PhotonPipelineResult> results;
        //

    public Cam(String name, Transform3d offset){
        this.cam = new PhotonCamera(name);
        this.offset = offset;
         poseEst = new PhotonPoseEstimator(
                APRIL_TAG_FIELD_LAYOUT,
                PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
                this.offset
        );
    }
    @Override
    public void periodic() {
        // RANDOM BS GOOOOOOOOOOOOOO
        results = cam.getAllUnreadResults();
        
    }
    public List<PhotonPipelineResult> getUnreadResults(){
        return results;
    }
    public PhotonPipelineResult getBestResult(){
        return ((Cam) results).getBestResult();
    }
    public boolean hasTargets{
        return ((PhotonPipelineResult) results).hasTargets();
    }
    public PoseStrategy returnPoseStrategy{
        return poseEst.getPrimaryStrategy();
    }
}
