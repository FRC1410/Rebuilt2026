package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import com.revrobotics.servohub.ServoHub;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kMultiTagStdDevs;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.kSingleTagStdDevs;

import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.targeting.PhotonTrackedTarget;

import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;

public class Vision implements TickedSubsystem {

    private final Cam[] eyesOfCthulu;
    private final Drivetrain dt;

    public Vision(Cam[] cams, Drivetrain dt) {
        eyesOfCthulu = cams;
        this.dt = dt;

    }

    /**
     * Function to calculate maximum angle error
     * @param distance Distance from tag in inches
     * @return Maximum angle error in degrees
     */
    public double calcMaxAngleError(double distance) {
        return (-0.0625293 * distance) + 9.15583;
    }


    public void periodic() {
        for (Cam c : eyesOfCthulu) {
            c.update();
            c.updateEstimator();
        }
    }

    public void autoAlign() {
        boolean found = false;
        for (Cam c : eyesOfCthulu) {
            c.lookForTag(7);
            if (Math.abs(c.returnCamYaw()) > Math.abs(calcMaxAngleError(c.returnCamDist())/5)) {
                found = true;
                // if (c.returnCamYaw() < 0.25 && c.returnCamYaw() > -0.25) {
                //     this.dt.setTurnRate(
                //             (-1.0 * c.returnCamYaw() * (Tuning.VISION_TURN_kP - 0.065) * (Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY.in(DegreesPerSecond) / 360))//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
                //     );
                // } else {
                    this.dt.setTurnRate(
                            (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP * (Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY.in(DegreesPerSecond) / 360))//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
                    );
                // }
            } else if (!found) {
                this.dt.setTurnRate(
                        (0)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
                );
                System.out.println(calcMaxAngleError(c.returnCamDist()));

            }
        }
        //System.out.println("Command Running");
    }

   
}
