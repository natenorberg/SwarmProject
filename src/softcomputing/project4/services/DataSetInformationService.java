package softcomputing.project4.services;

import softcomputing.project4.enums.DataSetSource;

/**
 * Provides information specific to each data set
 */
public class DataSetInformationService
{
    private static DataSetInformationService _instance;
    private final DataSetSource _dataSet;
    private final int _numInputs;
    private final int _numOutputs;
    private final String _filename;

    private DataSetInformationService(TunableParameterService parameterService) throws IllegalArgumentException
    {
          _dataSet = parameterService.getDataSet();

        switch (_dataSet)
        {
            case GlassIdentification:
                _numInputs = 9;
                _numOutputs = 5;
                _filename = "SC_data/GlassIdentification/GlassIDWithoutClassification.csv";
                break;
            default:
                throw new IllegalArgumentException("No information available for that data set");
        }
    }

    // Gets the singleton instance of this class
    public static DataSetInformationService getInstance()
    {
        // Lazy load the instance
        if (_instance == null) {
            _instance = new DataSetInformationService(TunableParameterService.getInstance());
        }

        return _instance;
    }

    /**
     * Gets the number of features in the data set
     * @return _numInputs
     */
    public int getNumInputs()
    {
        return _numInputs;
    }

    /**
     * Gets the number of cluster labels to use in the data set
     * @return _numOutputs
     */
    public int getNumOutputs()
    {
        return _numOutputs;
    }

    /**
     * Gets the filename of the data set
     * @return filename
     */
    public String getFilename()
    {
        return _filename;
    }
}
