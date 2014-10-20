package it.golem.web.servlet.json;

import it.golem.model.events.FileEvent;
import it.golem.resources.FileEventResource;
import it.golem.utils.JSONFactory;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class EmailEventSerlvet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		try {
			String day = nav.getParam("day");
			if (day != null && !"null".equals(day))
				writeJSON(nav, Integer.parseInt(day), null);
		} catch (NumberFormatException | ClassNotFoundException
				| NoSuchMethodException e) {
			errorResponse(nav);
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void errorResponse(Navigation nav) {
		try {
			writeJSON(nav, null, "not entity for this route");
		} catch (ClassNotFoundException | NoSuchMethodException | JSONException
				| IOException e) {
			e.printStackTrace();
		}
	}

	private void writeJSON(Navigation nav, Integer day, String msg)
			throws ClassNotFoundException, NoSuchMethodException,
			JSONException, IOException {

		JSONArray jarr = new JSONArray();
		if (day != null)
			for (FileEvent e : FileEventResource.get(day)) {
				try {
					jarr.put(JSONFactory.writeJson(e));
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}

		if (msg != null) {
			JSONObject json = new JSONObject();
			json.put("message", msg);
		}

		nav.setContentType("application/json");
		PrintWriter out = nav.getWriter();
		out.print(jarr);
		out.flush();
	}
}
