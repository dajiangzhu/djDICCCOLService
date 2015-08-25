package edu.uga.DICCCOL.tool;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.uga.DICCCOL.DicccolUtilIO;
import edu.uga.DICCCOL.visualization.GenerateDICCCOL;

public class GenerateDICCCOLConnVtk {
	private CommandLineParser cmdParser;
	private Options options;
	private CommandLine cmdLine;
	private HelpFormatter formatter;

	private String dicccolConnFileName;
	private String surFileName;
	private String predictionFileName;
	private int dicccolColumn;
	private String outputlFileName;

	public GenerateDICCCOLConnVtk() {
		cmdParser = new GnuParser();
		formatter = new HelpFormatter();
	}

	private void createOptions() {
		options = new Options();
		Option oInputFile_l = OptionBuilder.withArgName("String").hasArg().isRequired(false)
				.withDescription("input DICCCOL Connectivity file").create("l");
		Option oInputFile_s = OptionBuilder.withArgName("String").hasArg().isRequired(true)
				.withDescription("input surface file(*.vtk)").create("s");
		Option oInputFile_p = OptionBuilder.withArgName("String").hasArg().isRequired(true)
				.withDescription("input prediction file").create("p");
		Option oInputFile_c = OptionBuilder.withArgName("Integer").hasArg().isRequired(true)
				.withDescription("The column index of prediction result (model: 0-9  prediction: 10)").create("c");
		Option oInputFile_o = OptionBuilder.withArgName("String").hasArg().isRequired(true)
				.withDescription("output DICCCOL Connetivity vtk file").create("o");

		Option ohelp = new Option("help", "print this message");
		options.addOption(oInputFile_l);
		options.addOption(oInputFile_s);
		options.addOption(oInputFile_c);
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

	private void do_generateDICCCOLVtk() throws IOException {
		GenerateDICCCOL generateDicccolService = new GenerateDICCCOL(this.surFileName, this.predictionFileName,
				this.dicccolColumn, this.outputlFileName);
		generateDicccolService.setDicccolConnList(DicccolUtilIO.loadFileToArrayList(this.dicccolConnFileName));
		generateDicccolService.generateConnVtk();
	}

	private void dispatch(String[] strInputs) throws Exception {
		this.createOptions();
		this.parseArgs(strInputs);
		if (cmdLine == null || cmdLine.hasOption("help")) {
			formatter.printHelp("Usage Info : ", this.options);
			return;
		}

		this.dicccolConnFileName = cmdLine.getOptionValue("l");
		this.surFileName = cmdLine.getOptionValue("s");
		this.predictionFileName = cmdLine.getOptionValue("p");
		this.dicccolColumn = Integer.valueOf(cmdLine.getOptionValue("c").trim());
		this.outputlFileName = cmdLine.getOptionValue("o");
		this.do_generateDICCCOLVtk();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateDICCCOLConnVtk mainHandler = new GenerateDICCCOLConnVtk();
		try {
			mainHandler.dispatch(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
