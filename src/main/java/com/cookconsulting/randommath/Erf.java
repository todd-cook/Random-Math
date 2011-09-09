package com.cookconsulting.randommath;

/**
 * ERF - the error function (also called the Gauss error function) is a special function
 * (non-elementary) of sigmoid shape which occurs in probability, statistics and partial
 * differential equations.
 * http://en.wikipedia.org/wiki/Error_function
 * See: Abramowitz and Stegun: Handbook of Mathematical Functions
 * http://people.math.sfu.ca/~cbm/aands/
 *
 * @author Todd Cook
 * @author Greg Hewgill
 * @author John D Cook
 * @since 8/21/11 10:16 PM
 */
public class Erf {

    // constants
    private static final double a1 = 0.254829592;
    private static final double a2 = -0.284496736;
    private static final double a3 = 1.421413741;
    private static final double a4 = -1.453152027;
    private static final double a5 = 1.061405429;
    private static final double p = 0.3275911;

    public static double erf(double x) {

        // Save the sign of x
        double sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = Math.abs(x);

        // A&S formula 7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }

    /**
     * failed attempt at alternate
     * http://en.wikipedia.org/wiki/Error_function#Approximation_with_elementary_functions
     * @param x
     * @return
     */
    /*  public static double erf2(double x) {

       // double a = (8 * (Math.PI - 3)) / 3 * Math.PI * (4 - Math.PI);
        double a = 0.140012;
        double tmp = (4 / Math.PI + (a * (x * x))) / (1 + (a * (x * x)));
        double result = 1 - Math.exp(tmp);
        double sqrt = Math.sqrt(result);
        return Math.signum(x) * sqrt;
    }
    */

}
