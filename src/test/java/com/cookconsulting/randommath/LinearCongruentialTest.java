package com.cookconsulting.randommath;

import org.junit.Test;

/**
 * Test application for the RandomNumberFactory random number generator.
 * This verifies that the random numbers have the expected
 * distribution using a standard statistical test.
 * Unfortunately the test is more complicated than the generator itself.
 * <p/>
 * For more information on testing random number generators, see
 * chapter 10 of Beautiful Testing by Tim Riley and Adam Goucher.
 *
 * @author Todd Cook
 * @author John D. Cook
 * @since 8/21/11 10:32 PM
 */

public class LinearCongruentialTest extends RandomNumberFactoryTest {

    @Test
    public void KSTestLinearCongruential() {
        System.out.println("\n\nTesting LinearCongruential for Kolmogorov-Smirnov test for distributions:");
        kSTest(getLinearCongruential());
    }

    @Test
    public void TestDistributionsLinearCongruential() {
        System.out.println("Testing LinearCongruential for Distributions:");
        TestDistributions(getLinearCongruential());
    }

    private RandomNumberFactory getLinearCongruential() {
        RandomNumberFactory rnf =
            new RandomNumberFactory(RandomNumberAlgorithm.LINEAR_CONGRUENTIAL);
        rnf.setSeedFromSystemTime();
        return rnf;
    }

}
