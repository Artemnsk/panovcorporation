package worklists;

import java.util.Comparator;

import solvers.IntervalGeneticSolver;
import core.Box;

public class GeneticFitComparator implements Comparator<Box> {

	private IntervalGeneticSolver iGA;
	public GeneticFitComparator(IntervalGeneticSolver iGA) {
		super();
		this.iGA = iGA;
	}
	@Override
	public int compare(Box b1, Box b2) {
        assert(b1.getDimension() == b2.getDimension());
        if (b1 == b2)
            return 0;
        double fit1 = iGA.calcFitFunction(b1);
        double fit2 = iGA.calcFitFunction(b2);
        if (fit1 == fit2) {
            if (b1.equals(b2))
                return 0;
            double hi1 = b1.getFunctionValue().hi();
            double hi2 = b2.getFunctionValue().hi();
            if (hi1 == hi2) {
                double sumWid1 = 0, sumWid2 = 0;
                for (int i = b1.getDimension()-1; i >= 0; i--) {
                    sumWid1 += b1.getInterval(i).wid();
                    sumWid2 += b2.getInterval(i).wid();
                }
                if (sumWid1 == sumWid2) { // ok. everything is absolutely equals, but these boxes are different. lets distinguish them somehow
                    for (int i = b1.getDimension()-1; i >= 0; i--) {
                        if (b1.getInterval(i).lo() < b2.getInterval(i).lo() ||
                                b1.getInterval(i).hi() < b2.getInterval(i).hi() )
                            return -1;
                        if (b1.getInterval(i).lo() > b2.getInterval(i).lo() ||
                                b1.getInterval(i).hi() > b2.getInterval(i).hi() )
                            return 1;
                    }
                } else
                    return sumWid1 > sumWid2 ? -1 : 1; // wider boxes goes first
            } else
                return hi1 > hi2 ? -1 : 1; // offer boxes with wider function estimation first
        }
        return (fit1 > fit2) ? +1 : -1;
	}

}
