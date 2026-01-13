package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSpark;

public class TestSparkCommand extends Command{
    private final TestSpark testSpark;

    public TestSparkCommand(TestSpark testSpark) {
        this.testSpark = testSpark;
        // this.toggleButton = axis;
    }
    
    @Override
    public void initialize() {
        this.testSpark.toggle();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
