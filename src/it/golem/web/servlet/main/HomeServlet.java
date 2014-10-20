package it.golem.web.servlet.main;

import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

public class HomeServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomeServlet() {
		super(AccessRole.UNREGISTERED, HomeServlet.class);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		if (nav.getLoggedUser() != null)
			nav.fwd(PagePaths.PANEL_SERVLET);
		else
			nav.fwd(PagePaths.INDEX_JSP);
	}

}
