package Myvector;

public class Myvector {
	public static double[] normalize(double[] vector){
		double sum = 0;
		for(int i = 0; i < 3; i++){
			sum += vector[i];
		}
		for(int i = 0; i < 3; i++){
			vector[i] /= sum;
		}
		return vector;
	}
	
	public static double[] cross(double[] vector1, double[] vector2){
		double[] element;
		element = new double[vector1.length];
		for(int i = 0; i < vector1.length; i++){
			element[i] = (vector1[i] + vector2[i])/2;
		}
		return normalize(element);
	}
}
