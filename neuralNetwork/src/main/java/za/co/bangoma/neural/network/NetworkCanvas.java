package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;
import za.co.bangoma.neural.road.RoadCanvas;
import za.co.bangoma.neural.road.car.Car;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class NetworkCanvas extends Canvas {

    // Constants
    private static final int WIDTH = 400;
    private static final int NETWORK_X = 360;
    private static final int NETWORK_Y = 0;
    private final int MARGIN = 50;
    private final int TIMER_DELAY_IN_MILLISECONDS = 1000 / 60; // Running at 60fps basically

    // Attributes
    private final int height;
    private BufferedImage offScreenImage;
    private RoadCanvas roadCanvas;
    private Timer timer;
    private NeuralNetwork brain;

    // Constructor
    public NetworkCanvas(int canvasHeight, RoadCanvas roadCanvas) {
        this.height = canvasHeight;
        this.roadCanvas = roadCanvas;

        // Creating and defining our canvas for our screen
        setBackground(Color.BLACK);
        setBounds(NETWORK_X, NETWORK_Y, WIDTH, canvasHeight);

        // Initialise the off-screen image
        offScreenImage = new BufferedImage(WIDTH, canvasHeight, BufferedImage.TYPE_INT_ARGB);

        // Start a timer to trigger repaints at regular intervals
        this.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBrainData(); // Update the brain data before each repaint
                repaint();
            }
        }, 0, TIMER_DELAY_IN_MILLISECONDS); // Repaint approximately 60 times per second
    }

    // Getter/s
    @Override
    public int getHeight() {
        return height;
    }

    // Methods
    private void updateBrainData() {
        // Get the neural network data from the roadCanvas
        brain = roadCanvas.getMyCar().getBrain();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Paint your neural network using data from the brain
        paintNetworkComponents(g2d, brain);
    }

    private static boolean isNetworkValid(NeuralNetwork brain) {
        ArrayList<Boolean> levelsExist = new ArrayList<>();

        for (Level level : brain.getLevels()) {
            // If our level has inputs "feeding" outputs,
            // along with relevant weights and biases it is possible to visualise.
            if (!level.getInputs().isEmpty() && !level.getOutputs().isEmpty()) {
                if (
                        level.getInputs().size() != 5 || // if not equal to our sensor array
                        level.getInputs().size() != 6 && // If not equal to second level's inputs
                        level.getOutputs().size() != 6 || // If first level outputs not equal to second level
                        level.getOutputs().size() != 4 // If second level outputs don't match our output commands length
                ) {
                    levelsExist.add(false); // Return false if sizes don't match
                } else {
                    levelsExist.add(true);
                }
            } else {
                levelsExist.add(false);
            }
        }

        boolean networkValid = !levelsExist.contains(false) && !levelsExist.isEmpty();
        return networkValid;
    }

    private void paintNetworkComponents(Graphics2D graphics2D, NeuralNetwork network) {
        boolean networkValid = isNetworkValid(brain);
        if (networkValid) {
            System.out.println("You've got it!");
            for (Level level: network.getLevels()) {
                System.out.println("My first input is: " + level.getInputs().get(0));
                System.out.println("My first output is: " + level.getOutputs().get(0));
                System.out.println("My first bias is: " + level.getBiases().get(0));
                System.out.println("My first weight is: " + level.getWeights().get(0).get(0));
            }
        }
        
        int left = MARGIN;
        int top = MARGIN;

        int width = WIDTH - MARGIN * 2;
        int height = this.height - MARGIN * 2;

        int levelHeight = height / network.getLevels().size();

        for (int i = network.getLevels().size() - 1; i >= 0; i--) {

            double levelTop = top + Utils.linearInterpolation(
                    height - levelHeight,
                    0,
                    network.getLevels().size() == 1 ? 0.5 : (double) i / (network.getLevels().size() - 1)
            );

            if (i == 0) { // TEMPORARY DEBUGGING PURPOSES
                drawLevel(
                        graphics2D,
                        network.getLevels().get(i),
                        left,
                        levelTop,
                        width,
                        levelHeight,
                        i == network.getLevels().size() - 1 ? new String[]{"⬆", "⬅", "➡", "⬇"} : new String[]{}
                );
            } else { // TEMPORARY DEBUGGING PURPOSES
                drawLevel(
                        graphics2D,
                        network.getLevels().get(i),
                        left,
                        levelTop,
                        width,
                        levelHeight,
                        i == network.getLevels().size() - 1 ? new String[]{"⬆", "⬅", "➡", "⬇"} : new String[]{}
                );
            }

        }
    }

    private void drawLevel(Graphics2D graphics, Level level, int left, double top, int width, int height, String[] strings) {
        int right = left + width;
        int bottom = (int) (top + height);

        ArrayList<Double> inputs = level.getInputs();
        ArrayList<Double> outputs = level.getOutputs();
        ArrayList<Double> biases = level.getBiases();
        // Value of our "neuron's 'axons'"
        ArrayList<ArrayList<Double>> weights = level.getWeights();

        int nodeRadius = 30; // ALso known as the "neuronal soma" radius,
        // (that's just my understanding, not common practice)

        if (!inputs.isEmpty() && !outputs.isEmpty()) {
            for (int i = 0; i < level.getInputCount(); i++) {
                for (int j = 0; j < level.getOutputCount(); j++) {

                    graphics.setColor(Utils.getRGBA(weights.get(i).get(j)));

                    int inputX = (int) getNodeX(inputs, i, left, right);
                    int outputX = (int) getNodeX(outputs, j, left, right);

                    drawDashedLine(
                            graphics,
                            inputX,
                            bottom,
                            outputX,
                            (int) top
                    );
                }
            }

            for (int i = 0; i < level.getInputCount(); i++) {
                double x = getNodeX(inputs, i, left, right);

                graphics.setColor(Color.BLACK);
                graphics.fillOval((int) x - (nodeRadius / 3), bottom - (nodeRadius / 3), nodeRadius, nodeRadius);

                if (inputs.size() > 1 && inputs.size() > i) {
                    graphics.setColor(Utils.getRGBA(inputs.get(i)));
                }
                graphics.fillOval((int) x - (nodeRadius / 3), bottom - (nodeRadius / 3), (int) (nodeRadius * 0.6), (int) (nodeRadius * 0.6));
            }

            for (int i = 0; i < level.getOutputCount(); i++) {
                double x = getNodeX(outputs, i, left, right);

                graphics.setColor(Color.BLACK);
                graphics.fillOval((int) x - (nodeRadius / 3), (int) top - (nodeRadius / 3), nodeRadius, nodeRadius);

                if (outputs.size() > 1 && outputs.size() > i) {
                    graphics.setColor(Utils.getRGBA(outputs.get(i)));
                }
                graphics.fillOval((int) x - (nodeRadius / 3), (int) top - (nodeRadius / 3), (int) (nodeRadius * 0.6), (int) (nodeRadius * 0.6));

                // Biases drawn as a contour around the output.
            }
        }
    }

    private void drawDashedLine(Graphics graphics, int x1, int y1, int x2, int y2) {
        // Create a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) graphics.create();

        // Set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        // Draw to the copy
        g2d.drawLine(x1, y1, x2, y2);
        // Get rid of the copy
        g2d.dispose();
    }

    private double getNodeX(ArrayList<Double> nodes, int index, int left, int right) {
        return Utils.linearInterpolation(
                left,
                right,
                nodes.size() == 1 ? 0.5 : (double) index / (nodes.size() - 1)
        );
    }

}
