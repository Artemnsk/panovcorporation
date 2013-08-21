package solvers;

//setProblem - кастовать для своего крутого Box'a !
import Population.Population;
import static algorithms.OptimizationStatus.EMPTY_WORKLIST;
import static algorithms.OptimizationStatus.RUNNING;
import static algorithms.OptimizationStatus.STOP_CRITERION_SATISFIED;
import net.sourceforge.interval.ia_math.RealInterval;
import core.Box;
import splitters.BiggestSideEquallySplitter;
import splitters.Splitter;
import worklists.GeneticFitComparator;
import worklists.SortedWorkList;
import worklists.WorkList;
import choosers.Chooser;
import choosers.CurrentBestChooser;
import algorithms.BaseAlgorithm;
import algorithms.OptimizationStatus;
import algorithms.StopCriterion;
	
public class IntervalGeneticSolver extends BaseAlgorithm implements IntervalSolver {
	private double alpha, betta, gamma;
	public OptimizationStatus myStatus;
	
	public IntervalGeneticSolver(double alpha, double betta, double gamma) {
		assert (alpha >= 0 && alpha <= 1);
		assert (betta >= 0 && betta <= 1);
		assert (gamma >= 0 && gamma <= 1);
		this.alpha = alpha;
		this.betta = betta;
		this.gamma = gamma;
		
		GeneticFitComparator fitComparator = new GeneticFitComparator(this);
		WorkList workList = new SortedWorkList(fitComparator);
		Chooser chooser  = new CurrentBestChooser(workList);
		Splitter splitter = new BiggestSideEquallySplitter();
		
		setLogic(workList, chooser, splitter);
		
		// the algorithm is ready, but
		// area and function are  still not set. 
		// further call of init(Function f, Box area) is expected
	}
	
	public void changeCoefficients(double alpha, double betta, double gamma) {
		assert (alpha >= 0 && alpha <= 1);
		assert (betta >= 0 && betta <= 1);
		assert (gamma >= 0 && gamma <= 1);
		this.alpha = alpha;
		this.betta = betta;
		this.gamma = gamma;
	}
	
	public void changeCoefficients(double[] element) {
		assert (element[0] >= 0 && element[0] <= 1);
		assert (element[1] >= 0 && element[1] <= 1);
		assert (element[2] >= 0 && element[2] <= 1);
		this.alpha = element[0];
		this.betta = element[1];
		this.gamma = element[2];
	}
	
	public double calcFitFunction(Box b) {
		// Simple Fit function:
		// F = a*F.lo + b*wid(F) + c*wid(b)
		
		if ( Double.isInfinite(b.getFunctionValue().wid()) )
			return Double.POSITIVE_INFINITY;
		
		final int dim = b.getDimension();
		double F = 0;
		for (int i = 0; i < dim; i++){
			F += gamma * b.getInterval(i).wid();
		}
		//F /= dim; // normalize, otherwise more dimensions would mean more weight of the box's size.
		
		F += betta * (getLowBoundMaxValue() - b.getFunctionValue().lo());
		
		//F -= alpha * b.getFunctionValue().lo();
		assert(!Double.isNaN(F));
		//System.out.println(alpha + " " + betta + " " + gamma + "\n");
		return F; 		
	}
	
	protected OptimizationStatus iterate() {
		Box[] newBoxes;

		if (logging)
			System.out.println("WorkList size = " + workList.size());
		workBox = workList.extractNext();
		
		if (logging)
			System.out.println(workBox + " => ");
		if (workBox == null) {
			assert (workList.size() == 0);
			myStatus = EMPTY_WORKLIST;
			return EMPTY_WORKLIST;
		}
		
		newBoxes = splitter.splitIt(workBox);
		assert(newBoxes.length > 1);
		
		calculateIntervalExtensions(newBoxes);
		if (logging)
			for (Box b : newBoxes)
				System.out.println("  => " + b);
	
		workList.add(newBoxes);

		boolean shouldTerminate = processInfinityInExtensions(newBoxes);
		
		//additional "if" because in isDone() function we
		//return STOP_CRITERION_SATISFIED in the case, describing below
		
		if (stopCriterion.getIteration() + 1 > stopCriterion.getMaxIterations()){
			myStatus = RUNNING;
			return STOP_CRITERION_SATISFIED;
		}
		if ( shouldTerminate || isDone(workBox) ) {
			myStatus = STOP_CRITERION_SATISFIED;
			return STOP_CRITERION_SATISFIED;
		}
		myStatus = RUNNING;
		return RUNNING;
	}	
	
	private boolean processInfinityInExtensions(Box[] newBoxes) {
	    for (int i = 0; i < newBoxes.length; i++) {
	    	if (extensionContainsInfinity( newBoxes[i] ) ) {
	    		boolean terminationRequired = processBoxWithInfinityInFunctionEstimation(newBoxes[i]);
	    		if (terminationRequired)
	    			return true;
	    	}
	    }
	    return false; // Immediate abort of the iteration is not requested
	}
	private boolean processBoxWithInfinityInFunctionEstimation(Box box) {
		System.out.println("Got INFINITY while evaluating target function (" + targetFunction 
				+ ") on the following area:\n\t" + box.toStringArea() 
				+ "\nCurrent policy means stop computation after this.");
		return true; // true means stop iteration on this.
	}
	private boolean extensionContainsInfinity(Box box) {
		RealInterval extension = box.getFunctionValue();
		if (Double.isInfinite(extension.lo()) || Double.isInfinite(extension.hi()))
			return true;
		return false;
	}
}
