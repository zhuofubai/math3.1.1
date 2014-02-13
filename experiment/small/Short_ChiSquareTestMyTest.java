package experiment.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_ChiSquareTestMyTest {
	public static Random r = new Random(177756);
	public static int upper = 99999;

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

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int max_len = 100;
			// int iter = 1000;

			Profiler.visitNewTest(-1);
			// TODO Auto-generated method stub

			int yout = 0;
			int count2 = 0;
			String filedir = "TestFastCosine/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count = 0;

			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);
				int length = 2 + r.nextInt(max_len - 1);

				long[] obs = generateLongArray(length);
				double[] exp = generateExp(length);
				long[] obs2 = generateLongArray(length);
				long[][] counts = generateCounts(length);

				double[] truth = testChiSquare(obs, exp, obs2, counts);
				double[] result = testChiSquare_bug(obs, exp, obs2, counts);
				yout = testOut(truth, result);
				if (yout == 1) {
					count++;
				}
				out.write(i + " " + yout);
				out.write("\n");
				out.flush();
				// transformer_bug.log.setTestId(i);ffffff
				count2++;
			}
			Profiler.stopProfiling();
			System.out.println("count is :" + count + "  count2 is" + count2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end");
	}

	public static double[] testChiSquare(long[] obs, double[] exp, long[] obs2,
			long[][] counts) {
		ChiSquareTest chi = new ChiSquareTest();

		double pval1 = chi.chiSquareTest(exp, obs);
		boolean rej1 = chi.chiSquareTest(exp, obs, 0.5);
		double pval2 = chi.chiSquareTest(counts);
		boolean rej2 = chi.chiSquareTest(counts, 0.5);
		double pval3 = chi.chiSquareTestDataSetsComparison(obs, obs2);
		boolean rej3 = chi.chiSquareTestDataSetsComparison(obs, obs2, 0.5);
		double[] truth = { pval1, pval2, pval3 };

		// System.out.println(pval1);
		return truth;
	}

	public static double[] testChiSquare_bug(long[] obs, double[] exp,
			long[] obs2, long[][] counts) {
		ChiSquareTest chi = new ChiSquareTest();

		double pval1 = chi.chiSquareTest(exp, obs);
		boolean rej1 = chi.chiSquareTest(exp, obs, 0.5);
		double pval2 = chi.chiSquareTest(counts);
		boolean rej2 = chi.chiSquareTest(counts, 0.5);
		double pval3 = chi.chiSquareTestDataSetsComparison(obs, obs2);
		boolean rej3 = chi.chiSquareTestDataSetsComparison(obs, obs2, 0.5);
		double[] result = { pval1, pval2, pval3 };

		// System.out.println(pval1);
		return result;
	}

	public static double[] generateExp(int n) {
		double[] a = new double[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextDouble();
		}
		return a;
	}

	public static long[] generateLongArray(int n) {
		long[] a = new long[n];
		for (int i = 0; i < n; i++) {
			a[i] = (long) r.nextDouble() * (upper - 1) + 1L;

		}
		return a;
	}

	public static long[][] generateCounts(int length) {
		long[][] counts = new long[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				counts[i][j] = (long) r.nextDouble() * (upper - 1) + 1L;
			}
		}
		return counts;
	}
}
