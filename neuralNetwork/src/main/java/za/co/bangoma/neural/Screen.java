package za.co.bangoma.neural;


// Importing Java AWT to start
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import za.co.bangoma.neural.network.NetworkCanvas;
import za.co.bangoma.neural.road.RoadCanvas;


public class Screen extends Frame {

    RoadCanvas roadCanvas;
    NetworkCanvas networkCanvas;
    final int WIDTH = 775;
    final int HEIGHT = 720;


    // Constructor/s
    public Screen() {
        initialiseComponents();
    }

    public Screen(boolean testEnvironment) {
        if (testEnvironment) {
            roadCanvas = null;
            networkCanvas = null;
        } else {
            initialiseComponents();
        }
    }

    // Method/s
    public void initialiseComponents() {
        // Creating a button
        Button actionButton = new Button("Start");
        Button saveButton = new Button("save");
        Button discardButton = new Button("discard");

        // Setting my actionButton's position on screen, It's drawn from the bottom left.
        actionButton.setBounds(15, 45, 80, 30); // x pos, y pos, width and height in pixels.
        saveButton.setBounds(15, 80, 80, 30);
        discardButton.setBounds(15, 115, 80, 30);

        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roadCanvas.startAnimation();
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

        // We need to manually add the button to our frame.
        add(actionButton);
        add(saveButton);
        add(discardButton);

        // Adding RoadCanvas
        roadCanvas = new RoadCanvas(HEIGHT, 3);
        add(roadCanvas);

        networkCanvas = new NetworkCanvas(HEIGHT, roadCanvas);
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

    @Override
    public void paint(Graphics g) {
        // We call the super classes (Frame) paint method here to ensure everything is painted correctly.
        super.paint(g);
    }

}
