package softcomputing.project4.services;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Prints the output coordinates to a csv file
 */
public class CsvPrinterService
{
    private static CsvPrinterService _instance;
    private PrintStream _output;
    private String _filename;

    private CsvPrinterService(TunableParameterService parameterService, DataSetInformationService dataSetInformationService)
    {
        String filepath = "output/graph_data/";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        _filename = filepath.concat(dataSetInformationService.getDescription()).concat(timestamp);

        try {
            _output = new PrintStream(_filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prints a point to the csv output file
     * @param iteration
     * @param metric
     */
    public void writeGraphPoint(int iteration, double metric)
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
