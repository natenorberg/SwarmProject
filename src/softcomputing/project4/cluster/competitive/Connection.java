package softcomputing.project4.cluster.competitive;

import java.util.Random;

/**
 * Represents a connection in a neural network
 */
public class Connection
{
    private double _value;
    private double _weight;
    private static Random _random = new Random();

    // Constructor to create a connection with a randomly initialized weight
    public Connection()
    {
        this(_random.nextDouble());
    }

    // Constructor to create a connection with a specified weight
    public Connection(double weight)
    {
        _weight = weight;
    }


    // Gets the value in the connection
    public double getValue()
    {
        return _value;
    }

    // Sets the value in the connection
    public void setValue(double value)
    {
        _value = value;
    }

    // Gets the weight in the connection
    public double getWeight()
    {
        return _weight;
    }

    // Sets the weight in the connection
    public void setWeight(double weight)
    {
        _weight = weight;
    }
}
