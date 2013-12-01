package softcomputing.project4.cluster.competitive;

import softcomputing.project4.cluster.Cluster;
import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

import java.util.LinkedList;
import java.util.List;

/**
 * Uses a competitive learning neural net to cluster the data set
 */
public class CompetitiveClusterer extends Clusterer
{
    private NeuralNetwork _network;
    private int _numIterations;
    private int _numClusters;
    private List<Cluster> _clusters;

    /**
     * Public constructor
     */
    public CompetitiveClusterer()
    {
        this(TunableParameterService.getInstance(), DataSetInformationService.getInstance());
    }

    /**
     * Constructor with dependency injection
     * @param parameterService
     * @param dataSetInformationService
     */
    public CompetitiveClusterer(TunableParameterService parameterService, DataSetInformationService dataSetInformationService)
    {
        int numInputs = dataSetInformationService.getNumInputs();
        _numClusters = dataSetInformationService.getNumOutputs();
        _network = new NeuralNetwork(numInputs, _numClusters);

        _numIterations = parameterService.getNumberOfIterations();
    }

    @Override
    public void clusterDataSet(DataPoint[] dataSet)
    {
        // Initialize empty clusters
        _clusters = new LinkedList<Cluster>();
        for (int i=0; i<_numClusters; i++) {
            _clusters.add(new Cluster());
        }

        for (int i=0; checkStopConditions(i); i++)
        {
            for (DataPoint point : dataSet)
            {
                // Run the data through the network
                int clusterNumber = _network.runNetwork(point.getData());

                // Adds the point to the given cluster
                Cluster cluster = _clusters.get(clusterNumber);
                cluster.addPoint(point);
            }
        }
    }

    // Checks the stopping conditions and returns true if the algorithm should still run
    // (Moving this to a separate method in case we want to change the stopping conditions)
    private boolean checkStopConditions(int iteration)
    {
        return iteration < _numIterations;
    }
}
