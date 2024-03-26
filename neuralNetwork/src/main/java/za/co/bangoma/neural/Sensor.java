package za.co.bangoma.neural;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Sensor implements Drawable {

    private Car car;
    private int rayCount;
    private int rayLength;
    private double raySpread;
    private ArrayList<Point[]> rays;
    private ArrayList<Point> readings;

    public Sensor(Car car) {
        this.car = car;
        this.rayCount = 5;
        this.rayLength = 150;
        this.raySpread = Math.PI / 2; // 90 degrees

        this.rays = new ArrayList<>();
        this.readings = new ArrayList<>();
    }

    public void update(Point[] roadBorders, Car[] traffic) {
        castRays(this.car.getAngle());
        readings.clear();

        for (Point[] ray : rays) {
            readings.add(getReadings(ray, roadBorders, traffic));
        }
    }

    public void updateAngles(double carAngle) {
        castRays(carAngle);
    }


    private Point getReadings(Point[] ray, Point[] roadBorders, Car[] traffic) {
        ArrayList<Point> touches = new ArrayList<>();

        for (Point roadBorder : roadBorders) {
            Point touch = getIntersection(ray[0], ray[1], roadBorder, new Point(roadBorder.x, roadBorder.y + 1)); // Assuming road borders are represented as horizontal lines

            if (touch != null) {
                touches.add(touch);
            }
        }

        for (Car item : traffic) {
            Point[] poly = item.getPolygon();

            for (int k = 0; k < poly.length; k++) {
                Point value = getIntersection(ray[0], ray[1], poly[k], poly[(k + 1) % poly.length]);

                if (value != null) {
                    touches.add(value);
                }
            }
        }

        if (touches.isEmpty()) {
            return null;
        } else {
            double minOffset = Double.MAX_VALUE;
            Point minPoint = null;

            for (Point touch : touches) {
                double distance = Math.hypot(touch.x - ray[0].x, touch.y - ray[0].y);
                if (distance < minOffset) {
                    minOffset = distance;
                    minPoint = touch;
                }
            }

            return minPoint;
        }
    }

    public ArrayList<Point[]> getRays() {
        return rays;
    }

    private void castRays(double carAngle) {
        rays.clear();

        for (int i = 0; i < rayCount; i++) {
            // double rayAngle = Utils.linearInterpolation(raySpread / 2, -raySpread / 2, rayCount == 1 ? 0.5 : (double) i / (rayCount - 1)) + car.getAngle();
            double rayAngle = Utils.linearInterpolation(raySpread / 2, -raySpread / 2, rayCount == 1 ? 0.5 : (double) i / (rayCount - 1)) + carAngle;


            Point start = new Point((int) car.getX() + car.getWidth() / 2, (int) car.getY() + car.getHeight() / 2);
            Point end = new Point((int) (car.getX() - Math.sin(rayAngle) * rayLength) + car.getWidth() / 2, (int) (car.getY() - Math.cos(rayAngle) * rayLength) + car.getHeight() / 2);

            rays.add(new Point[]{start, end});
        }
    }

    private Point getIntersection(Point start1, Point end1, Point start2, Point end2) {
        double x1 = start1.getX();
        double y1 = start1.getY();
        double x2 = end1.getX();
        double y2 = end1.getY();
        double x3 = start2.getX();
        double y3 = start2.getY();
        double x4 = end2.getX();
        double y4 = end2.getY();

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        if (ua >= 0 && ua <= 1) {
            double x = x1 + ua * (x2 - x1);
            double y = y1 + ua * (y2 - y1);
            return new Point((int) x, (int) y);
        } else {
            return null;
        }
    }

    public void drawSensors(Graphics2D g) {
        AffineTransform originalTransform = g.getTransform(); // Save the original transformation
        g.translate(this.car.getX() + this.car.getWidth() / 2, this.car.getY() + this.car.getHeight() / 2); // Translate to the car's position
        g.rotate(Math.toRadians(this.car.getAngle())); // Rotate around the center of the car

        // Draw the sensor rays
        for (int i = 0; i < rayCount; i++) {
            Point[] ray = rays.get(i);
            Point end = readings.get(i) != null ? readings.get(i) : ray[1];

            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(2));
            g.drawLine(ray[0].x - (this.car.getX() + this.car.getWidth() / 2), ray[0].y - (this.car.getY() + this.car.getHeight() / 2), end.x - (this.car.getX() + this.car.getWidth() / 2), end.y - (this.car.getY() + this.car.getHeight() / 2));

            g.setColor(Color.BLACK);
            g.drawLine(ray[1].x - (this.car.getX() + this.car.getWidth() / 2), ray[1].y - (this.car.getY() + this.car.getHeight() / 2), end.x - (this.car.getX() + this.car.getWidth() / 2), end.y - (this.car.getY() + this.car.getHeight() / 2));
        }

        g.setTransform(originalTransform); // Restore the original transformation
    }

    @Override
    public void paint(Graphics2D g) {
        for (int i = 0; i < rayCount; i++) {
            Point[] ray = rays.get(i);
            Point end = readings.get(i) != null ? readings.get(i) : ray[1];

            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(2));
            g.drawLine(ray[0].x, ray[0].y, end.x, end.y);

            g.setColor(Color.BLACK);
            g.drawLine(ray[1].x, ray[1].y, end.x, end.y);
        }
    }
}

