package robot.src.main.java.org.frc1410.rebuilt2026.commands.autocommands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class AutoIntakeForwardCommand extends Command{
    private final Intake intake;
    
    public AutoIntakeForwardCommand(Intake intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
        this.intake.setSpeed(1);
    }

    @Override
    public void execute() {
        System.out.println(this.intake.getSpeed());
    }

    @Override
    public void end(boolean interrupted) {
        this.intake.setSpeed(0);
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}
