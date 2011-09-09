package com.cookconsulting.randommath;

/**
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/5/11
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ErfTest {

    @Test
    public void testErf() {

        double MAX_ERROR = 0.0;
        double error = 0d;
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

        for (int ii = 0; ii < seeds.size(); ii++) {
            System.out.println(Erf.erf(seeds.get(ii)));
            error = Math.abs(results.get(ii) - Erf.erf(seeds.get(ii)));
            if (error > MAX_ERROR) {
                MAX_ERROR = error;
            }
            System.out.println("erf: Maximum error: " + MAX_ERROR);
            assertTrue(MAX_ERROR < 1e-6);
        }

        /*
       for (int ii = 0; ii < seeds.size(); ii++) {
           System.out.println(  Erf.erf2(seeds.get(ii) ));
                   error = Math.abs(results.get(ii) - Erf.erf2(seeds.get(ii)));
                   if (error > MAX_ERROR) {
                       MAX_ERROR = error;
                   }
                   System.out.println("erf2: Maximum error: " + MAX_ERROR);
                   assertTrue(MAX_ERROR < 1e-6);
               }

        */

    }
}
