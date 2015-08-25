package edu.uga.DICCCOL;

import java.util.ArrayList;
import java.util.List;

import edu.uga.liulab.djVtkBase.djNiftiData;
import edu.uga.liulab.djVtkBase.djVtkSurData;

public class BoldService {
	private djVtkSurData surData;
	private djNiftiData fmriData;
	private List<Integer> predictedDicccol;
	private int Dicccol_Num=358;
	private int Dicccol_AtColumn=11;
	private int EXTRACTBOLDS_RANGE=1;

	public int getEXTRACTBOLDS_RANGE() {
		return EXTRACTBOLDS_RANGE;
	}

	public void setEXTRACTBOLDS_RANGE(int eXTRACTBOLDS_RANGE) {
		EXTRACTBOLDS_RANGE = eXTRACTBOLDS_RANGE;
	}

	public int getDicccol_num() {
		return Dicccol_Num;
	}

	public void setDicccol_num(int dicccol_num) {
		Dicccol_Num = dicccol_num;
	}

	public int getDicccol_atColumn() {
		return Dicccol_AtColumn;
	}

	public void setDicccol_atColumn(int dicccol_atColumn) {
		Dicccol_AtColumn = dicccol_atColumn;
	}

	public djVtkSurData getSurData() {
		return surData;
	}

	public void setSurData(djVtkSurData surData) {
		this.surData = surData;
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

	public void setPredictedDicccol(String predictedDicccolFile) {
		this.predictedDicccol = new ArrayList<Integer>();
		double[][] allContent = DicccolUtilIO.loadFileAsArray(predictedDicccolFile, Dicccol_Num, Dicccol_AtColumn);
		for (int i = 0; i < Dicccol_Num; i++)
			this.predictedDicccol.add((int) allContent[i][Dicccol_AtColumn-1]);
		allContent = null;
	}
	
	private double[] extractBoldSignals(int[] volumeCoord) {
		double[] boldsSig = new double[this.fmriData.tSize];
		for (int x = volumeCoord[0] - EXTRACTBOLDS_RANGE; x <= volumeCoord[0] + EXTRACTBOLDS_RANGE; x++)
			for (int y = volumeCoord[1] - EXTRACTBOLDS_RANGE; y <= volumeCoord[1] + EXTRACTBOLDS_RANGE; y++)
				for (int z = volumeCoord[2] - EXTRACTBOLDS_RANGE; z <= volumeCoord[2] + EXTRACTBOLDS_RANGE; z++)
					for (int t = 0; t < this.fmriData.tSize; t++)
						boldsSig[t] += this.fmriData.getValueBasedOnVolumeCoordinate(x, y, z, t);
		for (int t = 0; t < this.fmriData.tSize; t++)
			boldsSig[t] = boldsSig[t] / Math.pow((EXTRACTBOLDS_RANGE*2+1), 3);
		return boldsSig;
	}
	
	public double[][] extractBoldsOfDicccol()
	{
		double[][] allBoldSig = new double[Dicccol_Num][this.fmriData.tSize];
		for (int dicccolID = 0; dicccolID < Dicccol_Num; dicccolID++) {
			int ptID = this.predictedDicccol.get(dicccolID);
			float[] physicalCoord = { this.surData.getPoint(ptID).x, this.surData.getPoint(ptID).y,
					this.surData.getPoint(ptID).z };
			int[] volumeCoord = this.fmriData.convertFromPhysicalToVolume(physicalCoord);
			allBoldSig[dicccolID] = this.extractBoldSignals(volumeCoord);
		} // for each DICCCOL
		return allBoldSig;
	}
	
}
