package edu.uga.DICCCOL;

import java.util.Comparator;

public class ArrayComparator2 implements Comparator<Double[]> {
    private final boolean ascending;

    public ArrayComparator2( boolean ascending) {
        this.ascending = ascending;
    }

    public int compare(Double[] c1, Double[] c2) {
        final Double d1 = c1[0]+c1[1];
        final Double d2 = c2[0]+c2[1];
        int cmp = d1.compareTo(d2);
        return ascending ? cmp : -cmp;
    }
}
