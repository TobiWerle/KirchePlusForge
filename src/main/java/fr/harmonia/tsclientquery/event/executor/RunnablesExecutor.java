package fr.harmonia.tsclientquery.event.executor;

/**
 * an executor to execute runnable
 * 
 * @author ATE47
 * @see    AsynchronousEventExecutor
 * @see    SynchronousEventExecutor
 */
public interface RunnablesExecutor {
	/**
	 * add a runnable to execute
	 * 
	 * @param runnable the {@link Runnable} to add
	 */
	void add(Runnable runnable);

	/**
	 * stop this executor
	 */
	void start();

	/**
	 * stop this executor
	 */
	void stop();
}
