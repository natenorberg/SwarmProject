package softcomputing.project4.services;

import softcomputing.project4.enums.DataSetSource;

/**
 * Provides information specific to each data set
 */
public class DataSetInformationService
{
    private static DataSetInformationService _instance;
    private final DataSetSource _dataSet;

    private DataSetInformationService(TunableParameterService parameterService)
    {
          _dataSet = parameterService.getDataSet();
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
}
