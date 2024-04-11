package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;

import java.util.ArrayList;

public class NeuralNetwork {

    // Attributes
    private ArrayList<Level> levels;
    private static ArrayList<Double> outputs;

    // Constructor
    public NeuralNetwork(Integer[] neuronCounts) {
        levels = new ArrayList<>();
        createLevels(neuronCounts);
    }

    // Getter/s
    public ArrayList<Level> getLevels() {
        return levels;
    }

    public ArrayList<Double> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(ArrayList<Double> outputs) {
        this.outputs = outputs;
    }

    // Method/s
    private void createLevels(Integer[] neuronCounts) {
        for (int i = 0; i < neuronCounts.length - 1; i++) {
            levels.add(new Level(
                    neuronCounts[i],
                    neuronCounts[i + 1]
            ));
        }
    }

    public static ArrayList<Double> feedForward(ArrayList<Double> givenInputs, NeuralNetwork neuralNetwork) {
        ArrayList<Double> networkOutputs;

        networkOutputs = Level.feedForward(
                givenInputs,
                neuralNetwork.getLevels().get(0)
        );

        for (int i = 1; i < neuralNetwork.getLevels().size(); i++) {
            networkOutputs = Level.feedForward(
                    networkOutputs,
                    neuralNetwork.getLevels().get(i)
            );
        }

        neuralNetwork.setOutputs(networkOutputs);

        return networkOutputs;
    }

    public static NeuralNetwork mutate(NeuralNetwork neuralNetwork, double amount) {
        ArrayList<Level> currentLevels = neuralNetwork.getLevels();

        for (Level level: currentLevels) {
            ArrayList<Double> biases = level.getBiases();
            ArrayList<ArrayList<Double>> weights = level.getWeights();

            for (int i = 0; i < biases.size(); i++) {
                biases.set(
                        i,
                        Utils.linearInterpolation(
                                biases.get(i),
                                Math.random() * 2 - 1,
                                amount
                        )
                        );
            }

            for (ArrayList<Double> weightList : weights) {
                for (int j = 0; j < weightList.size(); j++) {
                    weightList.set(j, Utils.linearInterpolation(
                            weightList.get(j),
                            Math.random() * 2 - 1,
                            amount
                    ));
                }
            }
        }

        return neuralNetwork;
    }

    // Add a method to create a copy of the neural network
    public NeuralNetwork copy() {
        NeuralNetwork copy = new NeuralNetwork(new Integer[0]); // Empty constructor call
        ArrayList<Level> copiedLevels = new ArrayList<>();

        for (Level level : levels) {
            copiedLevels.add(level.copy()); // Copy each level
        }

        copy.levels = copiedLevels; // Assign the copied levels to the new network

        return copy;
    }

}
