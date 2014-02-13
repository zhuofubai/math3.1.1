package experiment.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.regression.SimpleRegression_bug1;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_simpleRegressionMyTest {
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
		try {
			int max_len = 10;
			//int iter = 1000;

			Profiler.visitNewTest(-1);
			// TODO Auto-generated method stub

			// int count = 0;
			// double result = 0, truth = 0;
			int yout = 0;
			int count2 = 0;
			String filedir = "TestSimpleRegression/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count = 0;
			double intercept=0;
			double slope=0;
			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);
				intercept=r.nextDouble();
				slope=r.nextDouble();
				double[]truth=testLinearModel(50,intercept,slope);
				double[]result=testLinearModel_bug(50,intercept,slope);
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
	
	public static void testSimpleReg(){
		Random rand=new Random();
		int iter=100;
		for(int i=0;i<iter;i++){
			double intercept=rand.nextDouble();
			double slope=rand.nextDouble();
			testLinearModel(50,intercept,slope);
		}
		
//		for(int i=0;i<iter;i++){
//			testRandom(50);
//			System.out.println("test"+i);
//		}
	}
	
//	public static void testRandom(Integer iter){
//		double[][] data=new double[iter][iter];
//		Random rand = new Random(); 
//		for(int i=0;i<iter;i++){
//			double x=rand.nextDouble();
//			double y=rand.nextDouble();
//			data[i][0]=x;
//			data[i][1]=y;
//			
//		}
//		
//		SimpleRegression reg=new SimpleRegression(true);
//		reg.addData(data);
//		double addx=rand.nextDouble();
//		double addy=rand.nextDouble();
//		reg.addData(addx, addy);
//		reg.removeData(addx, addy);
//		
//		double interceptCal=reg.getIntercept();
//		double slopeCal=reg.getSlope();
//		double sse=reg.getSumSquaredErrors();
//		double mse=reg.getMeanSquareError();
//		double rs=reg.getRSquare();
//		
//		double prex=rand.nextDouble();
//		double prey=reg.predict(prex);
//	}
	
	
	
	public static double[] testLinearModel(Integer iter, double intercept, double slope){
		double[][] data=new double[iter][iter];
		Random rand = new Random(); 
		for(int i=0;i<iter;i++){
			double x=rand.nextDouble();
			double noise=Math.random();
			double y=intercept+slope*x+noise;
			data[i][0]=x;
			data[i][1]=y;
			
		}
		
		SimpleRegression reg=new SimpleRegression(true);
		reg.addData(data);
		double addx=rand.nextDouble();
		double addy=intercept+slope*addx+Math.random();
		reg.addData(addx, addy);
		reg.removeData(addx, addy);
		
		double interceptCal=reg.getIntercept();
		double slopeCal=reg.getSlope();
		double sse=reg.getSumSquaredErrors();
		double mse=reg.getMeanSquareError();
		double rs=reg.getRSquare();
		
		double prex=rand.nextDouble();
		double prey=reg.predict(prex);
		
		
		double[] truth={interceptCal, slopeCal, sse, mse, rs, prex, prey};
		return truth;
	}
	
	public static double[] testLinearModel_bug(Integer iter, double intercept, double slope){
		double[][] data=new double[iter][iter];
		Random rand = new Random(); 
		for(int i=0;i<iter;i++){
			double x=rand.nextDouble();
			double noise=Math.random();
			double y=intercept+slope*x+noise;
			data[i][0]=x;
			data[i][1]=y;
			
		}
		
		SimpleRegression_bug1 reg=new SimpleRegression_bug1(true);
		reg.addData(data);
		double addx=rand.nextDouble();
		double addy=intercept+slope*addx+Math.random();
		reg.addData(addx, addy);
		reg.removeData(addx, addy);
		
		double interceptCal=reg.getIntercept();
		double slopeCal=reg.getSlope();
		double sse=reg.getSumSquaredErrors();
		double mse=reg.getMeanSquareError();
		double rs=reg.getRSquare();
		
		double prex=rand.nextDouble();
		double prey=reg.predict(prex);
		
		
		double[] result={interceptCal, slopeCal, sse, mse, rs, prex, prey};
		return result;
	}
}
