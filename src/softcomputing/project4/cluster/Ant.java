package softcomputing.project4.cluster;
import softcomputing.project4.data.DataPoint;

public class Ant {
	int x_pos;
	int y_pos;
	DataPoint payload = null;
	//constructor
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
