package fr.harmonia.tsclientquery;

import fr.harmonia.tsclientquery.query.NoAnswerQuery;
import fr.harmonia.tsclientquery.query.Query;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

class QueryWritter extends Thread {
	/**
	 * stupid query to say we're still here
	 * 
	 * @author ATE47
	 *
	 */
	private static class OkQuery extends NoAnswerQuery {

		public OkQuery() {
			super("ok");
		}

	}

	private TSClientQuery client;

	private OutputStream stream;

	public QueryWritter(TSClientQuery client, OutputStream stream) {
		this.client = client;
		this.stream = stream;
	}

	@Override
	public void run() {
		PrintStream writter = new PrintStream(stream);
		while (!this.isInterrupted()) {
			try {
				Query<?> q = client.queue.poll(20L, TimeUnit.SECONDS);

				if (q == null)
					q = new OkQuery();

				synchronized (client.currentQuery) {
					client.currentQuery.set(q);

					writter.print(q.createCommand() + "\n\r");

					client.currentQuery.wait();

					client.currentQuery.set(null);
				}

				synchronized (q) {
					q.notify();
				}
				if (client.floodRate != 0)
					Thread.sleep(client.floodRate);
			} catch (InterruptedException e) {
				break;
			}
		}
		writter.close();
	}
}
