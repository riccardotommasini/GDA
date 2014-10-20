package it.golem.web.servlet.auth;

import it.golem.model.users.User;
import it.golem.resources.AuthenticationResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;

import javax.servlet.ServletException;

import com.googlecode.objectify.NotFoundException;

@SuppressWarnings("serial")
public class ConfirmationServlet extends Controller {

	public ConfirmationServlet(){
		super(AccessRole.UNREGISTERED);
	}
	
	protected void post(Navigation nav) throws ServletException, IOException {
		String confirmCode = nav.getParam("confirmCode");
		String resetCode = nav.getParam("resetCode");

		if (confirmCode != null) {

			User createdUser;
			try {
				createdUser = AuthenticationResource
						.completeRegistration(confirmCode);

				nav.setAttribute("resultReg", true);
				nav.setLogin(createdUser);

				nav.fwd(PagePaths.PANEL_SERVLET);
			} catch (  NotFoundException e) {
				nav.fwd(PagePaths.ERROR_JSP);
			}

		} else if (resetCode != null) {
			try {
				String newPsw = nav.getParam("newPsw");
				if (AuthenticationResource.completeResetPsw(
						AuthenticationResource.hash(newPsw), resetCode) != null) {
					nav.setAttribute("resultReset", true);
					nav.fwd(PagePaths.CONFIRMATION_JSP);
				} else
					nav.fwd(PagePaths.ERROR_JSP);
			} catch (NotFoundException e) {
				nav.fwd(PagePaths.ERROR_JSP);
			}

		} else
			nav.fwd(PagePaths.ERROR_JSP);

	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		String confirmCode = nav.getParam("confirmCode");
		String resetCode = nav.getParam("resetCode");

		if (confirmCode != null) {
			try {
				AuthenticationResource.getPending(confirmCode);
				nav.setAttribute("confirmation", true);
				nav.setAttribute("confirmCode", confirmCode);
			} catch (NotFoundException e) {
				nav.setAttribute("confirmation", false);
				nav.fwd(PagePaths.CONFIRMATION_JSP);
			}
			nav.fwd(PagePaths.CONFIRMATION_JSP);
		} else if (resetCode != null) {
			try {
				AuthenticationResource.checkResetCode(resetCode);
				nav.setAttribute("resetting", true);
				nav.setAttribute("resetCode", resetCode);
				nav.fwd(PagePaths.CONFIRMATION_JSP);
			} catch (NotFoundException e) {
				nav.setAttribute("resetting", false);
				nav.setAttribute("resetCode", resetCode);
				nav.redirect("/panel");

			}
		}
	}
}
