package edu.uga.DICCCOL.stat;

import org.apache.commons.math3.stat.inference.TTest;


public class SimpleTTest {
	
	public double tTest(double[] dataSet1, double[] dataSet2) throws IllegalArgumentException	{
		double pValue=-10.0d;
		TTest ttest = new TTest();
		pValue = ttest.tTest(dataSet1, dataSet2);
		return pValue;
	}

}
