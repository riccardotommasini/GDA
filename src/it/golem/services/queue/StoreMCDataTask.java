package it.golem.services.queue;

import it.golem.utils.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;

public class StoreMCDataTask implements DeferredTask {

	/**
	 * 
	 */

	public static final Logger _log = Logger.getLogger(StoreMCDataTask.class
			.getName());
	private static final long serialVersionUID = 1L;
	private final String line;
	private final String fileName;
	@SuppressWarnings("unused")
	private final String taskName;
	@SuppressWarnings("unused")
	private final String queue;

	public StoreMCDataTask(String line, String fileName, String queue,
			String taskName) {
		this.taskName = taskName;
		this.queue = queue;
		this.line = line;
		this.fileName = fileName;
	}

	@Override
	public void run() {

		try {
			Parser.parseLine(line, Parser.measureDate(fileName), ",");

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

	}
}
