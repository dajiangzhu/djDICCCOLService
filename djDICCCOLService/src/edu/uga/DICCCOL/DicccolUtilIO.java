package edu.uga.DICCCOL;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.uga.liulab.djVtkBase.djVtkData;
import edu.uga.liulab.djVtkBase.djVtkPoint;
import edu.uga.liulab.djVtkBase.djVtkUtil;

public class DicccolUtilIO {

	public static double[][] loadFileAsArray(String fileName, int dimRow, int dimColumn) {
		double[][] result = new double[dimRow][dimColumn];
		List<String> tmpResult = DicccolUtilIO.loadFileToArrayList(fileName);
		if (tmpResult.size() != dimRow) {
			System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimRow is incorrect!");
			System.exit(0);
		} else {
			for (int i = 0; i < dimRow; i++) {
				String[] tmpLine = tmpResult.get(i).split("\\s+");
				if (tmpLine.length != dimColumn) {
					System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimColumn is incorrect in line-!" + i);
					System.exit(0);
				} else {
					for (int j = 0; j < dimColumn; j++)
						result[i][j] = Double.valueOf(tmpLine[j]);
				} // else
			} // for
		} // else
		return result;
	}
	
	public static double[][] loadFileAsArray(String fileName, int dimRow, int dimColumn, String separator) {
		double[][] result = new double[dimRow][dimColumn];
		List<String> tmpResult = DicccolUtilIO.loadFileToArrayList(fileName);
		if (tmpResult.size() != dimRow) {
			System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimRow is incorrect!");
			System.exit(0);
		} else {
			for (int i = 0; i < dimRow; i++) {
				String[] tmpLine = tmpResult.get(i).split(separator);
				if (tmpLine.length != dimColumn) {
					System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimColumn is incorrect in line-!" + i);
					System.exit(0);
				} else {
					for (int j = 0; j < dimColumn; j++)
						result[i][j] = Double.valueOf(tmpLine[j]);
				} // else
			} // for
		} // else
		return result;
	}
	
	public static int[][] loadFileAsIntArray(String fileName, int dimRow, int dimColumn) {
		int[][] result = new int[dimRow][dimColumn];
		List<String> tmpResult = DicccolUtilIO.loadFileToArrayList(fileName);
		if (tmpResult.size() != dimRow) {
			System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimRow is incorrect!");
			System.exit(0);
		} else {
			for (int i = 0; i < dimRow; i++) {
				String[] tmpLine = tmpResult.get(i).split("\\s+");
				if (tmpLine.length != dimColumn) {
					System.out.println("Error in DicccolUtilIO.loadFileAsArray: dimColumn is incorrect in line-!" + i);
					System.exit(0);
				} else {
					for (int j = 0; j < dimColumn; j++)
						result[i][j] = Integer.valueOf(tmpLine[j]);
				} // else
			} // for
		} // else
		return result;
	}
	
	public static String[][] loadFileAsStringArray(String fileName, int dimRow, int dimColumn) {
		String[][] result = new String[dimRow][dimColumn];
		List<String> tmpResult = DicccolUtilIO.loadFileToArrayList(fileName);
		if (tmpResult.size() != dimRow) {
			System.out.println("Error in DicccolUtilIO.loadFileAsStringArray: dimRow is incorrect!");
			System.exit(0);
		} else {
			for (int i = 0; i < dimRow; i++) {
				String[] tmpLine = tmpResult.get(i).trim().split("\\s+");
				if (tmpLine.length != dimColumn) {
					System.out.println("Error in DicccolUtilIO.loadFileAsStringArray: dimColumn is incorrect in line-!" + i);
					System.exit(0);
				} else 
					result[i]=tmpLine;
			} // for
		} // else
		return result;
	}

	public static List<String> loadFileToArrayList(String fileName) {
		List<String> resultList = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				resultList.add(strLine);
			}// while
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return resultList;
	}
	
	public static List<Integer> loadFileToIntegerArrayList(String fileName) {
		List<Integer> resultList = new ArrayList<Integer>();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				// System.out.println(strLine);
				resultList.add(Integer.valueOf(strLine.trim()) );
			}// while
			br.close();
			in.close();
			fstream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return resultList;
	}
	
	public static void writeArrayToFile(double[][] dataArray,int dimRow, int dimColumn, String seperator, String fileName)
	{
		List<String> dataList = new ArrayList<String>();
		for(int i=0;i<dimRow;i++)
		{
			String tmpStr = "";
			for(int j=0;j<dimColumn;j++)
				tmpStr = tmpStr+dataArray[i][j]+seperator;
			dataList.add(tmpStr);
		}
		DicccolUtilIO.writeArrayListToFile(dataList, fileName);
	}
	
	public static void writeIntArrayToFile(int[][] dataArray,int dimRow, int dimColumn, String seperator, String fileName)
	{
		List<String> dataList = new ArrayList<String>();
		for(int i=0;i<dimRow;i++)
		{
			String tmpStr = "";
			for(int j=0;j<dimColumn;j++)
				tmpStr = tmpStr+dataArray[i][j]+seperator;
			dataList.add(tmpStr);
		}
		DicccolUtilIO.writeArrayListToFile(dataList, fileName);
	}
	
	public static void writeStringArrayToFile(String[][] dataArray,int dimRow, int dimColumn, String seperator, String fileName)
	{
		List<String> dataList = new ArrayList<String>();
		for(int i=0;i<dimRow;i++)
		{
			String tmpStr = "";
			for(int j=0;j<dimColumn;j++)
				tmpStr = tmpStr+dataArray[i][j]+seperator;
			dataList.add(tmpStr);
		}
		DicccolUtilIO.writeArrayListToFile(dataList, fileName);
	}
	
    public static void writeArrayListToFile(List<String> dataList, String fileName) {
        try {
            System.out.println("Begin to write file:" + fileName + "...");
            FileWriter fw = null;
            fw = new FileWriter(fileName);
            for (int i = 0; i < dataList.size(); i++) {
                fw.write(dataList.get(i) + "\r\n");
            }
            fw.close();
            System.out.println("Write file done!");
        } catch (IOException ex) {
            Logger.getLogger(djVtkUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	
    public static void writeIntegerListToFile(List<Integer> dataList, String fileName) {
        try {
            System.out.println("Begin to write file:" + fileName + "...");
            FileWriter fw = null;
            fw = new FileWriter(fileName);
            for (int i = 0; i < dataList.size(); i++) {
                fw.write(dataList.get(i) + "\r\n");
            }
            fw.close();
            System.out.println("Write file done!");
        } catch (IOException ex) {
            Logger.getLogger(djVtkUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void writeVtkMatrix(double[][] mat, int dimRow, int dimColumn, String fileName)
	{
		double blockwidth=100.0;//76;//4.0;
		double blockheight=4.0;
		double interval=0.1;
		int numpoint=4*dimRow*dimColumn;
		int numcell = dimRow*dimColumn;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			fw.write("# vtk DataFile Version 3.0 \r\n");
			fw.write("vtk output \r\n");
			fw.write("ASCII \r\n");
			fw.write("DATASET POLYDATA \r\n");
			fw.write("POINTS "+numpoint+" float \r\n");
			
			for (int x = 0; x < dimRow; x++) {
				for (int y = 0; y < dimColumn; y++) {
					double rx, ry;
					rx = (blockwidth+interval)*dimColumn/2 + (blockwidth+interval)*y + interval;
					ry = (blockheight+interval)*dimRow/2 - (blockheight+interval)*x + interval;
					fw.write(rx+" "+ry+" 0.0 \r\n" );
					fw.write((rx+blockwidth)+" "+ry+" 0.0\r\n");
					fw.write(rx+" "+(ry+blockheight)+" 0.0\r\n");
					fw.write((rx+blockwidth)+" "+(ry+blockheight)+" 0.0\r\n");
				}
			} //for x
			fw.write("POLYGONS "+numcell+" "+5*numcell+"\r\n");
			for (int icell = 0; icell < numcell; icell++) {
				fw.write("4 "+(4*icell)+" "+(4*icell+1)+" "+(4*icell+3)+" "+(4*icell+2)+"\r\n");
			}
			fw.write("POINT_DATA "+numpoint+"\r\n");
			fw.write("SCALARS color float \r\n");
			fw.write("LOOKUP_TABLE default \r\n");
			
			for (int x = 0; x < dimRow; x++) {
				for (int y = 0; y < dimColumn; y++) {
					float val = (float)mat[x][y];
					fw.write(val+" "+val+" "+val+" "+val+"\r\n");
				}
			}
			fw.close();	
			System.out.println("Write file: "+fileName+" done...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    public static void writeToPointsVtkFile(String fileName, List<djVtkPoint> ptList) {
        System.out.println("Begin to write file:" + fileName + "...");
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            fw.write("# vtk DataFile Version 3.0\r\n");
            fw.write("vtk output\r\n");
            fw.write("ASCII\r\n");
            fw.write("DATASET POLYDATA\r\n");
            // print points info
            fw.write("POINTS " + ptList.size() + " float\r\n");
            for (int j = 0; j < ptList.size(); j++) {
                fw.write(ptList.get(j).x + " "
                        + ptList.get(j).y + " "
                        + ptList.get(j).z + "\r\n");
            }
            // print VERTICES info
            fw.write("VERTICES " + ptList.size() + " " + ptList.size() * 2 + " \r\n");
            for (int i = 0; i < ptList.size(); i++) {
                fw.write("1 " + i + "\r\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(djVtkData.class.getName()).log(Level.SEVERE, null,
                    ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(djVtkData.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
        System.out.println("Write file done!");
        System.out.println("That is all!");
    }
    
    public static void writeVtkMatrix1(double[][] mat, int dimRow, int dimColumn, String fileName)
	{
		double blockwidth=4.0;
		double blockheight=4.0;
		double interval=0.0;
		int numpoint=4*dimRow*dimColumn;
		int numcell = dimRow*dimColumn;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			fw.write("# vtk DataFile Version 3.0 \r\n");
			fw.write("vtk output \r\n");
			fw.write("ASCII \r\n");
			fw.write("DATASET POLYDATA \r\n");
			fw.write("POINTS "+numpoint+" float \r\n");
			
			for (int x = 0; x < dimRow; x++) {
				for (int y = 0; y < dimColumn; y++) {
					double rx, ry;
					rx = (blockwidth+interval)*dimColumn/2 + (blockwidth+interval)*y + interval;
					ry = (blockheight+interval)*dimRow/2 - (blockheight+interval)*x + interval;
					//dj modified
//					rx = (blockwidth+interval)*(double)x + interval;
//					ry = (blockheight+interval)*(double)y + interval;
					//dj modified end
					fw.write(rx+" "+ry+" 0.0 \r\n" );
					fw.write((rx+blockwidth)+" "+ry+" 0.0\r\n");
					fw.write(rx+" "+(ry+blockheight)+" 0.0\r\n");
					fw.write((rx+blockwidth)+" "+(ry+blockheight)+" 0.0\r\n");
				}
			} //for x
			fw.write("POLYGONS "+numcell+" "+5*numcell+"\r\n");
			for (int icell = 0; icell < numcell; icell++) {
				fw.write("4 "+(4*icell)+" "+(4*icell+1)+" "+(4*icell+3)+" "+(4*icell+2)+"\r\n");
			}
			fw.write("POINT_DATA "+numpoint+"\r\n");
			fw.write("SCALARS color float \r\n");
			fw.write("LOOKUP_TABLE default \r\n");
			
			for (int x = 0; x < dimRow; x++) {
				for (int y = 0; y < dimColumn; y++) {
					float val = (float)mat[x][y];
					fw.write(val+" "+val+" "+val+" "+val+"\r\n");
				}
			}
			fw.close();	
			System.out.println("Write file: "+fileName+" done...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
