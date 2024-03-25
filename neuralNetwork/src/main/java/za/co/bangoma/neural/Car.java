package za.co.bangoma.neural;

import java.awt.*;
import java.awt.geom.AffineTransform;


public class Car implements Vehicle, Drawable {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Color color; // It should be colour but it's American spelling 😅
    private Controls controls;

    private int maxSpeed;
    private double speed = 0;
    private double acceleration = 0.2;
    private double friction = 0.05;
    private double angle = 0;


    public Car(int x, int y, int width, int height, Color color, int maxSpeed, String controlType) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.width = width;
        this.height = height;
        this.color = color;
        this.maxSpeed = maxSpeed;
        if (controlType.equals("CONTROL")) {
            this.controls = new Controls();
        }
    }

    // Getters
    public Controls getControls() {
        return controls;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public double getAngle() {
        return angle;
    }

    @Override
    public void moveForward() {
        this.y--;
    }

    @Override
    public void move() {
        updateSpeed();
        updateAngle();
        updatePosition();
    }

    private void updateAngle() {
        if (this.speed != 0) {
            // Calculate turning radius based on speed
            double turningRadius = 100 / this.speed; // Adjust this value as needed for gameplay balance

            // Calculate angle change based on turning radius
            double angleChange = Math.atan(this.width / turningRadius);

            if (this.controls.isLeft()) {
                this.angle += angleChange; // Turn left
            }
            if (this.controls.isRight()) {
                this.angle -= angleChange; // Turn right
            }
        }
    }

    private void updatePosition() {
        // Convert angle to radians
        double angleInRadians = Math.toRadians(angle);
        // Calculate change in position based on angle and speed
        double deltaX = this.speed * Math.sin(angleInRadians);
        double deltaY = this.speed * Math.cos(angleInRadians);
        // Update the position
        this.x -= deltaX;
        this.y += deltaY; // Note the positive increment in y due to AWT's coordinate system
    }


    private void updateSpeed() {
        // If the upwards key is pressed accelerate or decelerate.
        if (this.controls.isForward()) {
            this.speed -= this.acceleration;
        }
        if (this.controls.isBack()) {
            this.speed += this.acceleration;
        }

        // Ensure once maximum speed is achieved it is not exceeded
        if (this.speed < -this.maxSpeed) {
            this.speed = -this.maxSpeed;
        }
        // If half of our maxSpeed is achieved while decelerating cap it a half maxSpeed
        if (this.speed > (double) this.maxSpeed / 2) {
            this.speed = (double) this.maxSpeed / 2;
        }

        // If our speed increases or decreases let us implement friction in bot directions.
        if (this.speed > 0) {
            this.speed -= this.friction;
        }
        if (this.speed < 0) {
            this.speed += this.friction;
        }

        // Ensure our car stays stationary in the event our speed
        // is not yet greater than the friction applied.
        if (Math.abs(this.speed) < this.friction) {
            this.speed = 0;
        }
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(this.x, this.y); // Translate to the car's position
        g2d.rotate(Math.toRadians(this.angle), (double) this.width / 2, (double) this.height / 2); // Rotate around the center of the car
        g2d.setColor(this.color);
        g2d.fillRect(0, 0, this.width, this.height); // Draw the car
        g2d.setTransform(originalTransform);
    }

}
