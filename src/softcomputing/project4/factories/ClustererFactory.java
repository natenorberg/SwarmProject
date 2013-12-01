package softcomputing.project4.factories;

import softcomputing.project4.cluster.antcolony.AntColonyClusterer;
import softcomputing.project4.cluster.competitive.CompetitiveClusterer;
import softcomputing.project4.enums.ClusteringAlgorithm;
import softcomputing.project4.cluster.*;
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

    /*
     * Creates the selected clusterer
     */
    public IClusterer getClusterer() throws ClassNotFoundException
    {
        switch (_algorithmType)
        {
            case KMeans:
                return new KMeansClusterer();
            case Competitive:
                return new CompetitiveClusterer();
            case AntColony:
                return new AntColonyClusterer();
            case PSO:
                return new PsoClusterer();
            default:
                throw new ClassNotFoundException("Not a valid class name");
        }
    }

}
