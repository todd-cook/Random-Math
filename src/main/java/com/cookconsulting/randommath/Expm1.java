package com.cookconsulting.randommath;

/**
 * one less than the exponential of x
 *
 * @author Todd Cook
 * @author Greg Hewgill
 * @author John D Cook
 * @since 8/21/11 10:16 PM
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
