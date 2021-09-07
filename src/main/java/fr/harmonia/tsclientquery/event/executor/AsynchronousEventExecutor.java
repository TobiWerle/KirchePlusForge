package fr.harmonia.tsclientquery.event.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * asynchronous implementation of {@link RunnablesExecutor}
 * 
 * @author ATE47
 */
public class AsynchronousEventExecutor implements RunnablesExecutor, Runnable {
	private final BlockingQueue<Runnable> runnables = new LinkedBlockingQueue<Runnable>();
	private Thread thread;

	@Override
	public void add(Runnable runnable) {
		runnables.add(runnable);
	}

	@Override
	public void run() {
		while (!thread.isInterrupted()) {
			try {
				runnables.take().run();
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	@Override
	public synchronized void start() {
		stop();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public synchronized void stop() {
		if (thread != null) {
			thread.interrupt();
			runnables.clear();
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}
	}

}
