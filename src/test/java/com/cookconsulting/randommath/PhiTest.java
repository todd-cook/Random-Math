package com.cookconsulting.randommath;

/**
 *
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/5/11
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PhiTest {

    @Test
    public void testPhi() {

        double absolute_error = 0d;
        double max_absolute_error = 0d;
        double relative_error = 0d;
        double max_relative_error = 0d;
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
            absolute_error = Math.abs(results.get(ii) - Phi.phi(seeds.get(ii)));
            relative_error = absolute_error / results.get(ii);
            max_absolute_error = Math.max(absolute_error, max_absolute_error);
            max_relative_error = Math.max(relative_error, max_relative_error);
            assertTrue(max_absolute_error < 1e-6);
        }
        System.out.println("Phi: max relative error: " + max_relative_error +
                               " max absolute error: " + max_absolute_error);
    }
}
