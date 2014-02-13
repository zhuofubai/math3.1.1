/**
 * 
 */
package experiment.small;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.DstNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastCosineTransformer_bug1;
import org.apache.commons.math3.transform.FastSineTransformer;
import org.apache.commons.math3.transform.FastSineTransformer_bug1;
import org.apache.commons.math3.transform.TransformType;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

/**
 * @author Zhuofu
 * 
 */
public class Short_TestFastSine {
	public static Random r = new Random(177756);
	/**
	 * 
	 */
	public Short_TestFastSine() {
		// TODO Auto-generated constructor stub
	}

	public static int testOut(double[] truth, double[] result) {
		double tolerance = 1E-12;

		if (truth.length != result.length) {
			System.out
					.println("two inputs' dimension is mismatch, the result length is: "
							+ result.length);
			return 1;}

		double diff=0;
		for (int i = 0; i < truth.length; i++) {
			
			diff= Math.abs(truth[i] - result[i]);
			if (diff > tolerance) {
				return 1;
			} 
			
		}
		return 0;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args){
		
		try{
			Profiler.visitNewTest(-1);
			// TODO Auto-generated method stub
			
			FastSineTransformer_bug1 transformer_bug = new FastSineTransformer_bug1(
				DstNormalization.STANDARD_DST_I);
		FastSineTransformer transformer = new FastSineTransformer(
				DstNormalization.STANDARD_DST_I);
			double result[];//, tolerance = 1E-12;
			double truth[];

			
			//double smallNum = 0;
			//int count = 0;
			//double result = 0, truth = 0;
			int yout = 0;
			int count2 = 0;
			String filedir = "TestFastCosine/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;
			out = new BufferedWriter(new FileWriter(filename, false));
			int count=0;
			
			for (int i = 0; i < 10; i++) {
				Profiler.visitNewTest(i);			
//				double y[]={Math.random()*5*Math.sin(a), Math.random()*5, Math.random()*5, Math.random()*5,
//						Math.random()*5, Math.random()*5, Math.random()*5, Math.random()*5};
				double[]y =createData();
				result = transformer_bug.transform(y, TransformType.FORWARD);
				truth =transformer.transform(y, TransformType.FORWARD);
				yout=Short_TestFastSine.testOut(truth,result);
				if (yout==1){count++;}
				out.write(i+" "+yout);
				out.write("\n");
				out.flush();
				//transformer_bug.log.setTestId(i);ffffff
				count2++;
			}
		//	Profiler.visitNewTest(1001);	
			//Test2.main();
			
			 
			Profiler.stopProfiling();
			System.out.println("count is :" + count + "  count2 is" + count2);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("end");
		
	}
	public static double[] createData(){
		int MAX=1;
		int MIN=-1;
		int k=2+r.nextInt(4);
		int datalength=(int)Math.pow(2, k);
		double []y=new double[datalength];
		for(int j=1;j<datalength;j++){
			if (j==1)
			{y[j]=0.0;}
			else
			{y[j]=MIN+r.nextDouble()*(MAX-MIN+1);
			}
		}
		return y;
	}
	public static double createSmallNumber() {
		double a = r.nextDouble();
		double b = r.nextDouble();
		return (a - 0.5) / Math.pow(10, b * 25);
	}
}
