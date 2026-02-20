package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Drivetrain;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Shoot.HoodStates;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Storage;

public class ResetCommand extends Command{
    Drivetrain drivetrain;
    Intake intake;
    Shoot shoot;
    Storage storage;

    public ResetCommand(Drivetrain drivetrain, Intake intake, Shoot shoot, Storage storage) {
        this.drivetrain = drivetrain;
        this.intake = intake;
        this.shoot = shoot;
        this.storage = storage;
    }

    @Override
    public void initialize() {
        this.drivetrain.drive(
            new ChassisSpeeds(
                0, 
                0, 
                0
            )
        );
        this.intake.setSpeed(0);
        this.shoot.setHoodPos(HoodStates.LOW_LEFT);
        this.shoot.resetSpeed();
        this.storage.setBeltSpeed(0);
        this.storage.setTransferSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
