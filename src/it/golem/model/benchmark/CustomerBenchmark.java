package it.golem.model.benchmark;

import it.golem.enums.Routing;
import it.golem.model.MCData;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.annotation.Subclass;

@Subclass(index = true)
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CustomerBenchmark extends Benchmark implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long minutes = new Long(0);
	private Float ARSV = new Float(0); // percentage good end calls
	private Float ACDV = new Float(0); // avg conversation duration in
	private Float ACDA = new Float(0);// avg conversation duration in seconds
	private Float buy = new Float(0);// euro
	private Float sell = new Float(0);// euro
	private Float revenue = new Float(0);// euro
	private Float gpPercent = new Float(0); // gp/(sell*minutes)
	private Float gp = new Float(0); // margine (buy-sell)*minutes
	private Float delta = new Float(0); // correttivo sul margine deciso da
	private boolean on = true, block = true;

	// golem

	@Override
	public JSONObject toJson() throws JSONException {

		JSONObject retJson = new JSONObject();
		JSONArray jarr = new JSONArray();
		retJson.put("name", getName());

		for (int i = 0; i < 24; i++) {
			JSONObject json = new JSONObject();
			json.put("Minutes", minutes);
			json.put("ARSV", ARSV);
			json.put("ACDV", ACDV);
			json.put("ACDA", ACDA);
			json.put("Sell", sell);
			json.put("Buy", buy);
			json.put("Revenue", revenue);
			json.put("GpPercent", gpPercent);
			json.put("Gp", gp);
			json.put("Delta", delta);
			json.put("Hour", i);
			json.put("Block", block);
			json.put("On", on);
			jarr.put(json);
		}
		retJson.put("Active", active);
		retJson.put("Alwaysplot", alwaysPlot);
		retJson.put("measures", jarr);
		return retJson;

	}

	@Override
	public MCData toMeasure(Integer hour) {
		MCData m = new MCData();
		m.setRoute(route);
		m.setMinutes(minutes);
		m.setARSV(ARSV);
		m.setACDA(ACDA);
		m.setACDV(ACDV);
		m.setBuy(buy);
		m.setSell(sell);
		m.setRevenue(revenue);
		m.setGpPercent(gpPercent);
		m.setDelta(delta);
		m.setBlock(block);
		m.setOn(on);
		m.setMeasureDate(new DateTime());
		m.setRouting(Routing.Benchmark);
		return m;
	}

}
