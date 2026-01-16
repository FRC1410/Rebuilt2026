package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
public class Vision {
    PhotonCamera kv1;
    PhotonCamera kv2;
    
    //nothing lol
    public Vision(String camName){
        this.kv1 = new PhotonCamera(camName);
    }
    public Vision(String camName, String camName2){
        this.kv1 = new PhotonCamera(camName);
        this.kv2 = this.kv1 = new PhotonCamera(camName2);
    }
}
