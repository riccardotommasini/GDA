package it.golem.services.queue;

import it.golem.datastore.OfyService;

import java.util.logging.Logger;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.googlecode.objectify.Key;

public class DeleteMCDataTask implements DeferredTask {

	/**
	 * 
	 */
	public static final Logger _log = Logger.getLogger(DeleteMCDataTask.class
			.getName());
	private static final long serialVersionUID = 1L;
	private final Key<?> ks;

	public DeleteMCDataTask(Key<?> k) {
		this.ks = k;
	}

	@Override
	public void run() {

		OfyService.ofy().delete().key(ks);

	}
}
