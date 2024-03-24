package za.co.bangoma.neural;


import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


public class RoadCanvas extends Canvas {

    // Width of my canvas is 200 pixels
    public static final int WIDTH = 200;
    // Set timer of the canvas to be 30fps
    private static final int TIMER_DELAY_IN_MILLISECONDS = 1000 / 30;
    private static final int STARTING_X = WIDTH / 2;

    private Timer timer;
    private Car myCar;
    private Car[] traffic;

    public RoadCanvas(int canvasSize) {
        setBackground(Color.BLACK);
        // setSize(WIDTH, canvasSize);
        setBounds(110, 0, WIDTH, canvasSize);

        int starting_y = canvasSize / 2;

        // Adding our car object. Drawn from the top left corner
        myCar = new Car(STARTING_X, starting_y, 30, 50, Color.BLUE);

        traffic = new Car[] {
                new Car(STARTING_X - 40, starting_y, 30, 50, Color.RED),
                new Car(STARTING_X + 40, starting_y, 30, 50, Color.RED)
        };

        // Simulate traffic
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Car car: traffic) {
                    int y = car.getY();
                    if (y > 0) {
                        car.moveForward(); // We move the square up by decrementing on the y-axis
                        repaint();
                    } else {
                        stopAnimation();
                    }
                }
            }
        }, 0, TIMER_DELAY_IN_MILLISECONDS); // Schedule the task to run 30fps
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // paint the boundary lines
        g.setColor(Color.YELLOW);
        g.fillRect((int) (getWidth() * 0.02), 0, (int) (getWidth() * 0.96), getHeight());

        // Paint the road
        g.setColor(Color.BLACK);
        g.fillRect((int) (getWidth() * 0.05), 0, (int) (getWidth() * 0.9), getHeight());

        // Paint the car
        g.setColor(myCar.getColor());
        g.fillRect(myCar.getX(), myCar.getY(), myCar.getWidth(), myCar.getHeight());

        // Paint the traffic
        for (Car car: this.traffic) {
            // Paint the car
            g.setColor(car.getColor());
            g.fillRect(car.getX(), car.getY(), car.getWidth(), car.getHeight());
        }
    }

    private void stopAnimation() {
        timer.cancel();
    }

}
