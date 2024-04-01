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
            // this.weights.get(i).add(i, new ArrayList<>(outputNeuronCount));
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
        for (int i = 0; i < level.inputs.size(); i++) {
            level.inputs.add(i, givenInputs.get(i));
        }

        for (int i = 0; i < level.outputs.size(); i++) {
            double sum = 0;

            for (int j = 0; j < level.inputs.size(); j++) {
                ArrayList<Double> currentWeight = level.weights.get(j);
                sum += level.inputs.get(i) * (double) currentWeight.get(i);
            }

            if (sum >= level.biases.get(i)) { // Switch the output neuron on
                level.outputs.add(i, 1.0);
            } else {
                // Switch the output neuron off as the product of the input
                // and weight wasn't enough to trigger it
                level.outputs.add(i, 0.0);
            }
        }

        return level.outputs;
    }
}
