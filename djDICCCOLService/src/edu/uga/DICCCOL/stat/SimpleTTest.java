package edu.uga.DICCCOL.stat;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.inference.TTestImpl;

public class SimpleTTest {
	
	public double tTest(double[] dataSet1, double[] dataSet2) throws IllegalArgumentException, MathException
	{
		double pValue=-10.0d;
		TTestImpl ttest = new TTestImpl();
		pValue = ttest.tTest(dataSet1, dataSet2);
		return pValue;
	}

}
