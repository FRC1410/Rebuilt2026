package framework.src.main.java.org.frc1410.framework.scheduler.subsystem;

import edu.wpi.first.wpilibj2.command.Subsystem;
import java.util.ArrayList;

public interface TickedSubsystem extends Subsystem {

    ArrayList<TickedSubsystem> allTickedSubsystems = new ArrayList<>();

    default void include() {
        allTickedSubsystems.add(this);
    }

    default void telem(){};

    static void runTelems() {
        for (TickedSubsystem subsystem : allTickedSubsystems) {
            subsystem.telem();
        }
    }

	default long getPeriod() {
		return -1L;
	}

	@Override
	void periodic();

	@Override
	default void simulationPeriodic() {
		
	}
}