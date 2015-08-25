package edu.uga.DICCCOL;

import java.util.List;

public class ArrayUtil {
	public static double[][] rescale(double[][] data, int dim1, int dim2)
	{
		double tmpMax = -1000.0;
		for(int i=0;i<dim1;i++)
			for(int j=0;j<dim2;j++)
				if(data[i][j]>tmpMax)
					tmpMax = data[i][j];
		for(int i=0;i<dim1;i++)
			for(int j=0;j<dim2;j++)
				data[i][j] /= tmpMax;
		return data;
	}

}
