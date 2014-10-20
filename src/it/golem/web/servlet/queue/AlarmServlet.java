package it.golem.web.servlet.queue;

import it.golem.alarm.Alarm;
import it.golem.alarm.BenchmarkAlarm;
import it.golem.model.MCData;
import it.golem.model.benchmark.Benchmark;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

public class AlarmServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6367141490410857607L;
	private static final Alarm<Benchmark, MCData> alarm = new BenchmarkAlarm();
	private static final boolean active = true;

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		// try {
		// String country = required(asString(nav.getParam("country")));
		// String customer = required(asString(nav.getParam("customer")));
		//
		// // Country cou = CountryResource.get(country).get(0);
		// Customer cos = CustomerResource.get(customer).get(0);
		//
		// Route route = RouteResource
		// .get(Key.create(cos), customer + country);
		//
		// List<Measure> measures = MeasureResource.getAllReceivedToday(route);
		// Collections.sort(measures);
		//
		// List<Benchmark> benchmarks = BenchmarkResource.get(route, active);
		//
		// for (Benchmark benchmark : benchmarks) {
		// if (benchmark.isActive())
		// alarm.compare(benchmark, measures);
		// }
		//
		// } catch (RequiredAttributeException e) {
		// System.out.println("missing args");
		// e.printStackTrace();
		// } catch (AlarmException e) {
		// System.out.println("Alarm Exception " + e.getMessage());
		// e.printStackTrace();
		// }

	}

}
