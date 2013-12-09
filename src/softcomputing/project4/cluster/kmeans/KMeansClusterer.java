package softcomputing.project4.cluster.kmeans;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.enums.StopCondition;
import softcomputing.project4.services.ConsoleWriterService;
import softcomputing.project4.services.CsvPrinterService;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

import java.util.LinkedList;
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
    private final boolean _printIntraClusterDistance;
    private final boolean _printInterClusterDistance;
    private final boolean _printDaviesBouldinIndex;
    private final boolean _createOutputCsv;
    private final CsvPrinterService _printer;
    private final ConsoleWriterService _output;
    private int _numIterationsToConverge;

    /**
     * Public constructor
     */
    public KMeansClusterer()
    {
        this(DataSetInformationService.getInstance(), TunableParameterService.getInstance(),
                CsvPrinterService.getInstance(), ConsoleWriterService.getInstance());
    }

    /**
     * Constructor that takes services through dependency injection
     * @param dataInfoService
     */
    public KMeansClusterer(DataSetInformationService dataInfoService,
                           TunableParameterService parameterService, CsvPrinterService printer, ConsoleWriterService output)
    {
        // Read in parameters
        _numClusters = dataInfoService.getNumOutputs();
        _numFeatures = dataInfoService.getNumInputs();
        _stopCondition = parameterService.getStopCondition();
        _numIterations = parameterService.getNumberOfIterations();
        _numIterationsToConverge = parameterService.getNumIterationsToConverge();
        // Read in print parameters
        _printIntraClusterDistance = parameterService.getPrintIntraClusterDistance();
        _printInterClusterDistance = parameterService.getPrintInterClusterDistance();
        _printDaviesBouldinIndex = parameterService.getPrintDaviesBouldinIndex();

        _createOutputCsv = parameterService.getCreateOutputCsv();
        _printer = printer;
        _output = output;
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

        int convergedRuns = 0;
        for (int i=0; checkStopConditions(i, convergedRuns); i++)
        {
            convergedRuns++; // This will be set back to false if not actually converged

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
                    convergedRuns = 0; // The algorithm has not converged so set this back to 0
                }
            }

            for (Cluster center : _clusters) {
                center.recalculateCenter();
            }


            // Build a format string based on print parameters
            String outputString = String.format("Run %d: ", i);

            if (_printIntraClusterDistance)
                outputString = outputString.concat(String.format("Average distance in clusters: %f, ", this.evaluateCluster()));
            if (_printInterClusterDistance)
                outputString = outputString.concat(String.format("Average distance between clusters: %f, ", this.averageDistanceBetweenCenters()));
            if (_printDaviesBouldinIndex)
                outputString = outputString.concat(String.format("Davies-Bouldin index: %f, ", this.daviesBouldinIndex()));

            _output.writeLine(outputString);

            // Write the output to the csv file to make a nice graph
            if (_createOutputCsv) {
                _printer.logGraphPoint(i, this.daviesBouldinIndex());
            }
        }
    }

    // Checks the stopping conditions
    private boolean checkStopConditions(int iteration, int convergedRuns)
    {
        switch (_stopCondition)
        {
            case Iterations:
                return iteration < _numIterations;
            case Convergence:
                return convergedRuns != _numIterationsToConverge;
            default:
                throw new IllegalArgumentException("This algorithm does not support that stop condition");
        }


    }
}
