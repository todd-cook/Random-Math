/*
 * Copyright (c) 2011, Todd Cook.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of the <ORGANIZATION> nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.cookconsulting.randommath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for generating Chi-Squared values
 * The chi-squared distribution with k degrees of freedom is the distribution of a sum of the
 * squares of k independent standard normal random variables.
 * <p/>
 * The chi-squared distribution is used in the common chi-squared tests for goodness of fit of an
 * observed distribution to a theoretical one, the independence of two criteria of classification
 * of qualitative data, and in confidence interval estimation for a population standard deviation
 * of a normal distribution from a sample standard deviation.
 * <p/>
 * For an easy to understand illustration/walk through, read this project's DiceSimulation unit test
 * <p/>
 * The algorithm used to create these Chi-Square values is my port of Gary Perlman's freely
 * available ISTAT package. see http://oldwww.acm.org/perlman/stat/
 *
 * @author Todd Cook
 * @since 2011/11/6
 */
public final class ChiSquaredTable {
    /**
     * The table is defined by the degrees of freedom (Number of distinct categories - 1)
     * And the columns of Probability Percentages, both these fields are initialized by the Builder
     */
    private int degreesOfFreedom;
    private List<Double> columnPercentages;
    private Map<Integer, List<Double>> table = null;

    /**
     * Builder class. See _Effective Java, second edition_ by Josh Bloch
     */
    static class Builder {
        // default values, override as desired with the builder methods
        private int degreesOfFreedom = 30;
        private List<Double> percentages =
            Arrays.asList(0.995, 0.99, 0.975, 0.95, 0.90, 0.85, 0.8, 0.75, 0.7, 0.65, 0.6, 0.55,
                          0.5, 0.45, 0.4, 0.35, 0.3, 0.25, 0.2, 0.10, 0.05, 0.025, 0.01, 0.005);

        public Builder() {
        }

        /**
         * degreesOfFreedom: 1 - number of distinct partitions of the data being analyzed
         *
         * @param degreesOfFreedom
         * @return
         */
        public Builder degreesOfFreedom(int degreesOfFreedom) {
            this.degreesOfFreedom = degreesOfFreedom;
            return this;
        }

        public Builder columnPercentages(List<Double> percentages) {
            this.percentages = percentages;
            return this;
        }

        public ChiSquaredTable build() {
            ChiSquaredTable cst = new ChiSquaredTable();
            cst.degreesOfFreedom = this.degreesOfFreedom;
            Collections.sort(percentages);
            cst.columnPercentages = percentages;
            cst.table = cst.createChiSquaredTable(degreesOfFreedom, cst.columnPercentages);
            return cst;
        }
    }

    private ChiSquaredTable() {
    }

    /**
     * @param degreesOfFreedom degree of freedom
     * @param percentage the desired percentage level
     * @return the expected Chi Squared value
     */
    public double getChiSquared(int degreesOfFreedom, Double percentage) {
        List<Double> elements = table.get(degreesOfFreedom);
        if (elements == null) {
            throw new IllegalArgumentException("Chi Squared Table doesn't contain entry for " +
                                                   "the requested degrees of freedom: "
                                                   + degreesOfFreedom + " please recreate properly");
        }
        Integer index = Collections.binarySearch(columnPercentages, percentage);
        if (index == null || index < 0) {
            throw new IllegalArgumentException("Chi Squared Table doesn't contain index for " +
                                                   "the requested percentage: "
                                                   + percentage + " please recreate properly");
        }
        return elements.get(index);
    }

    /**
     * Create a new Chi Squared table
     *
     * @param degreesOfFreedom 1 - number of distinct partitions of the data being analyzed
     * @param percentages list of probability values as doubles in the range [0,1]
     * @return
     */
    public Map<Integer, List<Double>> createChiSquaredTable(int degreesOfFreedom,
                                                            List<Double> percentages) {

        Map<Integer, List<Double>> tmp = new ConcurrentHashMap<Integer, List<Double>>();
        for (int df = 1; df <= degreesOfFreedom; df++) {
            List<Double> values = new ArrayList<Double>();
            for (Double percent : percentages) {
                values.add(critchi(percent, df));
            }
            tmp.put(df, values);
        }
        return Collections.unmodifiableMap(tmp);
    }

    /**
     * @return list of probability values as doubles in the range [0,1]
     */
    public List<Double> getPercentages() {
        return Collections.unmodifiableList(columnPercentages);
    }

    /**
     * @param expectedValueProbabilities
     * @param actualValues
     * @param sampleSize
     * @return
     */
    public double calculateChiSquared(Map<Integer, Double> expectedValueProbabilities,
                                      Map<Integer, Integer> actualValues, int sampleSize) {

        Map<Integer, Integer> expectedValues =
            calculateExpectedValues(expectedValueProbabilities, sampleSize);
        double sum = 0.0;
        for (Integer key : expectedValues.keySet()) {
            Integer ev = expectedValues.get(key);
            Integer av = actualValues.get(key);
            if (ev != null && av != null) {
                sum += Math.pow(ev - av, 2) / ev;
            }
        }
        return sum;
    }

    /**
     * Helper function for data processing;
     * TODO extract to generic utility class or force people to write their own boilerplate code
     *
     * @param probabilitiesMap
     * @param sampleSize
     * @return
     */
    public Map<Integer, Integer>
    calculateExpectedValues(Map<Integer, Double> probabilitiesMap, int sampleSize) {
        Map<Integer, Integer> values = new HashMap<Integer, Integer>();
        for (Integer key : probabilitiesMap.keySet()) {
            values.put(key, (int) Math.round(probabilitiesMap.get(key) * sampleSize));
        }
        return values;
    }

    /**
     * @param degreesOfFreedom degrees of freedom
     * @param chiSquareValue   calculated ChiSquare value
     * @return probability percentage [0,1]
     */
    public double getPercentage(int degreesOfFreedom, double chiSquareValue) {

        if (table.keySet().contains(degreesOfFreedom)) {
            List<Double> values = table.get(degreesOfFreedom);
            return getColumnCeiling(chiSquareValue, values);
        }
        else {
            List<Integer> cats = new ArrayList<Integer>();
            for (Integer i : table.keySet()) {
                cats.add(i);
            }
            Collections.sort(cats);
            int position = Math.abs(Collections.binarySearch(cats, degreesOfFreedom));
            if (position != cats.size() - 1) {
                return getColumnCeiling(chiSquareValue, table.get(position++));
            }
            // else we are at or above the last value in the table
            return getColumnCeiling(chiSquareValue, table.get(position));
        }
    }

    /**
     * Helper function
     *
     * @param chiSquareValue a user calculated  ChiSquare value
     * @param values         know ChiSquare values
     * @return the nearest percentage value
     */
    private double getColumnCeiling(double chiSquareValue, List<Double> values) {
        int colPosition = Collections.binarySearch(values, chiSquareValue);
        if (colPosition == 0) {
            return columnPercentages.get(colPosition);
        }
        if (colPosition >= 0) {
            return columnPercentages.get(colPosition - 1);
        }
        colPosition = Math.abs(colPosition);
        if (colPosition < values.size() - 1) {
            return columnPercentages.get(colPosition - 1);
        }
        return columnPercentages.get(colPosition - 2);
    }

    public List<Double> getChiSquaredForDegreesOfFreedom(int degreesOfFreedom) {
        return table.get(degreesOfFreedom);
    }

    /**
     * The Following section of code is my port of Gary Perlman's ChiSquare implementation from
     * his freely available ISTAT package
     */

    /**
     * FUNCTION poz: probability of normal z value
     * ALGORITHM
     * Adapted from a polynomial approximation in:
     * Ibbetson D, Algorithm 209
     * Collected Algorithms of the CACM 1963 p. 616
     * Note:
     * This routine has six digit accuracy, so it is only useful for absolute
     * z values < 6.  For z values >= to 6.0, poz() returns 0.0.
     * <p/>
     * VAR returns cumulative probability from -oo to z
     *
     * @param z VAR normal z value
     * @return
     */
    private double poz(double z) {
        double y, x, w;
        if (z == 0.0) {
            x = 0.0;
        }
        else {
            y = 0.5 * Math.abs(z);
            if (y >= (Z_MAX * 0.5)) {
                x = 1.0;
            }
            else if (y < 1.0) {
                w = y * y;
                x = ((((((((0.000124818987 * w
                    - 0.001075204047) * w + 0.005198775019) * w
                    - 0.019198292004) * w + 0.059054035642) * w
                    - 0.151968751364) * w + 0.319152932694) * w
                    - 0.531923007300) * w + 0.797884560593) * y * 2.0;
            }
            else {
                y -= 2.0;
                x = (((((((((((((-0.000045255659 * y
                    + 0.000152529290) * y - 0.000019538132) * y
                    - 0.000676904986) * y + 0.001390604284) * y
                    - 0.000794620820) * y - 0.002034254874) * y
                    + 0.006549791214) * y - 0.010557625006) * y
                    + 0.011630447319) * y - 0.009279453341) * y
                    + 0.005353579108) * y - 0.002141268741) * y
                    + 0.000535310849) * y + 0.999936657524;
            }
        }
        return (z > 0.0 ? ((x + 1.0) * 0.5) : ((1.0 - x) * 0.5));
    }

    ///Useful constants

    /* maximum meaningful z value */
    private static final double Z_MAX = 6.0;

    /* accuracy of critchi approximation */
    private static final double CHI_EPSILON = 0.000001;

    /* maximum chi square value */
    private static final double CHI_MAX = 99999.0;

    /* log (sqrt (pi)) */
    private static final double LOG_SQRT_PI = 0.5723649429247000870717135;

    /* 1 / sqrt (pi) */
    private static final double I_SQRT_PI = 0.5641895835477562869480795;

    /* max value to represent exp (x) */
    private static final double BIGX = 20.0;

    private double ex(double x) {
        return (((x) < -BIGX) ? 0.0 : Math.exp(x));
    }

    /**
     * FUNCTION pochisq: probability of chi sqaure value
     * ALGORITHM Compute probability of chi square value.
     * Adapted from:
     * Hill, I. D. and Pike, M. C.  Algorithm 299
     * Collected Algorithms for the CACM 1967 p. 243
     * Updated for rounding errors based on remark in
     * ACM TOMS June 1985, page 185
     *
     * @param x  obtained chi-square value
     * @param df degrees of freedom
     * @return
     */
    private double pochisq(double x, int df) {
        double a;
        double y = 0d;
        double s;
        double e, c, z;

        if (x <= 0.0 || df < 1) {
            return (1.0);
        }
        a = 0.5 * x;

        /* true if df is an even number */
        boolean even = (2 * (df / 2)) == df;
        if (df > 1) {
            y = ex(-a);
        }
        s = (even ? y : (2.0 * poz(-Math.sqrt(x))));   /* computes probability of normal z score */
        if (df > 2) {
            x = 0.5 * (df - 1.0);
            z = (even ? 1.0 : 0.5);
            if (a > BIGX) {
                e = (even ? 0.0 : LOG_SQRT_PI);
                c = Math.log(a);
                while (z <= x) {
                    e = Math.log(z) + e;
                    s += ex(c * z - a - e);
                    z += 1.0;
                }
                return (s);
            }
            else {
                e = (even ? 1.0 : (I_SQRT_PI / Math.sqrt(a)));
                c = 0.0;
                while (z <= x) {
                    e = e * (a / z);
                    c = c + e;
                    z += 1.0;
                }
                return (c * y + s);
            }
        }
        else {
            return (s);
        }
    }

    /**
     * FUNCTION critchi: compute critical chi square value to produce given p
     *
     * @param p
     * @param df
     * @return
     */
    private double critchi(double p, int df) {
        double minchisq = 0.0;
        double maxchisq = CHI_MAX;
        double chisqval;
        if (p <= 0.0) {
            return (maxchisq);
        }
        else if (p >= 1.0) {
            return (0.0);
        }
        chisqval = df / Math.sqrt(p);    /* fair first value */
        while (maxchisq - minchisq > CHI_EPSILON) {
            if (pochisq(chisqval, df) < p) {
                maxchisq = chisqval;
            }
            else {
                minchisq = chisqval;
            }
            chisqval = (maxchisq + minchisq) * 0.5;
        }
        return (chisqval);
    }
}