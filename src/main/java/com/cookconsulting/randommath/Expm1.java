package com.cookconsulting.randommath;

/**
 *
 * @author Todd Cook
 * @since 8/21/11 10:16 PM
 * @author Greg Hewgill
 * @author John D Cook
 */
public class Expm1 {
    public static double expm1(double x) {
        if (Math.abs(x) < 1e-5) {
            return x + 0.5 * x * x;
        }
        else {
            return Math.exp(x) - 1.0;
        }
    }
}
