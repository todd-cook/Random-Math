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

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * A Factory for Random Numbers; including useful statistical distribution methods.
 * <p/>
 * Based on John D. Cook's SimpleRNG, see: http://www.codeproject.com/KB/recipes/SimpleRNG.aspx
 * see also:http://www.codeproject.com/KB/recipes/pitfalls_random_number.aspx
 * http://www.johndcook.com
 * original code licensed under http://www.codeproject.com/info/cpol10.aspx
 *
 * @author Todd Cook - Java port, refactoring as a Factory class, unit tests
 * @author John D. Cook - inital C# version, named SimpleRNG
 * @since 8/21/11
 */
public class RandomNumberFactory implements RandomNumberGenerator, Serializable {

    private static final long serialVersionUID = 8292679980257982877L;

    private RandomNumberGenerator randomNumberGenerator;
    private String algorithmName;

    public RandomNumberFactory() {
        this( RandomNumberAlgorithm.MULTIPLY_WITH_CARRY);
    }

    public RandomNumberFactory(RandomNumberAlgorithm randomNumberAlgorithm) {
        try {
            Class cls = randomNumberAlgorithm.getClazz();
            Constructor ct
                = cls.getConstructor();
            this.randomNumberGenerator = (RandomNumberGenerator) ct.newInstance();
            this.algorithmName = randomNumberAlgorithm.getAlgorithmName();
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }

    /**
     * Pass through methods: the following methods configure the underlying random number generator
     */

    /**
     * The random generator seed can be set three ways:
     * 1) specifying two non-zero longs (in the range of unsigned integers)
     * 2) specifying one non-zero longs and taking a default value for the second
     * 3) setting the seed from the system time
     *
     * @param u
     * @param v
     */
    public void setSeed(long u, long v) {
        randomNumberGenerator.setSeed(u, v);
    }

    public void setSeed(long u) {
        randomNumberGenerator.setSeed(u);
    }

    public void setSeedFromSystemTime() {
        randomNumberGenerator.setSeedFromSystemTime();
    }

    @Override
    public long getLong() {
        return randomNumberGenerator.getLong();
    }

    @Override
    public long getUInt() {
        return randomNumberGenerator.getUInt();
    }

    @Override
    public int nextInt() {
        return randomNumberGenerator.nextInt();
    }

    @Override
    public int nextInt(int limit) {
        return randomNumberGenerator.nextInt(limit);
    }

    /**
     * Produce a uniform random sample from the open interval (0, 1).
     * The method will not return either end point.
     *
     * @return
     */
    public double getUniform() {
        // 0 <= u < 2^32
        long u = randomNumberGenerator.getUInt();
        // The magic number below is 1/(2^32 + 2).
        // The result is strictly between 0 and 1.
        return (u + 1.0) * 2.328306435454494e-10;
    }

    /**
     * Get normal (Gaussian) random sample with mean 0 and standard deviation 1
     *
     * @return
     */
    public double getNormal() {
        // Use Box-Muller algorithm
        double u1 = getUniform();
        double u2 = getUniform();
        double r = Math.sqrt(-2.0 * Math.log(u1));
        double theta = 2.0 * Math.PI * u2;
        return r * Math.sin(theta);
    }

    /**
     * Get normal (Gaussian) random sample with specified mean and standard deviation
     *
     * @param mean
     * @param standardDeviation
     * @return
     */
    public double getNormal(double mean, double standardDeviation) {
        if (standardDeviation <= 0.0) {
            String msg = String.format("Standard Deviation must be positive. Received %f.",
                                       standardDeviation);
            throw new IllegalArgumentException(msg);
        }
        return mean + standardDeviation * getNormal();
    }

    /**
     * Get exponential random sample with mean 1
     *
     * @return
     */
    public double getExponential() {
        return -Math.log(getUniform());
    }

    /**
     * Get exponential random sample with specified mean
     *
     * @param mean
     * @return
     */
    public double getExponential(double mean) {
        if (mean <= 0.0) {
            String msg = String.format("Mean must be positive. Received %f.", mean);
            throw new IllegalArgumentException(msg);
        }
        return mean * getExponential();
    }

    /**
     * The Gamma distribution is a two-parameter family of continuous probability distributions.
     * It has a scale parameter θ and a shape parameter k. If k is an integer, then the distribution
     * represents an Erlang distribution, i.e., the sum of k independent exponentially distributed
     * random variables, each of which has a mean of θ (which is equivalent to a rate parameter
     * of θ −1). The gamma distribution is frequently a probability model for waiting times...
     * http://en.wikipedia.org/wiki/Gamma_distribution
     * see also: http://en.wikipedia.org/wiki/Erlang_distribution
     * <p/>
     * Implementation based on "A Simple Method for Generating Gamma Variables"
     * by George Marsaglia and Wai Wan Tsang.  ACM Transactions on Mathematical Software
     * Vol 26, No 3, September 2000, pages 363-372.
     *
     * @param shape
     * @param scale
     * @return
     */
    public double getGamma(double shape, double scale) {

        double d, c, x, xsquared, v, u;

        if (shape >= 1.0) {
            d = shape - 1.0 / 3.0;
            c = 1.0 / Math.sqrt(9.0 * d);
            for (; ; ) {
                do {
                    x = getNormal();
                    v = 1.0 + c * x;
                }
                while (v <= 0.0);
                v = v * v * v;
                u = getUniform();
                xsquared = x * x;
                if (u < 1.0 - .0331 * xsquared * xsquared ||
                    Math.log(u) < 0.5 * xsquared + d * (1.0 - v + Math.log(v))) {
                    return scale * d * v;
                }
            }
        }
        else if (shape <= 0.0) {
            String msg = String.format("Shape must be positive. Received %f.", shape);
            throw new IllegalArgumentException(msg);
        }
        else {
            double g = getGamma(shape + 1.0, 1.0);
            double w = getUniform();
            return scale * g * Math.pow(w, 1.0 / shape);
        }
    }

    /**
     * Although not free, see the excellent explanation and applications in:
     * Knuth, Seminumerical Algorithms, section 3.3.1.A, pp. 42-48, e.g. he clarifies:
     * the number of "degrees of freedom", k-1, is one less than the number of categories
     * <p/>
     * From: http://en.wikipedia.org/wiki/Chi-squared_distribution
     * The chi-squared distribution with k degrees of freedom is the distribution of
     * a sum of the squares of k independent standard normal random variables.
     * It is one of the most widely used probability distributions in inferential statistics,
     * e.g., in hypothesis testing or in construction of confidence intervals.
     * <p/>
     * The chi-squared distribution is used in the common chi-squared tests for goodness of fit
     * of an observed distribution to a theoretical one, the independence of two criteria of
     * classification of qualitative data, and in confidence interval estimation for a population
     * standard deviation of a normal distribution from a sample standard deviation. Many other
     * statistical tests also use this distribution, like Friedman's analysis of variance by ranks.
     * <p/>
     * For examples of applications, see:
     * http://www.stat.yale.edu/Courses/1997-98/101/chigf.htm
     * <p/>
     * A Chi squared distribution with n degrees of freedom is a gamma distribution with
     * shape n/2 and scale 2.
     *
     * @param degreesOfFreedom
     * @return
     */
    public double getChiSquare(double degreesOfFreedom) {
        return getGamma(0.5 * degreesOfFreedom, 2.0);
    }

    /**
     * @param shape
     * @param scale
     * @return
     */
    public double getInverseGamma(double shape, double scale) {
        // If X is gamma(shape, scale) then
        // 1/Y is inverse gamma(shape, 1/scale)
        return 1.0 / getGamma(shape, 1.0 / scale);
    }

    /**
     * The Weibull distribution is a continuous probability distribution.
     * Many applications, such as failure rate analysis; If the quantity x is a "time-to-failure",
     * the Weibull distribution gives a distribution for which the failure rate is proportional to
     * a power of time. The shape parameter, k, is that power plus one, and so this parameter can
     * be interpreted directly as follows:
     * A value of k<1 indicates that the failure rate decreases over time.
     * This happens if there is significant "infant mortality", or defective items failing early
     * and the failure rate decreasing over time as the defective items are weeded out of the
     * population.
     * A value of k=1 indicates that the failure rate is constant over time. This might suggest
     * random external events are causing mortality, or failure.
     * A value of k>1 indicates that the failure rate increases with time. This happens if there
     * is an "aging" process, or parts that are more likely to fail as time goes on.
     * <p/>
     * e.g. see also:  http://www.mathpages.com/home/kmath122/kmath122.htm
     * <p/>
     * http://en.wikipedia.org/wiki/Weibull_distribution
     *
     * @param shape
     * @param scale
     * @return
     */
    public double getWeibull(double shape, double scale) {
        if (shape <= 0.0 || scale <= 0.0) {
            String msg = String.format("Shape and scale parameters must be positive. " +
                                           "Received shape %f and scale %f.", shape, scale);
            throw new IllegalArgumentException(msg);
        }
        return scale * Math.pow(-Math.log(getUniform()), 1.0 / shape);
    }

    /**
     * The Cauchy distribution is a continuous probability distribution which has no mean,
     * variance or higher moments defined.
     * see: http://en.wikipedia.org/wiki/Cauchy_distribution
     *
     * @param median
     * @param scale
     * @return
     */
    public double getCauchy(double median, double scale) {
        if (scale <= 0) {
            String msg = String.format("Scale must be positive. Received %f.", scale);
            throw new IllegalArgumentException(msg);
        }
        double p = getUniform();
        // Apply inverse of the Cauchy distribution function to a uniform
        return median + scale * Math.tan(Math.PI * (p - 0.5));
    }

    /**
     * The Student’s t-distribution (or simply the t-distribution) is a continuous probability
     * distribution that arises when estimating the mean of a normally distributed population
     * in situations where the sample size is small and population standard deviation is unknown.
     * It plays a role in a number of widely-used statistical analyses, including the Student’s
     * t-test for assessing the statistical significance of the difference between two sample
     * means, the construction of confidence intervals for the difference between two population
     * means, and in linear regression analysis.
     *
     * @param degreesOfFreedom
     * @return
     */
    public double getStudentT(double degreesOfFreedom) {
        if (degreesOfFreedom <= 0) {
            String msg = String.format("Degrees of freedom must be positive. Received %f.",
                                       degreesOfFreedom);
            throw new IllegalArgumentException(msg);
        }

        // See Seminumerical Algorithms by Knuth
        double y1 = getNormal();
        double y2 = getChiSquare(degreesOfFreedom);
        return y1 / Math.sqrt(y2 / degreesOfFreedom);
    }

    /**
     * the Laplace distribution is a continuous probability distribution, sometimes called
     * the double exponential distribution, because it can be thought of as two exponential
     * distributions (with an additional location parameter) spliced together back-to-back...
     * see: http://en.wikipedia.org/wiki/Laplace_distribution
     *
     * @param mean
     * @param scale
     * @return
     */
    public double getLaplace(double mean, double scale) {
        double u = getUniform();
        return (u < 0.5) ?
            mean + scale * Math.log(2.0 * u) :
            mean - scale * Math.log(2 * (1 - u));
    }

    public double getLogNormal(double mu, double sigma) {
        return Math.exp(getNormal(mu, sigma));
    }

    /**
     * The beta distribution is a family of continuous probability distributions defined on the
     * interval (0, 1) parameterized by two positive shape parameters, typically denoted by α and β.
     * see: http://en.wikipedia.org/wiki/Beta_distribution
     * <p/>
     * There are more efficient methods for generating beta samples.
     * However such methods are a little more efficient and much more complicated.
     * For an explanation of why the following method works, see
     * http://www.johndcook.com/distribution_chart.html#gamma_beta
     *
     * @param a
     * @param b
     * @return
     */
    public double getBeta(double a, double b) {
        if (a <= 0.0 || b <= 0.0) {
            String msg = String.format("Beta parameters must be positive. Received %f and %f.",
                                       a, b);
            throw new IllegalArgumentException(msg);
        }
        double u = getGamma(a, 1.0);
        double v = getGamma(b, 1.0);
        return u / (u + v);
    }

    public String getAlgorithmName() {
        return this.algorithmName;
    }
}
