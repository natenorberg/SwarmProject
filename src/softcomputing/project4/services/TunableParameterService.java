package softcomputing.project4.services;

import softcomputing.project4.enums.ClusteringAlgorithm;
import softcomputing.project4.enums.DataSetSource;

/**
 * This class is a central location to change all of the tunable parameters for the project
 * This class is passed in through dependency injection to all classes that need it
 */
public class TunableParameterService
{
    private static TunableParameterService _instance;
    private final DataSetSource _dataSet;
    private ClusteringAlgorithm _clusteringAlgorithm;

    // Private constructor
    private TunableParameterService()
    {
        // Properties will be initialized in here
        _clusteringAlgorithm = ClusteringAlgorithm.KMeans;
        _dataSet = DataSetSource.Placeholder;
    }

    // Gets the singleton instance of this class
    public static TunableParameterService getInstance()
    {
        // Lazy load the instance
        if (_instance == null)
            _instance = new TunableParameterService();

        return _instance;
    }

    // Gets the algorithm type used in this run
    public ClusteringAlgorithm getClusteringAlgorithm() {
        return _clusteringAlgorithm;
    }

    // Gets the data set used in this run
    public DataSetSource getDataSet(){
        return _dataSet;
    }
}
