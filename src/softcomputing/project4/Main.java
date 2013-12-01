package softcomputing.project4;
import java.util.Arrays;

import softcomputing.project4.cluster.Clusterer;
import softcomputing.project4.data.*;
import softcomputing.project4.factories.ClustererFactory;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException
    {
    	Parser csvParser = new CSVParser();
    	DataPoint[] dataSet = csvParser.loadDataSet();

        ClustererFactory clustererFactory = new ClustererFactory();
        Clusterer clusterer = clustererFactory.getClusterer();

        clusterer.clusterDataSet(dataSet);
    }
}
