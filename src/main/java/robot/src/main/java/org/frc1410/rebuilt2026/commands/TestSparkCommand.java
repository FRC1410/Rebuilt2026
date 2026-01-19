package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSpark;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSpark.TestStates;

public class TestSparkCommand extends Command{
    private final TestSpark testSpark;
    private TestStates state;

    public TestSparkCommand(TestSpark testSpark, TestStates state) {
        this.testSpark = testSpark;
        this.state = state;
    }
    
    @Override
    public void initialize() {
        this.testSpark.setState(this.state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
