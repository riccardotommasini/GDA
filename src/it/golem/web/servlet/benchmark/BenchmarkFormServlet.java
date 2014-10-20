package it.golem.web.servlet.benchmark;

import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.Route;
import it.golem.model.benchmark.CustomerBenchmark;
import it.golem.resources.BenchmarkResource;
import it.golem.resources.CountryResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.RouteResource;
import it.golem.utils.Descriptor;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.exceptions.RequiredAttributeException;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.googlecode.objectify.Ref;

public class BenchmarkFormServlet extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static List<String> attributes;

	public BenchmarkFormServlet() {
		super(AccessRole.ADMIN, BenchmarkFormServlet.class);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		attributes = Descriptor.describe(CustomerBenchmark.class,
				"serialVersionUID", "id", "block", "on");

		nav.setAttribute("countries", CountryResource.getAll());
		nav.setAttribute("customers", CustomerResource.getAll());
		nav.setAttribute("attributes", attributes);
		nav.fwd(PagePaths.BENCHMARK_JSP);

	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {

		try {

			String customer = required(asString(nav.getParam("customer")));
			String country = required(asString(nav.getParam("country")));

			Customer cus = CustomerResource.getCreate(customer).get(0);
			Country cou = CountryResource.getCreate(country).get(0);
			Route r = RouteResource.getCreate(cus, cou);

			CustomerBenchmark cb = new CustomerBenchmark();
			cb.setRoute(Ref.create(r));
			cb.setName("CB_" + customer + "_" + country + "_benchmakr");
			String attr;
			if ((attr = nav.getParam("minutes")) != null && !attr.isEmpty())
				cb.setMinutes((long) Float.parseFloat((attr)));
			if ((attr = nav.getParam("ACDA")) != null && !attr.isEmpty())
				cb.setACDA(new Float(attr));
			if ((attr = nav.getParam("ACDV")) != null && !attr.isEmpty())
				cb.setACDV(new Float(attr));
			if ((attr = nav.getParam("ARSV")) != null && !attr.isEmpty())
				cb.setARSV(new Float(attr));
			if ((attr = nav.getParam("buy")) != null && !attr.isEmpty())
				cb.setBuy(new Float(attr));
			if ((attr = nav.getParam("sell")) != null && !attr.isEmpty())
				cb.setSell(new Float(attr));
			if ((attr = nav.getParam("revenue")) != null && !attr.isEmpty())
				cb.setRevenue(new Float(attr));
			if ((attr = nav.getParam("gp_percent")) != null && !attr.isEmpty())
				cb.setGpPercent(new Float(attr));
			if ((attr = nav.getParam("gp")) != null && !attr.isEmpty())
				cb.setGp(new Float(attr));
			if ((attr = nav.getParam("delta")) != null && !attr.isEmpty())
				cb.setACDA(new Float(attr));
			/*
			 * if ((attr = nav.getParam("block")) != null && !attr.isEmpty())
			 * cb.setBlock("true".equals(attr)); if ((attr = nav.getParam("on"))
			 * != null && !attr.isEmpty()) cb.setOn("true".equals(attr));
			 */
			if ((attr = nav.getParam("active")) != null && !attr.isEmpty())
				cb.setActive("true".equals(attr));
			if ((attr = nav.getParam("alwaysplot")) != null && !attr.isEmpty())
				cb.setAlwaysPlot("true".equals(attr));

			BenchmarkResource.put(cb);
			nav.redirect(PagePaths.PANEL_SERVLET);

		} catch (NumberFormatException e) {
			e.printStackTrace();
			nav.fwd(PagePaths.EXCEPTION_JSP);
		} catch (RequiredAttributeException e) {
			e.printStackTrace();
			nav.fwd(PagePaths.EXCEPTION_JSP);
		}
	}
}
