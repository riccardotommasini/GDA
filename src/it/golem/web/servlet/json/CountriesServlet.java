package it.golem.web.servlet.json;

import it.golem.model.Country;
import it.golem.resources.CountryResource;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

public class CountriesServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
			
		nav.setContentType("application/json");
		JSONArray jarr = new JSONArray();
		for(Country c : CountryResource.getAll()){
			jarr.put(c.getName());
		}
		nav.getWriter().print(jarr);
		nav.getWriter().flush();
		
	}

}
