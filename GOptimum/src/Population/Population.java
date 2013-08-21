package Population;

import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/*
 * represents a population.
 * each record from the population is a pair: the object itself and its fit value
 */
public class Population {
	private TreeSet<Coefficients> population = new TreeSet<>();
	private static Random rnd = new Random();
	private Coefficients current;
	
	public double getCurrentCoeff(int i){
		return current.getElement(i);
	}
	
	static { // for debug -- so we could set fixed seed 
		long seed = 0;
		seed = System.currentTimeMillis(); 
		rnd.setSeed(seed);
	}
	
	public void setCurrent(Coefficients coeff){
		this.current = coeff;
	}
	
	public void setCurrent(double[] coeff){
		this.current = new Coefficients(coeff, (double)1);
	}
	
	public Population(int quantity) {
		// generateInitialRandomPopulation
		for(int i = 0; i < quantity; i++)
			population.add(new Coefficients()); // default constructor for Coefficients initialize it to a random value
		current = population.first();
	}
	public Coefficients getBestObject() {
		return population.first();
	}
	
	public double getBestFitness() {
		return getBestObject().getFitness();
	}
	
	public double[] getBest() {
		return getBestObject().coefficients;
	}
	
	private Coefficients getWorstObject() {
		return population.last();
	}
	public double[] getWorst() {
		return getWorstObject().coefficients;
	}
	
	public double[] getRandom() {
		int i = rnd.nextInt(population.size());
		Iterator<Coefficients> it = population.iterator();
		while (i-- > 0)
			it.next();
		return it.next().coefficients;
	}

	public boolean isBetterThanWorst(double fitness) {
		return getWorstObject().fitness < fitness;
	}

	public void replaceWorstWithThis(double[] element, double fitness) {
		Coefficients newElement = new Coefficients(element, fitness);
		boolean removed = population.remove(getWorstObject());
		assert (removed);
		population.add(newElement);
	}

	public double[] crossBestWithRnd() {
		double[] best = getBest();
		double[] rand = getRandom();
		double[] cros = Coefficients.cross(best, rand);
		return cros;
	}
	public void updateFitness(double[] coefficients, double fitness) {
		boolean removed = false;
		for (Coefficients c : population) {
			if (c.coefficients == coefficients) { // not values but reference comparison!
				removed = population.remove(c);
				assert (removed);
				Coefficients newCoefficients = new Coefficients(coefficients, fitness);
				population.add(newCoefficients);
				break;
			}
		}
		assert (removed);
	}
}

/*
 * Encapsulates the object and its Fit-function value
 */
class Coefficients implements Comparable {
	final double[] coefficients;
	final double fitness;
	final int size = 3;
	
	public double getElement(int i){
		return coefficients[i];
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public Coefficients() {
		this.coefficients = new double[size];
		for (int i = 0; i < size; i++)
			coefficients[i] = Math.random();
		normalize();		
		fitness = 1;
	}
	public Coefficients(double[] coefficients, double fitness) {
		assert coefficients.length == size;
		this.coefficients = coefficients;
		this.fitness = fitness;
	}
	
	public static double[] cross(double[] d1, double[] d2) {
		Coefficients tmp = new Coefficients(d1, 0);
		tmp.cross(d2);
		return tmp.coefficients;
	}

	private void normalize() {
		double sum = 0;
		for(int i = 0; i < size; i++){
			sum += coefficients[i];
		}
		for(int i = 0; i < size; i++){
			coefficients[i] /= sum;
		}
	}
	private void cross(double[] c) {
		for(int i = 0; i < size; i++){
			coefficients[i] = (coefficients[i] + c[i])/2;
		}
		normalize();
	}
	@Override
	public int compareTo(Object obj) {
		Coefficients that = (Coefficients)obj;
		if (that == null) 
			return -1;
		return Double.compare(that.fitness, this.fitness); // descending order
	}
}