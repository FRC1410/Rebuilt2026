package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class IntakeForwardCommand extends Command{
    private final Intake intake;

    private final Axis toggleButton;
    
    public IntakeForwardCommand(Intake intake, Axis axis) {
        this.intake = intake;
        this.toggleButton = axis;
    }

    @Override
    public void initialize() {
        // this.testSpark.setSpeed(1);
    }

    @Override
    public void execute() {
        if (this.toggleButton.button().isActive()) {
            this.intake.setSpeed(1);
            System.out.println(this.intake.getSpeed());
        } else {
            if (this.intake.getSpeed() == 1) {
                this.intake.setSpeed(0);
            }
        }
    }

    // @Override
    // public boolean isFinished() {
    
    //     return !this.toggleButton.button().isActive();
    // }

    @Override
    public void end(boolean interrupted) {
        this.intake.setSpeed(0);
    }
}
