package it.golem.model;

import it.golem.enums.Routing;
import it.golem.model.annotations.Checked;
import it.golem.utils.JSONFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.AlsoLoad;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MCData implements Serializable, Comparable<MCData> {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Parent
	private Ref<Route> route;

	/**
	 * creation Date is the system date at persistent time, measuredate is date
	 * of the file reception
	 **/
	private DateTime creationDate = new DateTime();
	@Index
	private DateTime measureDate;

	private Long minutes;
	private Float ARSV; // percentage good end calls
	private Float ACDV; // avg conversation duration in seconds
	private Float ACDA;// avg conversation duration in seconds
	private Float buy;// euro
	private Float sell;// euro
	private Float revenue;// euro
	@AlsoLoad("gp_percent")
	private Float gpPercent; // gp/(sell*minutes)
	private Float gp; // margine (buy-sell)*minutes
	private Float delta; // correttivo sul margine deciso da golem
	private boolean block;
	private boolean on;
	private Routing routing;
	@Index
	private int hour;

	@Override
	public int compareTo(MCData o) {
		return measureDate.compareTo(o.getMeasureDate());
	}

	public JSONObject toJson() throws ClassNotFoundException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, JSONException, NoSuchMethodException,
			SecurityException {
		return JSONFactory.writeJson(this);
	}

	public void setMeasureDate(DateTime dt) {
		this.measureDate = dt;
		this.hour = dt.getHourOfDay();
	}

	@Checked
	public Float getACDA() {
		return ACDA;
	}

	@Checked
	public Float getGp() {
		return gp;
	}

	@Checked
	public Float getARSV() {
		return ARSV;
	}

}
