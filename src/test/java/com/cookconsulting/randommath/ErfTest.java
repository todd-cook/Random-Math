package com.cookconsulting.randommath;

/**
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/5/11
 */

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ErfTest {

    @Test
    public void testErf() {

        double absolute_error = 0d;
        double max_absolute_error = 0d;
        double relative_error = 0d;
        double max_relative_error = 0d;
        List<Double> seeds = Arrays.asList(-3d, -1d, 0.0, 0.5, 2.1);
        List<Double> results = Arrays.asList(-0.999977909503,
                                             -0.842700792950,
                                             0.0,
                                             0.520499877813,
                                             0.997020533344);
        /**
         *  the actual java results yield:
         *  -0.9999778948511022
         *  -0.8427006897475899
         *   9.999999717180685E-10
         *   0.5205000163047472
         *   0.997020395440758
         */

        // This section is rather predictable and could be easily extracted using a functional
        // language such as scala, where methods can be first-class objects
        // for now we will leave such as an exercise for the reader
        for (int ii = 0; ii < seeds.size(); ii++) {
            //System.out.println(Erf.erf(seeds.get(ii)));
            //System.out.println("erf() error: " + error);
            absolute_error = Math.abs(results.get(ii) - Erf.erf(seeds.get(ii)));
            if (results.get(ii) != 0) {
                relative_error = new BigDecimal(absolute_error).divide(
                    new BigDecimal(results.get(ii).toString()),
                    BigDecimal.ROUND_HALF_EVEN).doubleValue();
            }
            max_absolute_error = Math.max(absolute_error, max_absolute_error);
            max_relative_error = Math.max(relative_error, max_relative_error);
            assertTrue(max_absolute_error < 1e-6);
        }
        System.out.println("erf: max relative error: " + max_relative_error +
                               " max absolute error: " + max_absolute_error);
    }
}
