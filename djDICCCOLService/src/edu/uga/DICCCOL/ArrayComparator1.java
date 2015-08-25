package edu.uga.DICCCOL;

import java.util.Comparator;

public class ArrayComparator1 implements Comparator<Double[]> {
    private final int columnToSort;
    private final boolean ascending;

    public ArrayComparator1(int columnToSort, boolean ascending) {
        this.columnToSort = columnToSort;
        this.ascending = ascending;
    }

    public int compare(Double[] c1, Double[] c2) {
        int cmp = c1[columnToSort].compareTo(c2[columnToSort]);
        return ascending ? cmp : -cmp;
    }
}
