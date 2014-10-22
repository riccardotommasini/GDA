package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.MCData;
import it.golem.model.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.googlecode.objectify.cmd.Query;

public class MCDataResource {

	private static Class<MCData> clazz = MCData.class;
	public static final Logger _log = Logger.getLogger(MCDataResource.class
			.getName());

	public static MCData get(Long id) {
		return OfyService.get(clazz, id);
	}

	public static List<MCData> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<MCData> getAllReceivedToday(Route r) {
		return getAllReceived(1, r);
	}

	public static List<MCData> getAllReceived(Integer day, Route r) {
		DateTime today = new LocalDate().toDateTimeAtStartOfDay();
		_log.info(today.minusDays(day).toString());
		_log.info(today.plusDays(day + 1).toString());
		List<MCData> list = OfyService
				.query(clazz, r, "measureDate >", (today.minusDays(day)))
				.filter("measureDate <", (today.plusDays(day + 1)))
				.order("measureDate").list();
		return list;
	}

	public static Query<MCData> queryAllReceived(Integer day, Route r) {

		DateTime today = new LocalDate().toDateTimeAtStartOfDay().plusDays(1);
		return OfyService
				.query(clazz, r, "measureDate >", (today.minusDays(day)))
				.filter("measureDate <", (today.plusDays(day + 1)))
				.order("measureDate");
	}

	public static List<MCData> get(DateTime of) {
		_log.info(of.toString());
		List<MCData> list = OfyService.query(clazz, "measureDate", of).list();
		return list;
	}

	public static void put(MCData m) {
		OfyService.save(m);

	}

	public static Map<Integer, List<MCData>> getLastDays(Route r, int days) {
		Map<Integer, List<MCData>> week = new HashMap<Integer, List<MCData>>();
		for (int i = 0; i < 24; i++) {
			week.put(i, new ArrayList<MCData>());
		}
		DateTime today = new LocalDate().toDateTimeAtStartOfDay();

		// Today Excluded
		for (int day = 1; day < days + 1; day++) {
			for (int hour = 0; hour < 24; hour++) {
				List<MCData> list = week.get(hour);
				MCData now = OfyService
						.query(clazz, r, "measureDate >",
								(today.minusDays(day)))
						.filter("measureDate <", (today.plusDays(day + 1)))
						.order("measureDate").filter("hour", hour).first()
						.now();

				if (now != null) {
					list.add(now);
					week.put(hour, list);
				}
			}
		}
		// }
		return week;
	}
}
