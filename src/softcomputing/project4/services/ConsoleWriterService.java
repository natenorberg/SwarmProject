package softcomputing.project4.services;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by norberg on 12/7/13.
 */
public class ConsoleWriterService
{
    private static ConsoleWriterService _instance;
    private String _filename;
    private PrintStream _output;
    private boolean _saveAsSampleRun;

    /**
     * Public constructor
     */
    private ConsoleWriterService() { this(TunableParameterService.getInstance(), DataSetInformationService.getInstance()); }

    private ConsoleWriterService(TunableParameterService parameterService, DataSetInformationService dataSetInformationService)
    {
        _saveAsSampleRun = parameterService.getSaveAsSampleRun();

        if (_saveAsSampleRun) {
            String filepath = "output/sample_runs/";
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            _filename = filepath.concat(dataSetInformationService.getDescription()).concat(timestamp);

            try {
                _output = new PrintStream(_filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a line to the console and (optionally) to the output file
     * @param line
     */
    public void writeLine(String line)
    {
        System.out.println(line);

        if (_saveAsSampleRun)
            _output.println(line);
    }

    /**
     * Writes a string to the console and (optionally) to the output file
     * @param string
     */
    public void writeString(String string)
    {
        System.out.print(string);

        if (_saveAsSampleRun)
            _output.print(string);
    }

    /**
     * Gets the singleton instance of this service
     * @return instance
     */
    public static ConsoleWriterService getInstance()
    {
        // Lazy load the instance
        if (_instance == null)
            _instance = new ConsoleWriterService();

        return _instance;
    }
}
