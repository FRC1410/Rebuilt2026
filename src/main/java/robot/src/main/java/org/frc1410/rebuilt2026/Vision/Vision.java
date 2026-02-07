package robot.src.main.java.org.frc1410.rebuilt2026.Vision;

import com.revrobotics.servohub.ServoHub;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Constants;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;

public class Vision implements TickedSubsystem {

    private final Cam[] eyesOfCthulu;
    private final Drivetrain dt;
    @SuppressWarnings("unused")
    private Matrix<N3, N1> curStdDevs; //unsued
    @SuppressWarnings("unused")
    private final EstimateConsumer estConsumer; //unused

    public Vision(Cam[] cams, Drivetrain dt, EstimateConsumer estConsumer) {
        eyesOfCthulu = cams;
        this.dt = dt;
        this.estConsumer = estConsumer;

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
