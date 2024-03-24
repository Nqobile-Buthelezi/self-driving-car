package za.co.bangoma.neural;

import java.awt.*;;


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

    public void paintCar(Graphics g) {
        System.out.println("The color of the car is " + this.color);
        g.setColor(this.color);
        // g.drawRect(this.x, this.y, this.width, this.height);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

}
