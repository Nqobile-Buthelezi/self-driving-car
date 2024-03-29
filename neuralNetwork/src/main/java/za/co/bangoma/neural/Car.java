package za.co.bangoma.neural;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;


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
    private ArrayList<Point[]> roadBorders;
    private Car[] traffic;
    private ArrayList<Point> polygon;
    private String controlType;


    public Car(int x, int y, int width, int height, Color color, int maxSpeed, String controlType, ArrayList<Point[]> roadBorders, Car[] traffic) {
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.width = width;
        this.height = height;
        this.color = color;
        this.maxSpeed = maxSpeed;
        this.roadBorders = roadBorders;
        this.traffic = traffic;
        this.controlType = controlType;
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

    public int getY() {
        return y;
    }

    // Method/s
    private ArrayList<Point> createPolygon() {
        ArrayList<Point> points = new ArrayList<>();

        // Calculate the initial points relative to the center of the car
        double halfWidth = this.width / 2.0;
        double halfHeight = this.height / 2.0;

        // Calculate the initial center point of the car's bounding box
        int centerX = this.x + this.width / 2;
        int centerY = this.y + this.height / 2;

        System.out.println("Center before rotation - X: " + centerX + ", Y: " + centerY);

        Point topRight = new Point((int) (halfWidth + halfWidth), (int) (-halfHeight + halfHeight));
        Point topLeft = new Point((int) (-halfWidth + halfWidth), (int) (-halfHeight + halfHeight));
        Point bottomLeft = new Point((int) (-halfWidth + halfWidth), (int) (halfHeight + halfHeight));
        Point bottomRight = new Point((int) (halfWidth + halfWidth), (int) (halfHeight + halfHeight));

        // Rotate each point around the center of the car by the angle of rotation
        AffineTransform rotation = AffineTransform.getRotateInstance(Math.toRadians(this.angle), halfWidth, halfHeight);

        rotation.transform(topRight, topRight);
        rotation.transform(topLeft, topLeft);
        rotation.transform(bottomLeft, bottomLeft);
        rotation.transform(bottomRight, bottomRight);

        // Translate the points to their absolute positions on the screen
        topRight.translate(this.x, this.y);
        topLeft.translate(this.x, this.y);
        bottomLeft.translate(this.x, this.y);
        bottomRight.translate(this.x, this.y);

        // Add the points to the list
        points.add(topRight);
        points.add(topLeft);
        points.add(bottomLeft);
        points.add(bottomRight);

        if (this.controlType.equals("CONTROL")) {
            System.out.println("The top right x coordinate is: " + topRight.x);
            System.out.println("The top right y coordinate is: " + topRight.y);
            System.out.println("The top left x coordinate is: " + topLeft.x);
            System.out.println("The top left y coordinate is: " + topLeft.y);
            System.out.println("The bottom left x coordinate is: " + bottomLeft.x);
            System.out.println("The bottom left y coordinate is: " + bottomLeft.y);
            System.out.println("The bottom right x coordinate is: " + bottomRight.x);
            System.out.println("The bottom right y coordinate of is: " + bottomRight.y);
        }

        return points;
    }


    private void updateAngle() {
        if (this.speed != 0) {
            // Adjust this value as needed for gameplay balance
            double turningRadius = -(Math.PI / 2);

            // Calculate angle change based on turning radius
            double angleChange = Math.atan(this.width / turningRadius);

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
        this.y += deltaY; // Note the positive increment in y due to AWT's coordinate sytem
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
        this.polygon = createPolygon();
        updatePosition();
    }

    @Override
    public void paint(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();
        // Translate to the car's position
        g2d.translate(this.x + this.width / 2, this.y + this.height / 2);
        // Rotate around the center of the car's bounding box
        g2d.rotate(Math.toRadians(this.angle), 0, 0); // Rotate around (0, 0)

        g2d.setColor(this.color);
        // Draw the car
        g2d.fillRect(-this.width / 2, -this.height / 2, this.width, this.height); // Draw centered at (0, 0)

        g2d.setTransform(originalTransform);

        ArrayList<Color> myColours = new ArrayList<>();
        myColours.add(Color.RED);
        myColours.add(Color.YELLOW);
        myColours.add(Color.BLUE);
        myColours.add(Color.GREEN);

        for (int i = 0; i < polygon.size(); i++) {
            Point currentPoint = polygon.get(i);

            g2d.setColor(myColours.get(i));

            if (i == polygon.size() - 1) {
                g2d.drawLine(
                        (int) currentPoint.getX(),
                        (int) currentPoint.getY(),
                        (int) polygon.get(0).getX(),
                        (int) polygon.get(0).getY()
                );
            } else {
                g2d.drawLine(
                        (int) currentPoint.getX(),
                        (int) currentPoint.getY(),
                        (int) polygon.get(i + 1).getX(),
                        (int) polygon.get(i + 1).getY()
                );
            }
        }
    }


}
