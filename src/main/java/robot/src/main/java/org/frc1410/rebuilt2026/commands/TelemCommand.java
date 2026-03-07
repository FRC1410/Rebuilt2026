package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.scheduler.subsystem.TickedSubsystem;

public class TelemCommand extends Command{

    public TelemCommand() {
    }

    @Override
    public void execute() {
        TickedSubsystem.runTelems();
    }

}
