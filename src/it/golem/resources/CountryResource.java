package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.Country;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.NotFoundException;

public class CountryResource {

	private static Class<Country> clazz = Country.class;

	public static Country put(Country c) {
		return OfyService.get(OfyService.save(c));
	}

	public static List<Country> getAll() {
		return OfyService.getAll(clazz).list();
	}

	public static List<Country> getCreate(String... country) {
		List<Country> countries = new ArrayList<Country>();
		try {
			for (String c : country) {
				countries.add(OfyService.get(clazz, c));
			}
			return countries;
		} catch (NotFoundException e) {
			if (country.length == 1)
				countries.add(OfyService.get(OfyService.save(new Country(
						country[0]))));
			else
				for (String c : country) {
					countries.add(OfyService.get(OfyService
							.save(new Country(c))));

				}

			return countries;
		}

	}

	public static List<Country> get(String... country) {
		List<Country> countries = new ArrayList<Country>();
		for (String c : country) {
			countries.add(OfyService.get(clazz, c));
		}
		return countries;

	}


}
