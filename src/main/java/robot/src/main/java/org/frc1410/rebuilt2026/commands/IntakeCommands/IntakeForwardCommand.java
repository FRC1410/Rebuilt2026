package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Axis;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class IntakeForwardCommand extends Command{
    private final Intake testSpark;

    private final Axis toggleButton;
    
    public IntakeForwardCommand(Intake testSpark, Axis axis) {
        this.testSpark = testSpark;
        this.toggleButton = axis;
    }

    @Override
    public void initialize() {
        this.testSpark.setSpeed(1);
    }
     @Override
    public boolean isFinished() {
    
        return !this.toggleButton.button().isActive();
    }

    @Override
    public void end(boolean interrupted) {
        this.testSpark.setSpeed(0);
    }
}
