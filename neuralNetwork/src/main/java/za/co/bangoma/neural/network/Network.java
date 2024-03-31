package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;

import java.util.ArrayList;

public class Network {

    // Attributes
    private static ArrayList<Level> levels;

    // Constructor
    public Network(Integer[] neuronCounts) {
        levels = new ArrayList<>();

        for (int i = 0; i < neuronCounts.length - 1; i++) {
            levels.add(new Level(
                    neuronCounts[i],
                    neuronCounts[i + 1]
            ));
        }
    }

    // Getter/s
    public static ArrayList<Level> getLevels() {
        return levels;
    }

    // Method/s
    private static ArrayList<Double> feedForward(ArrayList<Double> givenInputs, Network network) {
        ArrayList<Double> outputs = Level.feedForward(
                givenInputs,
                network.getLevels().get(0)
        );

        for (int i = 1; i < network.getLevels().size(); i++) {
            outputs = Level.feedForward(
                    outputs,
                    network.getLevels().get(0)
            );
        }

        return outputs;
    }

    private static void mutate(Network network, int amount) {
        ArrayList<Level> currentLevels = network.getLevels();

        for (Level level: currentLevels) {
            for (int i = 0; i < level.biases.size(); i++) {
                level.biases.add(
                        i,
                        Utils.linearInterpolation(
                                level.biases.get(i),
                                Math.random() * 2 - 1,
                                amount
                        )
                        );
            }
            for (int i = 0; i < level.weights.size(); i++) {
                for (int j = 0; j < level.weights.get(i).size(); j++) {
                    level.weights.get(i).add(
                            j,
                            Utils.linearInterpolation(
                                    (double) level.weights.get(i).get(j),
                                    Math.random() * 2 - 1,
                                    amount
                            )
                            );
                }
            }
        }
    }
}
