package edu.uga.DICCCOL.visualization;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jmat.data.AbstractMatrix;
import org.jmat.data.Matrix;

import edu.uga.DICCCOL.DicccolUtilIO;
import edu.uga.liulab.djVtkBase.djVtkPoint;
import edu.uga.liulab.djVtkBase.djVtkSurData;
import edu.uga.liulab.djVtkBase.djVtkUtil;

public class Circos_generateDicccol {

	public void geneDicccolOnly() {
		List<String> listToWrite = new ArrayList<String>();
		String tmpStr = "";
		String tmpColor = "";
		AbstractMatrix LOCATION_DICCCOL_M = Matrix.fromASCIIFile(new File("location_dicccol_circos.txt"));
		for (int i = 0; i < 358; i++) {
			if ((i >= 0) && (i <= 85))
				tmpColor = "chcolor-occipital-right"; //"chcolor-frontal-right";
			if ((i >= 86) && (i <= 106))
				tmpColor = "chcolor-occipital-right"; //"chcolor-parietal-right";
			if ((i >= 107) && (i <= 151))
				tmpColor = "chcolor-occipital-right"; //"chcolor-temporal-right";
			if ((i >= 152) && (i <= 176))
				tmpColor = "chcolor-occipital-right";

			if ((i >= 177) && (i <= 190))
				tmpColor = "chcolor-occipital-right"; //"chcolor-occipital-left";
			if ((i >= 191) && (i <= 232))
				tmpColor = "chcolor-occipital-right"; //"chcolor-temporal-left";
			if ((i >= 233) && (i <= 272))
				tmpColor = "chcolor-occipital-right"; //"chcolor-parietal-left";
			if ((i >= 273) && (i <= 357))
				tmpColor = "chcolor-occipital-right"; //"chcolor-frontal-left";
			int dicccolID = (int) LOCATION_DICCCOL_M.get(i, 1);
			tmpStr = "chr - D_" + dicccolID + " " + dicccolID + " 0 5 " + tmpColor;

			listToWrite.add(tmpStr);
		}
		DicccolUtilIO.writeArrayListToFile(listToWrite, "karyotype.edu.uga.liulab.dicccolonly.txt");
	}
	
	public void geneDicccolOnly_2012IPMI() {
		
		List<String> colorList = new ArrayList<String>();
		for(int i=0;i<358;i++)
			colorList.add("chcolor-occipital-right");
		
		List<String> connList = DicccolUtilIO.loadFileToArrayList("./2012IPMI/Dicccol_ABHigh_A_connList.txt");
		for(int i=0;i<connList.size();i++)
		{
			String[] currentLine = connList.get(i).trim().split("-");
			int dicccol_s = Integer.valueOf( currentLine[0].trim() );
			int dicccol_r = Integer.valueOf( currentLine[1].trim() );
			colorList.set(dicccol_s, "chcolor-temporal-right");
			colorList.set(dicccol_r, "chcolor-frontal-right");
		}
		
		List<String> listToWrite = new ArrayList<String>();
		String tmpStr = "";
		String tmpColor = "";
		AbstractMatrix LOCATION_DICCCOL_M = Matrix.fromASCIIFile(new File("location_dicccol_circos.txt"));
		for (int i = 0; i < 358; i++) {

			int dicccolID = (int) LOCATION_DICCCOL_M.get(i, 1);
			tmpStr = "chr - D_" + dicccolID + " " + dicccolID + " 0 5 " + colorList.get(dicccolID);
			listToWrite.add(tmpStr);
		}
		DicccolUtilIO.writeArrayListToFile(listToWrite, "karyotype.edu.uga.liulab.dicccolonly_ipmi2012.txt");
	}

	public void geneFuncNetwork() throws BiffException, IOException {
		File inputWorkbook = new File("./dicccolFunctionNetwork_4mm.xls");
		String dirResultPre = "./functionalNetwork_circos/";
		Workbook w;
		List<List<String>> dicccolList = new ArrayList<List<String>>();
		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sheet = w.getSheet("sheet1");

		System.out.println("begin to read excel...");
		float r = 1.09f;
		float tmpR0 = r;
		float tmpR1 = tmpR0 + 0.015f;
		List<String> listPlot = new ArrayList<String>();
		String strFunctionalNetworkName = "";
		float[] numOfInvolvedNetworks = new float[358];
		List<String> resultOfInvolvedNetworks = new ArrayList<String>();

		// ///////for meta analysis network////////////////////////////////////////////////////////////
		for (int col = 1; col < 10; col++) // Action
		{
			strFunctionalNetworkName = sheet.getCell(col, 0).getContents();
			List<String> listActive = new ArrayList<String>();
			List<String> listRest = new ArrayList<String>();
			System.out.println("################" + strFunctionalNetworkName);
			for (int row = 1; row < 359; row++) {
				Cell cell = sheet.getCell(col, row);
				if (cell.getContents().trim().length() != 0) {
					listActive.add("D_" + (row - 1) + " 0 5");
					numOfInvolvedNetworks[row - 1] += 1.0;
				} else
					listRest.add("D_" + (row - 1) + " 0 5");
			} // for row
			DicccolUtilIO.writeArrayListToFile(listActive, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".active.txt");
			DicccolUtilIO.writeArrayListToFile(listRest, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".rest.txt");
			// /
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".active.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-1");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".rest.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-1b ");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			tmpR0 = tmpR1;
			tmpR1 = tmpR0 + 0.015f;
		} // for col
		
		for (int col = 46; col < 56; col++) // Perception
		{
			strFunctionalNetworkName = sheet.getCell(col, 0).getContents();
			List<String> listActive = new ArrayList<String>();
			List<String> listRest = new ArrayList<String>();
			System.out.println("################" + strFunctionalNetworkName);
			for (int row = 1; row < 359; row++) {
				Cell cell = sheet.getCell(col, row);
				if (cell.getContents().trim().length() != 0) {
					listActive.add("D_" + (row - 1) + " 0 5");
					numOfInvolvedNetworks[row - 1] += 1.0;
				} else
					listRest.add("D_" + (row - 1) + " 0 5");
			} // for row
			DicccolUtilIO.writeArrayListToFile(listActive, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".active.txt");
			DicccolUtilIO.writeArrayListToFile(listRest, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".rest.txt");
			// /
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".active.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-5");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".rest.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-5b ");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			tmpR0 = tmpR1;
			tmpR1 = tmpR0 + 0.015f;
		} // for col
		
		//*****************************************************************************************************
		for (int col = 10; col < 28; col++) // Cognition
		{
			strFunctionalNetworkName = sheet.getCell(col, 0).getContents();
			List<String> listActive = new ArrayList<String>();
			List<String> listRest = new ArrayList<String>();
			System.out.println("################" + strFunctionalNetworkName);
			for (int row = 1; row < 359; row++) {
				Cell cell = sheet.getCell(col, row);
				if (cell.getContents().trim().length() != 0) {
					listActive.add("D_" + (row - 1) + " 0 5");
					numOfInvolvedNetworks[row - 1] += 1.0;
				} else
					listRest.add("D_" + (row - 1) + " 0 5");
			} // for row
			DicccolUtilIO.writeArrayListToFile(listActive, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".active.txt");
			DicccolUtilIO.writeArrayListToFile(listRest, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".rest.txt");
			// /
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".active.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-2");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".rest.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-2b ");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			tmpR0 = tmpR1;
			tmpR1 = tmpR0 + 0.015f;
		} // for col
		
		
		//*****************************************************************************************************
		for (int col = 28; col < 36; col++) // Emotion
		{
			strFunctionalNetworkName = sheet.getCell(col, 0).getContents();
			List<String> listActive = new ArrayList<String>();
			List<String> listRest = new ArrayList<String>();
			System.out.println("################" + strFunctionalNetworkName);
			for (int row = 1; row < 359; row++) {
				Cell cell = sheet.getCell(col, row);
				if (cell.getContents().trim().length() != 0) {
					listActive.add("D_" + (row - 1) + " 0 5");
					numOfInvolvedNetworks[row - 1] += 1.0;
				} else
					listRest.add("D_" + (row - 1) + " 0 5");
			} // for row
			DicccolUtilIO.writeArrayListToFile(listActive, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".active.txt");
			DicccolUtilIO.writeArrayListToFile(listRest, dirResultPre+"highlight.edu.uga.liulab." + strFunctionalNetworkName
					+ ".rest.txt");
			// /
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".active.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-3");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			listPlot.add("<plot>");
			listPlot.add("type = highlight");
			listPlot.add("file = highlight.edu.uga.liulab." + strFunctionalNetworkName + ".rest.txt");
			listPlot.add("r0 = " + tmpR0 + "r");
			listPlot.add("r1 = " + tmpR1 + "r");
			listPlot.add("fill_color = set1-9-qual-3b ");
			listPlot.add("stroke_thickness = 0p");
			listPlot.add("stroke_color = white");
			listPlot.add("z = 15");
			listPlot.add("</plot>");
			//
			tmpR0 = tmpR1;
			tmpR1 = tmpR0 + 0.015f;
		} // for col
		//*****************************************************************************************************
		
		
			// generate plot of functional networks
		DicccolUtilIO.writeArrayListToFile(listPlot, dirResultPre+"plot.edu.uga.liulab.network.txt");

		// generate stat result of # to the involved functional networks
		for (int i = 0; i < 358; i++) {
			numOfInvolvedNetworks[i] /= 45.0;
			resultOfInvolvedNetworks.add("D_" + i + " 0 5 " + numOfInvolvedNetworks[i]);
		}
		DicccolUtilIO.writeArrayListToFile(resultOfInvolvedNetworks, dirResultPre+"stat.edu.uga.liulab.network.txt");
		listPlot.clear();
		listPlot.add("<plot>");
		listPlot.add("type = heatmap");
		listPlot.add("min = 0.0");
		listPlot.add("max = " + this.findMax(numOfInvolvedNetworks));
		listPlot.add("r0 = " + (r - 0.07) + "r");
		listPlot.add("r1 = " + (r - 0.02) + "r");
		listPlot.add("file = stat.edu.uga.liulab.network.txt");
		listPlot.add("color = spectral-11-div-rev  ");
		listPlot.add("</plot>");
		DicccolUtilIO.writeArrayListToFile(listPlot, dirResultPre+"plot.edu.uga.liulab.networkNum.txt");

	}

	public void generateLinks(String linkFile) throws IOException {
		List<String> listLinks = new ArrayList<String>();
		List<String> listLinkData = new ArrayList<String>();
		List<String> listPlot = new ArrayList<String>();
		List<String> listLinkNum = new ArrayList<String>();
		float[] linkNum = new float[358];
		int pairCount = 0;
		
		Map<Integer, List<Integer>> anaResult = this.analyzeLobe();
		
		List<String> connList = DicccolUtilIO.loadFileToArrayList(linkFile);
		for(int i=0;i<connList.size();i++)
		{
			pairCount++;
			String[] currentLine = connList.get(i).trim().split("-");
			int dicccol_ID1 = Integer.valueOf( currentLine[0].trim() );
			int dicccol_ID2 = Integer.valueOf( currentLine[1].trim() );
			
			String tmpColor = "grey_a4";
			
//			if(currentLine[2].trim().equals("2"))
//				tmpColor="chcolor-black"; //chcolor-occipital-right
//			else
//				tmpColor="chcolor-frontal-right";

			//assign color to the links according to the lobes
//			if(anaResult.get(17).contains(dicccol_ID1) && anaResult.get(17).contains(dicccol_ID2))
//				tmpColor = "chcolor-frontal-right ";
//			if(anaResult.get(105).contains(dicccol_ID1) && anaResult.get(105).contains(dicccol_ID2))
//				tmpColor = "chcolor-parietal-right  ";
//			if(anaResult.get(59).contains(dicccol_ID1) && anaResult.get(59).contains(dicccol_ID2))
//				tmpColor = "chcolor-temporal-right ";
//			if(anaResult.get(45).contains(dicccol_ID1) && anaResult.get(45).contains(dicccol_ID2))
//				tmpColor = "chcolor-occipital-right ";
//			if(anaResult.get(73).contains(dicccol_ID1) && anaResult.get(73).contains(dicccol_ID2))
//				tmpColor = "chcolor-occipital-left ";
//			if(anaResult.get(83).contains(dicccol_ID1) && anaResult.get(83).contains(dicccol_ID2))
//				tmpColor = "chcolor-temporal-left  ";
//			if(anaResult.get(57).contains(dicccol_ID1) && anaResult.get(57).contains(dicccol_ID2))
//				tmpColor = "chcolor-parietal-left  ";
//			if(anaResult.get(30).contains(dicccol_ID1) && anaResult.get(30).contains(dicccol_ID2))
//				tmpColor = "chcolor-frontal-left  ";
			//end of the assigna to the colors
				
			listLinkData.add("link" + pairCount + " D_" + dicccol_ID1 + " 0 5 param="+tmpColor);
			listLinkData.add("link" + pairCount + " D_" + dicccol_ID2 + " 0 5 param="+tmpColor);
			linkNum[dicccol_ID1]++;
			linkNum[dicccol_ID2]++;
		} //for
		// /
		String linkDataFile = linkFile+".linkdata_ipmi2012.txt";//"linkdata.edu.uga.liulab.txt";
		DicccolUtilIO.writeArrayListToFile(listLinkData, linkDataFile);

		listLinks.add("<link>");
		listLinks.add("file = "+linkDataFile);
		listLinks.add("bezier_radius = 0r");
		listLinks.add("radius = 0.89r");
		listLinks.add("thickness = 3p");
		listLinks.add("color = grey_a4");
		listLinks.add("<rules>");
		listLinks.add("<rule>");
		listLinks.add("importance = 100");
		listLinks.add("condition = 1");
		listLinks.add("color = eval(_PARAM1_)");
		listLinks.add("</rule>");
		listLinks.add("</rules>");
		listLinks.add("</link>");
		String linksFile = linkFile+".links_ipmi2012.txt"; //"links.edu.uga.liulab.txt"
		DicccolUtilIO.writeArrayListToFile(listLinks, linksFile);

		for (int i = 0; i < 358; i++)
			listLinkNum.add("D_" + i + " 0 5 " + linkNum[i]);
		String linkStatFile = linkFile+".stat_ipmi2012.txt"; //"stat.edu.uga.liulab.link.txt"
		DicccolUtilIO.writeArrayListToFile(listLinkNum, linkStatFile);
		
		listPlot.clear();
		listPlot.add("<plot>");
		listPlot.add("type = histogram");
		listPlot.add("thickness = 2p");
		listPlot.add("color = chcolor-temporal-right");
		listPlot.add("fill_color = chcolor-temporal-right");
		listPlot.add("min = 0.0");
		listPlot.add("max = " + this.findMax(linkNum));
		listPlot.add("r0 = 0.9r");
		listPlot.add("r1 = 0.98r");
		listPlot.add("file = "+linkStatFile);
		listPlot.add("</plot>");
		String plotLinkNumFile = linkFile+".plot.linkNum_ipmi2012.txt"; //"plot.edu.uga.liulab.linkNum.txt"
		DicccolUtilIO.writeArrayListToFile(listPlot, plotLinkNumFile);

	}
	
	public Map<Integer, List<Integer>> analyzeLobe()
	{
		Map<Integer, List<Integer>> anaResult = new HashMap<Integer, List<Integer>>();
		anaResult.put(30, new ArrayList<Integer>());
		anaResult.put(57, new ArrayList<Integer>());
		anaResult.put(73, new ArrayList<Integer>());
		anaResult.put(83, new ArrayList<Integer>());
		anaResult.put(17, new ArrayList<Integer>());
		anaResult.put(105, new ArrayList<Integer>());
		anaResult.put(45, new ArrayList<Integer>());
		anaResult.put(59, new ArrayList<Integer>());
		anaResult.put(0, new ArrayList<Integer>());

		AbstractMatrix DICCCOL_M = Matrix.fromASCIIFile(new File("./finalizedMat.txt"));
		djVtkSurData surData = new djVtkSurData("./10.wm.lobe.vtk");
		for (int i = 0; i < 358; i++) {
			int ptID = (int) DICCCOL_M.get(i, 0);
			int label = Integer.valueOf(surData.pointsScalarData.get("Labels").get(ptID));
			anaResult.get(label).add(i);
			System.out.println( i + " " + label);
		}
		anaResult.get(30).add(185);
		return anaResult;
	}
	
	public void geneDicccolsForLobe()
	{
		djVtkSurData surData = new djVtkSurData("./10.wm.lobe.vtk");
		AbstractMatrix DICCCOL_M = Matrix.fromASCIIFile(new File("./finalizedMat.txt"));
		List<djVtkPoint> ptList = new ArrayList<djVtkPoint>();
		Map<Integer, List<Integer>> anaResult = this.analyzeLobe();
		
		ptList.clear();
		for(int i=0;i<anaResult.get(17).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(17).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_frontal_right.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(105).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(105).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_parietal_right.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(59).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(59).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_temporal_right.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(45).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(45).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_occipital_right.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(73).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(73).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_occipital_left.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(83).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(83).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_temporal_left.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(57).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(57).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_parietal_left.vtk", ptList);
		
		ptList.clear();
		for(int i=0;i<anaResult.get(30).size();i++)
			ptList.add( surData.getPoint( (int) DICCCOL_M.get(anaResult.get(30).get(i),0) ) );
		djVtkUtil.writeToPointsVtkFile("Dicccols_frontal_left.vtk", ptList);
	}

	public float findMax(float[] data) {
		float result = 0.0f;
		for (int i = 0; i < 358; i++)
			if (data[i] > result)
				result = data[i];
		return result;
	}
	
	public void statDICCCOL() throws BiffException, IOException
	{
		File inputWorkbook = new File("./dicccolFunctionNetwork_4mm.xls");
		String dirResultPre = "./functionalNetwork_circos/";
		Workbook w;
		List<List<String>> dicccolList = new ArrayList<List<String>>();
		w = Workbook.getWorkbook(inputWorkbook);
		Sheet sheet = w.getSheet("sheet1");

		System.out.println("begin to read excel...");
		String strFunctionalNetworkName = "";
		int[] flag = new int[358];

		// ///////for meta analysis network////////////////////////////////////////////////////////////
		for (int col = 1; col < 56; col++) // for each functional network
		{
			strFunctionalNetworkName = sheet.getCell(col, 0).getContents();
			System.out.println("################" + strFunctionalNetworkName);
			for (int row = 1; row < 359; row++) {
				Cell cell = sheet.getCell(col, row);
				if (cell.getContents().trim().length() != 0) 
					flag[row-1]=1;
			} // for row
		}
		
		int count=0;
		for(int i=0;i<358;i++)
			if(flag[i]!=0)
				count++;
		System.out.println("There are total "+count+" DICCCOLs which have lebeled!" );
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws BiffException
	 */
	public static void main(String[] args) throws BiffException, IOException {
		Circos_generateDicccol mainHandler = new Circos_generateDicccol();
//		 mainHandler.geneDicccolOnly();
//		mainHandler.geneFuncNetwork();
//		mainHandler.generateLinks("./2012IPMI/Dicccol_ABHigh_A_connList.txt");
		/////////////////////////////////////////////////////
//		mainHandler.geneDicccolsForLobe();
//		 mainHandler.statDICCCOL();
		////////////////////////////
		mainHandler.geneDicccolOnly_2012IPMI();

	}

}
