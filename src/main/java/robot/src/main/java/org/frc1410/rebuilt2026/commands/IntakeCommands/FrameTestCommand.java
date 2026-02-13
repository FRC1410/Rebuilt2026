package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.control.Button;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class FrameTestCommand extends Command{
    private final Intake intake;

    private final Button toggleButton1;
    private final Button toggleButton2;
    
    public FrameTestCommand(Intake intake, Button button1, Button button2) {
        this.intake = intake;
        this.toggleButton1 = button1;
        this.toggleButton2 = button2;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        if (this.toggleButton1.isActive()) {
            this.intake.raiseLowerIntakeFrame(1, 0);
            System.out.println(this.intake.getLeftFrame());
        } else if (!this.toggleButton2.isActive()) {
            this.intake.raiseLowerIntakeFrame(0, 0);
        }
        if (this.toggleButton2.isActive()) {
            this.intake.raiseLowerIntakeFrame(0, 1);
            System.out.println(this.intake.getRightFrame());
        } else if (!this.toggleButton2.isActive()) {
            this.intake.raiseLowerIntakeFrame(0, 0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.intake.raiseLowerIntakeFrame(0, 0);
        this.intake.raiseLowerIntakeFrame(0, 0);
    }
}
