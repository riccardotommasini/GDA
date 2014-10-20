package it.golem.web.servlet.auth;


import it.golem.model.users.User;
import it.golem.resources.AuthenticationResource;
import it.golem.resources.UserResource;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class LoginServlet extends Controller {

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		String email = nav.getParam("email");
		String password = nav.getParam("password");
		if ((email == null || email.equals(""))
				&& (password == null || password.equals(""))) {
			nav.setAttribute("errorLogin", true);
			nav.fwd(PagePaths.INDEX_JSP);
		} else {

			try {
				User u = UserResource.getByEmail(email);
				if (AuthenticationResource.hash(password).equals(
						u.getPasswordHash())) {
					nav.setLogin(u);
					nav.setAttribute("email", u.getEmail());
					nav.redirect("/panel");
				} else {
					nav.setAttribute("errorLogin", true);
					nav.fwd(PagePaths.INDEX_JSP);
				}

			} catch (NotFoundException nfe) {
				nav.setAttribute("errorLogin", true);
				nav.fwd(PagePaths.INDEX_JSP);
			}

		}
	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		// TODO Auto-generated method stub
		get(nav);
	}
	
	
}
