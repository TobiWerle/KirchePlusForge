package fr.harmonia.tsclientquery.event.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * synchronous implementation of {@link RunnablesExecutor}, the
 * {@link SynchronousEventExecutor#callRunnables()} method must be call when you
 * want to execute the runnables
 * 
 * @author ATE47
 *
 */
public class SynchronousEventExecutor implements RunnablesExecutor {
	private final BlockingQueue<Runnable> runnables = new LinkedBlockingQueue<Runnable>();

	@Override
	public void add(Runnable runnable) {
		runnables.add(runnable);
	}

	/**
	 * call the runnables
	 */
	public void callRunnables() {
		Runnable r;
		while ((r = runnables.poll()) != null)
			r.run();
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		runnables.clear();
	}

}
