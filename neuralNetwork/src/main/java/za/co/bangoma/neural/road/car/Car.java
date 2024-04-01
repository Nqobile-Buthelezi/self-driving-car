package za.co.bangoma.neural.road.car;

import za.co.bangoma.neural.*;
import za.co.bangoma.neural.network.NeuralNetwork;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<Point[]> roadBorders;
    private Car[] traffic;
    private ArrayList<Point> polygon;
    private String controlType;
    private boolean damaged = false;
    private NeuralNetwork brain;


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
            this.sensor.update(this.roadBorders, this.traffic);
            this.brain = new NeuralNetwork(new Integer[] {this.sensor.getRayCount(), 6, 4});
        }
    }

    // Getters
    public Controls getControls() {
        return controls;
    }

    public double getAngle() {
        return angle;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public ArrayList<Point> getPolygon() {
        return polygon;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Method/s
    private boolean assessDamage() {
        for (int i = 0; i < roadBorders.size(); i++) {
            if (Utils.carAndBorderIntersect(this.polygon, roadBorders.get(i))) {
                return true;
            }
        }

        for (int i = 0; i < traffic.length; i++) {
            if (Utils.carAndTrafficIntersect(this.polygon, traffic[i].polygon)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point> createPolygon() {
        ArrayList<Point> points = new ArrayList<>();

        // Calculate the initial points relative to the center of the car
        double halfWidth = this.width / 2.0;
        double halfHeight = this.height / 2.0;

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

        return points;
    }

    private void stop() {
        this.speed = 0;
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

    private void paintCarAndSensor(Graphics2D g2d) {
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

        // Define the desired thickness of the lines
        float strokeWidth = 2.0f; // You can adjust this value to increase or decrease the thickness

        // Create a new BasicStroke with the desired thickness
        Stroke oldStroke = g2d.getStroke(); // Save the old stroke to restore it later
        g2d.setStroke(new BasicStroke(strokeWidth));

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

        g2d.setStroke(oldStroke);

        // Draw the sensor
        if (this.controlType.equals("CONTROL")) {
            this.sensor.draw(g2d);
        }
    }

    private void paintDamagedCar(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();
        // Translate to the car's position
        g2d.translate(this.x + this.width / 2, this.y + this.height / 2);
        // Rotate around the center of the car's bounding box
        g2d.rotate(Math.toRadians(this.angle), 0, 0); // Rotate around (0, 0)

        g2d.setColor(Color.GRAY);
        // Draw the car
        g2d.fillRect(-this.width / 2, -this.height / 2, this.width, this.height); // Draw centered at (0, 0)

        g2d.setTransform(originalTransform);
    }

    @Override
    public void moveForward() {
        this.y--;
        this.polygon = createPolygon();
    }

    @Override
    public void move() {
        if (!this.damaged) {
                updateSpeed();
                updateAngle();
                this.polygon = createPolygon();
                updatePosition();
                if (this.controlType.equals("CONTROL")) {
                    this.sensor.update(this.roadBorders, this.traffic);

                    ArrayList<Double> sensorOffsetArray = new ArrayList<>();

                    for (Hashtable<String, Double> reading: this.sensor.getOffsetReadings()) {
                        double sensorOffset = reading.get("offset");
                        sensorOffsetArray.add(sensorOffset);
                    }

                    ArrayList<Double> outputs = NeuralNetwork.feedForward(sensorOffsetArray, this.brain);
                }

            this.damaged = assessDamage();
        } else {
            stop();
        }
    }

    @Override
    public void paint(Graphics2D g2d) {
        if (damaged) {
            paintDamagedCar(g2d);
        } else {
            paintCarAndSensor(g2d);
        }
    }

}
