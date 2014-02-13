package experiment.small;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.stat.regression.MillerUpdatingRegression;
import org.apache.commons.math3.stat.regression.MillerUpdatingRegression_bug1;
import org.apache.commons.math3.stat.regression.RegressionResults;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_MillerUpdatingRegressionMyTest {
	public static Random r = new Random(177756);
	/**
	 * @param args
	 */
	public static int testOut(double diff) {
		double tolerance = 1E-12;

		

			diff = Math.abs(diff);
			if (diff > tolerance) {
				return 1;
			}else

			{
		return 0;}
	}
	
	@SuppressWarnings("resource")
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
			String filedir = "TestFastCosine/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count = 0;
			double diff=0;
			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);
				int varnum=1+r.nextInt(max_len-1);
				int flag=r.nextInt(2);
				System.out.println("flag "+flag);
				diff=Short_MillerUpdatingRegressionMyTest.testReg(varnum,flag);
				yout=testOut(diff);
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
		
		
		
		
		Short_MillerUpdatingRegressionMyTest.testRegRepeat();

	}
	
	public static void testRegRepeat(){
		int repeat=1000;
		int max_len=10;
		
		 // number of variable no less than one
		
		for(int iter=0;iter<repeat;iter++){
		//	System.out.print(iter+"");
		//	int varnum=1+(int)(Math.random()*(max_len-1));
			
		}
	}
	
	public static double testReg(int varnum,int flag){
		double truth=0,result=0;
		//Random rand=new Random();
		MillerUpdatingRegression reg=new MillerUpdatingRegression(varnum,true);
		
		MillerUpdatingRegression_bug1 reg_bug= new MillerUpdatingRegression_bug1(varnum,true);
		
		int samplenum=varnum+20+(int)(r.nextDouble()*(varnum+1000-1)); // number of observations
		double[] coef=new double[varnum]; // randomly generate regression coefficients
		for(int i=0;i<varnum;i++){
			coef[i]=r.nextDouble();
		}
		

		double[][] x=new double[samplenum-1][];
		double[] y=new double[samplenum-1];
		for(int i=0;i<samplenum-1;i++){
			x[i]=new double[varnum];
			double xsum=0;
			for(int j=0;j<varnum;j++){
				x[i][j]=r.nextDouble(); // randomly generate data
				xsum=xsum+coef[j]*x[i][j];
			}
			y[i]=xsum+r.nextDouble(); // generate labels using data, coefficients and random noise
		}
	

		///////////////function 1: add data/////////////////////////////
		
		reg.addObservations(x, y);
		reg_bug.addObservations(x, y);
//		///////////////function 2: add data/////////////////////////////
//		reg.addObservation(xAdd, yAdd);
		
		//////////////function 3: regression////////////////////////////
	//	RegressionResults res=reg.regress(); // regression
		
		int regssornum=1+(int)(r.nextDouble()*(varnum-1));
		//////////////function 4: regression with partial variables//////////////
		RegressionResults res1=reg.regress(regssornum); // regression with the first few variables
		RegressionResults res1_bug=reg_bug.regress(regssornum);
		
		int[] regssors=new int[regssornum];
		for(int i=0;i<regssornum;i++){
			regssors[i]=1+(int)(r.nextDouble()*(regssornum-1));
		}
		/////////////function 5: regression with partial variables///////////////
		RegressionResults res2=reg.regress(regssors); // regression with chosen variables
		RegressionResults res2_bug=reg_bug.regress(regssors);
		if(flag==1){
		truth=res1.getRegressionSumSquares();
		result=res1_bug.getRegressionSumSquares();
		}
		else{
			truth=res2.getRegressionSumSquares();
			result=res2_bug.getRegressionSumSquares();
		}
		/////////////function 6: get partial correlation/////////////////////////
		double[] corr=reg.getPartialCorrelations(regssornum); // get partial correlations
		
		/////////////function 7: get diagonals of hat matrix//////////////////////
//		for(int i=0;i<x.length;i++){
//			double hat=reg.getDiagonalOfHatMatrix(x[i]);
//		}
		
		
//		
//		System.out.println("obs: "+reg.getN()+"; paras: "+varnum+", "+res1.getNumberOfParameters()
//				+", "+res2.getNumberOfParameters()+"; corr length: "+corr.length+"; ");
		
		double diff=truth-result;
		return diff;
	}
}
	//public void generateData(int varnum){}
