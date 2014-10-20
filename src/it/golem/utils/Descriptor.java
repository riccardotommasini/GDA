package it.golem.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Descriptor {

	public static <T> List<String> describe(Class<T> c, String... filters) {

		Field[] fields = c.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();

		for (Field f : fields) {
			if (filters == null || filters.length == 0
					|| !Arrays.asList(filters).contains(f.getName()))
				fieldNames.add(f.getName());
		}

		return fieldNames;
	}

	public static <T> Map<Class<?>, String> describeDeep(Class<T> c, String... filters) {

		Field[] fields = c.getDeclaredFields();
		Map<Class<?>, String> attrMap = new HashMap<Class<?>, String>();

		for (Field f : fields) {
			if (filters == null || filters.length == 0
					|| !Arrays.asList(filters).contains(f.getName())) {
				attrMap.put(f.getType(), f.getName());
				
			}

		}

		return attrMap;
	}

	public static <T> T fill(Class<T> c, List<String> attr, List<Object> values) {
		
		
		
		return null;
	}

}
