package softcomputing.project4.cluster.PSOClusterer;

import java.util.Arrays;

import softcomputing.project4.data.DataPoint;

public class Particle {
	double _velocity[][];
	double _intertia;
	double _pbest_fitness;
	
	double _phi_pbest; //used in velocity update, adjust range of possible change for personal best
	double _phi_gbest; //used in velocity update, adjust range of possible change for global best
	

	double[][] _centroids; 
	double[][] _personal_best; //best centroid values yet for this particle
	
	
	int[] _data_assignments; // keeps track of which cluster/centroid the data points belong to.
	
	/**
	 * Initiates the particle
	 * @param NumberOfCentroids
	 * @param centroidLength
	 * @param dataSet
	 */
	public Particle(int NumberOfCentroids,int centroidLength,double intertia, DataPoint[] dataSet){
		_centroids = new double[NumberOfCentroids][centroidLength];
		_data_assignments = new int[dataSet.length];
		_intertia = intertia;
		
		//initialize centroids as random members of the population. 
		for(int i = 0 ; i < _centroids.length; i ++){
			_centroids[i] = dataSet[(int)(Math.random()*dataSet.length)].getData();
		}
	}
	/**
	 * Clusters the data set based on the particles centroids
	 * @param dataSet
	 */
	public void clusterDataPoints(DataPoint[] dataSet){
		int closestCluster; 
		double closestClusterDistance;
		double clusterDistance;
		for(int i = 0 ; i < dataSet.length; i ++){ // for each datapoint;
			closestCluster = -1;
			closestClusterDistance = -1;
			for(int j = 0 ; j < _centroids.length; j++){ //find the closes cluster
				
				clusterDistance = euclideanDistance(dataSet[i].getData(),_centroids[j]);
				//if distance to this cluster is less than currently assigned cluster -->reassign
				if(clusterDistance < closestClusterDistance|| closestClusterDistance == -1){
					closestCluster = j;
					closestClusterDistance = clusterDistance;
				}
				_data_assignments[i] = closestCluster;
			}
			
		}
	}
	/**
	 * calculates the fitness of the particle based on its last clustering 
	 * @param dataSet
	 * @return
	 */
	public double calculateFitness(DataPoint[] dataSet){
		double fitness = 0;
		double clusterfitness;
		int members;
		for(int i = 0 ; i < _centroids.length; i ++){// for each centroid
			members = 0;
			clusterfitness = 0;
			for(int j = 0; j < _data_assignments.length; j++){ //for each data point
				if(_data_assignments[j] == i){ //if the data point belong to that centroid
					members++;
					clusterfitness += euclideanDistance(_centroids[i],dataSet[j].getData());
				}
			}
			clusterfitness = clusterfitness/members;
			fitness += clusterfitness;
		}
		fitness = fitness/_centroids.length;
		
		//update local best if this instance is better than previous ones
		if(fitness < _pbest_fitness||_pbest_fitness == -1){
			_pbest_fitness = fitness;
			for(int i = 0; i < _personal_best.length; i ++){
				_personal_best[i] = Arrays.copyOf(_centroids[i],_centroids[i].length);
			}
		}
		
		return fitness;
	}
	/**
	 * updates the centroids 
	 * @param global_best
	 */
	public void updateCentroids(double[][] global_best){
		//state update
		for(int i = 0 ; i < _centroids.length; i ++){
			for(int j = 0 ; j < _centroids[i].length; j ++){
				_centroids[i][j] +=  _velocity[i][j]; 
			}
		}
		//velocity update
		for(int i = 0 ; i < _velocity.length; i ++){
			for(int j = 0 ; j < _velocity[i].length; j++){
				_velocity[i][j]= _velocity[i][j]*_intertia + (Math.random()*_phi_pbest)*(_personal_best[i][j]-_centroids[i][j])
						+(Math.random()*_phi_gbest)*(global_best[i][j]-_centroids[i][j]);
			}
		}
	}
	/**
	 * Returns the values of the centroids
	 * @return
	 */
	public double[][] getCentroidsCopy(){
		double[][] tempCopy = new double[_centroids.length][_centroids[0].length];
		for(int i =0 ; i < tempCopy.length; i ++){
			tempCopy[i] = Arrays.copyOf(_centroids[i],_centroids[i].length);
		}
		return tempCopy;
	}
	
	
	
	public double euclideanDistance(double[] a, double[] b){
		//TODO: use  clusterer class?
		//find distance
	}

}
