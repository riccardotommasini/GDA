package it.golem.web.servlet.data;

import it.golem.model.users.User;
import it.golem.resources.UserResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

public class UserServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserServlet() {
		super(AccessRole.ADMIN, UserServlet.class);
	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		User u = UserResource.get(asString(nav.getParam("id")));
		if (asBoolean(nav.getParam("up"))){ 	
			u.setRole(AccessRole.next(u.getRole()));
		}
		if (asBoolean(nav.getParam("down"))) 
			u.setRole(AccessRole.prev(u.getRole()));
		UserResource.put(u);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		nav.setAttribute("admins",
				UserResource.getAll(AccessRole.ADMIN, nav.getLoggedUser()));
		nav.setAttribute("users",
				UserResource.getAll(AccessRole.USER, nav.getLoggedUser()));
		nav.setAttribute("employees",
				UserResource.getAll(AccessRole.EMPLOYEE, nav.getLoggedUser()));
		nav.fwd(PagePaths.USERS_JSP);
	}

}
