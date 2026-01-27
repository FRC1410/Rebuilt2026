package robot.src.main.java.org.frc1410.rebuilt2026.commands;

import edu.wpi.first.wpilibj2.command.Command;
import robot.src.main.java.org.frc1410.rebuilt2026.subsystems.TestSparkFlex;

public class TestSparkFlexCommand extends Command {

    private final TestSparkFlex testSparkFlex;
    private TestSparkFlex.TestStates state;

    public TestSparkFlexCommand(TestSparkFlex testSparkFlex, TestSparkFlex.TestStates state) {
        this.testSparkFlex = testSparkFlex;
        this.state = state;
    }

    @Override
    public void initialize() {
        // System.out.println(this.state);
        // System.out.println(this.testSparkFlex.getSpeed());
        this.testSparkFlex.setState(this.state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
