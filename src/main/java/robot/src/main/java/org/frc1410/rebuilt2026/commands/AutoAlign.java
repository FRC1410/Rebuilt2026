package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Cam;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.util.Tuning;

//Ratata
public class AutoAlign extends Command {

    private final Drivetrain drivetrain;
    private final Cam[] cams;

    public AutoAlign(Drivetrain dt, Cam[] eyes) {
        this.drivetrain = dt;
        this.cams = eyes;
    }

    @Override
    public void initialize() {
        this.drivetrain.aligning = true;
        // for (Cam c : cams) {
        //     c.lookForTag(7);
        //     if (c.returnCamYaw() != 0) {
        //         this.drivetrain.setTurnRate(
        //                 (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        //         );
        //     }
        // }
        // System.out.println("Command Init");
        // for (Cam c : cams) {
        //     c.lookForTag(7);
        //     if (c.returnCamYaw() != 0) {
        //         this.drivetrain.setTurnRate(
        //                 (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
        //         );
        //     }
        // }
        System.out.println("Command Running");
        // SmartDashboard.putBoolean("AutoAlign Init Working", true);
    }

    @Override
    public void execute() {
        // Read in relevant data from the Camera
        // SmartDashboard.putBoolean("AutoAlign EXEC", true);
        for (Cam c : cams) {
            c.lookForTag(7);
            if (c.returnCamYaw() != 0) {
                this.drivetrain.setTurnRate(
                        (-1.0 * c.returnCamYaw() * Tuning.VISION_TURN_kP)//* Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY
                );
            }
        }
        System.out.println("Command Running");
    }

    @Override
    public void end(boolean interrupted) {
        this.drivetrain.aligning = false;
        System.out.println("Command Finish");
    }

    @Override
    public boolean isFinished() {
        for (Cam c : cams) {
            c.lookForTag(7);
            if (c.returnCamYaw() < 5) {
                return true;
            }
        }
        return false;
    }
}
