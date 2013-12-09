package softcomputing.project4;
import java.util.Arrays;

import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.*;
import softcomputing.project4.factories.ClustererFactory;
import softcomputing.project4.services.ConsoleWriterService;
import softcomputing.project4.services.CsvPrinterService;
import softcomputing.project4.services.TunableParameterService;

public class Main {
	
    public static void main(String[] args) throws ClassNotFoundException
    {
        final TunableParameterService parameterService = TunableParameterService.getInstance();
        final int numberOfRuns = parameterService.getNumRuns();
        final boolean createOutputCsv = parameterService.getCreateOutputCsv();

        // Create parser and parse the data set
    	Parser csvParser = new CSVParser();
    	DataPoint[] dataSet = csvParser.loadDataSet();

        

        // Run the clustering algorithm a given number of times
        for (int i=0; i<numberOfRuns; i++) {
        	//create new instance of the clusterer
            ClustererFactory clustererFactory = new ClustererFactory();
            Clusterer clusterer = clustererFactory.getClusterer();
            //run the clusterer
            clusterer.clusterDataSet(dataSet);
        }

        // Print out a csv to graph everything at the end
        if (createOutputCsv)
            CsvPrinterService.getInstance().printGraphPoints();
    }
}
