package softcomputing.project4.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CSVParser implements Parser {

	@Override
	public DataPoint[] loadDataSet(String filepath) {
		ArrayList<String[]> rawData = importData(filepath);
		DataPoint[] dataset = inputToDataPoints(rawData);
		return dataset;
	}
	/**
	 * Imports the .csv file located at the input file path
	 * @param filepath - location of the file to be imported
	 * @return an array list of the data in the .csv
	 */
	public static ArrayList<String[]> importData(String filepath){
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
			System.err.println("Errpr: "+ e.getMessage());
		}
		return readPatterns;
	}
	/**
	 * Imports a comma separated file of input patterns
	 * @param filename
	 * @return a double array list of input patterns
	 */
	public static DataPoint[] inputToDataPoints(ArrayList<String[]> inputList){
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
}
