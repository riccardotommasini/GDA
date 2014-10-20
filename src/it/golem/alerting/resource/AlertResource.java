package it.golem.alerting.resource;

import it.golem.alerting.enums.AlertLevel;
import it.golem.alerting.enums.AlertStatus;
import it.golem.alerting.model.Alert;
import it.golem.alerting.model.AlertWithRoute;
import it.golem.datastore.OfyService;
import it.golem.model.Route;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.poi.ss.formula.functions.T;

public class AlertResource {

	public static int count(Class<T> cls) {
		return OfyService.count(cls);
	}

	public static Alert put(Alert a) {
		return OfyService.get(OfyService.save(a));
	}

	public static Alert get(Long id) {
		return OfyService.get(Alert.class, id);
	}

	public static List<Alert> getAll() {
		return OfyService.getAll(Alert.class).list();

	}

	public static List<Alert> getUnread() {
		List<Alert> list = OfyService.query(Alert.class, "status",
				AlertStatus.PENDING, 10).list();
		Collections.sort(list);
		return list;
	}

	public static List<Alert> getRead() {
		return OfyService.query(Alert.class, "status", AlertStatus.READ).list();
	}

	public static List<Alert> getByLevel(AlertLevel al) {
		return OfyService.query(Alert.class, "level", al).list();
	}

	public static List<AlertWithRoute> get(Route r) {
		return get(0, r);
	}

	public static List<AlertWithRoute> get(Integer day, Route r) {

		TimeZone gmt = TimeZone.getTimeZone("GMT+2");
		Calendar cal = Calendar.getInstance(gmt);
		Calendar cal1 = Calendar.getInstance(gmt);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		cal.add(Calendar.DATE, -1 * day);

		Date since = (gmt.inDaylightTime(cal.getTime())) ? new Date(
				cal.getTimeInMillis()) : cal.getTime();
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		cal1.set(Calendar.MILLISECOND, 0);
		cal1.add(Calendar.DATE, -1 * day + 1);
		Date to = (gmt.inDaylightTime(cal1.getTime())) ? new Date(
				cal1.getTimeInMillis()) : cal1.getTime();
		System.out.println(since + " " + to);
		List<AlertWithRoute> list = OfyService
				.query(AlertWithRoute.class, "route", r)
				.filter("measureDate >", since).filter("measureDate <", to)
				.limit(24).list();
		return list;
	}

	public static AlertWithRoute put(AlertWithRoute a) {
		return OfyService.get(OfyService.save(a));
	}

}
