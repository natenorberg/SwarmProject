package softcomputing.project4.cluster.PSOClusterer;

import java.util.LinkedList;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.ConsoleWriterService;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

/**
 * Clusters a data set using particle swarm optimization
 */
public class PSOClusterer extends Clusterer
{
    private final ConsoleWriterService _output;
    Particle[] swarm;
	TunableParameterService _myParameterService;
	int _it_num; //number of iterations
	int _n_cluster;
	int _n_particle;
	int _dpVectorLength;

    private final boolean _printIntraClusterDistance;
    private final boolean _printInterClusterDistance;
    private final boolean _printDaviesBouldinIndex;
	
    public PSOClusterer(){
    	this(TunableParameterService.getInstance(), DataSetInformationService.getInstance(), ConsoleWriterService.getInstance());
    }
    public PSOClusterer(TunableParameterService parameterService,
                        DataSetInformationService dataSetInformationService,
                        ConsoleWriterService output){
    	_it_num = parameterService.getNumberOfIterations();//number of iterations
    	_n_cluster = dataSetInformationService.getNumOutputs();//number of clusters
    	_n_particle = parameterService.getParticleNum();// number of particles
    	_dpVectorLength  = dataSetInformationService.getNumInputs();
    	_myParameterService = parameterService;
    	swarm = new Particle[_n_particle];
    	
        _printIntraClusterDistance = parameterService.getPrintIntraClusterDistance();
        _printInterClusterDistance = parameterService.getPrintInterClusterDistance();
        _printDaviesBouldinIndex = parameterService.getPrintDaviesBouldinIndex();

        _output = output;
    }

	public void clusterDataSet(DataPoint[] dataSet)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    	//Algorithm
		
    	//initialize each particle to contain N_c randomly selected cluster centroids
		for(int i = 0; i < swarm.length; i ++){
    		swarm[i] = new Particle(_n_cluster,_dpVectorLength,_myParameterService, dataSet);
    	}
    	//for each iteration
		double[][] globalBest = new double[_n_cluster][_dpVectorLength];
		double bestFitness = -1;
		double currentFitness = -1;
		boolean changed = false;
		for(int i = 0; i < _it_num; i ++){
			for(int j = 0 ; j < swarm.length; j ++){//for each particle 
				
				//assign clusters
				swarm[j].clusterDataPoints(dataSet);
				//measure fitness of clusters
				currentFitness = swarm[j].calculateFitness(dataSet);
				//_output.writeLine("fitness: "+ currentFitness);
				//update global best fitness
				if(currentFitness< bestFitness || bestFitness ==-1 ){
					bestFitness = currentFitness;
					_output.writeLine("new global best: "+bestFitness);
					changed = true;
					globalBest = swarm[j].getCentroidsCopy();
				}
				//update the cluster centroids
				swarm[j].updateCentroids(globalBest, changed,i);
			}		
			extractClusters(globalBest, dataSet);
			if(changed){
		        String outputString = String.format("Run %d: ", i);
		
		        if (_printIntraClusterDistance)
		            outputString = outputString.concat(String.format("Average distance in clusters: %f, ", this.evaluateCluster()));
		        if (_printInterClusterDistance)
		            outputString = outputString.concat(String.format("Average distance between clusters: %f, ", this.averageDistanceBetweenCenters()));
		        if (_printDaviesBouldinIndex)
		            outputString = outputString.concat(String.format("Davies-Bouldin index: %f, ", this.daviesBouldinIndex()));
		
		        _output.writeLine(outputString);
		        changed = false;
			}
		}
    }
	public void extractClusters(double[][] centroids, DataPoint[] dataSet){
		_clusters = new LinkedList<Cluster>();
		int[] clusterAssignment = new int[dataSet.length];
		double distance;
		double minDist;
		//assign points to centroids
		for(int i = 0 ; i < dataSet.length; i ++){
			minDist = -1;
			for(int j = 0; j < centroids.length; j++){
				distance = euclideanDistance(dataSet[i].getData(), centroids[j]);
				if(distance < minDist|| minDist ==-1){
					minDist= distance;
					clusterAssignment[i] = j;
				}
			}
		}
		//assign place points in clusters.
		for(int i = 0 ; i < centroids.length; i ++){
			Cluster newCluster = new Cluster(centroids[i]); //assign centroid values
			for(int j = 0 ; j < dataSet.length; j++){
				if(i== clusterAssignment[j]){
					newCluster.addPoint(dataSet[j]);
				}
			}
			_clusters.add(newCluster);
		}
	}
}
