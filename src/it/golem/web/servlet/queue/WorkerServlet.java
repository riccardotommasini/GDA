package it.golem.web.servlet.queue;

import it.golem.utils.Parser;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;

public class WorkerServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		try {
			String line = nav.getParam("line");

			// hourly_20141018_005500.csv
			String dateElements = nav.getParam("name");
			Parser.parseLine(line, Parser.measureDate(dateElements), ",");

		} catch (ParseException e) {
			e.printStackTrace();
			nav.getWriter().write("Parsing Error");
		}
		nav.getWriter().write("Stored Correctly");

	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		post(nav);

	}

}
