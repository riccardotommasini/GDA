package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.Route;
import it.golem.model.benchmark.Benchmark;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

public class BenchmarkResource {

	private static Class<Benchmark> clazz = Benchmark.class;

	public static Benchmark get(Long id) {
		return OfyService.get(clazz, id);
	}

	public static List<Benchmark> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<Benchmark> get(Route route) {
		return OfyService.query(clazz, route).list();
	}

	public static Key<Benchmark> put(Benchmark m) {
		return OfyService.save(m);

	}

	public static void deleteAll() {
		OfyService.deleteAllClass(clazz);
	}

	public static void delete(Benchmark e) {
		OfyService.delete(e);
	}

	public static void delete(Ref<Route> parent, String id) {
		Void res = OfyService.ofy().delete().type(clazz).parent(parent).id(id)
				.now();
	}

	public static <T> T get(Class<T> class1, String benchmarkName) {
		return OfyService.getAll(class1).filter("name", benchmarkName).first()
				.now();
	}

	public static Benchmark get(String name) {
		return OfyService.query(clazz, "name", name).first().now();
	}

	public static List<Benchmark> get(Route route, boolean active) {
		return OfyService.query(clazz, route, "active", active).list();
	}

}
