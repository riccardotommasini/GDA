package it.golem.web.servlet.json;

import it.golem.model.Route;
import it.golem.resources.CustomerResource;
import it.golem.resources.RouteResource;
import it.golem.utils.JSONFactory;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;

public class GraphServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7568184205037171109L;

	public GraphServlet() {
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		if (nav.getParam("country") == null || nav.getParam("customer") == null
				|| nav.getParam("day") == null) {
			errorResponse(nav);
			return;
		}

		if ("null".equals(nav.getParam("country"))
				|| "null".equals(nav.getParam("customer"))
				|| "null".equals(nav.getParam("day"))) {
			errorResponse(nav);
			return;
		}
		
		String countryQuery = asString(nav.getParam("country"));
		String customerQuery = asString(nav.getParam("customer"));
		String daysQuery = asString(nav.getParam("day"));
		String[] daysString = daysQuery.split(",");
		Integer[] daysInt = new Integer[daysString.length];
		for (int i = 0; i < daysString.length; i++) {
			daysInt[i] = Integer.parseInt(daysString[i]);
		}

		if (countryQuery.isEmpty() || customerQuery.isEmpty()) {
			errorResponse(nav);
			return;
		}

		try {
			// Skip servlet
			List<Route> routes = new ArrayList<Route>();

			if ("All".equals(customerQuery)) {
				routes.addAll(RouteResource.getAll());
			} else {
				String[] customerStrings = customerQuery.split(",");
				String[] countryStrings = countryQuery.split(",");
				for (String cus : customerStrings) {
					for (String cos : countryStrings) {
						routes.add(RouteResource.get(
								Key.create(CustomerResource.get(cus).get(0)),
								cus + cos));
					}

				}

			}
			nav.setContentType("application/json");
			JSONFactory.writeJSON(nav.getWriter(), routes, null, daysInt);
		}

		catch (NotFoundException e) {
			errorResponse(nav);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			errorResponse(nav);
		} catch (SecurityException e) {
			e.printStackTrace();
			errorResponse(nav);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			errorResponse(nav);
		}

	}

	private void errorResponse(Navigation nav) {
		nav.setContentType("application/json");
		try {
			JSONFactory.writeJSON(nav.getWriter(), new ArrayList<Route>(),
					"not entity for this route", (Integer[]) null);
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
