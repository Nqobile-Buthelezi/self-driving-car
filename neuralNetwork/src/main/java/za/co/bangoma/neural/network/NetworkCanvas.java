package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class NetworkCanvas extends Canvas {

    // Constants
    private static final int WIDTH = 400;
    private static final int NETWORK_X = 360;
    private static final int NETWORK_Y = 0;
    private final int MARGIN = 50;

    // Attributes
    private final int height;
    private BufferedImage offScreenImage;

    // Constructor
    public NetworkCanvas(int canvasHeight) {
        this.height = canvasHeight;

        // Creating and defining our canvas for our screen
        setBackground(Color.BLACK);
        setBounds(NETWORK_X, NETWORK_Y, WIDTH, canvasHeight);

        // Initialize the off-screen image
        offScreenImage = new BufferedImage(WIDTH, canvasHeight, BufferedImage.TYPE_INT_ARGB);
    }

    // Getter/s
    @Override
    public int getHeight() {
        return height;
    }

    // Methods
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Create off-screen graphics context
        Graphics2D offScreenGraphics = offScreenImage.createGraphics();

        offScreenGraphics.setColor(Color.BLUE);
        offScreenGraphics.fillOval(25, 50, 50, 50);

        // Paint everything onto the off-screen image
        paintNetWorkComponents(offScreenGraphics);

        // Draw the off-screen image onto the canvas
        g.drawImage(offScreenImage, 0, 0, null);

        // Dispose the off-screen graphics context
        offScreenGraphics.dispose();

    }

    private void paintNetWorkComponents(Graphics2D graphics2D) {

        int left = MARGIN;
        int top = MARGIN;

        int width = WIDTH - MARGIN * 2;
        int height = this.height - MARGIN * 2;

        int levelHeight = height / NeuralNetwork.getLevels().size();

        for (int i = NeuralNetwork.getLevels().size() - 1; i >= 0; i--) {

            double levelTop = top + Utils.linearInterpolation(
                    height - levelHeight,
                    0,
                    NeuralNetwork.getLevels().size() == 1 ? 0.5 : (double) i / (NeuralNetwork.getLevels().size() - 1)
            );

            drawLevel(
                    graphics2D,
                    NeuralNetwork.getLevels().get(i),
                    left,
                    levelTop,
                    width,
                    levelHeight,
                    i == NeuralNetwork.getLevels().size() - 1 ? new String[] {"⬆", "⬅", "➡", "⬇"} : new String[] {}
            );
        }
    }

    // Methods
    private void drawLevel(Graphics2D graphics, Level level, int left, double top, int width, int height, String[] strings) {
        int right = left + width;
        int bottom = (int) (top + height);

        graphics.setColor(Color.RED);
        graphics.fillOval(75, 50, 50, 50);

        ArrayList<Double> inputs = level.getInputs();
        ArrayList<Double> outputs = level.getOutputs();
        ArrayList<Double> biases = level.getBiases();
        // Value of our "neuron's 'axons'"
        ArrayList<ArrayList<Double>> weights = level.getWeights();

        int nodeRadius = 18; // ALso known as the "neuronal soma" radius,
        // (that's just my understanding, not common practice)

        for (int i = 0; i < level.getInputCount(); i++) {
            for (int j = 0; j < level.getOutputCount(); j++) {

                graphics.setColor(Utils.getRGBA(weights.get(i).get(j)));

                drawDashedLine(
                    graphics,
                        (int) getNodeX(inputs, i, left, right),
                        bottom,
                        (int) getNodeX(outputs, j, left, right),
                        (int) top
                );
            }
        }

        for (int i = 0; i < level.getInputCount(); i++) {
            double x = getNodeX(inputs, i, left, right);

            graphics.setColor(Color.BLACK);
            graphics.fillOval((int) x, bottom, nodeRadius, nodeRadius);

            graphics.setColor(Utils.getRGBA(inputs.get(i)));
            graphics.fillOval((int) x, bottom, (int) (nodeRadius * 0.6), (int) (nodeRadius * 0.6));
        }

        for (int i = 0; i < level.getOutputCount(); i++) {
            double x = getNodeX(outputs, i, left, right);

            graphics.setColor(Color.BLACK);
            graphics.fillOval((int) x, (int) top, nodeRadius, nodeRadius);

            graphics.setColor(Utils.getRGBA(outputs.get(i)));
            graphics.fillOval((int) x, (int) top, (int) (nodeRadius * 0.6), (int) (nodeRadius * 0.6));

            // Biases drawn as a contour around the output.
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
