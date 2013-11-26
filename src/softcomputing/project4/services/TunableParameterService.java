package softcomputing.project4.services;

import softcomputing.project4.ClusteringAlgorithm;

/**
 * This class is a central location to change all of the tunable parameters for the project
 * This class is passed in through dependency injection to all classes that need it
 */
public class TunableParameterService
{
    private static TunableParameterService _instance;
    private ClusteringAlgorithm _clusteringAlgorithm;

    // Private constructor
    private TunableParameterService()
    {
        // Properties will be initialized in here
        _clusteringAlgorithm = ClusteringAlgorithm.KMeans;
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

}
