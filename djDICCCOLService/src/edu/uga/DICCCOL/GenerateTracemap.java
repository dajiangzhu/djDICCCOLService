package edu.uga.DICCCOL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jmat.data.AbstractMatrix;
import org.jmat.data.Matrix;

import edu.uga.liulab.djVtkBase.djVtkDataDictionary;
import edu.uga.liulab.djVtkBase.djVtkFiberData;
import edu.uga.liulab.djVtkBase.djVtkHybridData;
import edu.uga.liulab.djVtkBase.djVtkPoint;
import edu.uga.liulab.djVtkBase.djVtkSurData;
import edu.uga.liulab.djVtkBase.djVtkUtil;

public class GenerateTracemap {
	private CommandLineParser cmdParser;
	private Options options;
	private CommandLine cmdLine;
	private HelpFormatter formatter;

	private String surFileName;
	private String fiberFileName;
	private String predictionFileName;
	private int dicccolColumn = 10;
	private int ringNum = 2;
	private List<Integer> dicccolList;
	private String outputlFileName;
	private boolean outputFiber = false;
	private boolean outputTracemapPoints = false;
	private boolean outputTracemapVector = false;

	public GenerateTracemap() {
		cmdParser = new GnuParser();
		formatter = new HelpFormatter();
	}

	private void createOptions() {
		options = new Options();
		Option oInputFile_s = OptionBuilder.withArgName("String").hasArg()
				.isRequired(true).withDescription("input surface file(*.vtk)")
				.create("s");
		Option oInputFile_f = OptionBuilder.withArgName("String").hasArg()
				.isRequired(true).withDescription("input fiber file(*.vtk)")
				.create("f");
		Option oInputFile_p = OptionBuilder.withArgName("String").hasArg()
				.isRequired(true).withDescription("input prediction file")
				.create("p");
		Option oInputFile_c = OptionBuilder
				.withArgName("Integer")
				.hasArg()
				.isRequired(false)
				.withDescription(
						"The column index of prediction result (default 10. Model: 0-9  Prediction: 10)")
				.create("c");
		Option oInputFile_r = OptionBuilder.withArgName("Integer").hasArg()
				.isRequired(false).withDescription("ring number (default 2)")
				.create("r");
		Option oInputFile_dl = OptionBuilder.withArgName("String").hasArg()
				.isRequired(false).withDescription("DICCCOL List File")
				.create("dl");
		Option oInputFile_o = OptionBuilder.withArgName("String").hasArg()
				.isRequired(true).withDescription("output prefex file")
				.create("o");

		Option oDoOutputFiber = new Option("of", "output extracted fibers.");
		Option oDoOutputTraceMapPoints = new Option("op",
				"output trace map points");
		Option oDoOutputTraceMapVector = new Option("ov",
				"output trace map vectors");

		Option ohelp = new Option("help", "print this message");

		options.addOption(oInputFile_s);
		options.addOption(oInputFile_f);
		options.addOption(oInputFile_c);
		options.addOption(oInputFile_r);
		options.addOption(oInputFile_dl);
		options.addOption(oInputFile_p);
		options.addOption(oInputFile_o);
		options.addOption(oDoOutputFiber);
		options.addOption(oDoOutputTraceMapPoints);
		options.addOption(oDoOutputTraceMapVector);
		options.addOption(ohelp);
	}

	private void parseArgs(String[] strInputs) {
		try {
			cmdLine = this.cmdParser.parse(this.options, strInputs);
		} catch (ParseException e) {
			formatter.printHelp("Usage Info : ", this.options);
			System.exit(0);
		}
	}

	private void do_generateTracemap() {

		AbstractMatrix DICCCOL_M = Matrix.fromASCIIFile(new File(
				this.predictionFileName));
		djVtkSurData surData = new djVtkSurData(this.surFileName);
		djVtkFiberData fiberData = new djVtkFiberData(this.fiberFileName);
		djVtkHybridData hybridData = new djVtkHybridData(surData, fiberData);
		hybridData.mapSurfaceToBox();
		hybridData.mapFiberToBox();

		List<List<Float>> modelFeature = new ArrayList<List<Float>>();
		if (this.dicccolList == null) {
			this.dicccolList = new ArrayList<Integer>();
			for (int d = 0; d < 358; d++)
				this.dicccolList.add(d);
		}

		// for (int dicccolID = 0; dicccolID < 358; dicccolID++) {
		for (int i = 0; i < this.dicccolList.size(); i++) {
			int dicccolID = this.dicccolList.get(i);
			System.out.println("DICCCOL_ID:" + dicccolID + "---------");
			int ptID = (int) DICCCOL_M.get(dicccolID, this.dicccolColumn);
			djVtkPoint dicccolPoint = surData.getPoint(ptID);
			// extract fibers
			Set ptSet = surData.getNeighbourPoints(ptID, this.ringNum);
			djVtkFiberData tmpFiberData = (djVtkFiberData) hybridData
					.getFibersConnectToPointsSet(ptSet).getCompactData();

			tmpFiberData.cell_alias = djVtkDataDictionary.VTK_FIELDNAME_FIBER_CELL;
			if (this.outputFiber)
				tmpFiberData.writeToVtkFileCompact(this.outputlFileName
						+ ".sub." + dicccolID + ".sid."+ptID+".vtk");
			//fiber.roi.sub.1.sid.10552.vtk

			fiberBundleService fiberBundleDescriptor = new fiberBundleService();
			fiberBundleDescriptor.setFiberData(tmpFiberData);
			fiberBundleDescriptor.setSeedPnt(dicccolPoint);
			fiberBundleDescriptor.createFibersTrace();
			List<djVtkPoint> allTracePointsList = fiberBundleDescriptor
					.getAllPoints();
			if (this.outputTracemapPoints)
				djVtkUtil.writeToPointsVtkFile(this.outputlFileName
						+ ".dicccol." + dicccolID + ".TracemapPoints.vtk",
						allTracePointsList);
			List<Float> tmpFeature = fiberBundleDescriptor
					.calFeatureOfTrace(allTracePointsList);
			modelFeature.add(tmpFeature);
			// //
			ptSet = null;
			tmpFiberData = null;
			allTracePointsList = null;
		} // for dicccolID
		if (this.outputTracemapVector)
			djVtkUtil.writeArrayListToFile(modelFeature, " ", 144,
					this.outputlFileName + "_TracemapFeatures.txt");

	}

	private void dispatch(String[] strInputs) throws Exception {
		this.createOptions();
		this.parseArgs(strInputs);
		if (cmdLine == null || cmdLine.hasOption("help")) {
			formatter.printHelp("Usage Info : ", this.options);
			return;
		}

		this.surFileName = cmdLine.getOptionValue("s");
		this.fiberFileName = cmdLine.getOptionValue("f");
		this.predictionFileName = cmdLine.getOptionValue("p");
		if (cmdLine.hasOption("c"))
			this.dicccolColumn = Integer.valueOf(cmdLine.getOptionValue("c")
					.trim());
		this.outputlFileName = cmdLine.getOptionValue("o");
		if (cmdLine.hasOption("r"))
			this.ringNum = Integer.valueOf(cmdLine.getOptionValue("r").trim());
		if (cmdLine.hasOption("dl"))
			this.dicccolList = DicccolUtilIO.loadFileToIntegerArrayList(cmdLine
					.getOptionValue("dl").trim());
		if (cmdLine.hasOption("of"))
			this.outputFiber = true;
		if (cmdLine.hasOption("op"))
			this.outputTracemapPoints = true;
		if (cmdLine.hasOption("ov"))
			this.outputTracemapVector = true;
		this.do_generateTracemap();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateTracemap mainHandler = new GenerateTracemap();
		try {
			mainHandler.dispatch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
