package robot.src.main.java.org.frc1410.rebuilt2026.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.Intake;


public class IntakeTestMotorCommand extends Command{
    private final Intake intake;

    public IntakeTestMotorCommand(Intake intake){
        this.intake = intake;
    }
    
    @Override
    public void initialize() {
      intake.setTestSpeed(0.5);
    }
    
   
     @Override
    public void end(boolean interrupted) {
        intake.setTestSpeed(0);
    }

}
