package softcomputing.project4.cluster.competitive;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a neural network
 */
public class NeuralNetwork
{
    private List<Connection> _inputs;
    private List<Connection> _outputs;
    private List<Neuron> _inputLayer;
    private List<Neuron> _outputLayer;

    /**
     * Constructor for neural network
     * @param numInputs
     * @param numOutputs
     */
    public NeuralNetwork(int numInputs, int numOutputs)
    {
        // Creates the connections in and out of the network
        createInputArray(numInputs);
        createOutputArray(numOutputs);

        // Create the neurons for the network
        // There will always be two layers so we don't need to be very generic
        _inputLayer = createInputLayer();
        _outputLayer = createOutputLayer();

        // Add connections between the layers
        addConnections();
    }

    /**
     * Runs a data point through the network
     * @param inputVector
     * @return classNumber
     */
    public int runNetwork(double[] inputVector)
    {
        // Set the inputs into the network
        for (int i=0; i<inputVector.length; i++)
        {
            _inputs.get(i).setValue(inputVector[i]);
        }

        // Run the data through the network
        for (Neuron node : _inputLayer)
        {
            node.evaluate();
        }

        for (Neuron node : _outputLayer)
        {
            node.evaluate();
        }

        // Find out which class label the network picked
        int classNumber = -1; // Initialize to -1 (not valid)
        double highestValue = -1;
        for (int i=0; i<_outputs.size(); i++)
        {
            double outputValue = _outputs.get(i).getValue();

            if (outputValue > highestValue)
            {
                highestValue = outputValue;
                classNumber = i;
            }
        }

        // Update the winning node and return the winning class number
        _outputLayer.get(classNumber).update();

        return classNumber;
    }

    /**
     * Hooks up connections between the two layers to make them completely connected
     */
    private void addConnections()
    {
        for (Neuron fromNeuron : _inputLayer)
        {
            for (Neuron toNeuron : _outputLayer)
            {
                Connection connection = new Connection(); // Create a new connection with a random weight

                // Connect it to both neurons
                fromNeuron.getOutputs().add(connection);
                toNeuron.getInputs().add(connection);
            }
        }
    }

    /**
     * Creates the input layer of neurons
     * @return Input layer
     */
    private List<Neuron> createInputLayer()
    {
        List<Neuron> inputNodes = new ArrayList<Neuron>();

        for (Connection input : _inputs)
        {
            // Wrap the input connection in a list
            List<Connection> inputList = new LinkedList<Connection>();
            inputList.add(input);

            // Create a neuron that has that connection as an input
            Neuron node = new Neuron(inputList, new LinkedList<Connection>(), ActivationFunctionType.Linear);
            inputNodes.add(node);
        }

        return inputNodes;
    }

    /**
     * Creates the output layer of neurons
     * @return Output layer
     */
    private List<Neuron> createOutputLayer()
    {
        List<Neuron> outputNodes = new ArrayList<Neuron>();

        for (Connection output : _outputs)
        {
            // Wrap the output connection in a list
            List<Connection> outputList = new LinkedList<Connection>();
            outputList.add(output);

            //Create a neuron that has that connection as an output
            Neuron node = new Neuron(new LinkedList<Connection>(), outputList, ActivationFunctionType.Sigmoid);
            outputNodes.add(node);
        }

        return outputNodes;
    }


    // Creates a list of the input connections to use in the network
    private void createInputArray(int numInputs)
    {
        _inputs = new ArrayList<Connection>();

        for (int i=0; i<numInputs; i++)
        {
            // Add a new connection with a weight of 1 to the network
            _inputs.add(new Connection(1));
        }
    }

    // Creates a list of the output connection to use in the network
    private void createOutputArray(int numOutputs)
    {
        _outputs = new ArrayList<Connection>();

        for (int i=0; i<numOutputs; i++)
        {
            // Add a new connection with a weight of 1 to the network outputs
            _outputs.add(new Connection(1));
        }
    }
}
