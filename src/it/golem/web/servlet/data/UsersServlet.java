package it.golem.web.servlet.data;

import it.golem.model.users.User;
import it.golem.resources.UserResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

public class UsersServlet extends Controller {

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		nav.setAttribute(
				"admins",
				removeLogged(UserResource.getAll(AccessRole.ADMIN),
						nav.getLoggedUser()));
		nav.setAttribute(
				"users",
				removeLogged(UserResource.getAll(AccessRole.USER),
						nav.getLoggedUser()));
		nav.fwd(PagePaths.USERS_JSP);
	}

	private List<User> removeLogged(List<User> all, User logged) {
		all.remove(logged);
		return all;
	}

}
