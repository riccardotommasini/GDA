package it.golem.services.queue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class QueueManager {
	private static final Queue lineQueue = QueueFactory.getQueue("line-queue");
	private static final Queue defQueue = QueueFactory.getDefaultQueue();
	public static final int RESCHEDULE_MAX = 10;
	private static boolean balancing = true;

	public static void pushDeferredTask(String queue, String taskName,
			DeferredTask dt) {
		if ("default".equals(queue)) {
			QueueFactory.getDefaultQueue().add(
					TaskOptions.Builder.withDefaults().payload(dt));
		}
		QueueFactory.getQueue(queue).add(
				TaskOptions.Builder.withDefaults().payload(dt));
	}

	public static void pushQueue(String fileName, String line) {
		if (balancing) {
			balancing = false;
			lineQueue.add(TaskOptions.Builder.withUrl("/worker")
					.param("line", line).param("name", fileName));
		} else {
			balancing = true;
			defQueue.add(TaskOptions.Builder.withUrl("/worker")
					.param("line", line.toString()).param("name", fileName));
		}
	}

	public static int getQueueLength(String q) {
		return QueueFactory.getQueue(q).fetchStatistics().getNumTasks();
	}

	public static int queueParsing(InputStream is, String fileName) {

		int lineNum = 0;

		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = br.readLine()) != null) {
				lineNum++;

				StoreMCDataTask dt = new StoreMCDataTask(line, fileName,
						"line-queue", "measure" + lineNum);
				if (balancing) {
					balancing = false;
					pushDeferredTask("line-queue", "measure_" + dt.hashCode()
							+ "_lineNum_" + lineNum, dt);
				} else {
					balancing = true;
					pushDeferredTask("default", "measure" + lineNum, dt);
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// pushQueue(fileName, line);
		return lineNum;
	}

	public static void removeTask(String q, String t) {
		QueueFactory.getQueue(q).deleteTask(t);

	}

}
