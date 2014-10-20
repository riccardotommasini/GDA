package it.golem.web.servlet.data;

import it.golem.alerting.enums.AlertStatus;
import it.golem.alerting.model.Alert;
import it.golem.alerting.resource.AlertResource;
import it.golem.web.enums.AccessRole;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;

import com.googlecode.objectify.Ref;

public class AlertServlet extends Controller {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlertServlet() {
		super(AccessRole.USER, AlertServlet.class);
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		Alert a = AlertResource.get(new Long(asString(nav.getParam("id"))));
		a.setStatus(AlertStatus.READ);
		a.setRead(new Date(System.currentTimeMillis()));
		a.setReader(Ref.create(nav.getLoggedUser()));
		AlertResource.put(a);
	}

}
