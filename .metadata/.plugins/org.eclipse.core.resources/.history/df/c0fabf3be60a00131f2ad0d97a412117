package worklists;

import java.util.Comparator;

import solvers.IntervalGeneticSolver;
import core.Box;

public class GeneticFitComparator implements Comparator<Box> {

	private IntervalGeneticSolver iGA;
	public GeneticFitComparator(IntervalGeneticSolver iGA) {
		this.iGA = iGA;
	}
	@Override
	public int compare(Box b1, Box b2) {
		assert(b1.getDimension() == b2.getDimension());
		double fit1 = iGA.calcFitFunction(b1);
		double fit2 = iGA.calcFitFunction(b2);
		//if (fit1 == fit2)
			//return ( new AscendingLowBoundBoxComparator() ).compare(b1, b2);
		if (fit1 <= fit2)
			return +1;
		else
			return -1;
	}

}
