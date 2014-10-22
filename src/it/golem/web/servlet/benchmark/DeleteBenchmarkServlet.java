package it.golem.web.servlet.benchmark;

import it.golem.model.Customer;
import it.golem.model.Route;
import it.golem.resources.BenchmarkResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.RouteResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

public class DeleteBenchmarkServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteBenchmarkServlet() {
		super(AccessRole.USER);
	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		String id = nav.getParam("id");
		String[] fields = id.split("_");
		Customer customer = CustomerResource.get(fields[1]).get(0);
		Route route = RouteResource.get(Key.create(customer), fields[1]
				+ fields[2]);
		BenchmarkResource.delete(Ref.create(route), id);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		post(nav);
	}

}
