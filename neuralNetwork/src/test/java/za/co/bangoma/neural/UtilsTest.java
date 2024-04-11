package za.co.bangoma.neural;

import junit.framework.TestCase;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class UtilsTest extends TestCase {

    public void testLinearInterpolation() {
        // Arrange
        double A = 0;
        double B = 10;
        double t = 0.5;

        // Act
        double result = Utils.linearInterpolation(A, B, t);

        // Assert
        assertEquals(5.0, result);
    }

    public void testGetRGBA() {
        // Arrange
        Double value = 0.5;

        // Act
        Color result = Utils.getRGBA(value);

        // Assert
        assertEquals(new Color(255, 255, 0, 127), result);
    }

    public void testGetIntersection() {
        // Arrange
        Point A = new Point(0, 0);
        Point B = new Point(10, 0);
        Point C = new Point(5, -5);
        Point D = new Point(5, 5);

        // Act
        Hashtable<String, Double> result = Utils.getIntersection(A, B, C, D);

        // Assert
        assertNotNull(result);
        assertEquals(5.0, result.get("x"));
        assertEquals(0.0, result.get("y"));
        assertEquals(0.5, result.get("offset"));
    }

    public void testCarAndBorderIntersect() {
        // Arrange
        ArrayList<Point> polygon = new ArrayList<>();
        polygon.add(new Point(0, 0));
        polygon.add(new Point(10, 0));
        polygon.add(new Point(10, 10));
        polygon.add(new Point(0, 10));

        Point[] points = {new Point(5, 5), new Point(15, 5)}; // Just for testing, need to adjust for real case

        // Act
        boolean result = Utils.carAndBorderIntersect(polygon, points);

        // Assert
        assertTrue(result);
    }

    public void testCarAndTrafficIntersect() {
        // Arrange
        ArrayList<Point> polygon = new ArrayList<>();
        polygon.add(new Point(0, 0));
        polygon.add(new Point(10, 0));
        polygon.add(new Point(10, 10));
        polygon.add(new Point(0, 10));

        ArrayList<Point> polygon1 = new ArrayList<>();
        polygon1.add(new Point(5, 5));
        polygon1.add(new Point(15, 5));
        // Just for testing, need to adjust for real case

        // Act
        boolean result = Utils.carAndTrafficIntersect(polygon, polygon1);

        // Assert
        assertTrue(result);
    }
}
