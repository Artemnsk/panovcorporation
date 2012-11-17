package solvers;

import Myvector.Myvector;
import Population.Population;
import static algorithms.OptimizationStatus.EMPTY_WORKLIST;
import static algorithms.OptimizationStatus.RUNNING;
import static algorithms.OptimizationStatus.STOP_CRITERION_SATISFIED;
import net.sourceforge.interval.ia_math.RealInterval;
import splitters.BiggestSideEquallySplitter;
import splitters.Splitter;
import worklists.GeneticFitComparator;
import worklists.SortedWorkList;
import worklists.WorkList;
import choosers.Chooser;
import choosers.CurrentBestChooser;
import Population.Population;
import algorithms.BaseAlgorithm;
import algorithms.OptimizationStatus;
import algorithms.StopCriterion;
import core.Box;
import functions.FunctionNEW;

public class IntervalGeneticSolverMain extends BaseAlgorithm implements IntervalSolver {
	public IntervalGeneticSolver IGS;
	private int searchIterate;
	private int goodIterate;
	private Population population;
	
	public IntervalGeneticSolverMain(double alpha, double betta, double gamma){
		assert (alpha >= 0 && alpha <= 1);
		assert (betta >= 0 && betta <= 1);
		assert (gamma >= 0 && gamma <= 1);
		this.searchIterate = 10;
		this.goodIterate = 15;
		this.population = new Population(10);
		this.IGS = new IntervalGeneticSolver(alpha, betta, gamma);
	}
	
	public IntervalGeneticSolverMain(double alpha, double betta, double gamma, 
			int searchIterate, int goodIterate) {
		assert (alpha >= 0 && alpha <= 1);
		assert (betta >= 0 && betta <= 1);
		assert (gamma >= 0 && gamma <= 1);
		this.population = new Population(10);
		this.searchIterate = searchIterate;
		this.goodIterate = goodIterate;
		this.IGS = new IntervalGeneticSolver(alpha, betta, gamma);
	}
	
	public double calcFitFunction(Box b) {
		return IGS.calcFitFunction(b);		
	}
	
	//ALGORITHM
	
	public void solve(){
		OptimizationStatus status;
		int k = 0;
		//if we want to count first function value
		status = iterate(1);
		double prevEstimate = IGS.getOptimumValue().wid();
		double curEstimate = 0;
		double weight = 0;
		double[] element, element1, element2;
		do {
			element1 = population.getBest();
			element2 = population.getRandom();
			element = Myvector.cross(element1, element2);
			IGS.changeCoefficients(element);
			status = iterate(searchIterate);
			curEstimate = IGS.getOptimumValue().wid();
			weight = 1 - curEstimate/prevEstimate;
			prevEstimate = curEstimate;
			element1 = population.getWorst();
			if(population.weights[population.current] < weight){
				population.weights[population.current] = weight;
				population.elements[population.current] = element;
				System.out.println("CHANGE");
			}
			population.weights[population.current] = weight;
		
			//запись или незапись проверенного вектора в популяцию
			//полезная работа алгоритма
			//перезапись полезного вектора в популяцию
			
			element = population.getBest();
			IGS.changeCoefficients(element);
			status = iterate(goodIterate);
			curEstimate = IGS.getOptimumValue().wid();
			weight = 1 - curEstimate/prevEstimate;
			population.weights[population.current] = weight;
			prevEstimate = curEstimate;
			System.out.println("Iterations: " + k + 
					" currBest: " + element[0] + " " + element[1] + " " + element[2]);
			k++;
			
		} while (status == RUNNING);
		//System.out.println("Iterations: " + k);
	}
	
	protected OptimizationStatus iterate(int i_quantity) {
		IGS.stopCriterion.setMaxIterations(i_quantity);
		IGS.stopCriterion.reset();
		IGS.solve();	
		return IGS.myStatus;
	}
	
	public RealInterval getOptimumValue(){
		return IGS.getOptimumValue();
	}
	
	public Box[] getOptimumArea(){
		return IGS.getOptimumArea();
	}
	
	public void setProblem(FunctionNEW f, Box area){
		IGS.setProblem(f, area);
	}
	
	public void setStopCriterion(StopCriterion stopCriterion){
		IGS.setStopCriterion(stopCriterion);
	}
	
	public void setPrecision( double pres ){
		IGS.setPrecision(pres);
	}
	
	public double getPrecision(){
		return IGS.getPrecision();
	}
	
	public double getLowBoundMaxValue(){
		return IGS.getLowBoundMaxValue();
	}
	
	//BASE ALGORITHM
	
}
