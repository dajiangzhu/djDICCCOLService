package edu.uga.DICCCOL.visualization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.uga.DICCCOL.CONSTANTS;
import edu.uga.DICCCOL.DicccolUtilIO;
import edu.uga.liulab.djVtkBase.djVtkData;
import edu.uga.liulab.djVtkBase.djVtkPoint;
import edu.uga.liulab.djVtkBase.djVtkSurData;

public class GenerateDICCCOL {

	private List<String> dicccolIDList = new ArrayList<String>();
	private List<String> dicccolConnList = new ArrayList<String>();
	private djVtkSurData surfaceData = null;
	private String[][] predictionData = null;
	private String outVtkFileName = null;
	private int columnIndex = -1; // model for 0-9; prediction for 10

	public GenerateDICCCOL() {
	}

	public GenerateDICCCOL(String dicccolIDFile, String surfaceFile, String predictionFile, int column,
			String outPutVtkFile) {
		this.dicccolIDList = DicccolUtilIO.loadFileToArrayList(dicccolIDFile.trim());
		this.surfaceData = new djVtkSurData(surfaceFile.trim());
		this.predictionData = DicccolUtilIO.loadFileAsStringArray(predictionFile.trim(), CONSTANTS.DICCCOL_I_Num,
				CONSTANTS.PREDICTIONRESULT_COLUMN_NUM);
		this.columnIndex = column;
		this.outVtkFileName = outPutVtkFile.trim();
	}

	public GenerateDICCCOL(String surfaceFile, String predictionFile, int column, String outPutVtkFile) {
		this.surfaceData = new djVtkSurData(surfaceFile.trim());
		this.predictionData = DicccolUtilIO.loadFileAsStringArray(predictionFile.trim(), CONSTANTS.DICCCOL_I_Num,
				CONSTANTS.PREDICTIONRESULT_COLUMN_NUM);
//		System.out.println("column="+column+" out="+outPutVtkFile.trim());
		this.columnIndex = column;
		this.outVtkFileName = outPutVtkFile.trim();
	}
	
	public void GenerateDICCCOLBallWithColor(djVtkSurData vtkPoints, djVtkSurData vtkBallTemplate, List<String> attriList, String outFileName)
	{
		djVtkSurData inputData = vtkPoints;
		djVtkSurData normalModelData = vtkBallTemplate; //new djVtkSurData("../sphere_radius2.vtk");
		djVtkSurData highlightModeData = vtkBallTemplate;

		String fileName = outFileName;
		System.out.println("Begin to write file:" + fileName + "...");
		Map<Integer, String> highLightROI = new HashMap<Integer, String>();
		FileWriter fw = null;

		try {
			// fw = new FileWriter(roiGroupInfo + ".vtk");
			fw = new FileWriter(fileName);
			fw.write("# vtk DataFile Version 3.0\r\n");
			fw.write("vtk output\r\n");
			fw.write("ASCII\r\n");
			fw.write("DATASET POLYDATA\r\n");

			int roiNum = inputData.nPointNum;
			int highROINum = highLightROI.size();
			int normalModelPtNum = normalModelData.nPointNum;
			int highModelPtNum = highlightModeData.nPointNum;
			int normalModelCellNum = normalModelData.nCellNum;
			int highModelCellNum = highlightModeData.nCellNum;

			System.out.println("the number of points in the input vtk is : " + roiNum);
			System.out.println("the number of points need to be highlighted is : " + highROINum);
			System.out.println("the number of points in the normalModel vtk is : " + normalModelPtNum);
			System.out.println("the number of points in the highModel vtk is : " + highModelPtNum);
			// print points info
			List<Integer> offsetList = new ArrayList<Integer>();
			fw.write("POINTS " + ((roiNum - highROINum) * normalModelPtNum + (highROINum * highModelPtNum))
					+ " float\r\n");
			for (int roiIndex = 0; roiIndex < roiNum; roiIndex++) {
				int countNormal = 0;
				int countHigh = 0;
				djVtkPoint currentROIPt = inputData.getPoint(roiIndex);
				if (highLightROI.containsKey(roiIndex)) {
					for (int modelPtIndex = 0; modelPtIndex < highModelPtNum; modelPtIndex++) {
						float x = highlightModeData.getPoint(modelPtIndex).x + currentROIPt.x;
						float y = highlightModeData.getPoint(modelPtIndex).y + currentROIPt.y;
						float z = highlightModeData.getPoint(modelPtIndex).z + currentROIPt.z;
						fw.write(x + " " + y + " " + z + "\r\n");
						offsetList.add(highModelPtNum);
						countHigh++;
					}
				} else {
					for (int modelPtIndex = 0; modelPtIndex < normalModelPtNum; modelPtIndex++) {
						float x = normalModelData.getPoint(modelPtIndex).x + currentROIPt.x;
						float y = normalModelData.getPoint(modelPtIndex).y + currentROIPt.y;
						float z = normalModelData.getPoint(modelPtIndex).z + currentROIPt.z;
						fw.write(x + " " + y + " " + z + "\r\n");
						offsetList.add(normalModelPtNum);
						countNormal++;
					} // for all points in the model vtk file
				}

			} // for all points in the input vtk file

			// print cells info
			int totalCellNum = (roiNum - highROINum) * normalModelCellNum + (highROINum * highModelCellNum);
			fw.write("POLYGONS " + totalCellNum + " " + (totalCellNum * 4) + " \r\n");
			int offset = 0;
			for (int roiIndex = 0; roiIndex < roiNum; roiIndex++) {
				if (highLightROI.containsKey(roiIndex)) {
					for (int modelCellIndex = 0; modelCellIndex < highModelCellNum; modelCellIndex++) {
						int ptId1 = highlightModeData.getcell(modelCellIndex).pointsList.get(0).pointId + offset;
						int ptId2 = highlightModeData.getcell(modelCellIndex).pointsList.get(1).pointId + offset;
						int ptId3 = highlightModeData.getcell(modelCellIndex).pointsList.get(2).pointId + offset;
						fw.write("3 " + ptId1 + " " + ptId2 + " " + ptId3 + " \r\n");
					} // for all cells in the model vtk file
					offset = offset + highModelPtNum;
				} else {
					for (int modelCellIndex = 0; modelCellIndex < normalModelCellNum; modelCellIndex++) {
						int ptId1 = normalModelData.getcell(modelCellIndex).pointsList.get(0).pointId + offset;
						int ptId2 = normalModelData.getcell(modelCellIndex).pointsList.get(1).pointId + offset;
						int ptId3 = normalModelData.getcell(modelCellIndex).pointsList.get(2).pointId + offset;
						fw.write("3 " + ptId1 + " " + ptId2 + " " + ptId3 + " \r\n");
					} // for all cells in the model vtk file
					offset = offset + normalModelPtNum;
				}

			} // for all points in the input vtk file

			fw.write("POINT_DATA " + ((roiNum - highROINum) * normalModelPtNum + (highROINum * highModelPtNum))
					+ "\r\n");
			fw.write("SCALARS TheColor float 1 \r\n");
			fw.write("LOOKUP_TABLE default \r\n");
			for (int roiIndex = 0; roiIndex < roiNum; roiIndex++)
				for (int modelPtIndex = 0; modelPtIndex < normalModelPtNum; modelPtIndex++)
					fw.write(attriList.get(roiIndex) + " \r\n");

		} catch (IOException ex) {
			Logger.getLogger(djVtkData.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fw.close();
			} catch (IOException ex) {
				Logger.getLogger(djVtkData.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("Write file:" + fileName + ".vtk done!");
		
	}

	public List<String> getDicccolIDList() {
		return dicccolIDList;
	}

	public void setDicccolIDList(List<String> dicccolIDList) {
		this.dicccolIDList = dicccolIDList;
	}

	public List<String> getDicccolConnList() {
		return dicccolConnList;
	}

	public void setDicccolConnList(List<String> dicccolConnList) {
		this.dicccolConnList = dicccolConnList;
	}

	public djVtkSurData getSurfaceData() {
		return surfaceData;
	}

	public void setSurfaceData(djVtkSurData surfaceData) {
		this.surfaceData = surfaceData;
	}

	public String[][] getPredictionData() {
		return predictionData;
	}

	public void setPredictionData(String[][] predictionData) {
		this.predictionData = predictionData;
	}

	public String getOutVtkFileName() {
		return outVtkFileName;
	}

	public void setOutVtkFileName(String outVtkFileName) {
		this.outVtkFileName = outVtkFileName.trim();
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public void generatePointsVtk() {
		if (this.dicccolIDList.size() == 0)
			for (int i = 0; i < CONSTANTS.DICCCOL_I_Num; i++)
				this.dicccolIDList.add(String.valueOf(i));

		List<djVtkPoint> ptList = new ArrayList<djVtkPoint>();
		for (int i = 0; i < this.dicccolIDList.size(); i++) {
			int dicccolID = Integer.valueOf(this.dicccolIDList.get(i).trim());
			int ptID = Integer.valueOf(this.predictionData[dicccolID][this.columnIndex].trim());
//			System.out.println("dicccolID:"+dicccolID+" ptID: "+ptID);
			ptList.add(this.surfaceData.getPoint(ptID));
		} // for i
		DicccolUtilIO.writeToPointsVtkFile(outVtkFileName, ptList);
	}

	public void generateConnVtk() throws IOException {
		if (this.dicccolConnList.size() != 0) {
			
			Set<djVtkPoint> ptSet_from = new HashSet<djVtkPoint>();
			Set<djVtkPoint> ptSet_to = new HashSet<djVtkPoint>();
			Set<djVtkPoint> ptSet_all = new HashSet<djVtkPoint>();
			List<String> ptScalarList = new ArrayList<String>();
			for(int i=0;i<358;i++)
				ptScalarList.add("0");
			//generate all DICCCOLs
			List<djVtkPoint> ptList = new ArrayList<djVtkPoint>();
			for (int i = 0; i < CONSTANTS.DICCCOL_I_Num; i++) {
				int ptID = Integer.valueOf(this.predictionData[i][this.columnIndex].trim());
				ptList.add(this.surfaceData.getPoint(ptID));
			} // for i
			//generate all connections
			List<String> lineList = new ArrayList<String>();
			for (int i = 0; i < this.dicccolConnList.size(); i++) {
				String[] currentLine = this.dicccolConnList.get(i).trim().split("-");
				String strLine = "2 " + currentLine[0].trim() + " " + currentLine[1].trim() + "\r\n";
				int ptTarget = Integer.valueOf(currentLine[1].trim());
				ptScalarList.set(ptTarget, "1");
//				ptScalarList.set(ptTarget, String.valueOf( (1.0/(float)this.dicccolConnList.size())*(float)(i+1)));
				lineList.add(strLine);
				int ptID1 = Integer.valueOf(this.predictionData[Integer.valueOf( currentLine[0].trim() )][this.columnIndex].trim());
				int ptID2 = Integer.valueOf(this.predictionData[Integer.valueOf( currentLine[1].trim() )][this.columnIndex].trim());
				ptSet_from.add( this.surfaceData.getPoint(ptID1) );
				ptSet_to.add( this.surfaceData.getPoint(ptID2) );
			}
			//Begin to write the vtk file
			FileWriter fw = null;
			fw = new FileWriter(this.outVtkFileName+".Conn.vtk");
			fw.write("# vtk DataFile Version 3.0\r\n");
			fw.write("vtk output\r\n");
			fw.write("ASCII\r\n");
			fw.write("DATASET POLYDATA\r\n");
			fw.write("POINTS " + ptList.size() + " float\r\n");
			for (int i = 0; i < ptList.size(); i++)
				fw.write(ptList.get(i).x + " " + ptList.get(i).y + " " + ptList.get(i).z + "\r\n");
			fw.write("LINES " + lineList.size() + " " + (lineList.size() * 3) + " \r\n");
			for (int i = 0; i < lineList.size(); i++)
				fw.write(lineList.get(i));
//			fw.write("POINT_DATA 358\r\n");
//			fw.write("SCALARS trace float\r\n");
//			fw.write("LOOKUP_TABLE default\r\n");
//			for (int i = 0; i < 358; i++)
//				fw.write(ptScalarList.get(i)+"\r\n");
			
			fw.close();
			System.out.println("Write file done!");
			ptSet_all.addAll(ptSet_from);
			ptSet_all.addAll(ptSet_to);
			ptList = new ArrayList<djVtkPoint>(ptSet_all);
			DicccolUtilIO.writeToPointsVtkFile(this.outVtkFileName+".Points.vtk", ptList);
//			DicccolUtilIO.writeToPointsVtkFile(this.outVtkFileName+".PointsFrom.vtk", new ArrayList<djVtkPoint>(ptSet_from));
//			DicccolUtilIO.writeToPointsVtkFile(this.outVtkFileName+".PointsTo.vtk", new ArrayList<djVtkPoint>(ptSet_to));

		} else
			System.out.println("You need set dicccolConnList first!");
	}

}
