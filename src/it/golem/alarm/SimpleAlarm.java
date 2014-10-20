package it.golem.alarm;

import it.golem.alerting.enums.AlertLevel;
import it.golem.alerting.enums.AlertMessages;
import it.golem.alerting.model.Alert;
import it.golem.alerting.resource.AlertResource;
import it.golem.model.events.FileEvent;
import it.golem.resources.FileEventResource;
import it.golem.services.AlertService;
import it.golem.services.EmailService;
import it.golem.web.utils.Days;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SimpleAlarm implements Alarm<String, FileEvent> {

	//EMAIL
	public static boolean emailLimit() {
		if (FileEventResource.count(Days.TODAY) > 24) {
			Alert a = new Alert(AlertMessages.MSG_TITLE_WARNING,
					"More than 24 emails", AlertLevel.ERROR);
			AlertResource.put(a);
			EmailService.logEmail("More than 24 emails");
			return false;
		}
		return true;

	}

	public static void duplicateEmail(String message) {
		EmailService.logEmail(message);
	}

	public static void missingEmail() {
		Calendar cal = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT+2");
		cal.setTimeZone(tz);
		Date date = new Date(System.currentTimeMillis());
		cal.setTime(date);

		int currentHour = cal.get(Calendar.HOUR_OF_DAY);

		if (FileEventResource.count(Days.TODAY) == currentHour) {
			return;
		}

		EmailService.logEmail("Missing Email " + currentHour);

	}

	
	//DATA
	
	public static void duplicateMeasure(String message) {
		AlertService.alertInfo(message);
	}
	
	
	
	public static void genericError(String msg) {
		EmailService.logEmail("Error on:  " + msg);

	}

	@Override
	public void send(String t) {
		EmailService.logEmail(t);

	}

	@Override
	public void compare(String t, List<FileEvent> list) {
		// TODO Auto-generated method stub

	}

}