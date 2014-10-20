package it.golem.model.benchmark;

import it.golem.enums.Routing;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.model.users.User;
import it.golem.resources.UserResource;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfNotNull;

@Subclass(index = true)
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AVGBenchmark extends Benchmark implements Serializable {

	private static final long serialVersionUID = 1L;

	private int avgDays;

	@Index({ IfNotNull.class })
	private Ref<User> author;
	private Long[] minutes = new Long[24];
	private Float[] ARSV = new Float[24]; // percentage good end calls
	private Float[] ACDV = new Float[24]; // avg conversation duration in
	private Float[] ACDA = new Float[24];// avg conversation duration in seconds
	private Float[] buy = new Float[24];// euro
	private Float[] sell = new Float[24];// euro
	private Float[] revenue = new Float[24];// euro
	private Float[] gpPercent = new Float[24]; // gp/(sell*minutes)
	private Float[] gp = new Float[24]; // margine (buy-sell)*minutes
	private Float[] delta = new Float[24]; // correttivo sul margine deciso da
	private boolean on, block;// golem

	public AVGBenchmark(Ref<Route> r, int days, Ref<User> u,
			List<MCData> measures, String name, boolean alwaysPlot,
			boolean active) {
		super.setRoute(r);
		super.setName(name);
		this.avgDays = days;
		this.alwaysPlot = alwaysPlot;
		this.active = active;

		author = u;

		Collections.sort(measures);
		//
		// Arrays.fill(minutes, new Long(0));
		// Arrays.fill(ACDA, new Float(0));
		// Arrays.fill(ACDV, new Float(0));
		// Arrays.fill(ARSV, new Float(0));
		// Arrays.fill(buy, new Float(0));
		// Arrays.fill(sell, new Float(0));
		// Arrays.fill(revenue, new Float(0));
		// Arrays.fill(gp, new Float(0));
		// Arrays.fill(gpPercent, new Float(0));
		// Arrays.fill(delta, new Float(0));
		on = true;
		block = true;
		for (MCData m : measures) {

			minutes[m.getHour()] = m.getMinutes();
			ARSV[m.getHour()] = m.getARSV();
			ACDV[m.getHour()] = m.getACDV();
			ACDA[m.getHour()] = m.getACDA();
			buy[m.getHour()] = m.getBuy();
			sell[m.getHour()] = m.getSell();
			revenue[m.getHour()] = m.getRevenue();
			gpPercent[m.getHour()] = m.getGpPercent();
			gp[m.getHour()] = m.getGp();
			delta[m.getHour()] = m.getDelta();
			on = on && m.isOn();
			block = block && m.isBlock();
		}
	}

	@Override
	public JSONObject toJson() throws JSONException {
		JSONObject retJson = new JSONObject();
		JSONArray jarr = new JSONArray();
		retJson.put("author", UserResource.getByEmail("tomma156@gmail.com"));
		retJson.put("name", getName());

		for (int i = 0; i < 24; i++) {
			JSONObject json = new JSONObject();
			json.put("Minutes", minutes[i]);
			json.put("ARSV", ARSV[i]);
			json.put("ACDV", ACDV[i]);
			json.put("ACDA", ACDA[i]);
			json.put("Sell", sell[i]);
			json.put("Buy", buy[i]);
			json.put("Revenue", revenue[i]);
			json.put("GpPercent", gpPercent[i]);
			json.put("Gp", gp[i]);
			json.put("Delta", delta[i]);
			json.put("Hour", i);
			jarr.put(json);
		}
		retJson.put("Active", active);
		retJson.put("Alwaysplot", alwaysPlot);
		retJson.put("measures", jarr);
		return retJson;
	}

	@Override
	public String toString() {
		return "AVGBenchmark [avgDays=" + avgDays + ", author=" + author
				+ ", minutes=" + Arrays.toString(minutes) + ", ARSV="
				+ Arrays.toString(ARSV) + ", ACDV=" + Arrays.toString(ACDV)
				+ ", ACDA=" + Arrays.toString(ACDA) + ", buy="
				+ Arrays.toString(buy) + ", sell=" + Arrays.toString(sell)
				+ ", revenue=" + Arrays.toString(revenue) + ", gp_percent="
				+ Arrays.toString(gpPercent) + ", gp=" + Arrays.toString(gp)
				+ ", delta=" + Arrays.toString(delta) + ", name=" + getName()
				+ "Active " + active + "AlwaysPlot " + alwaysPlot + "]";
	}

	/**
	 * 
	 */
	@Override
	public MCData toMeasure(Integer hour) {
		hour = hour != null ? hour : 0;
		MCData m = new MCData();
		m.setRoute(route);
		m.setMinutes(minutes[hour]);
		m.setARSV(ARSV[hour]);
		m.setACDA(ACDA[hour]);
		m.setACDV(ACDV[hour]);
		m.setBuy(buy[hour]);
		m.setSell(sell[hour]);
		m.setRevenue(revenue[hour]);
		m.setGpPercent(gpPercent[hour]);
		m.setDelta(delta[hour]);
		m.setBlock(block);
		m.setOn(on);
		m.setMeasureDate(new DateTime());
		m.setRouting(Routing.Benchmark);
		return m;
	}
}
