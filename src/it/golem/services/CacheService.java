package it.golem.services;

import it.golem.model.Route;
import it.golem.resources.RouteResource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.joda.time.DateTime;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheService {

	private static final MemcacheService syncCache = MemcacheServiceFactory
			.getMemcacheService();

	static {
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));

	}

	public static List<Route> routeCaching(DateTime d) {
		List<Route> routeOrder = RouteResource.get(d);
		syncCache.put("routeOrder", routeOrder);
		return routeOrder;
	}

	public static void caching(String key, Object o) {
		syncCache.put(key, o);
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> getList(String key) {
		return (ArrayList<E>) syncCache.get(key);
	}

	public static Object get(String key) {
		return syncCache.get(key);
	}

	public static void flush(String key) {
		if (syncCache.contains(key))
			syncCache.delete(key);
	}

	public static void flush() {
		syncCache.clearAll();

	}

}
