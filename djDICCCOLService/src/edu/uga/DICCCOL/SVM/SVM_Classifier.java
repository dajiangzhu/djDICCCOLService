package edu.uga.DICCCOL.SVM;

import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.core.Instances;

public class SVM_Classifier {

	public double[] SMO_Classification(String filepath, double GammaStart,
			double GammaEnd, double CStart, double CEnd) throws Exception {
		System.out
				.println("##################### SMO_Classification... #####################");

		double[] bestPerformance = new double[5]; // 0-bestACC 1-bestSPE
													// 2-bestSEN 3-bestGamma
													// 4-bestC
		for (int i = 0; i < 5; i++)
			bestPerformance[i] = 0.0;
		System.out.println("Reading Arff file: " + filepath);
		System.out.println("GammaStart: " + GammaStart + "  GammaEnd: "
				+ GammaEnd);
		System.out.println("CStart: " + CStart + "  CEnd: " + CEnd);
		FileReader trainreader = new FileReader(filepath);
		Instances train = new Instances(trainreader);
		train.setClassIndex(train.numAttributes() - 1);

		SMO svmClassifier = new SMO();
		RBFKernel rbf = new RBFKernel();
		for (double g = GammaStart; g <= GammaEnd; g = g + 0.02) {
//			System.out.println("Try g = "+g);
			for (double c = CStart; c <= CEnd; c = c + 1) {
//				System.out.println("Try SMO:  Gamma-" + g + " C-" + c);
				rbf.setGamma(g);
				svmClassifier.setKernel(rbf);
				svmClassifier.setC(c);
				svmClassifier.buildClassifier(train);

				Evaluation eval = new Evaluation(train);
				eval.crossValidateModel(svmClassifier, train, 10, new Random(1));

				double TP0 = eval.numTruePositives(0);
				double FP0 = eval.numFalsePositives(0);
				double TP1 = eval.numTruePositives(1);
				double FP1 = eval.numFalsePositives(1);

				double tmpACC = (TP0 + TP1) / (TP0 + TP1 + FP0 + FP1);
				double tmpSPE = TP0 / (TP0 + FP1);
				double tmpSEN = TP1 / (TP1 + FP0);
				System.out.println("SVMResult: ACC:"+tmpACC+" SPE:"+tmpSPE+" SEN:"+tmpSEN+" Gamma:"+g+" C:"+c);
				if (tmpACC > bestPerformance[0]) {
					bestPerformance[0] = tmpACC;
					bestPerformance[1] = tmpSPE;
					bestPerformance[2] = tmpSEN;
					bestPerformance[3] = g;
					bestPerformance[4] = c;
				} // if
//				System.out.println("All:" + (TP0 + TP1)
//						/ (TP0 + TP1 + FP0 + FP1) + "-(" + (TP0 + TP1) + "/"
//						+ (FP0 + FP1) + ")         0(Normal):"
//						+ (TP0 / (TP0 + FP1)) + "-(" + TP0 + "/" + FP1
//						+ ")     1(MDD):" + (TP1 / (TP1 + FP0)) + "-(" + TP1
//						+ "/" + FP0 + ")");
			} // for c
		} // for g
		System.out
				.println("##################### SMO_Classification finished! bestACC:"
						+ bestPerformance[0]
						+ "  bestSPE:"
						+ bestPerformance[1]
						+ "   bestSEN:"
						+ bestPerformance[2]
						+ "   bestGamma:"
						+ bestPerformance[3]
						+ "   bestC:"
						+ bestPerformance[4]
						+ "#####################");
		return bestPerformance;
	}

}
