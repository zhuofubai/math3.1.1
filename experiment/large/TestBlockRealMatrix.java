package experiment.large;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix_bug5;
import org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor;
import org.apache.commons.math3.linear.DefaultRealMatrixPreservingVisitor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
//import org.apache.commons.math3.linear.BlockRealMatrixTest.GetVisitor;
//import org.apache.commons.math3.linear.BlockRealMatrixTest.SetVisitor;
import org.junit.Assert;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;

public class TestBlockRealMatrix {
	static Random r = new Random(17899l);// 17899l
	static Random r2 = new Random(178991);

	public TestBlockRealMatrix() {
		// TODO Auto-generated constructor stub
	}

	public static int testOut(double truth, double result, double tolerance) {
		double diff = Math.abs(truth - result);
		if (diff > tolerance) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Profiler.visitNewTest(-1);
		try {
			int count = 0;
			int count2 = 0;
			String filedir = "TestBlockRealMatrix/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out = new BufferedWriter(new FileWriter(filename,
					false));

			double truth = 0;
			double result = 0;
			int yout = 0;
			int n = 5 + r.nextInt(65), row = 0, column = 0, column2 = 0, row2 = 0, column3 = 0, row3 = 0;
			double k = 1E-4;
			double tolerance = 1E-12, u0, u1, u2, u3;
			double[][] data1 = new double[n][n];
			double[][] data2 = new double[n][n];
			double[][] data3 = new double[n][n];
			double[][] data4 = new double[n][n];
			double []vect0=new double[n];
			for (int i = 0; i < 10000; i++) {
				Profiler.visitNewTest(i);
				row = r.nextInt(n - 1) + 1;
				column = r.nextInt(n - 1) + 1;
				column2 = r.nextInt(n - 1) + 1;
				row2 = r.nextInt(n - 1) + 1;
				column3=r.nextInt(n - 1) + 1;
				row3=r.nextInt(n - 1) + 1;
				u0 = r.nextDouble() * k;
				u1 = r.nextDouble() * k;
				u2 = r.nextDouble() * k;
				u3 = r.nextDouble() * k;
				data1 = createRandomArray(r, n);
				data2 = createRandomArray(r, n);
				data3 = createRandomArray(r, n);
				data4 = createRandomArray(r, n);
				vect0 = createRandomVector(r,n);
				truth = testNumericalOP(data1, data2, data3, data4, vect0, n,row,
						column, row2, column2,row3,column3, u0, u1, u2, u3);
				result = testNumericalOP_bug(data1, data2, data3, data4, vect0,n,
						row, column, row2, column2,row3,column3, u0, u1, u2, u3);
			//	System.out.println(result + "  " + truth);
				yout = testOut(result, truth, tolerance);
				if (yout == 1) {
					// System.out.println("case"+i);
					count++;
				}
				out.write(i + " " + yout);
				out.write("\n");
				out.flush();
				count2++;
			}

			System.out.println("count is :" + count + "  count2 is" + count2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static double testNumericalOP(double[][] data1, double[][] data2,
			double[][] data3, double[][] data4, double []vect0,int n, int row, int column,
			int row2, int column2, int row3, int column3, double u0, double u1,
			double u2, double u3) {

		RealMatrix m1 = createRandomMatrix(data1, n, n);
		RealMatrix m2 = createRandomMatrix(data2, n, n);
		RealMatrix m3 = createRandomMatrix(data3, n, n);
		RealMatrix m4 = createRandomMatrix(data4, n, n);
		// scalaradd
		m1 = m1.scalarAdd(u1);
		// scalrmulti
		m1 = m1.scalarMultiply(1 - u2);
		// add
		m1 = m1.add(m2);

		m1.addToEntry(row, column, u0);
		// minus
		m1 = m1.subtract(m3);
		// multiply
		m1 = m1.multiply(m4);

		// transpose
		m1 = m1.transpose();

		// get column
		//double[] vect0 = m1.getColumn(row);
		// premultiply
		double[] vect1 = m1.preMultiply(vect0);

		// operate
		double[] vect2 = m1.operate(vect1);
		// check get & set column
		m1.setColumn(column, vect1);
		RealMatrix m21 = m2.getColumnMatrix(column2);
		m1.setColumnMatrix(column2, m21);
		RealVector v32 = m3.getColumnVector(column2);
		m1.setColumnVector(column3, v32);
		// chect get & set row
		m1.setRow(row, vect2);
		RealMatrix m22 = m2.getRowMatrix(row2);
		m1.setRowMatrix(row2, m22);
		RealVector v33 = m3.getRowVector(row2);
		m1.setRowVector(row3, v33);
		// check get submatrix
		RealMatrix sub = m1.getSubMatrix(row - 1, row, column - 1, column);
		sub = sub.scalarAdd(u3);
		m1.setSubMatrix(sub.getData(), column - 1, row - 1);

		// norm
		double a = m1.getNorm();
		return a;
	}

	public static double testNumericalOP_bug(double[][] data1,
			double[][] data2, double[][] data3, double[][] data4,double[]vect0, int n,
			int row, int column, int row2, int column2, int row3, int column3,
			double u0, double u1, double u2, double u3) {

		RealMatrix m1 = createRandomMatrix_bug(data1, n, n);
		RealMatrix m2 = createRandomMatrix_bug(data2, n, n);
		RealMatrix m3 = createRandomMatrix_bug(data3, n, n);
		RealMatrix m4 = createRandomMatrix_bug(data4, n, n);

		// scalaradd
				m1 = m1.scalarAdd(u1);
				// scalrmulti
				m1 = m1.scalarMultiply(1 - u2);
				// add
				m1 = m1.add(m2);

				m1.addToEntry(row, column, u0);
				// minus
				m1 = m1.subtract(m3);
				// multiply
				m1 = m1.multiply(m4);

				// transpose
				m1 = m1.transpose();

				// get column
				//double[] vect0 = m1.getColumn(row);
				// premultiply
				double[] vect1 = m1.preMultiply(vect0);

				// operate
				double[] vect2 = m1.operate(vect1);
				// check get & set column
				m1.setColumn(column, vect1);
				RealMatrix m21 = m2.getColumnMatrix(column2);
				m1.setColumnMatrix(column2, m21);
				RealVector v32 = m3.getColumnVector(column2);
				m1.setColumnVector(column3, v32);
				// chect get & set row
				m1.setRow(row, vect2);
				RealMatrix m22 = m2.getRowMatrix(row2);
				m1.setRowMatrix(row2, m22);
				RealVector v33 = m3.getRowVector(row2);
				m1.setRowVector(row3, v33);
				// check get submatrix
				RealMatrix sub = m1.getSubMatrix(row - 1, row, column - 1, column);
				sub = sub.scalarAdd(u3);
				m1.setSubMatrix(sub.getData(), column - 1, row - 1);

				// norm
				double a = m1.getNorm();
				return a;
	}

	public void testWalk() {
		int rows = 150;
		int columns = 75;

		RealMatrix m = new BlockRealMatrix(rows, columns);
		m.walkInRowOrder(new SetVisitor());
		GetVisitor getVisitor = new GetVisitor();
		m.walkInOptimizedOrder(getVisitor);
		Assert.assertEquals(rows * columns, getVisitor.getCount());

		m = new BlockRealMatrix(rows, columns);
		m.walkInRowOrder(new SetVisitor(), 1, rows - 2, 1, columns - 2);
		getVisitor = new GetVisitor();
		m.walkInOptimizedOrder(getVisitor, 1, rows - 2, 1, columns - 2);
		Assert.assertEquals((rows - 2) * (columns - 2), getVisitor.getCount());
		for (int i = 0; i < rows; ++i) {
			Assert.assertEquals(0.0, m.getEntry(i, 0), 0);
			Assert.assertEquals(0.0, m.getEntry(i, columns - 1), 0);
		}
		for (int j = 0; j < columns; ++j) {
			Assert.assertEquals(0.0, m.getEntry(0, j), 0);
			Assert.assertEquals(0.0, m.getEntry(rows - 1, j), 0);
		}

		m = new BlockRealMatrix(rows, columns);
		m.walkInColumnOrder(new SetVisitor());
		getVisitor = new GetVisitor();
		m.walkInOptimizedOrder(getVisitor);
		Assert.assertEquals(rows * columns, getVisitor.getCount());

		m = new BlockRealMatrix(rows, columns);
		m.walkInColumnOrder(new SetVisitor(), 1, rows - 2, 1, columns - 2);
		getVisitor = new GetVisitor();
		m.walkInOptimizedOrder(getVisitor, 1, rows - 2, 1, columns - 2);
		Assert.assertEquals((rows - 2) * (columns - 2), getVisitor.getCount());
		for (int i = 0; i < rows; ++i) {
			Assert.assertEquals(0.0, m.getEntry(i, 0), 0);
			Assert.assertEquals(0.0, m.getEntry(i, columns - 1), 0);
		}
		for (int j = 0; j < columns; ++j) {
			Assert.assertEquals(0.0, m.getEntry(0, j), 0);
			Assert.assertEquals(0.0, m.getEntry(rows - 1, j), 0);
		}

		m = new BlockRealMatrix(rows, columns);
		m.walkInOptimizedOrder(new SetVisitor());
		getVisitor = new GetVisitor();
		m.walkInRowOrder(getVisitor);
		Assert.assertEquals(rows * columns, getVisitor.getCount());

		m = new BlockRealMatrix(rows, columns);
		m.walkInOptimizedOrder(new SetVisitor(), 1, rows - 2, 1, columns - 2);
		getVisitor = new GetVisitor();
		m.walkInRowOrder(getVisitor, 1, rows - 2, 1, columns - 2);
		Assert.assertEquals((rows - 2) * (columns - 2), getVisitor.getCount());
		for (int i = 0; i < rows; ++i) {
			Assert.assertEquals(0.0, m.getEntry(i, 0), 0);
			Assert.assertEquals(0.0, m.getEntry(i, columns - 1), 0);
		}
		for (int j = 0; j < columns; ++j) {
			Assert.assertEquals(0.0, m.getEntry(0, j), 0);
			Assert.assertEquals(0.0, m.getEntry(rows - 1, j), 0);
		}

		m = new BlockRealMatrix(rows, columns);
		m.walkInOptimizedOrder(new SetVisitor());
		getVisitor = new GetVisitor();
		m.walkInColumnOrder(getVisitor);
		Assert.assertEquals(rows * columns, getVisitor.getCount());

		m = new BlockRealMatrix(rows, columns);
		m.walkInOptimizedOrder(new SetVisitor(), 1, rows - 2, 1, columns - 2);
		getVisitor = new GetVisitor();
		m.walkInColumnOrder(getVisitor, 1, rows - 2, 1, columns - 2);
		Assert.assertEquals((rows - 2) * (columns - 2), getVisitor.getCount());
		for (int i = 0; i < rows; ++i) {
			Assert.assertEquals(0.0, m.getEntry(i, 0), 0);
			Assert.assertEquals(0.0, m.getEntry(i, columns - 1), 0);
		}
		for (int j = 0; j < columns; ++j) {
			Assert.assertEquals(0.0, m.getEntry(0, j), 0);
			Assert.assertEquals(0.0, m.getEntry(rows - 1, j), 0);
		}

	}

	private static class SetVisitor extends DefaultRealMatrixChangingVisitor {
		@Override
		public double visit(int i, int j, double value) {
			return i + j / 1024.0;
		}
	}

	private static class GetVisitor extends DefaultRealMatrixPreservingVisitor {
		private int count = 0;

		@Override
		public void visit(int i, int j, double value) {
			++count;
			Assert.assertEquals(i + j / 1024.0, value, 0.0);
		}

		public int getCount() {
			return count;
		}
	}

	private void checkGetSubMatrix(RealMatrix m, double[][] reference,
			int startRow, int endRow, int startColumn, int endColumn) {
		try {
			RealMatrix sub = m.getSubMatrix(startRow, endRow, startColumn,
					endColumn);
			if (reference != null) {
				Assert.assertEquals(new BlockRealMatrix(reference), sub);
			} else {
				Assert.fail("Expecting OutOfRangeException or NumberIsTooSmallException or NoDataException");
			}
		} catch (OutOfRangeException e) {
			if (reference != null) {
				throw e;
			}
		} catch (NumberIsTooSmallException e) {
			if (reference != null) {
				throw e;
			}
		} catch (NoDataException e) {
			if (reference != null) {
				throw e;
			}
		}
	}

	private void checkGetSubMatrix(RealMatrix m, double[][] reference,
			int[] selectedRows, int[] selectedColumns) {
		try {
			RealMatrix sub = m.getSubMatrix(selectedRows, selectedColumns);
			if (reference != null) {
				Assert.assertEquals(new BlockRealMatrix(reference), sub);
			} else {
				Assert.fail("Expecting OutOfRangeException or NumberIsTooSmallExceptiono r NoDataException");
			}
		} catch (OutOfRangeException e) {
			if (reference != null) {
				throw e;
			}
		} catch (NumberIsTooSmallException e) {
			if (reference != null) {
				throw e;
			}
		} catch (NoDataException e) {
			if (reference != null) {
				throw e;
			}
		}
	}

	private static double[][] createRandomArray(Random r, int n) {
		double[][] a = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = (2 * r.nextDouble() - 1) * 1E-5;
			}
		}
		return a;
	}
	private static double[] createRandomVector(Random r, int n){
		double[] a = new double[n];
		for (int i = 0; i < n; i++) {
				a[i] = (2 * r.nextDouble() - 1) ;
		}
		return a;
	}

	private static BlockRealMatrix createRandomMatrix(double[][] data,
			int rows, int columns) {
		BlockRealMatrix m = new BlockRealMatrix(rows, columns);
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				m.setEntry(i, j, data[i][j]);
			}
		}
		return m;
	}

	private static BlockRealMatrix_bug5 createRandomMatrix_bug(double[][] data,
			int rows, int columns) {
		BlockRealMatrix_bug5 m = new BlockRealMatrix_bug5(rows, columns);
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < columns; ++j) {
				m.setEntry(i, j, data[i][j]);
			}
		}
		return m;
	}
}
