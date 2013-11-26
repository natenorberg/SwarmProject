package softcomputing.project4.cluster;

/**
 * Common interface for all clustering algorithms
 */
public interface IClusterer
{
    // Clusters the data set
    // TODO: Create an object to represent data points and make the argument of this method take an array of that
    void clusterDataSet(Object[] dataSet);
}
