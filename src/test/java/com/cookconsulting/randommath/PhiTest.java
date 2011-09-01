package com.cookconsulting.randommath;

/**
 *
 * @author Todd Cook
 * @since 8/5/11 11:57 PM
 *
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PhiTest {

    @Test
    public void testPhi() {

        double MAX_ERROR = 0.0;
        double error = 0d;
        List<Double> seeds = Arrays.asList(-3d,
                                           -1d,
                                           0.0,
                                           0.5,
                                           2.1);

        List<Double> results = Arrays.asList(0.00134989803163,
                                             0.158655253931,
                                             0.5,
                                             0.691462461274,
                                             0.982135579437);

        /**
         *  the actual java results yield:
         *  0.0013499672813147567
         *  0.15865526383236372
         *  0.5000000005
         *  0.6914624627239938
         *  0.9821356419002172
         *
         */

        for (int ii = 0; ii < seeds.size(); ii++) {
            error = Math.abs(results.get(ii) - Phi.phi(seeds.get(ii)));
            if (error > MAX_ERROR) {
                MAX_ERROR = error;
            }
            System.out.println("Phi: Maximum error: " + MAX_ERROR);
            assertTrue(MAX_ERROR < 1e-6);
        }
    }
}
