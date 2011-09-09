package com.cookconsulting.randommath;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Todd Cook - refactoring, documentation, maven integration
 * @author Greg Hewgill - initial Java port
 * @since 8/7/11 1:01 PM
 */
public class LogFactorialTest {

    // ah yes python is a nice way to avoid java.math.BigInteger  & BigDecimal
    private BigDecimal factorial(int n) {
        BigDecimal r = BigDecimal.ONE;
        while (n > 0) {
            r = r.multiply(new java.math.BigDecimal(Integer.toString(n)));
            n -= 1;
        }
        return r;
    }

    @Test
    public void testLogFactorial() {
        List<Integer> seeds = Arrays.asList(0, 1, 10, 100, 1000, 10000);

        for (int i = 0; i < seeds.size(); i++) {
            int seed = seeds.get(i);

            System.out.println("seed: " + seed);
            System.out.println("LogFactorial:" + LogFactorial.logFactorial(seed));
            System.out.println("factorial(" + seed + ") " + factorial(seed));
        }

        double maxError = 0d;
        double error = 0d;
        for (int ii = 0; ii < seeds.size(); ii++) {
            int seed = seeds.get(ii);

            error = Math.abs(
                ((convertToNaturalLog(log10(factorial(seed), 18)).subtract(
                    new BigDecimal(LogFactorial.logFactorial(seed)))).doubleValue()));

            if (error > maxError) {
                maxError = error;
            }
            System.out.println("Seed: " + seed + " LogFactorial: Worst absolute error: " + maxError);
            assertTrue(maxError < 1e-6);
        }
    }

    /**
     * Convert Log 10 to natural log, by multiplying the natural log of 10
     *
     * @param bd
     * @return
     */
    private BigDecimal convertToNaturalLog(BigDecimal bd) {
        return bd.multiply(new BigDecimal(Math.log(10)));
    }

    /**
     * This is a hacky method for determining the log 10 of a BigDecimal.
     * Some textbooks advise to find the square root using Newton's method,
     * e.g. the winning answer on stackoverflow:
     * see http://stackoverflow.com/questions/739532/logarithm-of-a-bigdecimal
     * However, if the seed value isn't close enough, the algorithm will not terminate!
     * Better is a adequate albeit slightly hacky approach listed further down on the stackoverflow
     * entry.
     * see  http://everything2.com/index.pl?node_id=946812
     *
     * @param b
     * @param dp
     * @return
     */
    public BigDecimal log10(BigDecimal b, int dp) {
        final int NUM_OF_DIGITS = dp + 2; // need to add one to get the right number of dp
        //  and then add one again to get the next number
        //  so I can round it correctly.

        MathContext mc = new MathContext(NUM_OF_DIGITS, RoundingMode.HALF_EVEN);

        //special conditions:
        // log(-x) -> exception
        // log(1) == 0 exactly;
        // log of a number lessthan one = -log(1/x)
        if (b.signum() <= 0) {
            throw new ArithmeticException("log of a negative number! (or zero)");
        }
        else if (b.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO;
        }
        else if (b.compareTo(BigDecimal.ONE) < 0) {
            return (log10((BigDecimal.ONE).divide(b, mc), dp)).negate();
        }

        StringBuilder sb = new StringBuilder();
        //number of digits on the left of the decimal point
        int leftDigits = b.precision() - b.scale();

        //so, the first digits of the log10 are:
        sb.append(leftDigits - 1).append(".");

        //this is the algorithm outlined in the web page
        int n = 0;
        while (n < NUM_OF_DIGITS) {
            b = (b.movePointLeft(leftDigits - 1)).pow(10, mc);
            leftDigits = b.precision() - b.scale();
            sb.append(leftDigits - 1);
            n++;
        }

        BigDecimal ans = new BigDecimal(sb.toString());

        //Round the number to the correct number of decimal places.
        ans = ans.round(new MathContext(ans.precision() - ans.scale() + dp, RoundingMode.HALF_EVEN));
        return ans;
    }

}

