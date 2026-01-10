package framework.src.main.java.org.frc1410.framework.control.observer;

import framework.src.main.java.org.frc1410.framework.control.Button;
import framework.src.main.java.org.frc1410.framework.scheduler.task.LifecycleHandle;
import framework.src.main.java.org.frc1410.framework.scheduler.task.Observer;

public class WhileHeldOnceObserver implements Observer {

	private final Button button;
	private boolean wasActive = false;

	public WhileHeldOnceObserver(Button button) {
		this.button = button;
	}

	@Override
	public void tick(LifecycleHandle handle) {
		if (!wasActive && button.isActive()) {
			handle.requestExecution();
			wasActive = true;
		}

		if (!button.isActive()) {
			handle.requestSuspension();
		}

		wasActive = button.isActive();
	}
}