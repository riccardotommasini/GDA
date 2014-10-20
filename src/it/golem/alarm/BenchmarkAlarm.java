package it.golem.alarm;

import it.golem.alarm.exceptions.AlarmException;
import it.golem.model.MCData;
import it.golem.model.annotations.Checked;
import it.golem.model.benchmark.Benchmark;
import it.golem.services.AlertService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class BenchmarkAlarm implements Alarm<Benchmark, MCData> {

	@Override
	public void send(Benchmark b) {
		AlertService.alert(b.getRoute().safe());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void compare(Benchmark benchmark, List<MCData> measures)
			throws AlarmException {

		for (MCData m : measures) {
			MCData mb = benchmark.toMeasure(m.getHour());
			Method[] methods = m.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(Checked.class)) {
					try {
						if (((Comparable.class.cast(method.invoke(mb,
								(Object[]) null))).compareTo(Comparable.class
								.cast(method.invoke(m, (Object[]) null))) == 1)) {
							send(benchmark);
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						throw new AlarmException(e.getCause(), e.getMessage(),
								e.getStackTrace());
					}
				}
			}
		}

	}
}
