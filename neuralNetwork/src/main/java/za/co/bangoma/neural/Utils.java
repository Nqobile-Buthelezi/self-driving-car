package za.co.bangoma.neural;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Utils {

    public static double linearInterpolation(double A, double B, double t) {
        return A + (B - A) * t;
    }

    public static boolean polysIntersect(ArrayList<Point> poly1, Point[] poly2) {
        for (int i = 0; i < poly1.size(); i++) {
            for (int k = 0; k < poly2.length; k++) {
                Hashtable touch;
                touch = getIntersection(
                        poly1.get(i),
                        poly1.get((i + 1) % poly1.size()),
                        poly2[k],
                        poly2[(k + 1) % poly2.length]
                );
                if (touch != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean trafficIntersect(ArrayList<Point> poly1, ArrayList<Point> poly2) {
        for (int i = 0; i < poly1.size(); i++) {
            for (int k = 0; k < poly2.size(); k++) {
                Hashtable touch;
                touch = getIntersection(
                        poly1.get(i),
                        poly1.get((i + 1) % poly1.size()),
                        poly2.get(k),
                        poly2.get((k + 1) % poly2.size())
                );
                if (touch != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Hashtable getIntersection(Point A, Point B, Point C, Point D) {
        double tTop = (D.getX() - C.getX()) * (A.getY() - C.getY()) - (D.getY() - C.getY()) * (A.getX() - C.getX());
        double uTop = (C.getY() - A.getY()) * (A.getX() - B.getX()) - (C.getX() - A.getX()) * (A.getY() - B.getY());
        double bottom = (D.getY() - C.getY()) * (B.getX() - A.getX()) - (D.getX() - C.getX()) * (B.getY() - A.getY());;

        if (bottom != 0) {
            double t = tTop / bottom;
            double u = uTop / bottom;

            if (t >= 0 && t<= 1 && u >= 0 && u <= 1) {
                Hashtable result = new Hashtable<>();
                result.put("x", linearInterpolation(A.getX(), B.getX(), t));
                result.put("y", linearInterpolation(A.getY(), B.getY(), t));
                result.put("offset", t);
                return result;
            }
        }
        return null;
    }
}
