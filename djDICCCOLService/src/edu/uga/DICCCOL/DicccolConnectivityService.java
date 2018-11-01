package edu.uga.DICCCOL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uga.liulab.djVtkBase.djNiftiData;
import edu.uga.liulab.djVtkBase.djVtkCell;
import edu.uga.liulab.djVtkBase.djVtkFiberData;
import edu.uga.liulab.djVtkBase.djVtkHybridData;
import edu.uga.liulab.djVtkBase.djVtkPoint;
import edu.uga.liulab.djVtkBase.djVtkSurData;

public class DicccolConnectivityService {

	private djVtkSurData surData;
	private djVtkFiberData fiberData;
	private djNiftiData fmriData;
	private List<Integer> predictedDicccol;

	public djVtkSurData getSurData() {
		return surData;
	}

	public void setSurData(djVtkSurData surData) {
		this.surData = surData;
	}

	public djVtkFiberData getFiberData() {
		return fiberData;
	}

	public void setFiberData(djVtkFiberData fiberData) {
		this.fiberData = fiberData;
	}

	public djNiftiData getFmriData() {
		return fmriData;
	}

	public void setFmriData(djNiftiData fmriData) {
		this.fmriData = fmriData;
	}

	public List<Integer> getPredictedDicccol() {
		return predictedDicccol;
	}

	public void setPredictedDicccol(String predictedDicccolFile, int columnIndex) {
		this.predictedDicccol = new ArrayList<Integer>();
		double[][] allContent = DicccolUtilIO.loadFileAsArray(predictedDicccolFile, 358, 11);
		for (int i = 0; i < 358; i++)
			this.predictedDicccol.add((int) allContent[i][columnIndex]);
		allContent = null;
	}
	
	public List<djVtkPoint> getDicccolPts ()
	{
		List<djVtkPoint> dicccolPts = new ArrayList<djVtkPoint>();
		for (int i = 0; i < this.predictedDicccol.size(); i++)
			dicccolPts.add(this.surData.getPoint(this.predictedDicccol.get(i)));
		return dicccolPts;
	}
	
	public double[][] getFunctionalconnectivityMatrix()
	{
		double[][] connectivityMatrix = new double[358][358];
		
		for (int i = 0; i < this.predictedDicccol.size(); i++)
		{
			
		}
		
		return connectivityMatrix;
	}

	public double[][] getStructuralConnectivityMatrix() {
		double[][] connectivityMatrix = new double[358][358];
		List<List<Integer>> rawConnectivityInfo = new ArrayList<List<Integer>>();
		Set<Integer> allFiberIndex = new HashSet<Integer>();

		djVtkHybridData hybridData = new djVtkHybridData(this.surData, this.fiberData);
		hybridData.mapSurfaceToBox();
		hybridData.mapFiberToBox();
		for (int i = 0; i < this.predictedDicccol.size(); i++) {
			List<djVtkCell> tmpCellList = hybridData.getFibersConnectToPointsSet(this.surData.getNeighbourPoints(
					this.predictedDicccol.get(i), 3)).cellsOutput;
			List<Integer> newFiberIDList = new ArrayList<Integer>();
			for(int j=0;j<tmpCellList.size();j++)
				newFiberIDList.add(tmpCellList.get(j).cellId);
			rawConnectivityInfo.add(newFiberIDList);
			allFiberIndex.addAll(newFiberIDList);
		}
		
		for(int i=0;i<rawConnectivityInfo.size()-1;i++)
			for(int j=i+1;j<rawConnectivityInfo.size();j++)
			{
				int count=0;
				for(int m=0;m<rawConnectivityInfo.get(i).size();m++)
				{
					int currentCellID = rawConnectivityInfo.get(i).get(m);
					if(rawConnectivityInfo.get(j).contains(currentCellID))
						count++;
				} //for m
				connectivityMatrix[i][j] = connectivityMatrix[j][i]=(double)count/(double)allFiberIndex.size();
			} //for j
		return connectivityMatrix;
	}

}
