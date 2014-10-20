package it.golem.utils;

import it.golem.model.MCData;

import java.util.Comparator;

public class MCDataComparator implements Comparator<MCData> {

	@Override
	public int compare(MCData m1, MCData m2) {

		return -1 * m1.getMinutes().compareTo(m2.getMinutes());

	}

}
