package it.golem.web.utils;

import it.golem.model.users.User;
import it.golem.web.enums.AccessRole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Navigation {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public Navigation(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void fwd(String destination) throws IOException, ServletException {
		request.getRequestDispatcher(destination).forward(request, response);
	}

	public void redirect(String destination) throws IOException,
			ServletException {
		redirect(destination, true);
	}

	public void redirect(String destination, boolean isLocal)
			throws IOException, ServletException {
		String prefix = isLocal ? request.getContextPath() : "";
		response.sendRedirect(prefix + destination);
	}

	public User getLoggedUser() {
		return (User) request.getSession().getAttribute("user");
	}

	public void setLogin(User u) {
		request.getSession().setAttribute("user", u);
	}

	public void setStandardLayout(Long layout) {
		request.getSession().setAttribute("standardlayout", layout);
	}

	public void setLogout() {
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
	}

	/* Tiny wrapper around request\response objects... */
	@SuppressWarnings("unchecked")
	public <T> T getParam(String name) {
		return (T) request.getParameter(name);
	}

	public HttpServletRequest getReq() {
		return request;
	}

	public String[] getParamValues(String name) {
		return request.getParameterValues(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		return (T) request.getAttribute(name);
	}

	public <T> void setAttribute(String name, T value) {
		request.setAttribute(name, value);
	}

	public AccessRole getRole() {
		if (request != null) {
			HttpSession session = request.getSession();
			if (session != null) {
				User u = (User) session.getAttribute("user");
				if (u != null) {
					return u.getRole();
				}
			}
		}
		
		return AccessRole.UNREGISTERED;
	}

	/* send a 404 error. */
	public void sendNotFound() throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	public String getPath() {
		return request.getScheme() + "://" + request.getServerName() + ":"
				+ request.getServerPort() + request.getContextPath();
	}

	/*
	 * public MultipartFormProcesser getMultipart() throws Exception { // Create
	 * a factory for disk-based file items FileItemFactory factory = new
	 * DiskFileItemFactory(); // Create a new file upload handler
	 * ServletFileUpload upload = new ServletFileUpload(factory); // Parse the
	 * request try {
	 * 
	 * List<FileItem> items = upload.parseRequest(request); return new
	 * MultipartFormProcesser(items); } catch (FileUploadException e) { throw
	 * new Exception(e); } }
	 */

	public HttpServletResponse getRes() {
		return response;
	}

	public void setContentType(String type) {
		response.setContentType(type);

	}

	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

}
