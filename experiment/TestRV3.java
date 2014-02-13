package experiment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation_bug;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D_bug2;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import edu.cwru.eecs.gang.faultlocalization.expressionvalue.profiler.Profiler;
import experiment.Foo;

public class TestRV3 {
	static double tolerance = 1E-10;
	public static Random r = new Random(177756);
	public static int ncover=0;
	TestRV3() {
	}

	public static int testOut(double truth, double result, double tolerance) {
		double diff = Math.abs(truth - result);
		if (diff > tolerance) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) {
		
			Profiler.visitNewTest(-1);
		try {
			double smallNum = 0;
			int count = 0;
			double result = 0, truth = 0;
			int yout = 0;
			int count2 = 0;
			String filedir = "TestRotationAndVector3D/out";
			String filename = filedir + "/" + "out.txt";
			BufferedWriter out;

			out = new BufferedWriter(new FileWriter(filename, false));

			for (int i = 0; i < 10000; i++) {
				Profiler.visitNewTest(i);
				double a = r.nextDouble();
				double b = r.nextDouble();
				ParaPackage pack = new ParaPackage(r);
				try{
				truth = testComposeA(pack);
				result = testComposeA_bug(pack);
				// System.out.println(result+"  "+ truth);
				yout = testOut(truth, result, tolerance);
				if (yout == 1) {
					// System.out.println("case"+i);
					count++;
				}
				out.write(i + " " + yout);
				out.write("\n");
				out.flush();
				count2++;}
				catch(CardanEulerSingularityException e){
					System.out.println("Eurler exception");
				}
				catch(NotARotationMatrixException e){
					System.out.println("NotARotationMatrixException");
				}
			}
			System.out.println("count is :" + count + "  count2 is" + count2);
			System.out.println("ncover is " +ncover);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}

	public static double testComposeA(ParaPackage pack) {
		double threshold = 1E-10;
		Vector3D v1 = generateVector(pack.m1);
		Vector3D v2 = generateVector(pack.m2);
		Vector3D v3 = generateVector(pack.m3);
		Vector3D v4 = generateVector(pack.m4);
		Vector3D v5 = generateVector(pack.m5);
		Vector3D v6 = generateVector(pack.m6);
		Vector3D v7 = generateVector(pack.m7);
		Vector3D v8 = generateVector(pack.m8);
		Vector3D v9 = generateVector(pack.m9);
		Vector3D v10 = generateVector(pack.m10);
		Vector3D v11 = generateVector(pack.m11);
		Vector3D v12 = generateVector(pack.m12);

		Rotation r1 = new Rotation(v1, pack.angle1);
		Rotation r2 = new Rotation(v2, v3);
		Rotation r3 = new Rotation(v1, v2, v3, v4);
		// double[][] data= null;
		// while(data==null){
		// data=generateArray( r,threshold);
		// }
		double[][] data = r1.getMatrix();
		Rotation r4 = new Rotation(data, threshold);

		RotationOrder[] CardanOrders = { RotationOrder.XYZ, RotationOrder.XZY,
				RotationOrder.YXZ, RotationOrder.YZX, RotationOrder.ZXY,
				RotationOrder.ZYX };

		RotationOrder type = CardanOrders[pack.Coindex];
		Rotation r5 = new Rotation(type, pack.alpha1, pack.alpha2, pack.alpha3);
		double[] angles = r5.getAngles(type);

		RotationOrder[] EulerOrders = { RotationOrder.XYX, RotationOrder.XZX,
				RotationOrder.YXY, RotationOrder.YZY, RotationOrder.ZXZ,
				RotationOrder.ZYZ };

		RotationOrder type2 = EulerOrders[pack.Eoindex];
		Rotation r6 = new Rotation(type2, pack.alpha1, pack.alpha2, pack.alpha3);
		double[] angles2 = r6.getAngles(type2);

		Rotation r7 = r2.applyTo(r1);
		Rotation r8 = r2.applyInverseTo(r1);
		double[] Out = new double[3];
		double[] In = new double[3];
		System.arraycopy(pack.in, 0, In, 0, 3);
		System.arraycopy(pack.out, 0, Out, 0, 3);
		r1.applyInverseTo(In, Out);

		r1.applyInverseTo(r1.applyTo(v1));

		Vector3D n1 = r1.applyTo(v3);
		Vector3D n2 = r2.applyInverseTo(v4);
		Vector3D n3 = r3.getAxis();
		Vector3D n4 = r4.applyInverseTo(v6);
		Vector3D n5 = r5.applyTo(v7);
		Vector3D n6 = r6.applyTo(v8);
		Vector3D n7 = r7.applyInverseTo(v9);
		Vector3D n8 = r8.applyInverseTo(v10);
		Vector3D n9 = new Vector3D(Out[0], Out[1], Out[2]);
		Vector3D n10 = new Vector3D(angles[0], angles[1], angles[2]);
		Vector3D n11 = new Vector3D(angles2[0], angles2[1], angles2[2]);
		double dist1 = n1.distance(n2);
		double dist2 = n3.distanceInf(n4);
		double dist3 = n5.distanceInf(n6);
		double dist4 = n7.distanceSq(n8);
		double dist5 = n9.dotProduct(n10);
		Vector3D n12 = n11.crossProduct(n9);

		Vector3D n13 = new Vector3D(pack.p1, n1, pack.p2, n2, pack.p3, n3,
				pack.p4, n4);
		Vector3D n14 = new Vector3D(pack.p5, n5, pack.p6, n6, pack.p7, n7);

		double result1 = dist1 + dist2 + dist3 + dist4 + dist5;
		double result2 = n12.getNorm1() + n13.getNormSq() + n14.getNormInf();

		double result = result1 + result2;

		return result;
	}

	public static double testComposeA_bug(ParaPackage pack) {
		double threshold = 1E-10;
		Vector3D_bug2 v1 = generateVector_bug(pack.m1);
		Vector3D_bug2 v2 = generateVector_bug(pack.m2);
		Vector3D_bug2 v3 = generateVector_bug(pack.m3);
		Vector3D_bug2 v4 = generateVector_bug(pack.m4);
		Vector3D_bug2 v5 = generateVector_bug(pack.m5);
		Vector3D_bug2 v6 = generateVector_bug(pack.m6);
		Vector3D_bug2 v7 = generateVector_bug(pack.m7);
		Vector3D_bug2 v8 = generateVector_bug(pack.m8);
		Vector3D_bug2 v9 = generateVector_bug(pack.m9);
		Vector3D_bug2 v10 = generateVector_bug(pack.m10);
		Vector3D_bug2 v11 = generateVector_bug(pack.m11);
		Vector3D_bug2 v12 = generateVector_bug(pack.m12);

		Rotation_bug r1 = new Rotation_bug(v1, pack.angle1);
		Rotation_bug r2 = new Rotation_bug(v2, v3);
		Rotation_bug r3 = new Rotation_bug(v1, v2, v3, v4);
		// double[][] data= null;
		// while(data==null){
		// data=generateArray( r,threshold);
		// }
		double[][] data = r1.getMatrix();
		Rotation_bug r4 = new Rotation_bug(data, threshold);

		RotationOrder[] CardanOrders = { RotationOrder.XYZ, RotationOrder.XZY,
				RotationOrder.YXZ, RotationOrder.YZX, RotationOrder.ZXY,
				RotationOrder.ZYX };

		RotationOrder type = CardanOrders[pack.Coindex];
		Rotation_bug r5 = new Rotation_bug(type, pack.alpha1, pack.alpha2,
				pack.alpha3);
		double[] angles = r5.getAngles(type);

		RotationOrder[] EulerOrders = { RotationOrder.XYX, RotationOrder.XZX,
				RotationOrder.YXY, RotationOrder.YZY, RotationOrder.ZXZ,
				RotationOrder.ZYZ };

		RotationOrder type2 = EulerOrders[pack.Eoindex];
		Rotation_bug r6 = new Rotation_bug(type2, pack.alpha1, pack.alpha2,
				pack.alpha3);
		double[] angles2 = r6.getAngles(type2);

		Rotation_bug r7 = r2.applyTo(r1);
		Rotation_bug r8 = r2.applyInverseTo(r1);
		double[] Out = new double[3];
		double[] In = new double[3];
		System.arraycopy(pack.in, 0, In, 0, 3);
		System.arraycopy(pack.out, 0, Out, 0, 3);
		r1.applyInverseTo(In, Out);

		r1.applyInverseTo(r1.applyTo(v1));

		Vector3D_bug2 n1 = r1.applyTo(v3);
		Vector3D_bug2 n2 = r2.applyInverseTo(v4);
		Vector3D_bug2 n3 = r3.getAxis();
		Vector3D_bug2 n4 = r4.applyInverseTo(v6);
		Vector3D_bug2 n5 = r5.applyTo(v7);
		Vector3D_bug2 n6 = r6.applyTo(v8);
		Vector3D_bug2 n7 = r7.applyInverseTo(v9);
		Vector3D_bug2 n8 = r8.applyInverseTo(v10);
		Vector3D_bug2 n9 = new Vector3D_bug2(Out[0], Out[1], Out[2]);
		Vector3D_bug2 n10 = new Vector3D_bug2(angles[0], angles[1], angles[2]);
		Vector3D_bug2 n11 = new Vector3D_bug2(angles2[0], angles2[1],
				angles2[2]);
		double dist1 = n1.distance(n2);
		double dist2 = n3.distanceInf(n4);
		double dist3 = n5.distanceInf(n6);
		double dist4 = n7.distanceSq(n8);
		double dist5 = n9.dotProduct(n10);
		Vector3D_bug2 n12 = n11.crossProduct(n9);

		Vector3D_bug2 n13 = new Vector3D_bug2(pack.p1, n1, pack.p2, n2,
				pack.p3, n3, pack.p4, n4);
		Vector3D_bug2 n14 = new Vector3D_bug2(pack.p5, n5, pack.p6, n6,
				pack.p7, n7);

		double result1 = dist1 + dist2 + dist3 + dist4 + dist5;
		double result2 = n12.getNorm1() + n13.getNormSq() + n14.getNormInf();

		double result = result1 + result2;

		return result;
	}

	public static Vector3D_bug2 generateVector_bug(double[] m) {
		return new Vector3D_bug2(m[0], m[1], m[2]);
	}

	public static Vector3D generateVector(double[] m) {
		return new Vector3D(m[0], m[1], m[2]);
	}

	public static double createSmallNumber() {
		double a = r.nextDouble();
		double b = r.nextDouble();
		return (a - 0.5) / Math.pow(10, b * 25);
	}
	//
	// Rotation r1 = new Rotation(new Vector3D(2, -3, 5), 1.7);
	// Rotation r2 = new Rotation(new Vector3D(-1, 3, 2), 0.3);

}
