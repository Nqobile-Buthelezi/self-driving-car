package za.co.bangoma.neural.network;

import za.co.bangoma.neural.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


public class NetworkCanvas extends Canvas {

    // Constants
    private static final int WIDTH = 400;
    private static final int NETWORK_X = 360;
    private static final int NETWORK_Y = 0;
    private final int MARGIN = 50;

    // Attributes
    private final int height;

    // Constructor
    public NetworkCanvas(int canvasHeight) {
        this.height = canvasHeight;

        // Creating and defining our canvas for our screen
        setBackground(Color.BLACK);
        setBounds(NETWORK_X, NETWORK_Y, WIDTH, canvasHeight);
    }

    // Getter/s
    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void paint(Graphics g) {
        int left = MARGIN;
        int top = MARGIN;

        int width = WIDTH - MARGIN * 2;
        int height = this.height - MARGIN * 2;

        int levelHeight = Network.getLevels().size();

        for (int i = Network.getLevels().size() - 1; i >= 0; i--) {
            double levelTop = top + Utils.linearInterpolation(
                    height - levelHeight,
                    0,
                    Network.getLevels().size() == 1 ? 0.5 : (double) i / (Network.getLevels().size() - 1)
            );

            drawDashedLine(g, 0, 0, 0, 0);

            drawLevel(
                    Network.getLevels().get(i),
                    left,
                    levelTop,
                    width,
                    levelHeight,
                    i == Network.getLevels().size() - 1 ? new String[] {"⬆", "⬅", "➡", "⬇"} : new String[] {}
            );
        }

    }

    // Methods
    private void drawLevel(Level level, int left, double top, int width, int height, String[] strings) {
        int right = left + width;
        int bottom = (int) (top + height);

        ArrayList<Double> inputs = level.getInputs();
        ArrayList<Double> outputs = level.getOutputs();
        ArrayList<Double> biases = level.getBiases();
        ArrayList<ArrayList<Object>> weights = level.getWeights(); // Value of our "neuronal axons"

        int nodeRadius = 18; // ALso known as the "neuronal soma" radius
    }

    private void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2) {
        // Create a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) g.create();
        // Set the stroke of the copy, not the original
        Stroke dashed = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        // Draw to the copy
        g2d.drawLine(x1, y1, x2, y2);
        // Get rid of the copy
        g2d.dispose();
    }

}
