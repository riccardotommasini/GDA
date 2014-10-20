package it.golem.services;

import it.golem.alerting.enums.AlertLevel;
import it.golem.alerting.enums.AlertMessages;
import it.golem.alerting.model.Alert;
import it.golem.alerting.model.AlertWithRoute;
import it.golem.alerting.resource.AlertResource;
import it.golem.model.Route;

import java.util.List;

import com.googlecode.objectify.Ref;

public class AlertService {

	public static void alert(Route r) {

		List<AlertWithRoute> alerts = AlertResource.get(r);

		if (alerts.size() >= 3) {
			AlertResource.put(new AlertWithRoute(AlertMessages.MSG_DANGER, Ref
					.create(r), AlertLevel.DANGER));
		} else {
			AlertResource.put(new AlertWithRoute(AlertMessages.MSG_WARNING, Ref
					.create(r), AlertLevel.WARNING));
		}

		if (alerts.size() > 1
				&& alerts.get(alerts.size() - 1).getHour() == alerts.get(
						alerts.size() - 2).getHour() - 1) {
			AlertResource.put(new AlertWithRoute(AlertMessages.MSG_DANGER, Ref
					.create(r), AlertLevel.DANGER));
		} else {
			AlertResource.put(new AlertWithRoute(AlertMessages.MSG_WARNING, Ref
					.create(r), AlertLevel.WARNING));
		}

	}

	public static void alertInfo(String msg) {

		AlertResource.put(new Alert(AlertMessages.MSG_TITLE_INFO, msg,
				AlertLevel.INFO));

	}

	public static void alertWarning(String msg) {

		AlertResource.put(new Alert(AlertMessages.MSG_TITLE_WARNING, msg,
				AlertLevel.WARNING));

	}

	public static void alertDanger(String msg) {

		AlertResource.put(new Alert(AlertMessages.MSG_TITLE_DANGER, msg,
				AlertLevel.DANGER));

	}

}
