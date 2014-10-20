package it.golem.alerting.model;

import it.golem.alerting.enums.AlertLevel;
import it.golem.model.Route;

import java.util.Calendar;
import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
public class AlertWithRoute extends Alert {

	@Index
	private Ref<Route> route;
	@Index
	Date measureDate;

	public AlertWithRoute() {
		measureDate = new Date();
	}

	public AlertWithRoute(String msg, Ref<Route> r) {
		measureDate = new Date();
		route = r;
	}

	public AlertWithRoute(String msg, Ref<Route> r, AlertLevel level) {
		super(r.safe().toString(), msg, level);
		measureDate = new Date();
		route = r;
	}

	public Ref<Route> getRoute() {
		return route;
	}

	public Date getMeasureDate() {
		return measureDate;
	}

	public void setRoute(Ref<Route> route) {
		this.route = route;
	}

	public void setMeasureDate(Date measureDate) {
		this.measureDate = measureDate;
	}

	public int getHour() {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(measureDate);
		return calendar.get(Calendar.HOUR_OF_DAY);

	}
}
