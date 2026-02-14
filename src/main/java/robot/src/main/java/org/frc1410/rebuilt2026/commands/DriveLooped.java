package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import framework.src.main.java.org.frc1410.framework.control.Button;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.SWERVE_DRIVE_MAX_ANGULAR_VELOCITY;
import static robot.src.main.java.org.frc1410.rebuilt2026.util.Constants.SWERVE_DRIVE_MAX_SPEED;

public class DriveLooped extends Command {
    private final Drivetrain drivetrain;

    private final Axis xAxis;
    private final Axis yAxis;

    private final Axis rotationAxis;

    private final Button robotRelativeTrigger;

    public DriveLooped(Drivetrain drivetrain, Axis xAxis, Axis yAxis, Axis rotationAxis, Button robotRelativeTrigger) {
        this.drivetrain = drivetrain;

        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.rotationAxis = rotationAxis;
        this.robotRelativeTrigger = robotRelativeTrigger;

        this.addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        // if (this.drivetrain.aligning) {
        //     return;
        // }
        LinearVelocity xVelocity;
        LinearVelocity yVelocity;
        AngularVelocity angularVelocity;

        

        if(this.drivetrain.isSlowModeEnabled()) {
            xVelocity = SWERVE_DRIVE_MAX_SPEED.times(this.xAxis.get() * 0.6);
            yVelocity = SWERVE_DRIVE_MAX_SPEED.times(-this.yAxis.get() * 0.6);
            angularVelocity = SWERVE_DRIVE_MAX_ANGULAR_VELOCITY.times(-this.rotationAxis.get() * 0.6);
        } else {
            xVelocity = SWERVE_DRIVE_MAX_SPEED.times(this.xAxis.get());
            yVelocity = SWERVE_DRIVE_MAX_SPEED.times(-this.yAxis.get());
            angularVelocity = SWERVE_DRIVE_MAX_ANGULAR_VELOCITY.times(-this.rotationAxis.get());
        }

//        drivetrain.drive(new ChassisSpeeds(xVelocity, yVelocity, angularVelocity));

        if(robotRelativeTrigger.isActive()) { 
            drivetrain.drive(new ChassisSpeeds(xVelocity.times(-1), yVelocity.times(-1), angularVelocity));
        } else {
            this.drivetrain.fieldOrientedDrive(
                new ChassisSpeeds(
                    xVelocity.in(MetersPerSecond),
                    yVelocity.in(MetersPerSecond),
                    angularVelocity.in(RadiansPerSecond)
                )
            );
        }
        }

        @Override
        public boolean isFinished() {
        return false;
        }
}