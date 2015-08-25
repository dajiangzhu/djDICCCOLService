package edu.uga.DICCCOL.tool;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.uga.DICCCOL.BoldService;
import edu.uga.DICCCOL.DicccolUtilIO;
import edu.uga.liulab.djVtkBase.djNiftiData;
import edu.uga.liulab.djVtkBase.djVtkSurData;

public class ExtractDicccolBolds {

	private CommandLineParser cmdParser;
	private Options options;
	private CommandLine cmdLine;
	private HelpFormatter formatter;
	
	private String surFileName;
	private String fmriFileName;
	private String predictionFileName;
	private String outSignalFileName;
	
	
	public ExtractDicccolBolds() {
		cmdParser = new GnuParser();
		formatter = new HelpFormatter();
	}
	
	private void createOptions() {
		options = new Options();
		Option oInputFile_s = OptionBuilder.withArgName("String").hasArg().isRequired(true).withDescription("input surface file(*.vtk)")
		.create("s");
		Option oInputFile_f = OptionBuilder.withArgName("String").hasArg().isRequired(true).withDescription("input fMRI file(*.nii.gz)")
		.create("f");
		Option oInputFile_p = OptionBuilder.withArgName("pString").hasArg().isRequired(true).withDescription("input prediction file")
		.create("p");
		Option oInputFile_o = OptionBuilder.withArgName("String").hasArg().isRequired(true).withDescription("output BOLDs signal file")
		.create("o");
		
		Option ohelp = new Option("help", "print this message");
		options.addOption(oInputFile_s);
		options.addOption(oInputFile_f);
		options.addOption(oInputFile_p);
		options.addOption(oInputFile_o);
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
	
	private void do_extractSignals()
	{
		djVtkSurData surData = new djVtkSurData(this.surFileName);
		djNiftiData rsData = new djNiftiData(this.fmriFileName);
		BoldService boldService = new BoldService();
		boldService.setSurData(surData);
		boldService.setFmriData(rsData);
		boldService.setPredictedDicccol(this.predictionFileName);
		double[][] allBolds = boldService.extractBoldsOfDicccol();
		DicccolUtilIO.writeArrayToFile(allBolds, boldService.getDicccol_num(), rsData.tSize, " ", this.outSignalFileName);		
	}

	private void dispatch(String[] strInputs) throws Exception
	{
		this.createOptions();
		this.parseArgs(strInputs);
		if (cmdLine == null || cmdLine.hasOption("help")) {
			formatter.printHelp("Usage Info : ", this.options);
			return;
		}
		
		this.surFileName = cmdLine.getOptionValue("s");
		this.fmriFileName = cmdLine.getOptionValue("f");
		this.predictionFileName = cmdLine.getOptionValue("p");
		this.outSignalFileName = cmdLine.getOptionValue("o");
		this.do_extractSignals();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExtractDicccolBolds mainHandler = new ExtractDicccolBolds();
		try {
			mainHandler.dispatch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
