package experiment.small;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.analysis.function.HarmonicOscillator;
import org.apache.commons.math3.fitting.HarmonicFitter;
import org.apache.commons.math3.fitting.HarmonicFitter_bug2;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_HarmonicFitterMyTest {
	public static Random r = new Random(177756);
	
	public static int testOut(double[] truth, double[] result) {
		double tolerance = 7.6E-4;

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
				//System.out.println("diff "+diff+"i "+i);
				return 1;
			}

		}
		return 0;
	}
	static public void main(String[] args){
		
		try {
			int max_len = 10;
			//int iter = 1000;

			Profiler.visitNewTest(-1);
			// TODO Auto-generated method stub

			// int count = 0;
			// double result = 0, truth = 0;
			int yout = 0;
			int count2 = 0;
			String filedir = "TestHarmonicFitter/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count = 0;
			double a,w,p;
			double[] g, truth, result;
			for (int i = 0; i < 5000; i++) {
				if(i==93){
				//	System.out.println("here");
				}
				Profiler.visitNewTest(i);
				//int length = 3 + (int) (Math.random() * (max_len - 1));
				a = 0.2+0.01*r.nextDouble();
		        w = 3.4+0.01*r.nextDouble();
		        p = 4.1+0.01*r.nextDouble();
		        g=generateGaussianData(200);
		       // System.out.print("real value: "+a+" "+w+" "+p+"; ");
		        truth=fitterTest(a,  w, p,  g);
		        result=fitterTest_bug(a,  w, p,  g);
				yout=testOut(truth,result);
				if (yout==1)
				{count++;
				//System.out.println(i);
				}
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
		//Short_HarmonicFitterMyTest.fitterTestRepeat();
	}
	
	public static void fitterTestRepeat(){
		int repeat=1000;
		for(int iter=0;iter<repeat;iter++){
			//System.out.print(iter+"--");
			//Short_HarmonicFitterMyTest.fitterTest();
		}
		
	}
	
	public static double[] fitterTest_bug(double a, double w, double p, double[] nextG){
        //Random randomizer = new Random();
        
        HarmonicOscillator f = new HarmonicOscillator(a, w, p);
        
        HarmonicFitter_bug2 fitter =
            new HarmonicFitter_bug2(new LevenbergMarquardtOptimizer());
        int i=0;
        for (double x = 0.0; x < 10.0; x += 0.1) {
            fitter.addObservedPoint(1, x,
                                    f.value(x) + 0.01 * nextG[i]);
        i++;
        }

        double[] fitted1 = fitter.fit();
        return fitted1;
    //    System.out.print("no initial guess: "+fitted1[0]+" "+fitted1[1]+" "+fitted1[2]+"; ");
//        double[] fitted2 = fitter.fit(new double[]{randomizer.nextDouble(), randomizer.nextDouble(), randomizer.nextDouble()});
//        System.out.println("with initial guess: "+fitted2[0]+" "+fitted2[1]+" "+fitted2[2]);
	}
	
	public static double[] fitterTest(double a, double w, double p, double[] nextG){
        //Random randomizer = new Random();
        
        HarmonicOscillator f = new HarmonicOscillator(a, w, p);
        
        HarmonicFitter fitter =
            new HarmonicFitter(new LevenbergMarquardtOptimizer());
        int i=0;
        for (double x = 0.0; x < 10.0; x += 0.1) {
            fitter.addObservedPoint(1, x,
                                    f.value(x) + 0.01 * nextG[i]);
        i++;
        }

        double[] fitted1 = fitter.fit();
        return fitted1;
    //    System.out.print("no initial guess: "+fitted1[0]+" "+fitted1[1]+" "+fitted1[2]+"; ");
//        double[] fitted2 = fitter.fit(new double[]{randomizer.nextDouble(), randomizer.nextDouble(), randomizer.nextDouble()});
//        System.out.println("with initial guess: "+fitted2[0]+" "+fitted2[1]+" "+fitted2[2]);
	}
	
	public static double[] generateGaussianData(int n){
		double []g=new double[n];
		for(int i=0;i<n;i++){
			g[i]=r.nextGaussian();
		}
		return g;
	}
	public static double createSmallNumber() {
		double a = r.nextDouble();
		double b = r.nextDouble();
		return (a - 0.5);
	}
}

