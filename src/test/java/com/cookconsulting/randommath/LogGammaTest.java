package com.cookconsulting.randommath;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/7/11
 */
public class LogGammaTest {

    @Test
    public void testLogGamma() {

        List<Double> seeds = Arrays.asList(1e-12,
                                           0.9999,
                                           1.0001,
                                           3.1,
                                           6.3,
                                           11.9999,
                                           12d,
                                           12.0001,
                                           27.4);

        List<Double> results = Arrays.asList(27.6310211159,
                                             5.77297915613e-05,
                                             -5.77133422205e-05,
                                             0.787375083274,
                                             5.30734288962,
                                             17.5020635801,
                                             17.5023078459,
                                             17.5025521125,
                                             62.5755868211);

        /**
         *  the actual java results yield:
         *  27.63102111592797
         *  5.7729791561249E-5
         *  -5.7713342220443005E-5
         *  0.7873750832738625
         *  5.307342889624762
         *  17.5020635801404
         *  17.50230784587389
         *  17.502552112476394
         *  62.57558682105085
         */

        double worst_relative_error = 0.0;
        double computed = 0d;
        double absolute_error = 0d;
        double relative_error = 0d;

        for (int ii = 0; ii < seeds.size(); ii++) {
            computed = Gamma.logGamma(seeds.get(ii));
            absolute_error = Math.abs(computed - results.get(ii));
            relative_error = absolute_error / results.get(ii);

            if (relative_error > worst_relative_error) {
                worst_relative_error = relative_error;
            }
            System.out.println("LogGamma: relative error: " + relative_error);
            System.out.println("LogGamma(" + seeds.get(ii) + ") computed as: " + computed +
                                   " but exact value is: " + results.get(ii));
            assertTrue(worst_relative_error < 1e-10);
        }
    }
}

