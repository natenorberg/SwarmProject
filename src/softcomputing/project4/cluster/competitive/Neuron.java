package softcomputing.project4.cluster.competitive;

import softcomputing.project4.services.TunableParameterService;

import java.util.List;

/**
 * Represents a neuron in a neural network
 */
public class Neuron
{
    private final ActivationFunctionType _functionType;
    private final double _sigmoidAlpha;
    private final double _learningRate;
    private List<Connection> _inputs;
    private List<Connection> _outputs;

    // Constructor called elsewhere
    public Neuron(List<Connection> inputs, List<Connection> outputs, ActivationFunctionType functionType)
    {
        this(inputs, outputs, functionType, TunableParameterService.getInstance());
    }

    // Constructor that takes a parameter service
    public Neuron(List<Connection> inputs, List<Connection> outputs, ActivationFunctionType functionType, TunableParameterService parameterService)
    {
        _inputs = inputs;
        _outputs = outputs;
        _functionType = functionType;
        _sigmoidAlpha = parameterService.getSigmoidAlpha();
        _learningRate = parameterService.getNetworkLearningRate();
    }

    // Evaluates this neuron
    public void evaluate()
    {
        double sum = net(); // Get the sum of the inputs
        double outputValue = activationFunction(sum); // Run that value through the activation function

        // Push the value to the outputs
        for (Connection output : _outputs)
        {
            output.setValue(outputValue);
        }
    }

    // Gets the sum of the inputs multiplied by their weights
    private double net()
    {
        double sum = 0;

        for (Connection input : _inputs)
        {
            sum += input.getValue() * input.getWeight();
        }

        return sum;
    }

    // Runs the input through the activation function set for the network
    private double activationFunction(double input)
    {
        if (_functionType == ActivationFunctionType.Linear) {
            return input;
        }
        else {
            //Logistic function : 1 / (1 + e^-x)
            return 1 / (1 + (Math.pow(Math.E, -_sigmoidAlpha * input)));
        }
    }

    /**
     * Gets the node's inputs
     * @return Inputs
     */
    public List<Connection> getInputs()
    {
        return _inputs;
    }

    /**
     * Gets the node's outputs
     * @return Outputs
     */
    public List<Connection> getOutputs()
    {
        return _outputs;
    }

    /**
     * Updates the input weights if this node is the "winner"
     */
    public void update()
    {
        for (Connection input : _inputs)
        {
            double newWeight = input.getWeight() + _learningRate * input.getValue();
            input.setWeight(newWeight);
        }
    }
}

