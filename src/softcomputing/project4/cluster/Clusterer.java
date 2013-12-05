package softcomputing.project4.cluster;

import softcomputing.project4.data.DataPoint;

import java.util.List;

/**
 * Common interface for all clustering algorithms
 */
public abstract class Clusterer
{
    protected List<Cluster> _clusters;

    /**
     * Clusters the data set
     * @param dataSet
     */
    public abstract void clusterDataSet(DataPoint[] dataSet);

    /**
     * Finds the euclidean distance between two points
     * @param a
     * @param b
     * @return distance
     */
    public static double euclideanDistance(double[] a, double[] b){
        double output = 0;
        for(int i =0; i < a.length; i ++){
            output += Math.pow((a[i]-b[i]),2);
        }
        output = Math.sqrt(output);
        return output;
    }

    /**
     * Evaluates how good the algorithm clusters
     * @return fitness
     */
    protected double evaluateCluster()
    {
        // Using average distance to center of cluster as metric
        double totalDistance = 0;

        for (Cluster cluster : _clusters)
        {
            totalDistance += cluster.averageDistanceFromCenter();
        }

        return totalDistance / (double) _clusters.size();
    }

    protected double averageDistanceBetweenCenters()
    {
        double totalDistance = 0;
        int totalComparisons = 0; //We don't want to count clusters with nothing in them

        //Loop through and find the total distance between all of the cluster centers
        for (int i=0; i<_clusters.size()-1; i++)
        {
            Cluster cluster1 = _clusters.get(i);
            if (cluster1.getPoints().size() == 0)
                continue; // Move on to the next cluster and don't do comparisons here. It will only break stuff

            for (int j=i+1; j<_clusters.size(); j++)
            {
                Cluster cluster2 = _clusters.get(j);
                if (cluster2.getPoints().size() == 0)
                    continue; // Same check as before

                totalDistance += euclideanDistance(cluster1.getCenter(), cluster2.getCenter());
                totalComparisons++;
            }
        }

        // Return the average
        return totalDistance / (double) totalComparisons;
    }
}
