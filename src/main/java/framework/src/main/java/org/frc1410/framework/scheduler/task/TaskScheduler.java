package framework.src.main.java.org.frc1410.framework.scheduler.task;

import edu.wpi.first.wpilibj2.command.Command;
import framework.src.main.java.org.frc1410.framework.scheduler.loop.Loop;
import framework.src.main.java.org.frc1410.framework.scheduler.loop.LoopStore;
import framework.src.main.java.org.frc1410.framework.scheduler.task.impl.CommandTask;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockHandler;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.LockPriority;
import framework.src.main.java.org.frc1410.framework.scheduler.task.lock.TaskLock;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * The class responsible for orchestrating tasks. The scheduler passes
 * scheduled tasks down to loops through its {@link LoopStore} and has
 * no logic itself for scheduling or handling tasks.
 */
public final class TaskScheduler {

	public final LoopStore loopStore = new LoopStore(this);
	public final LockHandler lockHandler = new LockHandler();

	private void schedule(BoundTask task, Loop loop) {
		loop.add(task);
	}

	/**
	 * Schedule a {@link Task}.
	 *
	 * @param job The task to schedule.
	 * @param persistence The task's {@link TaskPersistence}.
	 * @param observer The task's observer.
	 * @param lockPriority The task's lock priority. Must be a value
	 *					 from the {@link LockPriority} interface.
	 * @param loop The {@link Loop} to write to.
	 *
	 * @throws NullPointerException If any of {@code job}, {@code persistence}, {@code observer},
	 *							  or {@code loop} are null.
	 * @throws IllegalArgumentException If {@code lockPriority} is not valid.
	 */
	public BoundTask schedule(Task job, TaskPersistence persistence, Observer observer, int lockPriority, Loop loop) {
		Objects.requireNonNull(persistence);
		Objects.requireNonNull(observer);
		LockPriority.requireInRange(lockPriority);

		// Create a new lock if the job has lock keys, else null
		var lock = job.getLockKeys().isEmpty() ? null : new TaskLock(lockPriority, job.getLockKeys());
		var task = new BoundTask(new LifecycleHandle(), job, persistence, observer, lock);

		schedule(task, loop);
		return task;
	}

	public BoundTask schedule(Supplier<Task> jobSupplier, TaskPersistence persistence, Observer observer, int lockPriority, Loop loop) {
		var deferJob = new DeferredTask(this, jobSupplier, persistence, observer, lockPriority);
		return schedule(deferJob, persistence, observer, LockPriority.NULL);
	}


	/**
	 * Schedule a {@link Task}.
	 *
	 * @param job The task to schedule.
	 * @param persistence The task's {@link TaskPersistence}.
	 * @param observer The task's observer.
	 * @param lockPriority The task's lock priority. Must be a value
	 *					 from the {@link LockPriority} interface.
	 * @param period The period to run the task on.
	 *
	 * @see TaskScheduler#schedule(Task, TaskPersistence, Observer, int)
	 *
	 * @throws NullPointerException If any of {@code job}, {@code persistence}, or {@code observer}
	 *							  are null.
	 * @throws IllegalArgumentException If {@code lockPriority} is not valid.
	 */
	public BoundTask schedule(Task job, TaskPersistence persistence, Observer observer, int lockPriority, long period) {
		return schedule(job, persistence, observer, lockPriority, loopStore.ofPeriod(period));
	}

	/**
	 * Schedule a {@link Task}.
	 *
	 * @param job The task to schedule.
	 * @param persistence The task's {@link TaskPersistence}.
	 * @param observer The task's observer.
	 * @param lockPriority The task's lock priority. Must be a value
	 *					 from the {@link LockPriority} interface.
	 *
	 * @throws NullPointerException If any of {@code job}, {@code persistence}, or {@code observer}
	 *							  are null.
	 * @throws IllegalArgumentException If {@code lockPriority} is not valid.
	 */
	public BoundTask schedule(Task job, TaskPersistence persistence, Observer observer, int lockPriority) {
		return schedule(job, persistence, observer, lockPriority, loopStore.main);
	}

	/**
	 * Schedule a default command. Default commands have the lowest possible lock priority
	 * and thus will be suspended if another command shares a lock key. This method always
	 * uses the {@link Observer#DEFAULT default observer}, meaning if the command finishes
	 * it will be rescheduled.
	 *
	 * @param command The command to schedule.
	 * @param persistence The task's persistence.
	 *
	 * @throws NullPointerException If {@code command} or {@code persistence} are null.
	 */
	public BoundTask scheduleDefaultCommand(Command command, TaskPersistence persistence) {
		Objects.requireNonNull(command);
		return schedule(new CommandTask(command), persistence, Observer.DEFAULT, LockPriority.LOWEST);
	}

	/**
	 * Schedule a default command. Default commands have the lowest possible lock priority
	 * and thus will be suspended if another command shares a lock key. This method always
	 * uses the {@link Observer#DEFAULT default observer}, meaning if the command finishes
	 * it will be rescheduled.
	 *
	 * @param command The command to schedule.
	 * @param persistence The task's persistence.
	 * @param period The period to run the task on.
	 *
	 * @throws NullPointerException If {@code command} or {@code persistence} are null.
	 */
	public void scheduleDefaultCommand(Command command, TaskPersistence persistence, long period) {
		Objects.requireNonNull(command);
		schedule(new CommandTask(command), persistence, Observer.DEFAULT, LockPriority.LOWEST, period);
	}

	/**
	 * Schedule an auto command. Similarly to default commands, auto commands are not bound
	 * to any buttons. They however have the highest task priority, meaning other tasks are
	 * not able to take ownership of their locks. They also use the {@link Observer#NO_OP
	 * no-op observer}, allowing them to terminate once the command finishes. Auto commands
	 * are also always {@link TaskPersistence#EPHEMERAL ephemeral}.
	 *
	 * @param command The command to schedule.
	 *
	 * @throws NullPointerException If {@code command} is null.
	 */
	public void scheduleAutoCommand(Command command) {
		Objects.requireNonNull(command);
		schedule(new CommandTask(command), TaskPersistence.EPHEMERAL, Observer.NO_OP, LockPriority.HIGHEST);
	}

	/**
	 * Schedule an auto command. Similarly to default commands, auto commands are not bound
	 * to any buttons. They however have the highest task priority, meaning other tasks are
	 * not able to take ownership of their locks. They also use the {@link Observer#NO_OP
	 * no-op observer}, allowing them to terminate once the command finishes. Auto commands
	 * are also always {@link TaskPersistence#EPHEMERAL ephemeral}.
	 *
	 * @param command The command to schedule.
	 * @param period The period to run the task on.
	 *
	 * @throws NullPointerException If {@code command} is null.
	 */
	public void scheduleAutoCommand(Command command, long period) {
		Objects.requireNonNull(command);
		schedule(new CommandTask(command), TaskPersistence.EPHEMERAL, Observer.NO_OP, LockPriority.HIGHEST, period);
	}
}
