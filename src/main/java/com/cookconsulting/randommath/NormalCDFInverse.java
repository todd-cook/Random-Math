package com.cookconsulting.randommath;

/**
 * The inverse of the standard normal cdf, called the quantile function or probit function,
 * is expressed in terms of the inverse error function.
 * See: http://en.wikipedia.org/wiki/Normal_distribution#Quantile_function
 * See: Abramowitz and Stegun: Handbook of Mathematical Functions
 * http://people.math.sfu.ca/~cbm/aands/
 *
 * @author Todd Cook
 * @author Greg Hewgill
 * @author John D Cook
 * @since 8/21/11 10:16 PM
 */
public class NormalCDFInverse {

    private static final double c[] = {2.515517, 0.802853, 0.010328};
    private static final double d[] = {1.432788, 0.189269, 0.001308};

    static double rationalApproximation(double t) {
        // Abramowitz and Stegun formula 26.2.23.
        // The absolute value of the error should be less than 4.5 e-4.
        double numerator = (c[2] * t + c[1]) * t + c[0];
        double denominator = ((d[2] * t + d[1]) * t + d[0]) * t + 1.0;
        return t - numerator / denominator;
    }

    public static double normalCDFInverse(double p) {
        assert (p > 0.0 && p < 1);
        // See article above for explanation of this section.
        if (p < 0.5) {
            // F^-1(p) = - G^-1(p)
            return -rationalApproximation(Math.sqrt(-2.0 * Math.log(p)));
        }
        else {
            // F^-1(p) = G^-1(1-p)
            return rationalApproximation(Math.sqrt(-2.0 * Math.log(1.0 - p)));
        }
    }
}
