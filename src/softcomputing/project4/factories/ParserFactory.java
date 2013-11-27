package softcomputing.project4.factories;

import softcomputing.project4.data.Parser;
import softcomputing.project4.enums.DataSetSource;
import softcomputing.project4.services.TunableParameterService;

/**
 * Creates parsers
 */
public class ParserFactory
{
    private final DataSetSource _dataSet;

    public ParserFactory(TunableParameterService parameterService)
    {
        _dataSet = parameterService.getDataSet();
    }

    // Creates the parser for the given data set
    public Parser getParser() throws ClassNotFoundException
    {
        switch (_dataSet) {
            default:
                throw new ClassNotFoundException("No parser exists for that format");
            case Placeholder:
                throw new ClassNotFoundException("No parser exists for that format");
        }
    }
}
