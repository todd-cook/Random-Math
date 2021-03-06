package com.cookconsulting.randommath;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/5/11 11:57 PM
 */
public class NormalCDFInverseTest {

    @Test
    public void testNormalCDFInverse() {

        double max_absolute_error = 0d;
        double absolute_error = 0d;
        double relative_error = 0d;
        double max_relative_error = 0d;
        List<Double> seeds = Arrays.asList(0.0000001,
                                           0.00001,
                                           0.001,
                                           0.05,
                                           0.15,
                                           0.25,
                                           0.35,
                                           0.45,
                                           0.55,
                                           0.65,
                                           0.75,
                                           0.85,
                                           0.95,
                                           0.999,
                                           0.99999,
                                           0.9999999);

        List<Double> results = Arrays.asList(-5.199337582187471,
                                             -4.264890793922602,
                                             -3.090232306167813,
                                             -1.6448536269514729,
                                             -1.0364333894937896,
                                             -0.6744897501960817,
                                             -0.38532046640756773,
                                             -0.12566134685507402,
                                             0.12566134685507402,
                                             0.38532046640756773,
                                             0.6744897501960817,
                                             1.0364333894937896,
                                             1.6448536269514729,
                                             3.090232306167813,
                                             4.264890793922602,
                                             5.199337582187471);

        /**
         *  the actual java results yield:
         *  -5.199081889321649
         *  -4.264844571546845
         *  -3.090522225780171
         *  -1.6452114401438158
         *  -1.036431485189561
         *  -0.6741891400433162
         *  -0.3848770849965131
         *  -0.12538099310291884
         *  0.12538099310291884
         *  0.3848770849965131
         *  0.6741891400433162
         *  1.0364314851895606
         *  1.645211440143815
         *  3.090522225780171
         *  4.264844571547861
         *  5.1990818894194755
         */

        for (int ii = 0; ii < seeds.size(); ii++) {
            absolute_error = Math.abs(results.get(ii) - NormalCDFInverse.normalCDFInverse(seeds.get(ii)));
            relative_error = absolute_error / results.get(ii);
            max_relative_error = Math.max(max_relative_error, relative_error);
            max_absolute_error = Math.max(absolute_error, max_absolute_error);
            assertTrue(max_absolute_error < 1e-3);
        }
        System.out.println("NormalCDFInverse: max relative error: " + max_relative_error +
                               " max absolute error: " + max_absolute_error);
    }
}