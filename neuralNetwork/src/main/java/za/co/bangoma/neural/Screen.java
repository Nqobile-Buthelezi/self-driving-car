package za.co.bangoma.neural;


// Importing Java AWT to start
import java.awt.*;
import java.awt.event.*;

import za.co.bangoma.neural.network.NetworkCanvas;
import za.co.bangoma.neural.road.RoadCanvas;

/**
 * The Screen class represents the main graphical interface for the self-driving car simulation.
 * It extends the Frame class and contains components for controlling the simulation and displaying
 * visual elements.
 */
public class Screen extends Frame implements WindowListener {

    // Attributes
    RoadCanvas roadCanvas;
    NetworkCanvas networkCanvas;
    final int WIDTH = 775;
    final int HEIGHT = 720;


    // Constructor/s
    public Screen() {
        addWindowListener(this);
        initialiseComponents();
    }

    public Screen(boolean testEnvironment) {
        addWindowListener(this);
        if (testEnvironment) {
            roadCanvas = null;
            networkCanvas = null;
        } else {
            initialiseComponents();
        }
    }

    // Method/s

    /**
     * Initialises graphical components of the screen. 🏁
     */
    public void initialiseComponents() {
        // Creating a button
        Button actionButton = new Button("Start");
        Button saveButton = new Button("save");
        Button discardButton = new Button("discard");

        // Setting my actionButton's position on screen, It's drawn from the bottom left.
        actionButton.setBounds(15, 45, 80, 30); // x pos, y pos, width and height in pixels.
        saveButton.setBounds(15, 80, 80, 30);
        discardButton.setBounds(15, 115, 80, 30);

        // Setting Background colours of my buttons
        actionButton.setBackground(Color.YELLOW);
        saveButton.setBackground(Color.GREEN);
        discardButton.setBackground(Color.RED);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roadCanvas.startAnimation();
                networkCanvas.setVisible(true); // Show the network canvas when "start" is clicked
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roadCanvas.saveBestBrain();
            }
        });

        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roadCanvas.discardBestBrain();
            }
        });

        // We need to manually add the button/s to our frame.
        add(actionButton);
        add(saveButton);
        add(discardButton);

        // Adding RoadCanvas
        roadCanvas = new RoadCanvas(HEIGHT, 3);
        add(roadCanvas);

        // Adding the NetworkCanvas
        networkCanvas = new NetworkCanvas(HEIGHT, roadCanvas);
        networkCanvas.setVisible(false); // Initially hide the network canvas
        add(networkCanvas);

        // Set the width and height of our Frame/Screen
        setSize(WIDTH, HEIGHT);

        // Setting the title of our screen
        setTitle("Self Driving Car");

        // no layout Manager
        setLayout(null);

        // Let us manually set the visibility of our screen, the default is off
        setVisible(true);
    }

    /**
     * Overrides the paint method to ensure correct painting behavior.
     * @param g The Graphics object used for painting.
     */
    @Override
    public void paint(Graphics g) {
        super.setBackground(Color.BLACK);
        // We call the super classes (Frame) paint method here to ensure everything is painted correctly.
        super.paint(g);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
