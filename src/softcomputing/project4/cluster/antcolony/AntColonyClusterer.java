package softcomputing.project4.cluster.antcolony;

import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;
/**
 * Clusters a data set using Ant Colony Optimization
 */
public class AntColonyClusterer extends Clusterer
{
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
	//Dissimilarity measures 
	float gamma;// small --> many clusters, big--> few poorly related clusters 
	float gamma_1;
	float gamma_2;
	
    public AntColonyClusterer(){
    	this(TunableParameterService.getInstance(), DataSetInformationService.getInstance());
    }
    public AntColonyClusterer(TunableParameterService parameterService, DataSetInformationService dataSetInformationService){
    	
    	//TODO: possibly dynamically determine some of the variables based on the dataset? grid size / ant number?
    	
        //tunable parameters
    	//number of iteration
    	_it_num = parameterService.getNumberOfIterations();
    	//size of grid
    	_x_size = parameterService.getXSize();
    	_y_size = parameterService.getYSize();
    	//number of ants
    	_ant_num = parameterService.getAntNum();
    	//ant visibility (always odd?)
    	n_patch= parameterService.getAntVisibility(); 
    	//Dissimilarity measures 
    	gamma =parameterService.getGamma();// small --> many clusters, big--> few poorly related clusters 
    	gamma_1 =parameterService.getGamma1();
    	gamma_2 =parameterService.getGamma2();
    	

    	_grid = new DataPoint[_x_size][_y_size];
    	_colony = new Ant[_ant_num];
    	
    }

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
		printMap();
		for(int i = 0; i < _it_num; i ++){
			for(int j = 0; j < _colony.length; j ++){
				//System.out.println("colony member: "+ j);
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
			
		}
		printMap();
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
    // find the local density of simular data points
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
    	score = score/(n_patch*n_patch);
    	
    	if(score < 0){
    		score = 0;
    	}
    	return score;
    }
    private void printMap(){
    	for(int i =0; i < _grid.length; i ++){
    		for(int j = 0 ; j < _grid[i].length; j++){
    			if(_grid[i][j] != null){
    				System.out.print("# ");
    			}
    			else{
    				System.out.print("  ");
    			}
    		}
    		System.out.println("|");
    	}
    	System.out.println("done");
    }

}
