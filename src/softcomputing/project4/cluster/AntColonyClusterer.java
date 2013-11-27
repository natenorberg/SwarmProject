package softcomputing.project4.cluster;

import softcomputing.project4.data.DataPoint;

/**
 * Clusters a data set using Ant Colony Optimization
 */
public class AntColonyClusterer implements IClusterer
{
	private DataPoint[][] _grid;
	private Ant[] _colony;
	
	
    //tunable parameters
	//number of iteration
	int _it_num = 0;
	//size of grid
	int _x_size = 0;
	int _y_size = 0;
	//number of ants
	int _ant_num = 0;
	//ant visibility (always odd?)
	int n_patch= 3; 
	//Dissimilarity measures 
	float gamma =0;// small --> many clusters, big--> few poorly related clusters 
	float gamma_1 =0;
	float gamma_2 =0;
	
    
    public void clusterDataSet(DataPoint[] dataSet)
    {
    	_grid = new DataPoint[_x_size][_y_size];
    	_colony = new Ant[_ant_num];
    
    	//place each data vector from the data set randomly on the grid
		populateGrid(dataSet);
		
		//place ant_number of ants on the grid at randomly selected sites
		for(int i = 0 ; i < _colony.length; i ++ ){
    		_colony[i] = new Ant(((int) (Math.random() * (_x_size+1)))-1,((int) (Math.random() * (_y_size+1)))-1);
    	}
		double density =0;
		double p_pickup =0;
		double p_drop;
		int new_x;
		int new_y;
		//consider other stoping conditions?
		for(int i = 0; i < _it_num; i ++){
			for(int j = 0; j < _colony.length; j ++){
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
					if(Math.random()<= p_drop){
						_grid[_colony[j].getX()][_colony[j].getY()] = _colony[j].putDown();
					}
				}
				//move to random neighboring site
				do{ //make sure new x is inbounds
				new_x = _colony[j].getX() + (((int)(Math.random()*3))-1);
				}while(0<=new_x&&new_x<_x_size);
				do{ //make sure new y is inbounds 
				new_y = _colony[j].getY() + (((int)(Math.random()*3))-1);
				}while(0<=new_y&&new_y<_y_size);
				_colony[j].setX(new_x);
				_colony[j].setY(new_y);
			}
		}
    }
    /**
     * Place the data vectors randomly on the grid
     * @param dataSet
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
    	for(int i = x_start; i < n_patch; i ++){
    		if(0<=i && i < _x_size){//inbounds
	    		for(int j = y_start; j < n_patch; j ++){
	    			if(0<=j && j < _y_size){//inbounds
	    				//if vector present
	    				if(_grid[i][j]!=null){
	    					//add to density summation
	    					score += (1-(euclideanDistance(a_point.getData(), _grid[i][j].getData())/gamma));
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
    // find euclideanDistance
    private double euclideanDistance(double[] a, double[] b){
    	double output = 0;
    	for(int i =0; i < a.length; i ++){
    		output += Math.pow((a[i]-b[i]),2);
    	}
    	output = Math.sqrt(output);
    	return output;
    }

}
