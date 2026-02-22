package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Button;
import robot.src.main.java.org.frc1410.rebuilt2026.Vision.Vision;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;

//Ratata
public class AutoAlign extends Command {

    private final Drivetrain drivetrain;
    private final Vision kv;

    public AutoAlign(Drivetrain dt, Vision kv) {
        this.drivetrain = dt;
        this.kv = kv;
    }

    @Override
    public void initialize() {

    }
    @Override
    public void execute() {
        // Read in relevant data from the Camera
        // SmartDashboard.putBoolean("AutoAlign EXEC", true);
        kv.autoAlign();
        // System.out.println("Command Running");
    }

    @Override
    public void end(boolean interrupted) {
        this.drivetrain.aligning = false;
        //System.out.println("Command Finish");
    }

    // @Override
    // public boolean isFinished() {
    //     // for (Cam c : cams) {
    //     //     c.lookForTag(7);
    //     //     if (c.returnCamYaw() < 5) {
    //     //         return true;
    //     //     }
    //     // }
    //     return false;
    // }
}
