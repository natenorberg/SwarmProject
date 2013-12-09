package softcomputing.project4.cluster.antcolony;

import softcomputing.project4.data.DataPoint;
/**
 * Represent the datapoints so that they may be clustered
 * by the hierarchical agglomerative clustering algorithm 
 *
 */
public class AntGridPoint {
	int _x_pos;
	int _y_pos;
	
	DataPoint _datapoint;
	
	/**
	 * Constructor, creates data grid point
	 * @param newPoint - corresponding datapoint
	 * @param x - x coordinates of point
	 * @param y - y coordinates of point
	 */
	public AntGridPoint(DataPoint newPoint, int x, int y){
		_datapoint = newPoint;
		_x_pos = x;
		_y_pos = y;
	}
	
	public int getXpos(){
		return _x_pos;
	}
	public int getYpos(){
		return _y_pos;
	}
	public DataPoint getDataPoint(){
		return _datapoint;
	}
}
