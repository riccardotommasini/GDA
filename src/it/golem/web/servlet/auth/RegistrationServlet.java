package it.golem.web.servlet.auth;


import it.golem.datastore.OfyService;
import it.golem.model.users.User;
import it.golem.resources.AuthenticationResource;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class RegistrationServlet extends Controller {

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		String name = nav.getParam("name");
		String surname = nav.getParam("surname");
		String email = nav.getParam("email");
		String password = nav.getParam("password");

		if (OfyService.query(User.class, "email", email).count()>0) {
			nav.setAttribute("existingEmail", true);
			nav.setAttribute("toggleRegistration", true);
			nav.fwd(PagePaths.INDEX_JSP);
		} else {
				AuthenticationResource.createPending(name, surname, email, AuthenticationResource.hash(password));
				nav.fwd(PagePaths.REGISTRATION_JSP);
		}

	}
}
