package za.co.bangoma.neural;

import java.awt.*;


public class Car {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color; // It should be colour but it's American spelling 😅

    public Car(int x, int y, int width, int height, Color color) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

}
