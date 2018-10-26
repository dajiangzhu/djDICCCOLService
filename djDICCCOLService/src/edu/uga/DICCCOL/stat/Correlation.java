package edu.uga.DICCCOL.stat;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class Correlation {

	public double Correlation_Pearsons(double[] dataSet1, double[] dataSet2) {
		double correlation_p = -10.0d;
		PearsonsCorrelation correlationHandler = new PearsonsCorrelation();
		correlation_p = correlationHandler.correlation(dataSet1, dataSet2);
		if (Double.isNaN(correlation_p))
			return 0.0;
		else
			return correlation_p;
	}

}
