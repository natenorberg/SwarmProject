package softcomputing.project4;
import java.util.Arrays;

import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.*;
import softcomputing.project4.factories.ClustererFactory;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException
    {
        final int numberOfRuns = 2;

    	Parser csvParser = new CSVParser();
    	DataPoint[] dataSet = csvParser.loadDataSet();

        ClustererFactory clustererFactory = new ClustererFactory();
        Clusterer clusterer = clustererFactory.getClusterer();

        for (int i=0; i<numberOfRuns; i++) {
            clusterer.clusterDataSet(dataSet);
        }
    }
}
