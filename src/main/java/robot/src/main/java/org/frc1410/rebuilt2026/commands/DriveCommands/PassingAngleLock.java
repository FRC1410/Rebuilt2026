package robot.src.main.java.org.frc1410.rebuilt2026.commands.DriveCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import framework.src.main.java.org.frc1410.framework.control.Button;

public class PassingAngleLock extends Command {

    Drivetrain drive;
    PIDController controller;

    Button toggleButton;

    public PassingAngleLock(Drivetrain drive, Button toggleButton) {
        this.drive = drive;
        this.controller = new PIDController(2, 0, 0.7);
        this.toggleButton = toggleButton;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (this.toggleButton.isActive()) {
            this.drive.aligning = true;
            if (Math.abs((this.drive.getGyroYaw().getRadians() % (Math.PI * 2)) + (Math.PI * 2)) > 0.3) {
                this.drive.setTurnRate(this.controller.calculate((this.drive.getGyroYaw().getRadians() % (Math.PI * 2)), -(Math.PI*2)));
                // System.out.println(this.drive.getGyroYaw().getRadians());
                // System.out.println((this.drive.getGyroYaw().getRadians() - Math.PI));
            }
        } else {
            this.drive.aligning = false;
        }
        // System.out.println("Aligning: " + this.drive.aligning);
        // System.out.println("At Point: " + (Math.abs((this.drive.getGyroYaw().getRadians() % (Math.PI * 2)) - Math.PI) > 0.1));
    }
}
