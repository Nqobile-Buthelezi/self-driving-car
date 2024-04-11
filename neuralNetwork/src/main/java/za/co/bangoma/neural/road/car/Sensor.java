package za.co.bangoma.neural.road.car;


import za.co.bangoma.neural.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Sensor {

    // Attributes
    private Car car;
    private int rayCount;
    private double raySpread;
    private int rayLength;
    private ArrayList<RayCoordinates> rays;
    private ArrayList<Hashtable<String, Double>> readings;

    // Constructor
    public Sensor(Car car) {
        this.car = car;
        this.rayCount = 5;
        this.rayLength = 150;
        this.raySpread = Math.PI / 2;
        this.rays = new ArrayList<>();
        this.readings = new ArrayList<>();
    }

    // Getters
    public int getRayCount() {
        return rayCount;
    }

    public double getRaySpread() {
        return raySpread;
    }

    public int getRayLength() {
        return rayLength;
    }

    public ArrayList<RayCoordinates> getRays() {
        return rays;
    }

    public ArrayList<Hashtable<String, Double>> getOffsetReadings() {
        return readings;
    }

    // Methods
    private void castRays() {
        this.rays = new ArrayList<>();

        for (int i = 0; i < this.rayCount; i++) {
            // Calculate the relative angle for each ray
            double rayAngleRelativeToCar = (double) i / (this.rayCount - 1) * this.raySpread - this.raySpread / 2;

            // Calculate the absolute angle of the ray by adding the car's angle
            RayCoordinates coordinates = getRayCoordinates(rayAngleRelativeToCar);
            this.rays.add(coordinates);
        }
    }

    private RayCoordinates getRayCoordinates(double rayAngleRelativeToCar) {
        double rayAngle = Math.toDegrees(rayAngleRelativeToCar) + this.car.getAngle();

        // Calculate the start and end points of the ray based on the absolute angle
        double startX = this.car.getX() + (double) (this.car.getWidth() / 2);
        double startY = this.car.getY() + (double) (this.car.getHeight() / 2);
        double endX = startX + Math.sin(Math.toRadians(rayAngle)) * this.rayLength;
        double endY = startY - Math.cos(Math.toRadians(rayAngle)) * this.rayLength;

        RayCoordinates coordinates = new RayCoordinates(startX, startY, endX, endY);
        return coordinates;
    }

    private Hashtable<String, Double> getReadings(Point[] ray, ArrayList<Point[]> roadBorders, Car[] traffic) {
        ArrayList<Hashtable<String, Double>> touches = new ArrayList<>();

        for (int i = 0; i < roadBorders.size(); i++) {
            Hashtable<String, Double> touch = Utils.getIntersection(
                    ray[0],
                    ray[1],
                    roadBorders.get(i)[0],
                    roadBorders.get(i)[1]
            );

            if (touch != null) {
                touches.add(touch);
            }
        }

        for (int i = 0; i < traffic.length; i++) {
            ArrayList<Point> poly = traffic[i].getPolygon();

            for (int k = 0; k < poly.size(); k++) {
                Hashtable<String, Double> value = Utils.getIntersection(
                        ray[0],
                        ray[1],
                        poly.get(k),
                        poly.get((k + 1) % poly.size())
                );

                if (value != null) {
                    touches.add(value);
                }
            }
        }

        if (touches.isEmpty()) {
            return null;
        } else {
            // Find the minimum offset among all touches
            double minOffset = Double.MAX_VALUE; // Initialise with a very large value
            Hashtable<String, Double> minTouch = null;

            for (Hashtable<String, Double> touch : touches) {
                double offset = touch.get("offset");
                if (offset < minOffset) {
                    minOffset = offset;
                    minTouch = touch;
                }
            }

            return minTouch;
        }
    }

    public void update(ArrayList<Point[]> roadBorders, Car[] traffic) {
        this.castRays();
        this.readings.clear();

        for (int i = 0; i < this.rays.size(); i++) {
            RayCoordinates myRay = this.rays.get(i);

            Point startingPoint = new Point((int) myRay.getStartX(), (int) myRay.getStartY());
            Point endingPoint = new Point((int) myRay.getEndX(), (int) myRay.getEndY());


            Point[] myPoints = {startingPoint, endingPoint};

            Hashtable<String, Double> emptyReading = new Hashtable<>();
            emptyReading.put("x", myRay.getEndX());
            emptyReading.put("y", myRay.getEndY());
            emptyReading.put("offset", 0.0);

            this.readings.add(
                    this.getReadings(
                            myPoints,
                            roadBorders,
                            traffic
                    ) != null ? this.getReadings(
                            myPoints,
                            roadBorders,
                            traffic
                    ) : emptyReading
            );
        }
    }

    public void draw(Graphics2D graphics2D) {
        if (!this.rays.isEmpty() && !this.readings.isEmpty()) {
            // Defines the thickness of our Sensor line
            float strokeWidth = 2.5f;

            // Creates a new BasicStroke with the desired thickness
            Stroke oldStroke = graphics2D.getStroke();

            graphics2D.setStroke(new BasicStroke(strokeWidth));

            for (int i = 0; i < this.rayCount; i++) {

                // Create endpoint for ray
                Hashtable<String, Double> end = new Hashtable<>();
                if (this.rays.size() > i) {
                    end.put("x", this.rays.get(i).getEndX());
                    end.put("y", this.rays.get(i).getEndY());
                }

                if (i >= this.readings.size()) {
                    // index does not exist
                    continue;
                } else {
                    // index exists
                    // If there is a reading coordinate of the intercept
                    // make that coordinate our new end coordinate
                    if ((i < this.readings.size()) && (this.readings.get(i) != null)) {
                        end.put("x", this.readings.get(i).get("x"));
                        end.put("y", this.readings.get(i).get("y"));
                    }
                }

                graphics2D.setColor(Color.YELLOW);

                if (this.rays.size()> i) {
                    graphics2D.drawLine(
                            (int) this.rays.get(i).getStartX(),
                            (int) this.rays.get(i).getStartY(),
                            end.get("x").intValue(),
                            end.get("y").intValue()
                    );
                }


                graphics2D.setColor(Color.BLACK);

                if (this.rays.size()> i) {
                    graphics2D.drawLine(
                            (int) this.rays.get(i).getEndX(),
                            (int) this.rays.get(i).getEndY(),
                            end.get("x").intValue(),
                            end.get("y").intValue()
                    );
                }

            }

            graphics2D.setStroke(oldStroke);
        }
    }
}

