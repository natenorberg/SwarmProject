package softcomputing.project4.cluster.competitive;

import softcomputing.project4.cluster.IClusterer;
import softcomputing.project4.data.DataPoint;
import softcomputing.project4.services.DataSetInformationService;
import softcomputing.project4.services.TunableParameterService;

/**
 * Uses a competitive learning neural net to cluster the data set
 */
public class CompetitiveClusterer implements IClusterer
{
    private NeuralNetwork _network;

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
        int numOutputs = dataSetInformationService.getNumOutputs();
        _network = new NeuralNetwork(numInputs, numOutputs);

    }

    @Override
    public void clusterDataSet(DataPoint[] dataSet)
    {
        // TODO: Implement competitive learning
    }
}
