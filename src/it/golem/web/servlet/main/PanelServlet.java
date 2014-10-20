package it.golem.web.servlet.main;

import it.golem.alerting.resource.AlertResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.FileEventResource;
import it.golem.resources.RouteResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

public class PanelServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelServlet() {
		super(AccessRole.USER);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		nav.setAttribute("emails", FileEventResource.get(-1));

		nav.setAttribute("customers", CustomerResource.getAll());
		nav.setAttribute("routes", RouteResource.getAll());
		nav.setAttribute("alerts", AlertResource.getUnread());
		nav.fwd(PagePaths.PANEL_JSP);
	}

}
