package za.co.bangoma.neural;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;


public class Car implements Vehicle, Drawable {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Color color; // It should be colour but it's American spelling 😅
    private Controls controls;
    private Sensor sensor;

    private int maxSpeed;
    private double speed = 0;
    private double acceleration = 0.2;
    private double friction = 0.05;
    private double angle = 0;
    private Point[] roadBorders;
    private Car[] traffic;
    private Point[] polygon;


    public Car(int x, int y, int width, int height, Color color, int maxSpeed, String controlType, Point[] roadBorders, Car[] traffic) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.width = width;
        this.height = height;
        this.color = color;
        this.maxSpeed = maxSpeed;
        this.roadBorders = roadBorders;
        this.traffic = traffic;
        this.polygon = createPolygon();

        if (controlType.equals("CONTROL")) {
            this.controls = new Controls();
            this.sensor = new Sensor(this);
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

    public Point[] getPolygon() {
        return this.polygon;
    }

    // Methods
    // Method to create polygon points
    private Point[] createPolygon() {
        ArrayList<Point> points = new ArrayList<>();

        double radius = Math.hypot(this.width, this.height) / 2; // "radius" distance from centre to corner
        double alpha = Math.atan2(this.width, this.height); // Makes use of the arc tangent method to identify the angle of the "radius"

        // Top right point
        int topRightX = (int) (this.x - Math.sin(this.angle - alpha) * radius);
        int topRightY = (int) (this.y - Math.cos(this.angle - alpha) * radius);
        points.add(new Point(topRightX, topRightY));

        // Top left point
        int topLeftX = (int) (this.x - Math.sin(this.angle + alpha) * radius);
        int topLeftY = (int) (this.y - Math.cos(this.angle + alpha) * radius);
        points.add(new Point(topLeftX, topLeftY));

        // Bottom left point
        int bottomLeftX = (int) (this.x - Math.sin(Math.PI + this.angle - alpha) * radius);
        int bottomLeftY = (int) (this.y - Math.cos(Math.PI + this.angle - alpha) * radius);
        points.add(new Point(bottomLeftX, bottomLeftY));

        // Bottom right point
        int bottomRightX = (int) (this.x - Math.sin(Math.PI + this.angle + alpha) * radius);
        int bottomRightY = (int) (this.y - Math.cos(Math.PI + this.angle + alpha) * radius);
        points.add(new Point(bottomRightX, bottomRightY));

        // Convert ArrayList<Point> to Point[]
        return points.toArray(new Point[0]);
    }

    private void updateAngle() {
        if (this.speed != 0) {
            // Calculate turning radius based on speed
            // double turningRadius = 100 / this.speed; // Adjust this value as needed for gameplay balance
            double turningRadius = -(Math.PI / 2);

            // Calculate angle change based on turning radius
            double angleChange = Math.atan(this.width / turningRadius);

            // Log the current angle before adjustments
            // System.out.println("Current Angle: " + this.angle);

            // Adjust angle based on controls
            if (this.controls.isLeft()) {
                this.angle += angleChange; // Turn left
            }
            if (this.controls.isRight()) {
                this.angle -= angleChange; // Turn right
            }

            // Ensure angle stays within 0 to 360 degrees range
            if (this.angle >= 360) {
                this.angle -= 360;
            } else if (this.angle < 0) {
                this.angle += 360;
            }

            // Log the updated angle
            // System.out.println("Updated Angle: " + this.angle);
        }

        // Update sensor angles
        if (this.sensor != null) {
            this.sensor.updateAngles(this.angle);
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
    public void moveForward() {
        this.y--;
    }

    @Override
    public void move() {
        updateSpeed();
        updateAngle();
        updatePosition();
        if (sensor != null) {
            sensor.update(this.roadBorders, this.traffic); // You need to pass roadBorders and traffic here
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

//        if (this.sensor != null && !this.sensor.getRays().isEmpty()) {
//            this.sensor.drawSensors(g2d);
//        }
    }

}
