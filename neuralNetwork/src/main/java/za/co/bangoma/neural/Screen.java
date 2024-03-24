package za.co.bangoma.neural;

// Importing Java AWT to start
import java.awt.*; // Abstract Window Toolkit


public class Screen extends Frame {

    Car myCar;

    final int WIDTH = 600;
    final int HEIGHT = 700;

    int CENTRE_X = WIDTH / 2;
    int CENTRE_Y = HEIGHT / 2;

    // Constructor
    public Screen() {

        // Creating a button
        Button actionButton = new Button("Start");

        // Setting my actionButton's position on screen, It's drawn from the bottom left.
        actionButton.setBounds(15, 45, 80, 30); // x pos, y pos, width and height in pixels.

        // We need to manually add the button to our frame.
        add(actionButton);

        this.myCar = new Car(CENTRE_X, CENTRE_Y, 30, 50, Color.BLUE); // Drawn from the top left

        // Set te width and height of our Frame/Screen
        setSize(WIDTH, HEIGHT);

        // Setting the title of our screen
        setTitle("Self Driving Car");

        // no layout Manager
        setLayout(null);

        // Let us manually set the visibility of our screen, the default is off
        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        // super.paint(g);
        myCar.paintCar(g); // Call the paintCar method here
    }

}
