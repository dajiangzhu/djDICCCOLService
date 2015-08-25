package edu.uga.DICCCOL;

import java.util.Comparator;
import java.util.Map;

public class MapComparator implements Comparator<String> {
	private boolean ascending;
	private Map<String, Integer> map;

	public MapComparator(Map<String, Integer> map, boolean ascending) {
		this.ascending = ascending;
		this.map = map;
	}

	public int compare(String s1, String s2) {
		Integer x = map.get(s1);
		Integer y = map.get(s2);
		if (x.equals(y)) {
			if (this.ascending)
				return s1.compareTo(s2);
			else
				return s2.compareTo(s1);
		}
		if (this.ascending)
			return x.compareTo(y);
		else
			return y.compareTo(x);
	}

}
