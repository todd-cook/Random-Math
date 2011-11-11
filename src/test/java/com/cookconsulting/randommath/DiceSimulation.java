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

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * The probabilities of rolling a fair six-sided die are, for each number: 1/6
 * The probable values of two fair six-sided die are:
 * 2 : 1 / 36
 * 3: 1 / 18
 * 4: 1 / 12
 * 5: 1 / 9
 * 6: 5 / 36
 * 7: 1 / 6
 * 8: 5 / 36
 * 9: 1 / 9
 * 10: 1 / 12
 * 11: 1 / 18
 * 12: 1 / 36
 * <p/>
 * These expected probabilities can be compared to actual values to determine how the real data
 * is to the probabilistic model. In this manner, one can tell if dice are loaded, or if a random
 * number generator is sufficiently random for modelling a simulation. This comparison can be done
 * by calculating the Chi-Square values for the probability distribution.
 * <p/>
 * Only when the values dip below 5% (0.05, 0.01, 0.001) is it consider to be significant.
 * <p/>
 * see: http://en.wikipedia.org/wiki/Chi_square#Table_of_.CF.872_value_vs_p-value
 * see Knuth, Art of Computer Programming, Vol. 2, pp.42-47
 * <p/>
 * Knuth recommends the common practice of gathering sample sizes so that there is a probability
 * that at least five elements should occur in the least common probability; e.g. given to dice
 * probabilities above: min probability 1/36; 36 * 5 = 180
 * <p/>
 * Note: because of the difficulties of representing the fractions in floating point numbers, even
 * the simple dice probabilities will not add up to one, but rather:
 * <p/>
 * scala>  1 / 36d + 1 / 18d + 1 / 12d + 1 / 9d + 5 / 36d + 1 / 6d + 5 / 36d + 1 / 9d
 * + 1 / 12d + 1 / 18d + 1 / 36d
 * res42: Double = 1.0000000000000002
 * as should be expected:
 * scala> 1/6d+1/6d+1/6d+1/6d+1/6d+1/6d
 * res49: Double = 0.9999999999999999
 * Hence, we will use a nearEquivalence method of checking that the double values are close enough.
 *
 * @author Todd Cook
 * @since 2011/11/6
 */
public class DiceSimulation {

    private final HashMap<Integer, Double> probabilitesForTwoDice = create2DiceProbabilites();
    ChiSquaredTable cst = new ChiSquaredTable.Builder().build();

    /**
     * Utiliity function for checking near equivalence of double values
     *
     * @param expected
     * @param actual
     * @param tolerance
     * @return
     */
    public boolean nearEquivalent(double expected, double actual, double tolerance) {
        Double difference = expected - actual;
        System.out.println(String.format("expected: %f actual: %f difference: %f" +
                                             " relative error: %f ", expected, actual, difference,
                                         difference / expected));
        return (difference / expected < tolerance);
    }

    public static HashMap<Integer, Double> create2DiceProbabilites() {
        HashMap<Integer, Double> p = new HashMap<Integer, Double>();
        p.put(2, 1 / 36d);
        p.put(3, 1 / 18d);
        p.put(4, 1 / 12d);
        p.put(5, 1 / 9d);
        p.put(6, 5 / 36d);
        p.put(7, 1 / 6d);
        p.put(8, 5 / 36d);
        p.put(9, 1 / 9d);
        p.put(10, 1 / 12d);
        p.put(11, 1 / 18d);
        p.put(12, 1 / 36d);
        return p;
    }

    @Test
    public void testKnuthDiceExample() {
        Map<Integer, Integer> actualValues = new HashMap<Integer, Integer>();
        actualValues.put(2, 2);
        actualValues.put(3, 4);
        actualValues.put(4, 10);
        actualValues.put(5, 12);
        actualValues.put(6, 22);
        actualValues.put(7, 29);
        actualValues.put(8, 21);
        actualValues.put(9, 15);
        actualValues.put(10, 14);
        actualValues.put(11, 9);
        actualValues.put(12, 6);
        double chiSquaredValue =
            cst.calculateChiSquared(probabilitesForTwoDice, actualValues, 144);
        double percentage = cst.getPercentage(10, chiSquaredValue);
        System.out.println(String.format("Chi Squared Value: %f percentage %f.",
                                         chiSquaredValue, percentage));
        assertTrue(nearEquivalent(chiSquaredValue, 7.14583d, 0.001));
    }

    @Test
    public void testKnuthDiceExample2() {

        Map<Integer, Integer> actualValues = new HashMap<Integer, Integer>();
        actualValues.put(2, 4);
        actualValues.put(3, 10);
        actualValues.put(4, 10);
        actualValues.put(5, 13);
        actualValues.put(6, 20);
        actualValues.put(7, 18);
        actualValues.put(8, 18);
        actualValues.put(9, 11);
        actualValues.put(10, 13);
        actualValues.put(11, 14);
        actualValues.put(12, 13);
        double chiSquaredValue =
            cst.calculateChiSquared(probabilitesForTwoDice, actualValues, 144);
        double percentage = cst.getPercentage(10, chiSquaredValue);
        System.out.println(String.format("Chi Squared Value: %f percentage %f.",
                                         chiSquaredValue, percentage));
        assertTrue(nearEquivalent(chiSquaredValue, 29.49166d, 0.001));
    }

    @Test
    public void testKnuthDiceExample3() {
        Map<Integer, Integer> actualValues = new HashMap<Integer, Integer>();
        actualValues.put(2, 3);
        actualValues.put(3, 7);
        actualValues.put(4, 11);
        actualValues.put(5, 15);
        actualValues.put(6, 19);
        actualValues.put(7, 24);
        actualValues.put(8, 21);
        actualValues.put(9, 17);
        actualValues.put(10, 13);
        actualValues.put(11, 9);
        actualValues.put(12, 5);
        double chiSquaredValue =
            cst.calculateChiSquared(probabilitesForTwoDice, actualValues, 144);
        double percentage = cst.getPercentage(10, chiSquaredValue);
        System.out.println(String.format("Chi Squared Value: %f percentage %f.",
                                         chiSquaredValue, percentage));
        assertTrue(nearEquivalent(chiSquaredValue, 1.14166d, 0.001));
    }

    @Test
    public void testMersenne() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.MERSENNE_TWISTER);
        rnf.setSeedFromSystemTime();
        testDiceSimulation(rnf, 1000, 180);
    }

    @Test
    public void testLinearCongruential() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.LINEAR_CONGRUENTIAL);
        rnf.setSeedFromSystemTime();
        testDiceSimulation(rnf, 1000, 180);
    }

    @Test
    public void testMWC() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.MULTIPLY_WITH_CARRY);
        rnf.setSeedFromSystemTime();
        testDiceSimulation(rnf, 1000, 180);
    }

    /**
     * Utility method for testing simulations
     * Interesting simulation note: trying to model the dice roll event as a single random act,
     * causes the metric to go wildly awry, as one should suspect:
     * fm.add(rnf.nextInt(10) + 2);
     * yields:
     * <p/>
     * MERSENNE_TWISTER iterations: 1000 iterations mean Chi Squared Value: 68.744717
     * Std Deviation: 25.170323 percentage 0.005120 std dev: 0.001445.
     * whereas:
     * fm.add(rnf.nextInt(6) + 1 + rnf.nextInt(6) + 1);
     * MERSENNE_TWISTER iterations: 1000 iterations mean Chi Squared Value: 9.706987
     * Std Deviation: 4.501019 percentage 0.180365 std dev: 0.257793.
     *
     * @param rnf
     * @param iterations
     * @param sampleSize
     */
    private void testDiceSimulation(RandomNumberFactory rnf, int iterations, int sampleSize) {
        FrequencyMap<Integer> fm = new FrequencyMap<Integer>();
        RunningStats rsChiSquared = new RunningStats();
        RunningStats rsPercentage = new RunningStats();
        for (int i = 0; i < iterations; i++) {
            for (int ii = 0; ii < sampleSize; ii++) {
                // fm.add(rnf.nextInt(10) + 2);  //
                fm.add(rnf.nextInt(6) + 1 + rnf.nextInt(6) + 1);
            }
            double chiSquaredValue =
                cst.calculateChiSquared(probabilitesForTwoDice, fm.getMap(), sampleSize);
            double percentage = cst.getPercentage(10, chiSquaredValue);
            rsChiSquared.push(chiSquaredValue);
            rsPercentage.push(percentage);
            fm.clear();
        }
        System.out.println(String.format(
            "%s iterations: %d iterations mean Chi Squared Value: %f" +
                " Std Deviation: %f percentage %f std dev: %f.",
            rnf.getAlgorithmName(), iterations, rsChiSquared.mean(),
            rsChiSquared.standardDeviation(),
            rsPercentage.mean(), rsPercentage.standardDeviation()));
    }
}
