package softcomputing.project4.data;

import softcomputing.project4.cluster.Cluster;

/**
 * Class to represent a data point
 */
public class DataPoint
{
    private double[] _data;
    private Cluster _cluster;

    // Constructor
    public DataPoint(double[] data)
    {
        _data = data;
    }

    /**
     * Gets the input data this point represents
     * @return data
     */
    public double[] getData()
    {
        return _data;
    }

    /**
     * Gets the cluster this point belongs to
     * @return cluster
     */
    public Cluster getCluster()
    {
        return _cluster;
    }

    /**
     * Assigns this point to a different cluster
     * @param cluster
     */
    public void setCluster(Cluster cluster)
    {
        _cluster = cluster;
    }
}
