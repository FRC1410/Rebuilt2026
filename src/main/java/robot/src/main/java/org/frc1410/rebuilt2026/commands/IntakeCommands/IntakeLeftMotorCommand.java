package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class IntakeLeftMotorCommand extends Command{
    private final Intake intake;

    public IntakeLeftMotorCommand(Intake intake){
        this.intake = intake;
    }
    
    @Override
    public void initialize() {
      intake.setFrameLeftPosition(1);
    }
    
   
     @Override
    public void end(boolean interrupted) {
        intake.setFrameLeftPosition(0);
    }

}