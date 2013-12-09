package softcomputing.project4.cluster.antcolony;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure to hold clusters of Ant grid points
 * as the are clustered and removed from the grid by the 
 * hierarchical agglomerative clustering algorithm 
 *
 */

public class AntCluster {
	private AntCluster _closestCluster;
	private int _closestClusterIndex;
	private double _closestClusterDistance;
	
	List<AntGridPoint> _clusterMembers;
	
	/**
	 * Constructor, creates cluster with the one input member
	 * @param firstMember
	 */
	public AntCluster(AntGridPoint firstMember){
		_clusterMembers = new ArrayList<AntGridPoint>();
		_clusterMembers.add(firstMember);
	}
	/**
	 * Combines the input cluster with this cluster
	 * @param input
	 */
	public void joinCluster(AntCluster input){
		_clusterMembers.addAll(input.getMembers());
	}
	/**
	 * get the members of the the cluster
	 * @return -the list of AntGridPoint members of this cluster
	 */
	public List<AntGridPoint> getMembers(){
		return _clusterMembers;
	}
	/**
	 * Finds the other cluster with the member 
	 * which is closest to one of its members
	 * @param otherClusters - cluster to compare against
	 * @param currentClusterIndex - location of this cluster in that list 
	 * so the cluster doesn't compare against itself. 
	 * @return the index of the closest cluster in the input list of clusters
	 */
	public double findClosestCluster(List<AntCluster> otherClusters, int currentClusterIndex){
		int closestOtherCluster = 0;
		double closestOtherClusterDistance= -1;
		double distance;
		List<AntGridPoint> otherClusterMembers;
		//for each data point in the cluster
		for(int i = 0; i < _clusterMembers.size(); i ++){
			//for each other cluster
			for(int j = 0 ; j < otherClusters.size(); j ++){
				if( currentClusterIndex != j){ //don't check distance within current cluster
					otherClusterMembers = otherClusters.get(j).getMembers();
					//for each datapoint in the other clusters, measure distance
					for(int k = 0 ; k < otherClusterMembers.size(); k ++){
						distance = Math.sqrt(Math.pow((otherClusterMembers.get(k).getXpos()-_clusterMembers.get(i).getXpos()),2)
								+Math.pow((otherClusterMembers.get(k).getYpos()-_clusterMembers.get(i).getYpos()),2));
						//if new low distance, update closest cluster
						if(distance< closestOtherClusterDistance ||closestOtherClusterDistance ==-1){
							closestOtherClusterDistance = distance;
							closestOtherCluster =  j;
						}
					}
				}
				
			}
		}
		_closestClusterIndex = closestOtherCluster;
		return closestOtherClusterDistance;
		
	}
	/**
	 * gets index of closest index found
	 * during last run of "findClosestCluster"
	 * @return index of cluster
	 */
	public int getClosestClusterIndex(){
		return _closestClusterIndex;
	}

}
