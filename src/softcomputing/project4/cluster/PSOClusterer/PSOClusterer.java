package softcomputing.project4.cluster.PSOClusterer;

import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

/**
 * Clusters a data set using particle swarm optimization
 */
public class PSOClusterer extends Clusterer
{
	Particle[] swarm;
	int _it_num; //number of iterations
	int _n_cluster;
	int _n_particle;
	int _dpVectorLength;
	double _intertia;
	
    public PSOClusterer(){
    	this(TunableParameterService.getInstance(), DataSetInformationService.getInstance());
    }
    public PSOClusterer(TunableParameterService parameterService, DataSetInformationService dataSetInformationService){
    	_it_num = parameterService.getNumberOfIterations();//number of iterations
    	_n_cluster = dataSetInformationService.getNumOutputs();//number of clusters
    	//_n_particle = parameterService.getParticleNum();// number of particles
    	_dpVectorLength  = dataSetInformationService.getNumInputs();
    	//_intertia = parameterService.getParticleNum(); //intertia of particles
    	swarm = new Particle[_n_particle];
    	
    }

	public void clusterDataSet(DataPoint[] dataSet)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    	//Algorithm
		
    	//initialize each particle to contain N_c randomly selected cluster centroids
		for(int i = 0; i < swarm.length; i ++){
    		swarm[i] = new Particle(_n_cluster,_dpVectorLength,_intertia, dataSet);
    	}
    	//for each iteration
		double[][] globalBest = new double[_n_cluster][_dpVectorLength];
		double bestFitness = -1;
		double currentFitness = -1;
		for(int i = 0; i < _it_num; i ++){
			for(int j = 0 ; j < swarm.length; j ++){//for each particle 
				//assign clusters
				swarm[j].clusterDataPoints(dataSet);
				//measure fitness of clusters
				currentFitness = swarm[j].calculateFitness(dataSet);
				
				//update global best fitness
				if(currentFitness< bestFitness || bestFitness ==-1 ){
					bestFitness = currentFitness;
					globalBest = swarm[j].getCentroidsCopy();
				}
				//update the cluster centroids
				swarm[j].updateCentroids(globalBest);
			}
		}
    }
}
