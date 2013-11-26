package softcomputing.project4.factories;

import softcomputing.project4.ClusteringAlgorithm;
import softcomputing.project4.cluster.IClusterer;
import softcomputing.project4.services.TunableParameterService;

/**
 * Factory to create the clustering algorithm used
 */
public class ClustererFactory
{
    private final ClusteringAlgorithm _algorithmType;

    // Public constructor that will be called
    public ClustererFactory()
    {
        this(TunableParameterService.getInstance());
    }

    // Overloaded constructor that takes a parameter service through dependency injection
    public ClustererFactory(TunableParameterService parameterService)
    {
        _algorithmType = parameterService.getClusteringAlgorithm();
    }

    public IClusterer getClusterer() throws ClassNotFoundException
    {
        switch (_algorithmType)
        {
            case KMeans:
                throw new ClassNotFoundException("Not implemented yet");
            case AntColony:
                throw new ClassNotFoundException("Not implemented yet");
            case PSO:
                throw new ClassNotFoundException("Not implemented yet");
            default:
                throw new ClassNotFoundException("Not a valid class name");
        }
    }

}
