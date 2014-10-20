package it.golem.web.servlet.json;

import it.golem.model.Route;
import it.golem.model.events.FileEvent;
import it.golem.resources.FileEventResource;
import it.golem.resources.RouteResource;
import it.golem.services.CacheService;
import it.golem.utils.JSONFactory;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.googlecode.objectify.Ref;

public class RoutesServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger _log = Logger.getLogger(RoutesServlet.class
			.getName());

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		String daysQuery = asString(nav.getParam("days"));

		String[] daysString = daysQuery.split(",");
		Integer[] daysInt = new Integer[daysString.length];
		for (int i = 0; i < daysString.length; i++) {
			daysInt[i] = Integer.parseInt(daysString[i]);
		}

		Map<Integer, FileEvent> files = FileEventResource.getOrder(daysInt);
		Map<Integer, List<Route>> routesMap = new HashMap<Integer, List<Route>>();
		_log.info(files.size() + "");
		for (Integer day : files.keySet()) {
			FileEvent fe = files.get(day);
			List<Ref<Route>> refRouteOrder = fe.getRouteOrder();
			List<Route> routeOrder = new ArrayList<Route>();
			if (refRouteOrder.isEmpty()) {
				routeOrder = RouteResource.get(fe.getFileDate());
				for (Route r : routeOrder) {
					refRouteOrder.add(Ref.create(r));
				}

				fe.setRouteOrder(refRouteOrder);

				CacheService.caching("routeOrder", routeOrder);
				FileEventResource.put(fe);
			} else {
				for (Ref<Route> rr : refRouteOrder) {
					routeOrder.add(rr.get());
				}
			}
			routesMap.put(day, routeOrder);

		}

		nav.setContentType("application/json");
		JSONFactory.writeJSON(nav.getWriter(), routesMap);

	}
}
