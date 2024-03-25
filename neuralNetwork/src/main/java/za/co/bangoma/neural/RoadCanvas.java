package za.co.bangoma.neural;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class RoadCanvas extends Canvas implements KeyListener {

    // Width of my canvas is 200 pixels
    public static final int WIDTH = 200;
    // Set timer of the canvas to be 30fps
    private static final int TIMER_DELAY_IN_MILLISECONDS = 1000 / 66;
    private static final int ROAD_X = 110;
    private static final int ROAD_Y = 0;

    private Timer timer;
    private Car myCar;
    private Car[] traffic;
    private int laneCount;
    private int left;
    private ArrayList<Drawable> drawables;
    private BufferedImage offScreenImage;

    public RoadCanvas(int canvasSize, int laneCount) {
        setBackground(Color.BLACK);
        setBounds(ROAD_X, ROAD_Y, WIDTH, canvasSize);

        int starting_y = canvasSize / 2;

        this.laneCount = laneCount;

        this.left = (int) ((WIDTH - WIDTH * 0.9) / 2);
        int right = (int) (WIDTH * 0.9 + (WIDTH * 0.1 / 2));

        // Adding our car object. Drawn from the top left corner
        myCar = new Car(getLaneCentre(1), starting_y, 30, 50, Color.BLUE, 3, "CONTROL");

        traffic = new Car[] {
                new Car(getLaneCentre(0), starting_y, 30, 50, Color.RED, 2, "TRAFFIC"),
                new Car(getLaneCentre(2), starting_y, 30, 50, Color.RED, 2, "TRAFFIC")
        };

        this.drawables = new ArrayList<>();
        this.drawables.add(myCar);
        this.drawables.addAll(Arrays.asList(traffic));

        // Adding keyboard listeners to the canvas, while implementing KeyListener
        addKeyListener(this);

        // Add off-screen image with the same dimensions as the canvas
        offScreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Create off-screen graphics context
        Graphics2D offScreenGraphics = offScreenImage.createGraphics();

        // Paint everything onto the off-screen image
        paintComponents(offScreenGraphics);

        // Draw the off-screen image onto the canvas
        g.drawImage(offScreenImage, 0, 0, null);

        // Dispose the off-screen graphics context
        offScreenGraphics.dispose();
    }

    public void startAnimation() {
        // Simulate traffic
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
                setFocusable(true); // Ensure the canvas can receive keyboard focus

                myCar.move();

                for (Car vehicle: traffic) {
                    int y = vehicle.getY();
                    if (true) {
                        vehicle.moveForward(); // We move the square up by decrementing on the y-axis
                    } else {
                        stopAnimation();
                    }
                }

            }
        }, 0, TIMER_DELAY_IN_MILLISECONDS); // Schedule the task to run 30fps
    }

    private void paintComponents(Graphics2D g2d) {
        // paint the boundary lines
        g2d.setColor(Color.YELLOW);
        // Drawn from top left
        g2d.fillRect((int) (getWidth() * 0.02), 0, (int) (getWidth() * 0.96), getHeight());

        // Paint the road
        g2d.setColor(Color.BLACK);
        // Drawn from top left
        g2d.fillRect((int) (getWidth() * 0.05), 0, (int) (getWidth() * 0.9), getHeight());

        // Paint the lanes
        g2d.setColor(Color.WHITE);
        int laneWidth = (int) ((WIDTH * 0.9) / laneCount);
        int left = (int) (WIDTH * 0.05);
        for (int i = 0; i < laneCount - 1; i++) {
            drawDashedLine(g2d, laneWidth + left + laneWidth * i, 0, laneWidth + left + laneWidth * i, getHeight());
        }

        // Paint all drawables
        for (Drawable drawable : this.drawables) {
            drawable.paint(g2d);
        }
    }

    private void stopAnimation() {
        timer.cancel();
    }

    private int getLaneCentre(int laneIndex) {
        // Get the width of a lane based on the road width
        int laneWidth = (int) (WIDTH * 0.9 / this.laneCount);
        // Returns the centre on the lane selected counting from the left side
        // Ensures that if the laneIndex provided exceeds the lane's the last lane is chosen.
        return this.left + laneWidth / 2 + Math.min(laneIndex, this.laneCount - 1) * laneWidth;
    }

    public void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2){
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

    @Override
    public void keyTyped(KeyEvent e) {
        // Not really going to be used here
    }

    @Override
    public void keyPressed(KeyEvent e) {
        myCar.getControls().keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        myCar.getControls().keyReleased(e);
    }
}
