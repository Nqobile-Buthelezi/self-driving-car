package za.co.bangoma.neural;

import java.awt.*;


public class Car implements Vehicle {

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

    @Override
    public void moveForward() {
        this.y--;
    }

    @Override
    public void move() {
        // If the upwards key is pressed accelerate or decelerate.
        if (this.controls.isForward()) {
            this.speed += this.acceleration;
        }
        if (this.controls.isBack()) {
            this.speed -= this.acceleration;
        }

        // Ensure once maximum speed is achieved it is not exceeded
        if (this.speed > this.maxSpeed) {
            this.speed = this.maxSpeed;
        }
        // If half of our maxSpeed is achieved while decelerating cap it a half maxSpeed
        if (this.speed < (double) -this.maxSpeed / 2) {
            this.speed = (double) -this.maxSpeed / 2;
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

        // If our speed is greater than zero we will implement turning
        // functionality. Ordinarily cars cannot turn unless they are in motion
        // The same rule will apply to our vehicle
        if (this.speed != 0) {
            // Convert angle to radians
            double angleInRadians = Math.toRadians(angle);

            // Update the position based on the angle and speed
            this.x += Math.sin(angleInRadians) * this.speed;
            this.y -= Math.cos(angleInRadians) * this.speed;

            int flip = this.speed > 0 ? 1 : -1;

            // Update the angle of the car based on the control inputs.
            if (this.controls.isLeft()) {
                this.angle += 0.3 * flip; // Positive angle for turning left
            }
            if (this.controls.isRight()) {
                this.angle -= 0.3 * flip; // negative angle for turning right
            }
        }
    }
}
