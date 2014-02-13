package experiment.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator_bug;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_SplineInterpolatorMyTest {
	public static Random r = new Random(177756);
	/** error tolerance for spline interpolator value at knot points */
	protected double knotTolerance = 1E-12;

	/** error tolerance for interpolating polynomial coefficients */
	protected double coefficientTolerance = 1E-6;

	/** error tolerance for interpolated values -- high value is from sin test */
	protected static double interpolationTolerance = 1E-2;

	/**
	 * @param args
	 */
	public static int testOut(double truth, double result) {
		//double tolerance = inter;

		double diff = 0;

		diff = Math.abs(truth - result);
		if (diff > interpolationTolerance) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		try {
			int max_len = 100;
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
			double truth=0 ;
			double result=0;
			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);
				

					int len = 3 + r.nextInt(max_len - 1);
					double[] x = new double[len];
					double[] y = new double[len];
					for (int j = 0; j < len; j++) {
						x[j] = r.nextDouble();
						y[j] = r.nextDouble();
					}
					PolynomialSplineFunction tru=Short_SplineInterpolatorMyTest.testInterpolation(x, y);
					PolynomialSplineFunction res=Short_SplineInterpolatorMyTest.testInterpolation_bug(x, y);
					double var=getMean(x);
				truth=tru.value(var);
				result=res.value(var);
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
	
	public static PolynomialSplineFunction testInterpolation(double[] x,
			double[] y) {
		Random rand = new Random();
		SplineInterpolator spline = new SplineInterpolator();

		Arrays.sort(x);
		PolynomialSplineFunction tru = spline.interpolate(x, y);
		//System.out.println("knots: " + res.getN());

		return tru;

	}

	public static PolynomialSplineFunction testInterpolation_bug(double[] x,
			double[] y) {
		Random rand = new Random();
		SplineInterpolator_bug spline = new SplineInterpolator_bug();

		Arrays.sort(x);
		PolynomialSplineFunction res = spline.interpolate(x, y);
		//System.out.println("knots: " + res.getN());

		return res;

	}
	public static double getMean(double[] a){
		double sum=0;
		for(int i=0;i<a.length;i++ ){
			sum+=a[i];
		}
		return sum/a.length;
	}
	
}
