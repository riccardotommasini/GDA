package it.golem.web.servlet.json;

import it.golem.datastore.OfyService;
import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.model.benchmark.AVGBenchmark;
import it.golem.resources.CountryResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.MCDataResource;
import it.golem.resources.RouteResource;
import it.golem.services.CacheService;
import it.golem.utils.MCDataUtils;
import it.golem.web.enums.AccessRole;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

public class SlidingBenchmarkServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Logger _log = Logger
			.getLogger(SlidingBenchmarkServlet.class.getName());

	public SlidingBenchmarkServlet() {
		super(AccessRole.ADMIN);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		nav.setContentType("application/json");
		try {
			String country = asString(nav.getParam("country"));
			String customer = asString(nav.getParam("customer"));
			// Integer day = asInteger(nav.getParam("day"));

			Customer cu = CustomerResource.get(customer).get(0);
			Route r = RouteResource.get(Key.create(cu), customer + country);

			Map<Integer, List<MCData>> week = MCDataResource
					.getLastDays(r, 7);
			JSONArray responseJson = new JSONArray();
			List<MCData> benchmarkList = new ArrayList<MCData>();
			MCData m;
			for (int hour = 0; hour < week.keySet().size(); hour++) {
				m = MCDataUtils.avg(week.get(hour));

				JSONObject jmeasure;
				if (m == null) {
					m = MCDataUtils.getEmpty(r, hour);
				}
				m.setHour(hour);

				benchmarkList.add(m);
				jmeasure = m.toJson();
				responseJson.put(hour, jmeasure);
			}

			String dateName = ((new SimpleDateFormat("yyyyMMdd"))
					.format(new Date()));
			String name = dateName + "_" + customer + "_" + country
					+ "_benchmark";
			CacheService.caching(name, new AVGBenchmark(Ref.create(r),
					benchmarkList.size(), Ref.create(nav.getLoggedUser()),
					benchmarkList, name, false, true));
			nav.getWriter().print(responseJson);
			nav.getWriter().flush();

		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | JSONException e) {

			PrintWriter out = nav.getWriter();
			out.println("Stack Trace:<br/>");
			e.printStackTrace(out);
			out.println("<br/><br/>Stack Trace (for web display):</br>");
			out.println(displayErrorForWeb(e));
			_log.warning("Error" + e.getMessage().toString());
			_log.info(e.getStackTrace().toString());
		}

	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		String dateName = ((new SimpleDateFormat("yyyyMMdd"))
				.format(new Date()));

		Country country = CountryResource
				.get(asString(nav.getParam("country"))).get(0);
		Customer customer = CustomerResource.get(
				asString(nav.getParam("customer"))).get(0);

		Route route = RouteResource.get(Key.create(customer),
				customer.getName() + country.getName());

		String name = dateName + "_" + customer.getName() + "_"
				+ country.getName() + "_benchmark";
		AVGBenchmark b = (AVGBenchmark) CacheService.get(name);

		b.setAuthor(Ref.create(nav.getLoggedUser()));

		if (b.getName() == null)
			b.setName(name);

		if (b.getRoute() == null) {
			b.setRoute(Ref.create(route));
		}

		OfyService.save(b);
	}

	public String displayErrorForWeb(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String stackTrace = sw.toString();
		return stackTrace.replace(System.getProperty("line.separator"),
				"<br/>\n");
	}

}
