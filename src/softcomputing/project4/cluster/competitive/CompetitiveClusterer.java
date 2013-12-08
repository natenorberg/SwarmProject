package softcomputing.project4.cluster.competitive;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.enums.StopCondition;
import softcomputing.project4.services.CsvPrinterService;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

import java.util.LinkedList;
import java.util.List;

/**
 * Uses a competitive learning neural net to cluster the data set
 */
public class CompetitiveClusterer extends Clusterer
{
    private final StopCondition _stopCondition;
    private final int _numIterationsToConverge;
    private final boolean _createOutputCsv;
    private final CsvPrinterService _printer;
    private NeuralNetwork _network;
    private int _numIterations;
    private int _numInputs;
    private int _numClusters;
    private final boolean _printIntraClusterDistance;
    private final boolean _printInterClusterDistance;
    private final boolean _printDaviesBouldinIndex;

    /**
     * Public constructor
     */
    public CompetitiveClusterer()
    {
        this(TunableParameterService.getInstance(), DataSetInformationService.getInstance(), CsvPrinterService.getInstance());
    }

    /**
     * Constructor with dependency injection
     * @param parameterService
     * @param dataSetInformationService
     */
    public CompetitiveClusterer(TunableParameterService parameterService,
                                DataSetInformationService dataSetInformationService, CsvPrinterService printer)
    {
        _numInputs = dataSetInformationService.getNumInputs();
        _numClusters = dataSetInformationService.getNumOutputs();

        _stopCondition = parameterService.getStopCondition();
        _numIterations = parameterService.getNumberOfIterations();
        _numIterationsToConverge = parameterService.getNumIterationsToConverge();

        _printIntraClusterDistance = parameterService.getPrintIntraClusterDistance();
        _printInterClusterDistance = parameterService.getPrintInterClusterDistance();
        _printDaviesBouldinIndex = parameterService.getPrintDaviesBouldinIndex();

        _createOutputCsv = parameterService.getCreateOutputCsv();
        _printer = printer;
    }

    @Override
    public void clusterDataSet(DataPoint[] dataSet)
    {
        //Create a new network
        _network = new NeuralNetwork(_numInputs, _numClusters);

        // Initialize empty clusters
        _clusters = new LinkedList<Cluster>();
        for (int i=0; i<_numClusters; i++) {
            _clusters.add(new Cluster());
        }

        int convergedRuns = 0;
        for (int i=0; checkStopConditions(i, convergedRuns); i++)
        {
            convergedRuns++; // This will be set back to 0 if we haven't actually converged
            for (DataPoint point : dataSet)
            {
                // Run the data through the network
                int clusterNumber = _network.runNetwork(point.getData());

                // Adds the point to the given cluster
                Cluster cluster = _clusters.get(clusterNumber);
                Cluster oldCluster = point.getCluster();

                // Add point to the cluster it was assigned to
                if (cluster != null && !cluster.getPoints().contains(point))
                {
                    cluster.getPoints().add(point);
                    point.setCluster(cluster);
                }

                // Remove point from old cluster if it has changed
                if (oldCluster != null && oldCluster != cluster)
                {
                    oldCluster.getPoints().remove(point);
                    convergedRuns = 0; // The algorithm has not converged
                }

            }

            // Find the centers of the clusters to evaluate the density better
            for (Cluster cluster : _clusters)
            {
                cluster.recalculateCenter();
            }

            // Build a format string based on print parameters
            String outputString = String.format("Run %d: ", i);

            if (_printIntraClusterDistance)
                outputString = outputString.concat(String.format("Average distance in clusters: %f, ", this.evaluateCluster()));
            if (_printInterClusterDistance)
                outputString = outputString.concat(String.format("Average distance between clusters: %f, ", this.averageDistanceBetweenCenters()));
            if (_printDaviesBouldinIndex)
                outputString = outputString.concat(String.format("Davies-Bouldin index: %f, ", this.daviesBouldinIndex()));

            System.out.println(outputString);

            // Print output to csv file
            if (_createOutputCsv) {
                _printer.logGraphPoint(i, this.daviesBouldinIndex());
            }
        }
    }

    // Checks the stopping conditions and returns true if the algorithm should still run
    // (Moving this to a separate method in case we want to change the stopping conditions)
    private boolean checkStopConditions(int iteration, int convergedRuns)
    {
        switch (_stopCondition)
        {
            case Iterations:
                return iteration < _numIterations;
            case Convergence:
                return convergedRuns != _numIterationsToConverge;
            default:
                throw new IllegalArgumentException("That stop condition is not supported by this algorithm.");
        }
    }
}
