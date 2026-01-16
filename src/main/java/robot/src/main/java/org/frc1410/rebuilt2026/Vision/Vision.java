package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;

import edu.wpi.first.math.geometry.Transform3d;
public class Vision {
    private PhotonCamera[] EoC;
    private Transform3d[] EoCoffsets;
    private PhotonPoseEstimator PoC;
    PhotonCamera kv1;
    PhotonCamera kv2;
    boolean targetVisible = false;
    double targetYaw = 0.0;
    int tagName;

    public Vision(String camName){
        this.kv1 = new PhotonCamera(camName);
        EoC = new PhotonCamera[]{kv1};
    }
    public Vision(String camName, String camName2){
        this.kv1 = new PhotonCamera(camName);
        this.kv2 = new PhotonCamera(camName2);
        EoC = new PhotonCamera[]{kv1, kv2};
    }
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
                        this.tagName = target.getFiducialId();
                        this.targetYaw = target.getYaw();
                        this.targetVisible = true;
                    }
                }
            }
        }
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
