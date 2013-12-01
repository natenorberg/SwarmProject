package softcomputing.project4.cluster;

import softcomputing.project4.data.DataPoint;

/**
 * Common interface for all clustering algorithms
 */
public abstract class Clusterer
{
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
        // TODO: Find a good metric to compare the different algorithms with

        return 1; // Placeholder to make the build work until there is something intelligent to put here
    }
}
