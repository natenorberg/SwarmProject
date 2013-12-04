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
            case BreastCancer:
                _numInputs = 30;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/breast_cancer_wisconsin/BreastCancerWithoutClassification.csv";
                break;
            case Cardiotocography:
                _numInputs = 22;
                _numOutputs = 3; // Data set has 3 class labels
                _filename = "SC_data/Cardiotocography/CTG_no_class_labels.csv";
                break;
            case ClimateModel:
                _numInputs = 18;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/ClimateModel/ClimateModel_no_class_labels.csv";
                break;
            case EColi:
                _numInputs = 7;
                _numOutputs = 8; // Data set has 8 class labels
                _filename = "SC_data/Ecoli/Ecoli_no_class_labels.csv";
                break;
            case GlassIdentification:
                _numInputs = 9;
                _numOutputs = 10;
                _filename = "SC_data/GlassIdentification/GlassIDWithoutClassification.csv";
                break;
            case HillVally:
                _numInputs = 100;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/Hill-Vally Data Set/HillVally_no_class_labels.csv";
                break;
            case IndianLiver:
                _numInputs = 10;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/IndianLiver/ILPD_no_class_labels.csv";
                break;
            case Ionosphere:
                _numInputs = 33;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/Ionosphere/ionosphere_no_class_labels.csv";
                break;
            case MagicTelescope:
                _numInputs = 10;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/MagicTelescope/MagicTelescope_no_class_labels.csv";
                break;
            case Musk:
                _numInputs = 166;
                _numOutputs = 2; // Data set has 2 class labels
                _filename = "SC_data/Musk/muskData_no_class_labels.csv";
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
