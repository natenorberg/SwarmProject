package softcomputing.project4.services;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Prints the output coordinates to a csv file
 */
public class CsvPrinterService
{
    private static CsvPrinterService _instance;
    private PrintStream _output;
    private String _filename;
    private int _numRuns;
    private List<double[]> _outputPoints;
    private int _run;

    private CsvPrinterService(TunableParameterService parameterService, DataSetInformationService dataSetInformationService)
    {
        _outputPoints = new LinkedList<double[]>();

        String filepath = "output/graph_data/";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        _filename = filepath.concat(dataSetInformationService.getDescription()).concat(timestamp);
        _numRuns = parameterService.getNumRuns();

        try {
            _output = new PrintStream(_filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Records a point to print out later, but doesn't print it yet
     * @param iteration
     * @param metric
     */
    public void logGraphPoint(int iteration, double metric)
    {
        // Creates an array for the given iteration
        if (_outputPoints.size() <= iteration)
            _outputPoints.add(iteration, new double[_numRuns]);

        // Get the array to update
        double[] iterationData = _outputPoints.get(iteration);

        // Check to see if there's already data for this run and iteration
        // If there is, we're at the next run already
        if (iterationData[_run] != 0)
            _run++;

        // Record the data
        iterationData[_run] = metric;
    }

    /**
     * Averages all the run data and prints out a csv file
     */
    public void printGraphPoints()
    {
        for (int i=0; i<_outputPoints.size(); i++)
        {
            double[] row = _outputPoints.get(i);

            double total = 0;

            for (int j=0; j<row.length; j++)
            {
                double point = row[j];
                /*
                 * We need to account for a stop condition of convergence.  In this case
                 * we want to keep using the value it converged on rather than just zero.
                 */
                if (point != 0) {
                    total += point;
                }
                else {
                    point = _outputPoints.get(i-1)[j]; // Add the previous value to the array
                                                       // This is important because we'll need to propagate this value each time
                    total += point;
                }
            }
            // Calculate the average
            double average = total / (double) _numRuns;

            // Write the average as a point in the csv file
            writeGraphPoint(i, average);
        }
    }

    /**
     * Prints a point to the csv output file
     * @param iteration
     * @param metric
     */
    private void writeGraphPoint(int iteration, double metric)
    {
        _output.format("%d,%f\n", iteration, metric);
    }

    /**
     * Gets the singleton instance of this service
     * @return instance
     */
    public static CsvPrinterService getInstance()
    {
        if (_instance == null)
            _instance = new CsvPrinterService(TunableParameterService.getInstance(), DataSetInformationService.getInstance());

        return _instance;
    }
}
