package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class AutoFrameTestCommand extends Command{
    private final Intake intake;
    
    public AutoFrameTestCommand(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        // Hardcoded to test left frame motor
        this.intake.raiseLowerIntakeFrame(0.5, 0);
        System.out.println(this.intake.getLeftFrame());
    }

    @Override
    public void end(boolean interrupted) {
        this.intake.raiseLowerIntakeFrame(0, 0);
    }
}
