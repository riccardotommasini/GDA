package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.utils.MCDataComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.util.DatastoreUtils;

public class RouteResource {

	private static Class<Route> clazz = Route.class;

	public static Route get(Key<Customer> c, String routeName) {
		Key<Route> k = DatastoreUtils.createKey(c, clazz, routeName);
		return OfyService.get(k);
	}

	public static Route getCreate(Customer cu, Country co) {
		try {
			return OfyService.get(DatastoreUtils.createKey(Key.create(cu),
					clazz, cu.getName() + co.getName()));
		} catch (NotFoundException e) {
			Route r = new Route(cu.getName(), co.getName(), Ref.create(cu),
					Ref.create(co));
			return OfyService.get(put(r));
		}
	}

	public static Key<Route> put(Route r) {
		return OfyService.save(r);
	}

	public static List<Route> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<Route> getAll(Customer parent) {
		List<Route> list = OfyService.query(clazz, parent).list();
		return list;
	}

	/**
	 * @param d
	 * @return list of all the route received the day d order by minutes
	 */
	public static List<Route> get(DateTime d) {
		List<MCData> measures = MCDataResource.get(d);
		System.out.println(measures.size());
		Collections.sort(measures, new MCDataComparator());
		List<Route> routeOrder = new ArrayList<Route>(); // TODO test with
															// hashset e repeate
															// the query
															// different times
															// in sequence
		// to understand why it reaches on result less each time
		// or
		for (MCData measure : measures) {
			Route r = measure.getRoute().get();
			if (r != null && !routeOrder.contains(r))
				routeOrder.add(r);
		}
		return routeOrder;
	}

}
