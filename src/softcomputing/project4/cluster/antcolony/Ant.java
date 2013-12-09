package softcomputing.project4.cluster.antcolony;
import softcomputing.project4.data.DataPoint;
/**
 * Represents the ants in the ant colony clustering algorithm
 */
public class Ant {
	private int x_pos;
	private int y_pos;
	private DataPoint payload = null;
	/**
	 * Constructor, creates ant
	 * @param x_start - ant x starting position on grid
	 * @param y_start - ant y starting position on grid
	 */
	public Ant(int x_start, int y_start){
		x_pos = x_start;
		y_pos = y_start;
	}
	/**
	 * pick up a data point
	 * @param load - point picked up
	 */
	public void pickup(DataPoint load){
		payload = load;
	}
	/**
	 * put down data point
	 * @return - data point carried
	 */
	public DataPoint putDown(){
		DataPoint temp = payload;
		payload = null;
		return temp;
		
	}
	/*
	 * get and set functions
	 */
	public DataPoint getPayload(){
		return payload;
	}
	public int getX(){
		return x_pos;
	}
	public int getY(){
		return y_pos;
	}
	public void setX(int input){
		x_pos = input;
	}
	public void setY(int input){
		y_pos = input;
	}
}
