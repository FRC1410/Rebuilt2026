package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class IntakeReverseCommand extends Command{
    private final Intake intake;

    private final Axis toggleButton;

    public IntakeReverseCommand(Intake intake, Axis axis) {
        this.intake = intake;
        this.toggleButton = axis;
    }

    @Override
    public void initialize() {
    //     this.testSpark.setSpeed(-0.5);
    }

    @Override
    public void execute() {
        if (this.toggleButton.button().isActive()) {
            this.intake.setSpeed(-0.5);
            System.out.println(this.intake.getSpeed());
        } else {
            if (this.intake.getSpeed() == -0.5) {
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
