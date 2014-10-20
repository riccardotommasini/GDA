package it.golem.datastore;

import it.golem.alerting.model.Alert;
import it.golem.alerting.model.AlertWithRoute;
import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.model.benchmark.AVGBenchmark;
import it.golem.model.benchmark.Benchmark;
import it.golem.model.benchmark.CustomerBenchmark;
import it.golem.model.events.Event;
import it.golem.model.events.FileEvent;
import it.golem.model.users.PendingUser;
import it.golem.model.users.User;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.SimpleQuery;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

/*
 * Classe DAO con cui si gestiscono le operazioni col Datastore
 * 
 * 
 * 
 * 
 * */
public class OfyService {

	static {
		JodaTimeTranslators.add(ofy().factory());
		ObjectifyService.register(Benchmark.class);
		ObjectifyService.register(AVGBenchmark.class);
		ObjectifyService.register(CustomerBenchmark.class);
		ObjectifyService.register(FileEvent.class);
		ObjectifyService.register(Event.class);
		ObjectifyService.register(Alert.class);
		ObjectifyService.register(AlertWithRoute.class);
		ObjectifyService.register(MCData.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(PendingUser.class);
		ObjectifyService.register(Country.class);
		ObjectifyService.register(Customer.class);
		ObjectifyService.register(Route.class);

	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();// prior to v.4.0 use .begin() ,
										// since v.4.0 use
										// ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

	public static <E> E get(Class<E> clazz, Long id) throws NotFoundException {
		return secureEntity(ofy().load().type(clazz).id(id));
	}

	public static <E> E get(Class<E> clazz, String id) throws NotFoundException {
		return secureEntity(ofy().load().type(clazz).id(id));
	}

	public static <E> E get(Key<E> k) {
		return secureEntity(ofy().load().key(k));
	}

	public static <E> Ref<E> getRef(Class<E> clazz, Long id)
			throws NotFoundException {
		return Ref.create(secureEntity(ofy().load().type(clazz).id(id)));
	}

	public static <E> Ref<E> getRef(Class<E> clazz, String id)
			throws NotFoundException {
		return Ref.create(secureEntity(ofy().load().type(clazz).id(id)));
	}

	public static <E> Key<E> getKey(Class<E> clazz, Long id)
			throws NotFoundException {
		return Key.create(secureEntity(ofy().load().type(clazz).id(id)));
	}

	public static <E> Key<E> getKey(Class<E> clazz, String id)
			throws NotFoundException {
		return Key.create(secureEntity(ofy().load().type(clazz).id(id)));
	}

	public static <E> Key<E> saveSync(final E clazz) {
		E e = ofy().transact(new Work<E>() {
			@Override
			public E run() {
				Key<E> k = ofy().save().entity(clazz).now();
				return ofy().load().key(k).now();
			}
		});
		return Key.create(e);
	}

	public static <E> Key<E> save(final E clazz) {
		return ofy().save().entity(clazz).now();
	}

	public static <E> LoadType<E> getAll(Class<E> clazz) {
		return ofy().load().type(clazz);
	}

	public static <E, T> Query<E> query(Class<E> clazz, T parent,
			String filter, Object filterObj, Integer limit)
			throws NotFoundException {
		if (limit != null)
			return query(clazz, parent, filter, filterObj).limit(limit);
		else
			return query(clazz, parent, filter, filterObj);
	}

	public static <E, T> Query<E> query(Class<E> clazz, T parent,
			String filter, Object filterObj) throws NotFoundException {
		if (parent != null)
			return query(clazz, filter, filterObj).ancestor(parent);
		else
			return query(clazz, filter, filterObj);
	}

	public static <E, T> Query<E> query(Class<E> clazz, T parent)
			throws NotFoundException {
		return load(clazz).ancestor(parent);
	}

	public static <E, T> Query<E> query(Class<E> clazz)
			throws NotFoundException {
		return load(clazz);
	}

	public static <E> Query<E> query(Class<E> clazz, String filter,
			Object filterObj, Integer limit) {
		if (limit != null)
			return query(clazz, filter, filterObj).limit(limit);
		else
			return query(clazz, filter, filterObj);
	}

	public static <E> Query<E> query(Class<E> clazz, String filter,
			Object filterObj) {
		return load(clazz).filter(filter, filterObj);
	}

	public static <E, T> SimpleQuery<E> query(Class<E> clazz, T parent,
			Key<?> key) throws NotFoundException {
		if (parent != null)
			return load(clazz).ancestor(parent).filterKey(key);
		else
			return query(clazz, key);
	}

	public static <E, T> SimpleQuery<E> query(Class<E> clazz, Key<?> key)
			throws NotFoundException {
		return load(clazz).filterKey(key);
	}

	public static <E> Query<E> query(Query<E> query, String filter,
			Object filterObj, Integer limit) {
		return query.filter(filter, filterObj).limit(limit);
	}

	public static <E> Query<E> query(Query<E> query, String filter,
			Object filterObj) {
		return query.filter(filter, filterObj);
	}

	public static <E> Query<E> query(Query<E> query, Integer limit) {
		return query.limit(limit);
	}

	public static <E> int count(Class<E> clazz) {
		return load(clazz).count();
	}

	public static <E> void delete(E e) {
		ofy().delete().entity(e);
	}

	public static void deleteAll() {
		ofy().delete().keys(ofy().load().keys());
	}

	public static void deleteAllClass(Class<?> c) {
		ofy().delete().keys(ofy().load().type(c).keys());
	}

	private static <E> LoadType<E> load(Class<E> clazz) {
		return ofy().load().type(clazz);
	}

	private static <E> E secureEntity(LoadResult<E> l) throws NotFoundException {
		if (l.safe() != null)
			return l.safe();
		else
			throw new NotFoundException();
	}

}
