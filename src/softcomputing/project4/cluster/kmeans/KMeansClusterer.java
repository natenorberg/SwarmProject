package softcomputing.project4.cluster.kmeans;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.enums.StopCondition;
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
    private final int _numIterations;
    private final StopCondition _stopCondition;

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
        _stopCondition = parameterService.getStopCondition();
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

        boolean converged = false;
        for (int i=0; checkStopConditions(i, converged); i++)
        {
            converged = true; // This will be set back to false if not actually converged

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
                }

                // Assign point to closest cluster
                Cluster oldCentroid = point.getCluster();
                if (closestCentroid != null && !closestCentroid.getPoints().contains(point))
                {
                    closestCentroid.getPoints().add(point);
                    point.setCluster(closestCentroid);
                }

                // Remove point from previous cluster if it changes
                if (oldCentroid != null && oldCentroid != closestCentroid) {
                    oldCentroid.getPoints().remove(point);
                    converged = false;
                }
            }

            for (Cluster center : _clusters) {
                center.recalculateCenter();
            }

            System.out.format("Run %d: Average distance in clusters: %f\n", i, this.evaluateCluster());
        }
    }

    // Checks the stopping conditions
    private boolean checkStopConditions(int iteration, boolean converged)
    {
        switch (_stopCondition)
        {
            case Iterations:
                return iteration < _numIterations;
            case Convergence:
                return !converged;
            default:
                throw new IllegalArgumentException("This algorithm does not support that stop condition");
        }


    }
}
