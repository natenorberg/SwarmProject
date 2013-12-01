package softcomputing.project4.data;

import softcomputing.project4.services.DataSetInformationService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVParser implements Parser {

    private final String _filename;

    /**
     * Public constructor
     */
    public CSVParser()
    {
        this(DataSetInformationService.getInstance());
    }

    // Constructor with dependency injection
    public CSVParser(DataSetInformationService dataSetInformationService)
    {
        _filename = dataSetInformationService.getFilename();
    }

	@Override
	public DataPoint[] loadDataSet() {
		ArrayList<String[]> rawData = importData(_filename);
		DataPoint[] dataset = inputToDataPoints(rawData);
		dataset = normalizeData(dataset);
		return dataset;
	}
	/**
	 * Imports the .csv file located at the input file path
	 * @param filepath - location of the file to be imported
	 * @return an array list of the data in the .csv
	 */
	public ArrayList<String[]> importData(String filepath){
		BufferedReader buffReader = null;
		String line = "";
		ArrayList<String[]> readPatterns = new ArrayList<String[]>();
		
		try {
			buffReader = new BufferedReader(new FileReader(filepath));
			while((line = buffReader.readLine()) != null){
				String[] stringNum = line.split(","); //split at commas
				readPatterns.add(stringNum);
			}
			buffReader.close();
		}
		catch (Exception e){
			System.err.println("Error: "+ e.getMessage());
		}
		return readPatterns;
	}
	/**
	 * Imports a comma separated file of input patterns
	 * @param inputList
	 * @return a double array list of input patterns
	 */
	public DataPoint[] inputToDataPoints(ArrayList<String[]> inputList){
		DataPoint[] output= new DataPoint[inputList.size()];
		double[] pointHolder;
		
		for(int i = 0 ; i < inputList.size(); i ++){
			pointHolder = new double[inputList.get(0).length];
			for(int j = 0; j < inputList.get(i).length; j ++){
				pointHolder[j] = Double.parseDouble(inputList.get(i)[j]);
			}
			output[i] = new DataPoint(pointHolder);
		}
		return output;
	}
	// normalize the data
	public DataPoint[] normalizeData(DataPoint[] input){
		DataPoint[] normalized = new DataPoint[input.length];
		double high;
		double low;
		for(int i = 0; i < input[0].getData().length;i ++){ //for each dimension of the data points
			low = 0;
			high = 0; 
			for(int j = 0 ; j < input.length; j ++){ //find the min and max values 
				//find min
				if(low > input[j].getData()[i] || j ==0){
					low = input[j].getData()[i];
				}
				//find max
				if(high < input[j].getData()[i] || j==0){
					high = input[j].getData()[i];
				}
			}
			for(int j = 0 ; j < input.length; j ++){
				//normalize within range 0 to 1
				input[j].getData()[i] = (input[j].getData()[i] - low)/(high-low);
			}
		}
		return normalized;
	}
}
