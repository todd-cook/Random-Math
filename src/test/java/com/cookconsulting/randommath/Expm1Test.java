package com.cookconsulting.randommath;

/**
 *
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/7/11
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Expm1Test {

    @Test
    public void testExpm1() {

        double MAX_ERROR = 0.0;
        double error = 0d;
        List<Double> seeds = Arrays.asList(-1d,
                                           0.0,
                                           1e-5 - 1e-8,
                                           1e-5 + 1e-8,
                                           0.5);
        List<Double> results = Arrays.asList(-0.632120558828558,
                                             0.0,
                                             0.000009990049900216168,
                                             0.00001001005010021717,
                                             0.6487212707001282);
        /**
         *  the actual java results yield:
         *  -0.6321205588285577
         *  0.0
         *  9.990049900050001E-6
         *  1.0010050100150991E-5
         *  0.6487212707001282
         */

        for (int ii = 0; ii < seeds.size(); ii++) {
            error = Math.abs(results.get(ii) - Expm1.expm1(seeds.get(ii)));
            if (error > MAX_ERROR) {
                MAX_ERROR = error;
            }
            System.out.println("Expm1: Maximum error: " + MAX_ERROR);
            assertTrue(MAX_ERROR < 1e-6);
        }
    }
}
