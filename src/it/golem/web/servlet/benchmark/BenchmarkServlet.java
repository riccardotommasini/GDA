package it.golem.web.servlet.benchmark;

import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.Route;
import it.golem.model.benchmark.Benchmark;
import it.golem.resources.BenchmarkResource;
import it.golem.resources.CountryResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.RouteResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Key;

public class BenchmarkServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BenchmarkServlet() {
		super(AccessRole.USER);
	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		boolean activation = asBoolean(nav.getParam("activation"));
		boolean alwaysPlot = asBoolean(nav.getParam("alwaysplot"));
		Benchmark b = BenchmarkResource
				.get(asString(nav.getParam("benchmark")));
		if (b != null) {
			if (activation)
				b.setActive(!b.isActive());
			if (alwaysPlot)
				b.setAlwaysPlot(!b.isAlwaysPlot());
		}

		BenchmarkResource.put(b);

		nav.setContentType("application/json");
		try {
			nav.getWriter().print(b.toJson());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nav.getWriter().flush();
		return;
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		if (asBoolean(nav.getParam("list"))) {
			Country country = CountryResource.get(
					asString(nav.getParam("country"))).get(0);
			Customer customer = CustomerResource.get(
					asString(nav.getParam("customer"))).get(0);

			Route route = RouteResource.get(Key.create(customer),
					customer.getName() + country.getName());

			List<Benchmark> benchmarks = BenchmarkResource.get(route);

			nav.setContentType("application/json");

			JSONObject json = new JSONObject();
			JSONArray jarrOptionsActive = new JSONArray();
			JSONArray jarrOptionsDisabled = new JSONArray();

			JSONArray jarrPlot = new JSONArray();
			try {
				for (Benchmark benchmark : benchmarks) {
					if (benchmark.isActive()) {
						jarrOptionsActive.put(benchmark.getName());
					} else if (!benchmark.isActive()) {
						jarrOptionsDisabled.put(benchmark.getName());
					}
					if (benchmark.isAlwaysPlot()) {
						jarrPlot.put(benchmark.toJson());
					}
				}
				json.put("toplot", jarrPlot);
				json.put("active_opt", jarrOptionsActive);
				json.put("disabled_opt", jarrOptionsDisabled);
				nav.getWriter().print(json);
				nav.getWriter().flush();
				return;
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else if (nav.getParam("benchmark") != null) {
			nav.setContentType("application/json");
			try {
				nav.getWriter().print(
						BenchmarkResource.get(Benchmark.class,
								asString(nav.getParam("benchmark"))).toJson());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			nav.getWriter().flush();
			return;
		}
		nav.redirect(PagePaths.ERROR_JSP);
	}

}
