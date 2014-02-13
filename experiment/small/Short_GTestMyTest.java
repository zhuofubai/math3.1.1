package experiment.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.stat.inference.GTest;
import org.apache.commons.math3.stat.inference.GTest_bug1;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_GTestMyTest {
	public static Random r = new Random(177756);

	/**
	 * @param args
	 */

	public static int testOut(double[] truth, double[] result) {
		double tolerance = 1E-12;

		if (truth.length != result.length) {
			System.out
					.println("two inputs' dimension is mismatch, the result length is: "
							+ result.length);
			return 1;
		}

		double diff = 0;
		for (int i = 0; i < truth.length; i++) {

			diff = Math.abs(truth[i] - result[i]);
			if (diff > tolerance) {
				return 1;
			}

		}
		return 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Short_GTestMyTest.testGTestRepeat();
		// GTestMyTest.testGTest(3);
	}

	public static void testGTestRepeat() {
		try {
			int max_len = 10;
			//int iter = 1000;

			Profiler.visitNewTest(-1);
			// TODO Auto-generated method stub

			// int count = 0;
			// double result = 0, truth = 0;
			int yout = 0;
			int count2 = 0;
			String filedir = "TestFastCosine/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count = 0;

			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);
				int length = 3 + (int) (Math.random() * (max_len - 1));
				long[] obs = generateLongArray(length);
				double[] exp = generateExp(length);

				long[] obs2 = generateLongArray(length);
				int[] a = generateIntArray();
				double[] truth = testGTest(obs, exp, obs2, a);
				double[] result = testGTest_bug(obs, exp, obs2, a);
				yout=testOut(truth,result);
				if (yout==1){count++;}
				out.write(i+" "+yout);
				out.write("\n");
				out.flush();
				//transformer_bug.log.setTestId(i);ffffff
				count2++;
			}
			Profiler.stopProfiling();
			System.out.println("count is :" + count + "  count2 is" + count2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	public static double[] testGTest(long[] obs, double[] exp, long[] obs2,
			int[] a) {
		GTest g = new GTest();

		int k11 = a[0];
		int k12 = a[1];
		int k21 = a[2];
		int k22 = a[3];

		double gval = g.g(exp, obs);
		double pval = g.gTest(exp, obs);
		double pval_inst = g.gTestIntrinsic(exp, obs);
		boolean hypo = g.gTest(exp, obs, 0.5);
		double gval2 = g.gDataSetsComparison(obs, obs2);
		double pval_loglr = g.rootLogLikelihoodRatio(k11, k12, k21, k22);
		double pval2 = g.gTestDataSetsComparison(obs, obs2);
		boolean hypo2 = g.gTestDataSetsComparison(obs, obs2, 0.5);
		double[] truth = { gval, pval, pval_inst, gval2, pval_loglr, pval2 };
		System.out.println(gval + "\t" + pval + "\t" + pval_inst + "\t" + hypo
				+ "\t" + gval2 + "\t" + pval_loglr + "\t" + pval2 + "\t"
				+ hypo2);

		return truth;
	}

	public static double[] testGTest_bug(long[] obs, double[] exp, long[] obs2,
			int[] a) {
		GTest_bug1 g = new GTest_bug1();

		int k11 = a[0];
		int k12 = a[1];
		int k21 = a[2];
		int k22 = a[3];

		double gval = g.g(exp, obs);
		double pval = g.gTest(exp, obs);
		double pval_inst = g.gTestIntrinsic(exp, obs);
		boolean hypo = g.gTest(exp, obs, 0.5);
		double gval2 = g.gDataSetsComparison(obs, obs2);
		double pval_loglr = g.rootLogLikelihoodRatio(k11, k12, k21, k22);
		double pval2 = g.gTestDataSetsComparison(obs, obs2);
		boolean hypo2 = g.gTestDataSetsComparison(obs, obs2, 0.5);
		double[] result = { gval, pval, pval_inst, gval2, pval_loglr, pval2 };
		System.out.println(gval + "\t" + pval + "\t" + pval_inst + "\t" + hypo
				+ "\t" + gval2 + "\t" + pval_loglr + "\t" + pval2 + "\t"
				+ hypo2);

		return result;
	}

	public static int[] generateIntArray() {
		int upper = 999999;
		int[] a = new int[4];
		a[0] = r.nextInt(upper - 1);
		a[1] = r.nextInt(upper - 1);
		a[2] = r.nextInt(upper - 1);
		a[3] = r.nextInt(upper - 1);

		return a;
	}

	public static double[] generateExp(int n) {
		double[] a = new double[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextDouble()+0.0001;
		}
		return a;
	}

	public static long[] generateLongArray(int n) {
		long[] a = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = Math.abs((long)r.nextInt(100))+1L;
		}
		return a;
	}
	public static double createSmallNumber() {
		double a = r.nextDouble();
		double b = r.nextDouble();
		return (a - 0.5) / Math.pow(10, b * 25);
	}

}
