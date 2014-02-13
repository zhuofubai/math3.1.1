package experiment.small;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SymmLQ;
import org.apache.commons.math3.linear.SymmLQ_bug;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class Short_SymmLQMyTest {
	public static Random r = new Random(177756);

	/**
	 * @param args
	 */
	public static int testOut(RealVector truth, RealVector result) {
		final double enorm = truth.subtract(result).getNorm() / truth.getNorm();
		final double etol = 1E-5;
		if (enorm>etol){
			return 1;
		}else{
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
			int maxN=50;
			int n=0;
			double shift=0;
			double pertbn=0;
			for (int i = 0; i < 1000; i++) {
				Profiler.visitNewTest(i);
				 
				 n=1+r.nextInt(maxN-1);
				shift = r.nextDouble();
				pertbn = r.nextDouble();
				//System.out.print("dim="+n);
				RealVector truth=Short_SymmLQMyTest.saundersTest(n, true, true, shift, pertbn);
				RealVector result=Short_SymmLQMyTest.saundersTest_bug(n, true, true, shift, pertbn);
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
	
//	public static void lqTestRepeat(){
//		int repeat=1000;
//		for(int iter=0;iter<repeat;iter++){
//			System.out.print(iter+"--");
//			Short_SymmLQMyTest.lqTest();
//		}
//	}
	
	public static void lqTest(){
		int maxN=50;
		int n=1+(int) (Math.random()*(maxN-1));
		double shift = Math.random();
		double pertbn = Math.random();
		System.out.print("dim="+n);
		Short_SymmLQMyTest.saundersTest(n, true, true, shift, pertbn);
		
		
	}

	private static RealVector saundersTest(final int n, final boolean goodb,
			final boolean precon, final double shift, final double pertbn) {
		
		final RealLinearOperator a = new RealLinearOperator() {

			@Override
			public RealVector operate(final RealVector x) {
				if (x.getDimension() != n) {
					throw new DimensionMismatchException(x.getDimension(), n);
				}
				final double[] y = new double[n];
				for (int i = 0; i < n; i++) {
					y[i] = (i + 1) * 1.1 / n * x.getEntry(i);
				}
				return new ArrayRealVector(y, false);
			}

			@Override
			public int getRowDimension() {
				return n;
			}

			@Override
			public int getColumnDimension() {
				return n;
			}
		};
		final double shiftm = shift;
		final double pertm = FastMath.abs(pertbn);
		final RealLinearOperator minv;
		if (precon) {
			minv = new RealLinearOperator() {
				@Override
				public int getRowDimension() {
					return n;
				}

				@Override
				public int getColumnDimension() {
					return n;
				}

				@Override
				public RealVector operate(final RealVector x) {
					if (x.getDimension() != n) {
						throw new DimensionMismatchException(x.getDimension(),
								n);
					}
					final double[] y = new double[n];
					for (int i = 0; i < n; i++) {
						double d = (i + 1) * 1.1 / n;
						d = FastMath.abs(d - shiftm);
						if (i % 10 == 0) {
							d += pertm;
						}
						y[i] = x.getEntry(i) / d;
					}
					return new ArrayRealVector(y, false);
				}
			};
		} else {
			minv = null;
		}
		final RealVector xtrue = new ArrayRealVector(n);
		for (int i = 0; i < n; i++) {
			xtrue.setEntry(i, n - i);
		}
		final RealVector b = a.operate(xtrue);
		b.combineToSelf(1.0, -shift, xtrue);
		final SymmLQ solver = new SymmLQ(2 * n, 1E-12, true);
		final RealVector x = solver.solve(a, minv, b, goodb, shift);
		final RealVector y = a.operate(x);
		
		return x;

	}
	private static RealVector saundersTest_bug(final int n, final boolean goodb,
			final boolean precon, final double shift, final double pertbn) {
		
		final RealLinearOperator a = new RealLinearOperator() {

			@Override
			public RealVector operate(final RealVector x) {
				if (x.getDimension() != n) {
					throw new DimensionMismatchException(x.getDimension(), n);
				}
				final double[] y = new double[n];
				for (int i = 0; i < n; i++) {
					y[i] = (i + 1) * 1.1 / n * x.getEntry(i);
				}
				return new ArrayRealVector(y, false);
			}

			@Override
			public int getRowDimension() {
				return n;
			}

			@Override
			public int getColumnDimension() {
				return n;
			}
		};
		final double shiftm = shift;
		final double pertm = FastMath.abs(pertbn);
		final RealLinearOperator minv;
		if (precon) {
			minv = new RealLinearOperator() {
				@Override
				public int getRowDimension() {
					return n;
				}

				@Override
				public int getColumnDimension() {
					return n;
				}

				@Override
				public RealVector operate(final RealVector x) {
					if (x.getDimension() != n) {
						throw new DimensionMismatchException(x.getDimension(),
								n);
					}
					final double[] y = new double[n];
					for (int i = 0; i < n; i++) {
						double d = (i + 1) * 1.1 / n;
						d = FastMath.abs(d - shiftm);
						if (i % 10 == 0) {
							d += pertm;
						}
						y[i] = x.getEntry(i) / d;
					}
					return new ArrayRealVector(y, false);
				}
			};
		} else {
			minv = null;
		}
		final RealVector xtrue = new ArrayRealVector(n);
		for (int i = 0; i < n; i++) {
			xtrue.setEntry(i, n - i);
		}
		final RealVector b = a.operate(xtrue);
		b.combineToSelf(1.0, -shift, xtrue);
		final SymmLQ_bug solver = new SymmLQ_bug(2 * n, 1E-12, true);
		final RealVector x = solver.solve(a, minv, b, goodb, shift);
		final RealVector y = a.operate(x);
		
		return x;

	}
}
