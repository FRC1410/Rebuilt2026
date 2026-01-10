package framework.src.main.java.org.frc1410.framework.scheduler.task;

import framework.src.main.java.org.frc1410.framework.scheduler.loop.Loop;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.TaskLock;

/**
 * Represents a task that is bound to a loop and being actively ticked. This class
 * acts as a manager over its held task and holds all of its runtime state.
 *
 * @see Task
 * @see Loop
 */
public record BoundTask(
		LifecycleHandle handle,
		Task job,
		TaskPersistence persistence,
		Observer observer,
		TaskLock lock
) {
	public BoundTask {
		observer.init(handle); // Set the state to its correct initial value
	}
}