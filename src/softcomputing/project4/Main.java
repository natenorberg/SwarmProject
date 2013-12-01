package softcomputing.project4;
import java.util.Arrays;

import softcomputing.project4.data.*;

public class Main {

    public static void main(String[] args) {
    	Parser csvParser = new CSVParser();
    	DataPoint[] breastCancerSet = csvParser.loadDataSet("SC_data/GlassIdentification/GlassIDWithoutClassification.csv");
    }
}
