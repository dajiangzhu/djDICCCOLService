package edu.uga.DICCCOL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DicccolUtil {
	
	public static List<Integer> geneRandom(int numOfNeed, int maxValue) {
		Random rng = new Random(); // Ideally just create one instance globally
		List<Integer> generated = new ArrayList<Integer>();
		for (int i = 0; i < numOfNeed; i++) {
			while (true) {
				Integer next = rng.nextInt(maxValue) + 1;
				if (!generated.contains(next)) {
					// Done for this iteration
					generated.add(next);
//					 System.out.println(next);
					break;
				} // if
			} // while
		} // for
		return generated;
	}
	
	public static double meanOfMatrix(double[][] dataMatrix, int dimRow, int dimColumn)
	{
		double resultMean=0.0;
		for(int i=0;i<dimRow;i++)
			for(int j=0;j<dimColumn;j++)
				resultMean+=dataMatrix[i][j];
		resultMean/= (dimRow*dimColumn);
		return resultMean;
	}
	
	public static double[][] normalizeMatrix_1(double[][] dataMatrix, int dimRow, int dimColumn)
	{
		double tmpMax = -1000.0;
		for(int i=0;i<dimRow;i++)
			for(int j=0;j<dimColumn;j++)
				if(dataMatrix[i][j]>tmpMax)
					tmpMax = dataMatrix[i][j];
		for(int i=0;i<dimRow;i++)
			for(int j=0;j<dimColumn;j++)
				dataMatrix[i][j] /= tmpMax;
		return dataMatrix;
		
	}
	
	public static double[][] calSimMatrix(double[][] dataMatrix, int dimRow, int dimColumn)
	{
		double[][] simM = new double[dimRow][dimRow];
		for(int i=0;i<dimRow-1;i++)
			for(int j=i+1;j<dimRow;j++)
			{
				for(int k=0;k<dimColumn;k++)
					simM[i][j] += Math.pow((dataMatrix[i][k]-dataMatrix[j][k]), 2);
				simM[i][j] = Math.pow(simM[i][j],0.5);
				simM[j][i] = simM[i][j];
			}
		double tmpMax = -1000.0;
		for(int i=0;i<dimRow-1;i++)
			for(int j=i+1;j<dimRow;j++)
				if(simM[i][j]>tmpMax)
					tmpMax = simM[i][j];
		for(int i=0;i<dimRow;i++)
			for(int j=0;j<dimRow;j++)
				simM[i][j] /= tmpMax;

		for(int i=0;i<dimRow;i++)
			for(int j=0;j<dimRow;j++)
				simM[i][j] = 1.0-simM[i][j];
		return simM;
	}
	
	public static String doubleArrayToString(double[] data, int len)
	{
		String result = "";
		for(int i=0;i<len;i++)
			result += String.valueOf(data[i])+" "; 
		return result;
	}

}
