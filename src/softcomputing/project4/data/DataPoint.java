package softcomputing.project4.data;

/**
 * Class to represent a data point
 */
public class DataPoint
{
    private double[] _data;

    // Constructor
    public DataPoint(double[] data)
    {
        _data = data;
    }

    public double[] getData()
    {
        return _data;
    }
}
