package za.co.bangoma.neural;

import java.awt.*;


public class NetworkCanvas extends Canvas {

    private static final int WIDTH = 400;
    private static final int NETWORK_X = 360;
    private static final int NETWORK_Y = 0;

    public NetworkCanvas(int canvasHeight) {

        setBackground(Color.BLACK);
        setBounds(NETWORK_X, NETWORK_Y, WIDTH, canvasHeight);

    }

}
