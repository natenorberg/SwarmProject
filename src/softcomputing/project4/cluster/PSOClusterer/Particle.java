package softcomputing.project4.cluster.PSOClusterer;

import java.util.Arrays;

import softcomputing.project4.services.TunableParameterService;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;

public class Particle {
	double _velocity[][];
	double _intertia;
	double _pbest_fitness;
	
	double _phi_pbest = .2; //used in velocity update, adjust range of possible change for personal best
	double _phi_gbest = .2; //used in velocity update, adjust range of possible change for global best
	double _delta = 0; //used to define vMax between 0 and 1.
	double _inDev = 3;
	double _gamma = 1;
	double _vMax;
	int _iterations;

	double[][] _centroids; 
	double[][] _personal_best; //best centroid values yet for this particle
	
	
	int[] _data_assignments; // keeps track of which cluster/centroid the data points belong to.
	
	/**
	 * Initiates the particle
	 * @param NumberOfCentroids
	 * @param centroidLength
	 * @param dataSet
	 */
	public Particle(int NumberOfCentroids,int centroidLength,TunableParameterService parameterService, DataPoint[] dataSet){
		_centroids = new double[NumberOfCentroids][centroidLength];
		_velocity = new double[NumberOfCentroids][centroidLength];
		_personal_best =new double[NumberOfCentroids][centroidLength];
		_data_assignments = new int[dataSet.length];
		
		//get tunable parameters
		_intertia = parameterService.getintertia(); //intertia of particles
		_phi_pbest = parameterService.getPhiPbest();
		_phi_gbest =parameterService.getPhiGbest();
		_delta = parameterService.getDelta();
		_iterations = parameterService.getNumberOfIterations();
		_vMax = _delta;  //all weights have been normalized don't need to multiply by difference between high and low
		
		//initialize centroids with random numbers
		for(int i = 0 ; i < _centroids.length; i ++){
			for(int j = 0; j < _centroids[i].length; j ++){
				_centroids[i][j] = (Math.random());
			}
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
				
				clusterDistance = Clusterer.euclideanDistance(dataSet[i].getData(), _centroids[j]);
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
				if(_data_assignments[j] == i){ //if the data point belongs to that centroid
					members++; //ad to member count
					//add member distance to cluster fitness
					clusterfitness += Clusterer.euclideanDistance(_centroids[i],dataSet[j].getData());
				}
			}
			clusterfitness = clusterfitness/members;
			if(members ==0){
				clusterfitness =1;// penalty for having empty clusters
			}
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
	 * Update the centroids 
	 * @param global_best
	 */
	public void updateCentroids(double[][] global_best, int noChange, int iter_count){
		//decrease vMax id no change after 10 iterations
		if(noChange > 10){
			_vMax = _gamma *_vMax; //range for all dimensions is one since they are normalized
			
		}
		// anneal gamma
		_gamma = ((1-(0.01))*(((double)(_iterations - iter_count))/_iterations))+(0.01);
		
		//update intertia
		_intertia = randomGaussian(.72,1.2 ); //.72,1.2
		
		//velocity update
		double vPrime =0; //potential new velocity
		for(int i = 0 ; i < _velocity.length; i ++){
			for(int j = 0 ; j < _velocity[i].length; j++){
				//determine vPrime
				vPrime = _velocity[i][j]*_intertia + (Math.random()*_phi_pbest)*(_personal_best[i][j]-_centroids[i][j])
							+(Math.random()*_phi_gbest)*(global_best[i][j]-_centroids[i][j]);
				//check if vPrime within velocity clamp
				if(vPrime < _vMax){
					_velocity[i][j] = vPrime;
				}
				else{
					_velocity[i][j] = _vMax;
				}
			}
		}
		//state update of centroids
		for(int i = 0 ; i < _centroids.length; i ++){
			for(int j = 0 ; j < _centroids[i].length; j ++){
				_centroids[i][j] += _velocity[i][j]; 
			}
		}
	}
	
	/**
	 * Generates a random number from a gaussian distribution
	 * @param mean - mean value for distribution
	 * @param stdeviation - standard deviation for the distribution
	 * @return - a random normally distributed number
	 */
	public static double randomGaussian(double mean, double stdeviation){
		double out = 0;
		double r_one = Math.random(); //random number one
		double r_two = Math.random(); //random number two
		if(r_one == 0){
			out = 1;
		}
		if(r_two == 0){
			out = 1;
		}
		//Box-Muller Transformation
		out = Math.sqrt(-2*Math.log(r_one))*(Math.cos(2*Math.PI*r_two));
		out = out * stdeviation +mean;
		//System.out.println( out);
		return out;
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

}
