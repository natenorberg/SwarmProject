package softcomputing.project4.cluster;

import softcomputing.project4.data.DataPoint;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent a cluster
 */
public class Cluster
{
    private List<DataPoint> _points;
    private double[] _center;

    /**
     * Creates a cluster without a given center
     */
    public Cluster() {
        _points = new LinkedList<DataPoint>(); // New up the list when the cluster is created to avoid null ref exceptions
    }

    /**
     * Creates a cluster centered around a given center
     */
    public Cluster(double[] center) {
        _center = center;
        _points = new LinkedList<DataPoint>(); // New up the list when the cluster is created to avoid null ref exceptions
    }

    /**
     * Adds the point to the cluster if it's not there already
     * @param point
     */
    public void addPoint(DataPoint point)
    {
        // Check to see if the point is already in the cluster
        if (!_points.contains(point)) {
            _points.add(point);
        }
    }

    /**
     * Gets the average distance
     * @return
     */
    public double intraClusterDistance()
    {
        double totalDistance = 0;

        // Sum over the points and find the distance
        for (DataPoint point : _points)
        {
            totalDistance += Clusterer.euclideanDistance(point.getData(), _center);
        }

        return totalDistance;
    }

    /**
     * Getter for data points in clusers
     * @return _points
     */
    public List<DataPoint> getPoints()
    {
        return _points;
    }

    /**
     * Gets the coordinates for the center of the cluster
     * @return
     */
    public double[] getCenter()
    {
        return _center;
    }

    /**
     * Recalculates the center of the cluster to be the "center of gravity" for all the points in the cluster
     * Note: This method is to be used exclusively for k-means.
     */
    public void recalculateCenter()
    {
        for (int i=0; i<_center.length; i++)
        {
            double sum = 0;
            for (DataPoint point : _points)
            {
                sum += point.getData()[i];
            }

            double average = sum / _points.size();
            _center[i] = average;
        }
    }
}
