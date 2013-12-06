package softcomputing.project4.cluster.antcolony;

import java.util.ArrayList;
import java.util.List;



public class AntCluster {
	AntCluster _closestCluster;
	int _closestClusterIndex;
	double _closestClusterDistance;
	
	List<AntGridPoint> _clusterMembers;
	
	public AntCluster(AntGridPoint firstMember){
		_clusterMembers = new ArrayList<AntGridPoint>();
		_clusterMembers.add(firstMember);
	}
	public void joinCluster(AntCluster input){
		_clusterMembers.addAll(input.getMembers());
	}
	public List<AntGridPoint> getMembers(){
		return _clusterMembers;
	}
	public double findClosestCluster(List<AntCluster> otherClusters, int currentClusterIndex){
		int closestOtherCluster = 0;
		double closestOtherClusterDistance= -1;
		double distance;
		List<AntGridPoint> otherClusterMembers;
		//for each data point in the cluster
		for(int i = 0; i < _clusterMembers.size(); i ++){
			for(int j = 0 ; j < otherClusters.size(); j ++){
				if( currentClusterIndex != j){ //don't check distance within current cluster
					otherClusterMembers = otherClusters.get(j).getMembers();
					for(int k = 0 ; k < otherClusterMembers.size(); k ++){
						distance = Math.sqrt(Math.pow((otherClusterMembers.get(k).getXpos()-_clusterMembers.get(i).getXpos()),2)
								+Math.pow((otherClusterMembers.get(k).getYpos()-_clusterMembers.get(i).getYpos()),2));
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
	public int getClosestClusterIndex(){
		return _closestClusterIndex;
	}

}
