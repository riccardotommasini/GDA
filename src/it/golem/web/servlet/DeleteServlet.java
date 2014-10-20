package it.golem.web.servlet;

import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DeleteServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter fmt = DateTimeFormat
			.forPattern("yyyy-MM-dd");

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		// DateTime minusDays = LocalDate.parse("2014-10-19", fmt)
		// .toDateTimeAtStartOfDay().minusDays(1);
		//
		// QueryResultIterator<Key<MCData>> iterator = OfyService.ofy().load()
		// .type(MCData.class).filter("measureDate <", minusDays)
		// .limit(100000).keys().iterator();
		//
		// while (iterator.hasNext()) {
		// Key<MCData> next = iterator.next();
		// QueueManager.pushDeferredTask("clean", "" + next.hashCode(),
		// new DeleteMCDataTask(next));
		//
		// }

	}
}
