package it.golem.web.utils;

import it.golem.web.enums.AccessRole;
import it.golem.web.enums.PagePaths;
import it.golem.web.exceptions.RequiredAttributeException;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Controller extends HttpServlet {

	private static final long serialVersionUID = -3211583010566998686L;
	
	private AccessRole minimumRole;

	protected static Logger _log = null;
	protected final String BASEPATH = "/";

	public Controller() {
		this(AccessRole.UNREGISTERED);
	}

	public Controller(AccessRole role) {
		this.minimumRole = role;
		Controller._log  = Logger.getLogger(Controller.class.getName());
	}
	
	public Controller(AccessRole role, Class<?> clazz) {
		this.minimumRole = role;
		Controller._log  = Logger.getLogger(clazz.getName());
	}

	protected final void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		act('G', request, response);

	}

	protected final void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		act('P', request, response);
	}

	private void act(char type, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Navigation nav = new Navigation(request, response);
		if (checkLogin(nav)) {
			if (type == 'G') {
				get(nav);
			} else if (type == 'P') {
				post(nav);
			}
		} else {
			nav.fwd(PagePaths.ERROR_JSP);
		}
	}

	protected abstract void get(Navigation nav) throws IOException,
			ServletException;

	protected void post(Navigation nav) throws IOException, ServletException {
		get(nav);
	}

	private boolean checkLogin(Navigation nav) {
		return minimumRole.compareTo(nav.getRole()) >= 0;
	}

	protected String asString(Object o) {
		return (String) o;
	}

	protected Long asLong(Object o) throws NumberFormatException {
		return new Long((String) o);
	}

	protected Integer asInteger(Object o) throws NumberFormatException {
		return o != null ? Integer.parseInt((String) o) : 1;
	}

	protected boolean asBoolean(Object o) {
		return "true".equals((String) o);
	}

	protected <T> T required(T t) throws RequiredAttributeException {
		if (t != null)
			return t;
		else
			throw new RequiredAttributeException();
	}
	

}
