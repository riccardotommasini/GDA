package it.golem.web.servlet.json;

import it.golem.model.Customer;
import it.golem.resources.CustomerResource;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

public class CustomersServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		nav.setContentType("application/json");
		JSONArray jarr = new JSONArray();
		for(Customer c : CustomerResource.getAll()){
			jarr.put(c.getName());
		}
		nav.getWriter().print(jarr);
		nav.getWriter().flush();

	}

}
