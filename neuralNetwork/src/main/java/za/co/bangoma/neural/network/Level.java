package za.co.bangoma.neural.network;

import java.util.ArrayList;

public class Level {

    // Attributes
    ArrayList<Double> inputs;
    ArrayList<Double> outputs;
    ArrayList<Double> biases;
    ArrayList<ArrayList<Double>> weights;
    private int inputCount;
    private int outputCount;

    // Constructor
    public Level(int inputNeuronCount, int outputNeuronCount) {
        setInputCount(inputNeuronCount);
        setOutputCount(outputNeuronCount);

        this.inputs = new ArrayList<>(inputNeuronCount);
        this.outputs = new ArrayList<>(outputNeuronCount);
        this.biases = new ArrayList<>(outputNeuronCount);

        this.weights = new ArrayList<>(outputNeuronCount);

        for (int i = 0; i < inputNeuronCount; i++) {
            this.weights.add(new ArrayList<Double>(outputNeuronCount));
        }

        randomize(); // The American spelling really is terrible but standard
    }

    // Getters
    public ArrayList<Double> getInputs() {
        return inputs;
    }

    public ArrayList<Double> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<Double> outputs) {
        this.outputs = outputs;
    }

    public ArrayList<Double> getBiases() {
        return biases;
    }

    public ArrayList<ArrayList<Double>> getWeights() {
        return weights;
    }

    public int getInputCount() {
        return inputCount;
    }

    public void setInputCount(int inputCount) {
        this.inputCount = inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public void setOutputCount(int outputCount) {
        this.outputCount = outputCount;
    }

    // Methods
    private void randomize() {
        for (int i = 0; i < getInputCount(); i++) {
            // for each input create a weight for each output neuron
            for (int k = 0; k < getOutputCount(); k++) {
                // Generate a value between -1 and 1 for the weight
                weights.get(i).add(k, Math.random() * 2 - 1);
            }
        }

        for (int i = 0; i < getOutputCount(); i++) {
            biases.add(i, Math.random() * 2 - 1);
        }
    }

    public static ArrayList<Double> feedForward(ArrayList<Double> givenInputs, Level level) {
        // Clear out inputs and outputs before adding more data
        level.inputs.clear();
        level.outputs.clear();

        // Copy the given inputs to the inputs array
        for (double input : givenInputs) {
            level.inputs.add(input);
        }

        // Calculate outputs based on inputs, weights, and biases
        for (int i = 0; i < level.getOutputCount(); i++) {
            double sum = 0;

            for (int j = 0; j < level.getInputCount(); j++) {
                ArrayList<Double> currentWeight = level.weights.get(j);
                sum += level.inputs.get(j) * currentWeight.get(i);
            }

            if (sum >= level.biases.get(i)) { // Switch the output neuron on
                level.outputs.add(1.0);
            } else {
                // Switch the output neuron off as the product of the input
                // and weight wasn't enough to trigger it
                level.outputs.add(0.0);
            }
        }

        return level.outputs;
    }

}
