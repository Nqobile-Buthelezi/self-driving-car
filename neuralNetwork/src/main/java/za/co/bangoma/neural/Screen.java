package za.co.bangoma.neural;

// Importing Java AWT to start
import java.awt.*; // Abstract Window Toolkit
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Screen extends Frame {

    RoadCanvas roadCanvas;
    final int WIDTH = 600;
    final int HEIGHT = 700;


    // Constructor
    public Screen() {

        // Creating a button
        Button actionButton = new Button("Start");

        // Setting my actionButton's position on screen, It's drawn from the bottom left.
        actionButton.setBounds(15, 45, 80, 30); // x pos, y pos, width and height in pixels.

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roadCanvas.startAnimation();
            }
        });

        // We need to manually add the button to our frame.
        add(actionButton);

        // Adding RoadCanvas
        roadCanvas = new RoadCanvas(HEIGHT, 3);
        add(roadCanvas);

        // Set the width and height of our Frame/Screen
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
        // We call the super classes (Frame) paint method here to ensure everything is painted correctly.
        super.paint(g);
    }

}
