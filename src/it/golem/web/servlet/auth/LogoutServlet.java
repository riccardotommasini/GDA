package it.golem.web.servlet.auth;


import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class LogoutServlet extends Controller {

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		nav.setLogout();
		nav.redirect("/");
	}

}