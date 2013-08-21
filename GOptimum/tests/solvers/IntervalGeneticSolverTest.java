package solvers;

import java.util.Random;
import net.sourceforge.interval.ia_math.RealInterval;

import org.junit.Test;

import testharness.TestHarness;
import testharness.TestData;
import algorithms.Algorithm;

public class IntervalGeneticSolverTest {
	protected Random rnd = new Random();
	protected TestHarness test = new TestHarness();
	protected TestData result = null;
	
	protected double alpha = 1, betta = 0, gamma = 0;

//	@Ignore
	@Test(timeout=6*1000) //12 sec
	public final void test1() {
		Algorithm a = new IntervalGeneticSolver(alpha, betta, gamma);
		final int dim = rnd.nextInt(9)+1;
		RealInterval area = new RealInterval(-rnd.nextInt(70), rnd.nextInt(200));
		result = test.f_DeJong_Zero(a, dim, area);
	}
	@Test(timeout=12*1000) //12 sec
	public final void test1p() {
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		final int dim = 2;
		RealInterval area = new RealInterval(-700, 200);
		result = test.f_DeJong_Zero(a, dim, area);
	}
	@Test(timeout=12*1000) //12 sec
	public final void test11() {
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		final int dim = rnd.nextInt(9)+1;
		result = test.f_DeJong_NotSim(a, dim);
	}
	@Test(timeout=12*1000) //12 sec
	public final void test2() {
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		RealInterval area = new RealInterval(-1000, 2000);
		result = test.f_Price5_Zero(a, area);
	}
	@Test(timeout=12*1000) //12 sec
	public final void test3() {
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		RealInterval area = new RealInterval(-1000, 2000);
		result = test.f_Rastrigin10(a, area);
	}
	
//	@Ignore
	@Test(timeout=12*1000) //12 sec
	public final void test4() {
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		RealInterval area = new RealInterval(-1, 1);
		result = test.f_SixHumpCamelBack(a, area);
	}
	@Test(timeout=12*1000) //12 sec
	public final void test5() {
		int dim = 7;
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		RealInterval area = new RealInterval(-1000, 2000);
		result = test.f_RosenbrockGn(a, dim, area);
	}
	@Test(timeout=30*1000) //30 sec
	public final void test5_1() {
		int dim = 2;
		Algorithm a = new IntervalGeneticSolverMain(alpha, betta, gamma);
		RealInterval area = new RealInterval(-10, 10);
		result = test.f_RosenbrockGn(a, dim, area);
	}
}
