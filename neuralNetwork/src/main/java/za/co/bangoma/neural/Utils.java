package za.co.bangoma.neural;

public class Utils {

    public static double linearInterpolation(double A, double B, double t) {
        return A + (B - A) * t;
    }

}
