package it.golem.utils;

import it.golem.enums.Routing;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.web.servlet.json.SlidingBenchmarkServlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import com.googlecode.objectify.Ref;

public class MCDataUtils {
	public static final Logger _log = Logger
			.getLogger(SlidingBenchmarkServlet.class.getName());

	public static MCData avg(List<MCData> ms) {
		if (ms == null || ms.isEmpty()) {
			_log.info("empty list");
			return null;
		}
		List<MCData> measures = new ArrayList<MCData>(ms);

		Collections.sort(measures);

		int count = 7;

		long minutes = 0;
		Float ARSV = new Float(0), ACDV = new Float(0), ACDA = new Float(0), buy = new Float(
				0), sell = new Float(0), revenue = new Float(0), gpPercent = new Float(
				0), gp = new Float(0), delta = new Float(0);

		MCData m = new MCData();
		m.setRoute(measures.get(0).getRoute());
		m.setBlock(measures.get(0).isBlock());
		m.setOn(measures.get(0).isOn());
		m.setMeasureDate(new DateTime());
		m.setRouting(measures.get(0).getRouting());

		for (MCData measure : measures) {
			if (measure != null) {
				minutes += measure.getMinutes();
				ACDV += measure.getACDV() * measure.getMinutes();
				ACDA += measure.getACDA() * measure.getMinutes();
				ARSV += measure.getARSV() * measure.getMinutes();
				buy += measure.getBuy();
				delta += measure.getDelta();
				gp += measure.getGp() * measure.getMinutes();
				gpPercent += measure.getGpPercent() * measure.getMinutes();
				sell += measure.getSell();
				revenue += measure.getRevenue();
			}
		}
		m.setMinutes(minutes / count);
		m.setSell(sell / count);
		m.setRevenue(revenue / count);
		m.setBuy(buy / count);
		m.setDelta(delta / count);
		if (minutes == 0)
			minutes = 1;

		m.setACDA(ACDA / minutes);
		m.setACDV(ACDV / minutes);
		m.setARSV(ARSV / minutes);
		m.setGp(gp / minutes);
		m.setGpPercent(gpPercent / minutes);

		return m;
	}

	private static boolean contains(MCData m, List<MCData> clean) {
		for (MCData measure : clean) {
			if (measure.getMeasureDate().equals(m.getMeasureDate())) {
				return true;
			}

		}
		return false;
	}

	public static MCData getEmpty(Route r, int hour) {
		long minutes = 0;
		Float ARSV = new Float(0), ACDV = new Float(0), ACDA = new Float(0), buy = new Float(
				0), sell = new Float(0), revenue = new Float(0), gpPercent = new Float(
				0), gp = new Float(0), delta = new Float(0);

		MCData m = new MCData();
		m.setMinutes(minutes);
		m.setSell(sell);
		m.setRevenue(revenue);
		m.setBuy(buy);
		m.setDelta(delta);

		m.setACDA(ACDA);
		m.setACDV(ACDV);
		m.setARSV(ARSV);
		m.setGp(gp);
		m.setGpPercent(gpPercent);
		m.setRoute(Ref.create(r));
		m.setBlock(false);
		m.setOn(false);
		m.setMeasureDate(new DateTime());
		m.setHour(hour);
		m.setRouting(Routing.Benchmark);
		return m;
	}
}
