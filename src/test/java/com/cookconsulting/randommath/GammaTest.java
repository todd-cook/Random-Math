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
public class GammaTest {

    @Test
    public void testGamma() {

        List<Double> seeds = Arrays.asList(1e-20,
                                           2.19824158876e-16,
                                           2.24265050974e-16,
                                           0.00099,
                                           0.00100,
                                           0.00101,
                                           6.1,
                                           11.999,
                                           12d,
                                           12.001,
                                           15.2);

        List<Double> results = Arrays.asList(1e+20,
                                             4.5490905327e+15, //  # 0.99*DBL_EPSILON
                                             4.45900953205e+15, //  # 1.01*DBL_EPSILON
                                             1009.52477271,
                                             999.423772485,
                                             989.522792258,
                                             142.451944066,
                                             39819417.4793,
                                             39916800.0,
                                             40014424.1571,
                                             149037380723.0);

        /**
         *  the actual java results yield:
         *  1.0E20
         *  4.549090532692938E15
         *  4.4590095320555945E15
         *  1009.5241240938724
         *  999.4237724847056
         *  989.5227922579786
         *  142.45194406567867
         *  3.981941747930387E7
         *  3.991680000000017E7
         *  4.0014424157092094E7
         *  1.490373807233864E11
         */

        double max_relative_error = 0.0;
        double computed = 0d;
        double absolute_error = 0d;
        double relative_error = 0d;
        double max_absolute_error = 0d;

        for (int ii = 0; ii < seeds.size(); ii++) {
            computed = Gamma.gamma(seeds.get(ii));
            absolute_error = Math.abs(computed - results.get(ii));
            relative_error = absolute_error / results.get(ii);
            max_absolute_error = Math.max(absolute_error, max_absolute_error);
            max_relative_error = Math.max(relative_error, max_relative_error);
            //System.out.println("Gamma: relative error: " + relative_error);
            //System.out.println("Gamma(" + seeds.get(ii) + ") computed as: " + computed +
            //                       " but exact value is: " + results.get(ii));
            assertTrue(max_relative_error < 1e-6);
        }
        System.out.println("Gamma: max relative error: " + max_relative_error +
                               " max absolute error: " + max_absolute_error);
    }
}