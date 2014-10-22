package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.events.FileEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.cmd.Query;

public class FileEventResource {
	private static Class<FileEvent> clazz = FileEvent.class;
	public static final Logger _log = Logger.getLogger(FileEventResource.class
			.getName());

	public static FileEvent put(FileEvent c) {
		return OfyService.get(OfyService.save(c));
	}

	public static List<FileEvent> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<FileEvent> get(Integer day) throws NotFoundException {

		return OfyService.query(clazz, "fileDate >", getFrom(day))
				.filter("fileDate <", getTo(day)).order("fileDate").list();
	}

	public static int count(Integer day) {
		return OfyService.query(clazz, "received >", getFrom(day))
				.filter("received <", getTo(day)).count();
	}

	public static FileEvent getLast(Integer day) {
		DateTime from = new LocalDate().toDateTimeAtStartOfDay().plusDays(
				day + 1);
		DateTime to = new LocalDate().toDateTimeAtStartOfDay()
				.plusDays(day + 2);
		_log.info(from + " " + to);
		Query<FileEvent> q = OfyService.query(clazz, "fileDate >", from)
				.filter("fileDate <", to).order("- fileDate");

		return q.count() > 0 ? q.first().safe() : null;
	}

	public static boolean exitFile(String fileName) {
		return OfyService.query(clazz).filter("fileName", fileName).count() > 0;
	}

	public static Map<Integer, FileEvent> getOrder(Integer... days) {
		DateTime from = new LocalDate().toDateTimeAtStartOfDay();
		Map<Integer, FileEvent> map = new HashMap<Integer, FileEvent>();
		for (Integer day : days) {
			_log.info(from.plusDays(day + 1).toString());
			FileEvent e = OfyService
					.query(clazz, "received <", from.plusDays(day + 1))
					.order("-received").first().now();
			if (e != null)
				map.put(day, e);
		}
		return map;
	}

	private static DateTime getTo(int day) {
		DateTime today = new LocalDate().toDateTimeAtStartOfDay();
		return today.plusDays(day + 1);
	}

	private static DateTime getFrom(int day) {
		DateTime today = new LocalDate().toDateTimeAtStartOfDay();
		return today.minusDays(day);
	}

}
