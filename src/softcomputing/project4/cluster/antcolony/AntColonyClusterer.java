package softcomputing.project4.cluster.antcolony;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.ConsoleWriterService;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;
/**
 * Clusters a data set using Ant Colony Optimization
 */
public class AntColonyClusterer extends Clusterer
{
    private final ConsoleWriterService _output;
    private DataPoint[][] _grid;
	private Ant[] _colony;
	
	
	
    //tunable parameters
	//number of iterations
	int _it_num;
	//size of grid
	int _x_size;
	int _y_size;
	//number of ants
	int _ant_num;
	//ant visibility (always odd?)
	int n_patch; 
	double _visibility;
	int _finalVisibility;
	//Dissimilarity measures 
	float gamma;// small --> many clusters, big--> few poorly related clusters 
	float gamma_1;
	float gamma_2;

    private int _numClusters;
    private final boolean _printIntraClusterDistance;
    private final boolean _printInterClusterDistance;
    private final boolean _printDaviesBouldinIndex;
	
    /**
     * Public constructor
     */
    public AntColonyClusterer(){
    	this(TunableParameterService.getInstance(), DataSetInformationService.getInstance(), ConsoleWriterService.getInstance());
    }
    /**
     * Constructor that takes services through dependency injection
     * @param parameterService
     * @param dataSetInformationService
     * @param output
     */
    public AntColonyClusterer(TunableParameterService parameterService, DataSetInformationService dataSetInformationService,
                              ConsoleWriterService output){
    	
        //read in tunable parameters
    	//number of iteration
    	_it_num = parameterService.getNumberOfIterations();
    	//size of grid
    	_x_size = parameterService.getXSize();
    	_y_size = parameterService.getYSize();
    	//number of ants
    	_ant_num = parameterService.getAntNum();
    	//ant visibility (always odd?)
    	_finalVisibility= parameterService.getAntVisibility()+1; 
    	//Dissimilarity measures 
    	gamma =parameterService.getGamma();// small --> many clusters, big--> few poorly related clusters 
    	gamma_1 =parameterService.getGamma1();
    	gamma_2 =parameterService.getGamma2();
    	//number of clusters
    	_numClusters = dataSetInformationService.getNumOutputs();
    	
    	//create the grid and colony
    	_grid = new DataPoint[_x_size][_y_size];
    	_colony = new Ant[_ant_num];
    	
    	//read in print parameters
        _printIntraClusterDistance = parameterService.getPrintIntraClusterDistance();
        _printInterClusterDistance = parameterService.getPrintInterClusterDistance();
        _printDaviesBouldinIndex = parameterService.getPrintDaviesBouldinIndex();
    	_output = output;
    }
    @Override
    /**
     * performs the clustering 
     */
    public void clusterDataSet(DataPoint[] dataSet){
    	//place each data vector from the data set randomly on the grid
		populateGrid(dataSet);
		//place ant_number of ants on the grid at randomly selected sites
		for(int i = 0 ; i < _colony.length; i ++ ){
    		_colony[i] = new Ant(((int) (Math.random() * (_x_size))),((int) (Math.random() * (_y_size))));
    	}

		
		double density =0;
		double p_pickup =0;
		double p_drop;
		int new_x;
		int new_y;
		//consider other stopping conditions?
		for(int i = 0; i < _it_num; i ++){
			//linearly increase the ants field of perception
			_visibility= ((double)(1- _finalVisibility)*((double)(_it_num-i)/_it_num))+_finalVisibility;
			n_patch = ((int)_visibility * 2)+1;
			for(int j = 0; j < _colony.length; j ++){
				//_output.writeLine("colony member: "+ j);
				//if not burdened and site is occupied
				if(_colony[j].getPayload()==null&&_grid[_colony[j].getX()][_colony[j].getY()]!=null){
					//find local density of vectors
					density = localDensity(_grid[_colony[j].getX()][_colony[j].getY()], _colony[j].getX(), _colony[j].getY());
					p_pickup = (gamma_1/(gamma_1+density));
					if(Math.random() <= p_pickup){
						//pick up the datapoint
						_colony[j].pickup(_grid[_colony[j].getX()][_colony[j].getY()]);
						_grid[_colony[j].getX()][_colony[j].getY()] = null;
					}
				}
				//ant is burdened and site is empty 
				else if(_colony[j].getPayload()!=null&&_grid[_colony[j].getX()][_colony[j].getY()]==null){
					density = localDensity(_colony[j].getPayload(), _colony[j].getX(), _colony[j].getY());
					
					if(density < gamma_2){
						
						p_drop = 2*density;
					}
					else{
						p_drop = 1;
					}
					
					//drop data point
					if(Math.random()<= p_drop){//||i==(_it_num-1)){
						_grid[_colony[j].getX()][_colony[j].getY()] = _colony[j].putDown();
					}
				}
				
				//move to random neighboring site
				do{ //make sure new x is inbounds
				new_x = _colony[j].getX() + (((int)(Math.random()*3))-1);
				}while(0>new_x||new_x>=_x_size);
				do{ //make sure new y is inbounds 
				new_y = _colony[j].getY() + (((int)(Math.random()*3))-1);
				}while(0>new_y||new_y>=_y_size);
				_colony[j].setX(new_x);
				_colony[j].setY(new_y);
				
			}
			extractClusters();
	        // Build a format string based on print parameters
	        String outputString = String.format("ACO, iteration %d: ", i);

	        if (_printIntraClusterDistance)
	            outputString = outputString.concat(String.format("Average distance in clusters: %f, ", this.evaluateCluster()));
	        if (_printInterClusterDistance)
	            outputString = outputString.concat(String.format("Average distance between clusters: %f, ", this.averageDistanceBetweenCenters()));
	        if (_printDaviesBouldinIndex)
	            outputString = outputString.concat(String.format("Davies-Bouldin index: %f, ", this.daviesBouldinIndex()));

	        _output.writeLine(outputString);
			
			
		}
		
    }
    /**
     * Place the data vectors randomly on the grid
     * @param newPoints
     */
    private void populateGrid(DataPoint[] newPoints){
    	int rand_x;
    	int rand_y;
    	for(int i = 0 ; i < newPoints.length; i ++ ){
    		do{
    		rand_x = ((int) (Math.random() * _x_size));
    		rand_y = ((int) (Math.random() * _y_size));
    		}while(_grid[rand_x][rand_y]!=null);
    		_grid[rand_x][rand_y] = newPoints[i];
    	}
    }
    /**
     * Find the local density of similar points surrounding the ant
     * @param a_point -point being examined by ant
     * @param x_loc -x location of point
     * @param y_loc -y location of point
     * @return -measure of similarity
     */
    private double localDensity(DataPoint a_point, int x_loc,int y_loc){
    	double score = 0; 
    	int x_start = x_loc - ((n_patch-1)/2);
    	int y_start = y_loc - ((n_patch-1)/2);
    	for(int i = x_start; i < x_start+n_patch; i ++){
    		if(0<=i && i < _x_size){//inbounds
	    		for(int j = y_start; j < y_start+n_patch; j ++){
	    			if(0<=j && j < _y_size){//inbounds
	    				//if vector present
	    				if(_grid[i][j]!=null){
	    					//add to density summation
	    					score += (1-(Clusterer.euclideanDistance(a_point.getData(), _grid[i][j].getData())/gamma));
	    				}
	    			}
	    		}
    		}
    	}
    	//divide by size of visual field
    	score = score/(n_patch*n_patch);
    	//make sure it is greater than 0
    	if(score < 0){
    		score = 0;
    	}
    	return score;
    }
    /**
     * Uses a hierarchical agglomerative clustering algorithm to place points
     * which are closest together on the 2-d particle map in the same clusters
     * RESULTS are stored in the "_clusters" variable
     */
    private void extractClusters(){
    	List<AntCluster> currentClusters = new ArrayList<AntCluster>();
    	_clusters = new LinkedList<Cluster>();
    	
    	//create a cluster from every point on the graph
    	for(int i = 0 ; i < _grid.length; i++){
    		for(int j = 0; j < _grid[i].length; j ++){
    			if(_grid[i][j]!=null){
    				currentClusters.add(new AntCluster(new AntGridPoint(_grid[i][j], i , j)));
    			}
    		}
    	}
    	int closestClusterIndex = 0 ;
    	double closestClusterDistance;
    	double  ccDistance; //min cluster distance for current cluster
    	_output.writeLine("Removing ACO clusters, this takes a while.");
    	while(currentClusters.size() > _numClusters){
    		//find the closets cluster for each cluster.
    		closestClusterDistance =-1;//reinitialize for each cluster reduction
    		
    		for(int i =0; i < currentClusters.size(); i ++){
    			ccDistance = currentClusters.get(i).findClosestCluster(currentClusters, i);
    			if(closestClusterDistance < ccDistance || closestClusterDistance==-1){
    				closestClusterDistance = ccDistance;
    				closestClusterIndex = i;
    			}
    		}
    		//combine the clusters with the closest points
    		currentClusters.get(closestClusterIndex).joinCluster(
    				currentClusters.remove(currentClusters.get(closestClusterIndex).getClosestClusterIndex()));
    	}
    	_output.writeLine("ACO clusters formed!");
    	//move antColony clusters into cluster type so they can be analyzed
    	for(int i = 0 ; i < _numClusters; i ++){
    		Cluster newCluster = new Cluster();
    		for(int j = 0; j < currentClusters.get(i).getMembers().size(); j ++){
    			newCluster.addPoint(currentClusters.get(i).getMembers().get(j).getDataPoint());
    		}
    		newCluster.recalculateCenter();
    		_clusters.add(newCluster);
    	}
    }
    /**
     * prints a text output of the current location of datapoints on the map
     */
    private void printMap(){
    	for(int i =0; i < _grid.length; i ++){
    		for(int j = 0 ; j < _grid[i].length; j++){
    			if(_grid[i][j] != null){
    				_output.writeString("# ");
    			}
    			else{
    				_output.writeString("  ");
    			}
    		}
    		_output.writeLine("|");
    	}
    	_output.writeLine("done");
    }

}
