package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;

import java.util.ArrayList;

public class NeuralNetwork {

    // Attributes
    private static ArrayList<Level> levels;

    // Constructor
    public NeuralNetwork(Integer[] neuronCounts) {
        levels = new ArrayList<>();
        createLevels(neuronCounts);
    }

    // Getter/s
    public static ArrayList<Level> getLevels() {
        return levels;
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
        ArrayList<Double> outputs = Level.feedForward(
                givenInputs,
                neuralNetwork.getLevels().get(0)
        );

        for (int i = 1; i < neuralNetwork.getLevels().size(); i++) {
            outputs = Level.feedForward(
                    outputs,
                    neuralNetwork.getLevels().get(0)
            );
        }

        return outputs;
    }

    private static void mutate(NeuralNetwork neuralNetwork, int amount) {
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
    }
}
