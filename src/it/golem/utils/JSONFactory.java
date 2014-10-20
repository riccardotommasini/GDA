package it.golem.utils;

import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.resources.MCDataResource;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class JSONFactory {

	public static <E> JSONObject writeJson(E e) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, JSONException {
		Class<?> cl = e.getClass();
		JSONObject json = new JSONObject();

		Method[] methods = cl.getDeclaredMethods();

		for (Method m : methods) {
			JSONObject j = null;
			if (m.getName().startsWith("get")) {
				j = checkPrimitive(m, e);
				if (j != null)
					json.put(m.getName().substring(3), j);
				else
					json.put(m.getName().substring(3),
							m.invoke(e, (Object[]) null));

			} else if (m.getName().startsWith("is")) {
				j = checkPrimitive(m, e);
				if (j != null)
					json.put(m.getName().substring(2), j);
				else
					json.put(m.getName().substring(2),
							m.invoke(e, (Object[]) null));
			}

		}
		return json;

	}

	private static <E> JSONObject checkPrimitive(Method m, E e)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, JSONException {
		if (m.isAccessible() && !m.getReturnType().isPrimitive()
				&& !isWrapper(m.getReturnType())
				&& m.getParameterTypes().length == 0
				&& m.getGenericParameterTypes().length == 0
				&& m.getTypeParameters().length == 0) {
			return writeJson(m.invoke(e, (Object[]) null));
		} else {
			return null;
		}

	}

	private static boolean isWrapper(Class<?> c) {
		return (c.equals(String.class) || c.equals(Integer.class)
				|| c.equals(Boolean.class) || c.equals(Float.class)
				|| c.equals(Double.class) || c.equals(Long.class)
				|| c.equals(Void.class) || c.equals(Byte.class)
				|| c.equals(Short.class) || c.equals(Date.class));
	}

	public static void writeJSON(PrintWriter out, List<Route> routes)
			throws IOException {
		try {
			JSONObject json;
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < routes.size(); i++) {
				Route route = routes.get(i);
				json = new JSONObject();
				json.put("country", route.getCountry().getKey().getName());
				json.put("customer", route.getCustomer().getKey().getName());
				jsonArray.put(json);
			}

			out.print(jsonArray);
			out.flush();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeJSON(PrintWriter out, List<Route> routes,
			String msg, Integer... days) throws ClassNotFoundException,
			NoSuchMethodException {
		JSONObject json = new JSONObject();
		try {
			if (msg != null) {
				json.put("message", msg);
			} else {
				for (Integer day : days) {
					JSONArray jarrDay = new JSONArray();
					JSONObject jsonRoute;
					for (Route r : routes) {
						jsonRoute = new JSONObject();

						JSONArray jarrMeasure = new JSONArray();
						listToJson(day, jarrDay, jsonRoute, r, jarrMeasure);
					}

					if (jarrDay.length() > 0)
						json.put(day.toString(), jarrDay);
				}

			}
			out.print(json);
			out.flush();
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SecurityException | JSONException e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}

	private static void listToJson(Integer day, JSONArray jarrDay,
			JSONObject jsonRoute, Route r, JSONArray jarrMeasure)
			throws ClassNotFoundException, IllegalAccessException,
			InvocationTargetException, JSONException, NoSuchMethodException {
		for (MCData measure : MCDataResource.getAllReceived(day, r)) {
			jarrMeasure.put(measure.toJson());
		}
		if (jarrMeasure.length() > 0)
			jsonRoute.put(r.toString(), jarrMeasure);
		if (jsonRoute.length() > 0)
			jarrDay.put(jsonRoute);
	}

	public static void writeJSON(PrintWriter out,
			Map<Integer, List<Route>> routesMap) {

		try {
			JSONArray jarr = new JSONArray();

			for (Integer day : routesMap.keySet()) {

				List<Route> routes = routesMap.get(day);
				for (Route r : routes) {
					JSONObject json = new JSONObject();
					json.put("country", r.getCountry().getKey().getName());
					json.put("customer", r.getCustomer().getKey().getName());
					json.put("day", day);
					json.put("order", routes.indexOf(r));
					jarr.put(json);
				}

			}
			out.print(jarr);
			out.flush();
		} catch (JSONException e) {
			System.out.println("Error");
			e.printStackTrace();
		}

	}

}
