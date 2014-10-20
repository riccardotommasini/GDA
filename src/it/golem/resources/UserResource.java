package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.users.User;
import it.golem.web.enums.AccessRole;

import java.util.List;

import com.googlecode.objectify.Key;

public class UserResource {
	
	private static final Class<User> clazz = User.class;

	public static User put(User user) {
		return OfyService.get(OfyService.save(user));
	}

	public static User getByEmail(String email) {
		return OfyService.query(clazz, "email", email).first().safe();
	}

	public static User get(String id) {
		return OfyService.get(clazz, id);
	}

	public static List<User> getAll(AccessRole role) {
		return OfyService.query(clazz).filter("role", role).list();
	}
	
	public static List<User> getAll(AccessRole role, User u) {
		return OfyService.query(clazz).filter("role", role).filterKey("!=", Key.create(u)).list();
	}

}
