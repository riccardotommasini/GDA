package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.Customer;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.util.DatastoreUtils;

public class CustomerResource {

	private static Class<Customer> clazz = Customer.class;

	public static Customer put(Customer c) {
		return OfyService.get(OfyService.save(c));
	}

	public static List<Customer> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<Customer> getCreate(String... customer)
			throws NotFoundException {

		List<Customer> customers = new ArrayList<Customer>();
		try {
			for (String c : customer) {
				Key<Customer> key = DatastoreUtils.createKey(null, clazz, c);
				customers.add(OfyService.get(key));
			}
			return customers;
		} catch (NotFoundException e) {
			if (customer.length == 1)
				customers.add(OfyService.get(OfyService.save(new Customer(
						customer[0]))));
			else
				for (String c : customer) {
					customers.add(OfyService.get(OfyService
							.save(new Customer(c))));
				}

			return customers;
		}

	}

	public static List<Customer> get(String... customer)
			throws NotFoundException {

		List<Customer> customers = new ArrayList<Customer>();
		for (String c : customer) {
			Key<Customer> key = DatastoreUtils.createKey(null, clazz, c);
			customers.add(OfyService.get(key));
		}
		return customers;

	}

}
