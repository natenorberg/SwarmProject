package softcomputing.project4.data;

/**
 * Interface for all parsers
 */
public interface Parser
{
    // Loads the data set used by this parser
    DataPoint[] loadDataSet(String filepath);
    
}
