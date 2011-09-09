package com.cookconsulting.randommath;

/**
 * Phi: in probability theory is the probability density function of the normal distribution.
 * See: Abramowitz and Stegun: Handbook of Mathematical Functions
 * http://people.math.sfu.ca/~cbm/aands/
 *
 * @author Todd Cook - refactoring, unit tests, documentation
 * @author Greg Hewgill - initial Java port
 * @author John D Cook - original C# version
 * @since 8/21/11 10:16 PM
 */
public class Phi {

    private static final double a1 = 0.254829592;
    private static final double a2 = -0.284496736;
    private static final double a3 = 1.421413741;
    private static final double a4 = -1.453152027;
    private static final double a5 = 1.061405429;
    private static final double p = 0.3275911;

    public static double phi(double x) {
        // Save the sign of x
        double sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x = Math.abs(x) / Math.sqrt(2.0);

        // A&S formula 7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return 0.5 * (1.0 + sign * y);
    }
}
