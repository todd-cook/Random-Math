package com.cookconsulting.randommath;

import org.junit.Test;

import java.util.Arrays;

/**
 * Test application for the RandomNumberFactory random number generator.
 * This verifies that the random numbers have the expected
 * distribution using a standard statistical test.
 * Unfortunately the test is more complicated than the generator itself.
 * <p/>
 * For more information on testing random number generators, see
 * chapter 10 of Beautiful Testing by Tim Riley and Adam Goucher.
 *
 * @author Todd Cook - Java port, refactoring, unit tests
 * @author John D. Cook - C# version, SimpleRNG
 * @since 8/21/11
 */
public class RandomNumberFactoryTest {

    @Test
    public void createFromEnum() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.MULTIPLY_WITH_CARRY);
        rnf.setSeedFromSystemTime();
        System.out.println(rnf.getLong());
    }

    @Test
    public void createFromEnum2() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.MERSENNE_TWISTER);
        rnf.setSeedFromSystemTime();
        System.out.println(rnf.getLong());
    }

    @Test
    public void createFromEnum3() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.LINEAR_CONGRUENTIAL);
        rnf.setSeedFromSystemTime();
        System.out.println(rnf.getLong());
        System.out.println(rnf.getUInt());
    }

    /**
     * Kolmogorov-Smirnov test for distributions.  See Knuth volume 2, page 48-51 (third edition).
     * This test should *fail* on average one time in 1000 runs.
     * That's life with random number generators: if the test passed all the time,
     * the source wouldn't be random enough!  If the test were to fail more frequently,
     * the most likely explanation would be a bug in the code.
     */
    public void kSTest(RandomNumberFactory rnf) {

        int numReps = 1000;
        double failureProbability = 0.001; // probability of test failing with normal input
        int j;
        double[] samples = new double[numReps];

        for (j = 0; j != numReps; ++j) {
            samples[j] = rnf.getUniform();
        }

        Arrays.sort(samples);

        double CDF;
        double temp;
        int j_minus = 0, j_plus = 0;
        double K_plus = -Double.MAX_VALUE;
        double K_minus = -Double.MAX_VALUE;

        for (j = 0; j != numReps; ++j) {
            CDF = samples[j];
            temp = (j + 1.0) / numReps - CDF;
            if (K_plus < temp) {
                K_plus = temp;
                j_plus = j;
            }
            temp = CDF - (j + 0.0) / numReps;
            if (K_minus < temp) {
                K_minus = temp;
                j_minus = j;
            }
        }

        double sqrtNumReps = Math.sqrt((double) numReps);
        K_plus *= sqrtNumReps;
        K_minus *= sqrtNumReps;

        // We divide the failure probability by four because we have four tests:
        // left and right tests for K+ and K-.
        double p_low = 0.25 * failureProbability;
        double p_high = 1.0 - 0.25 * failureProbability;
        double cutoff_low =
            Math.sqrt(0.5 * Math.log(1.0 / (1.0 - p_low))) - 1.0 / (6.0 * sqrtNumReps);
        double cutoff_high =
            Math.sqrt(0.5 * Math.log(1.0 / (1.0 - p_high))) - 1.0 / (6.0 * sqrtNumReps);

        System.out.printf("\n\nTesting the random number distribution using " +
                              "the Kolmogorov-Smirnov (KS) test.\n");

        System.out.println(String.format("K+ statistic: %f", K_plus));
        System.out.println(String.format("K+ statistic: %f", K_minus));
        System.out.println(String.format("Acceptable interval: [%f, %f]",
                                         cutoff_low, cutoff_high));
        System.out.println(String.format("K+ max at %d %f", j_plus, samples[j_plus]));
        System.out.println(String.format("K- max at %d %f", j_minus, samples[j_minus]));

        if (cutoff_low <= K_plus &&
            K_plus <= cutoff_high &&
            cutoff_low <= K_minus &&
            K_minus <= cutoff_high) {
            System.out.println("\nKS test passed\n");
        }
        else {
            System.out.println("\nKS test failed\n");
        }
    }

    // Convenience function for TestDistributions()
    public void PrintResults(String name,
                             double expectedMean,
                             double expectedVariance,
                             double computedMean,
                             double computedVariance) {
        System.out.println(String.format("Testing %s", name));
        System.out.println(String.format("Expected mean:     %f, computed mean:     %f",
                                         expectedMean, computedMean));
        System.out.println(String.format("Expected variance: %f, computed variance: %f",
                                         expectedVariance, computedVariance));
        System.out.printf("");
    }

    /**
     * Verify that distributions have the correct mean and variance.
     * Note that sample mean and sample variance will not exactly match
     * the expected mean and variance.
     * <p/>
     * Test the derived distributions by looking at their means and variances.
     */
    public void TestDistributions(RandomNumberFactory rnf) {

        final int numSamples = 100000;
        double mean, variance, stdev, shape, scale, degreesOfFreedom;
        RunningStats rs = new RunningStats();

        // Gamma distribution
        rs.clear();
        shape = 10;
        scale = 2;
        for (int i = 0; i < numSamples; ++i) {
            rs.push(rnf.getGamma(shape, scale));
        }
        PrintResults("gamma", shape * scale, shape * scale * scale, rs.mean(), rs.variance());

        // Normal distribution
        rs.clear();
        mean = 2;
        stdev = 5;
        for (int i = 0; i < numSamples; ++i) {
            rs.push(rnf.getNormal(2, 5));
        }
        PrintResults("normal", mean, stdev * stdev, rs.mean(), rs.variance());

        // Student t distribution
        rs.clear();
        degreesOfFreedom = 6;
        for (int i = 0; i < numSamples; ++i) {
            rs.push(rnf.getStudentT(6));
        }
        PrintResults("Student t", 0, degreesOfFreedom / (degreesOfFreedom - 2.0),
                     rs.mean(), rs.variance());

        // Weibull distribution
        rs.clear();
        shape = 2;
        scale = 3;
        mean = 3 * Math.sqrt(Math.PI) / 2;
        variance = 9 * (1 - Math.PI / 4);
        for (int i = 0; i < numSamples; ++i) {
            rs.push(rnf.getWeibull(shape, scale));
        }
        PrintResults("Weibull", mean, variance, rs.mean(), rs.variance());

        // Beta distribution
        rs.clear();
        double a = 7, b = 2;
        mean = a / (a + b);
        variance = mean * (1 - mean) / (a + b + 1);
        for (int i = 0; i < numSamples; ++i) {
            rs.push(rnf.getBeta(a, b));
        }
        PrintResults("Beta", mean, variance, rs.mean(), rs.variance());
    }

}
