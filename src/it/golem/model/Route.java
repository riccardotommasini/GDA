package it.golem.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route implements Serializable, Comparable<Route> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	String routeName;

	@Parent
	private Ref<Customer> customer;

	@Index
	private Ref<Country> country;

	private String sCountry;
	private String sCustomer;

	public Route(String customer2, String country2, Ref<Customer> customer,
			Ref<Country> country) {
		this.routeName = customer2 + country2;
		this.customer = customer;
		this.country = country;
		this.sCountry = country2;
		this.sCustomer = customer2;
	}

	@Override
	public int compareTo(Route o) {
		return routeName.compareTo(o.getRouteName());
	}

	public JSONObject toJson() throws JSONException {

		JSONObject json = new JSONObject();
		json.put("customer", sCountry);
		json.put("country", sCountry);
		return json;
	}

}
