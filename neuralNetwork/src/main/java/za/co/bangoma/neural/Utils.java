package za.co.bangoma.neural;

import java.awt.*;
import java.util.Hashtable;

public class Utils {

    public static double linearInterpolation(double A, double B, double t) {
        return A + (B - A) * t;
    }

    public static Hashtable<String, Double> getIntersection(Point A, Point B, Point C, Point D) {
        // Calculate the numerator of the equations for t and u
        double tTop = (D.getX() - C.getX()) * (A.getY() - C.getY()) - (D.getY() - C.getY()) * (A.getX() - C.getX());
        double uTop = (C.getY() - A.getY()) * (A.getX() - B.getX()) - (C.getX() - A.getX()) * (A.getY() - B.getY());
        // Calculate the denominator of the equations
        double bottom = (D.getY() - C.getY()) * (B.getX() - A.getX()) - (D.getX() - C.getX()) * (B.getY() - A.getY());

        // Check if the denominator is not equal to zero (to avoid division by zero)
        if (bottom != 0) {
            // Calculate the parameters t and u
            double t = tTop / bottom;
            double u = uTop / bottom;

            // Check if t and u are within the range [0, 1] to ensure intersection is
            // within line segments AB and CD
            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                // If the intersection point lies within the line segments,
                // create a Hashtable to store the result.
                Hashtable<String, Double> result = getStringDoubleHashtable(A, B, t);
                // Return the Hashtable containing the intersection point coordinates
                return result;
            }
        }
        // If no intersection point found or denominator is zero, return null
        return null;
    }

    private static Hashtable<String, Double> getStringDoubleHashtable(Point A, Point B, double t) {
        Hashtable<String, Double> result = new Hashtable<>();
        // Use linear interpolation to find the coordinates of the intersection point
        result.put("x", linearInterpolation(A.getX(), B.getX(), t));
        result.put("y", linearInterpolation(A.getY(), B.getY(), t));
        // Store the parameter t as an offset for further calculations
        result.put("offset", t);
        return result;
    }

}
