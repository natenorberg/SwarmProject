package softcomputing.project4.cluster.kmeans;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Clusters a data set using k-means
 */
public class KMeansClusterer extends Clusterer
{
    private final int _numClusters;
    private final int _numFeatures;
    private List<Cluster> _clusters;
    private final int _numIterations;

    /**
     * Public constructor
     */
    public KMeansClusterer()
    {
        this(DataSetInformationService.getInstance(), TunableParameterService.getInstance());
    }

    /**
     * Constructor that takes services through dependency injection
     * @param dataInfoService
     */
    public KMeansClusterer(DataSetInformationService dataInfoService, TunableParameterService parameterService)
    {
        _numClusters = dataInfoService.getNumOutputs();
        _numFeatures = dataInfoService.getNumInputs();
        _numIterations = parameterService.getNumberOfIterations();
    }

    @Override
    public void clusterDataSet(DataPoint[] dataSet)
    {
        // Initialize k random centers
        _clusters = new LinkedList<Cluster>();
        Random random = new Random();
        for (int i=0; i<_numClusters; i++)
        {
            double[] coordinates = new double[_numFeatures];
            for (int j=0; j<_numFeatures; j++)
            {
                coordinates[j] = random.nextDouble();
            }

            _clusters.add(new Cluster(coordinates));
        }

        for (int i=0; checkStopConditions(i); i++)
        {
            // Assign points to clusters
            for (DataPoint point : dataSet)
            {
                double shortestDistance = Double.MAX_VALUE; // Set this to max value
                Cluster closestCentroid = null;

                for (Cluster cluster : _clusters)
                {
                    // Find the closest centroid for each point
                    double distance = Clusterer.euclideanDistance(point.getData(), cluster.getCenter());
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        closestCentroid = cluster;
                    }

                    // Assign point to closest cluster
                    if (closestCentroid != null && !closestCentroid.getPoints().contains(point))
                    {
                        closestCentroid.getPoints().add(point);
                    }

                    // Remove point from previous cluster if it changes
                    Cluster oldCentroid = point.getCluster();
                    if (oldCentroid != closestCentroid) {
                        oldCentroid.getPoints().remove(point);
                        point.setCluster(closestCentroid);
                    }
                }
            }

            for (Cluster center : _clusters) {
                center.recalculateCenter();
            }
        }
    }

    // Checks the stopping conditions
    private boolean checkStopConditions(int iteration)
    {
        // TODO: Add a converged stopping tag to the algorithm and add in appropriate logic
        return iteration < _numIterations;
    }
}
