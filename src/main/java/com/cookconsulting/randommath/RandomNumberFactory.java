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
public class RandomNumberFactory implements Serializable {

    private static final long serialVersionUID = 8292679980257982877L;

    private RandomNumberGenerator randomNumberGenerator;

    public RandomNumberFactory() {
        randomNumberGenerator = new MultiplyWithCarry();

    }

    public RandomNumberFactory(RandomNumberAlgorithm randomNumberAlgorithm) {
        try {
            Class cls = randomNumberAlgorithm.getClazz();
            Constructor ct
                = cls.getConstructor();
            this.randomNumberGenerator = (RandomNumberGenerator) ct.newInstance();
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

    public long getLong() {
        return randomNumberGenerator.getLong();
    }

    public long getUInt() {
        return randomNumberGenerator.getUInt();
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
            String msg = String.format("Shape must be positive. Received {0}.", standardDeviation);
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
            String msg = String.format("mean must be positive. Received {0}.", mean);
            throw new IllegalArgumentException(msg);
        }
        return mean * getExponential();
    }

    /**
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
            String msg = String.format("Shape must be positive. Received {0}.", shape);
            throw new IllegalArgumentException(msg);
        }
        else {
            double g = getGamma(shape + 1.0, 1.0);
            double w = getUniform();
            return scale * g * Math.pow(w, 1.0 / shape);
        }
    }

    /**
     * A chi squared distribution with n degrees of freedom
     * is a gamma distribution with shape n/2 and scale 2.
     *
     * @param degreesOfFreedom
     * @return
     */
    public double getChiSquare(double degreesOfFreedom) {
        return getGamma(0.5 * degreesOfFreedom, 2.0);
    }

    public double getInverseGamma(double shape, double scale) {
        // If X is gamma(shape, scale) then
        // 1/Y is inverse gamma(shape, 1/scale)
        return 1.0 / getGamma(shape, 1.0 / scale);
    }

    public double getWeibull(double shape, double scale) {
        if (shape <= 0.0 || scale <= 0.0) {
            String msg = String.format("Shape and scale parameters must be positive. " +
                                           "Received shape {0} and scale{1}.", shape, scale);
            throw new IllegalArgumentException(msg);
        }
        return scale * Math.pow(-Math.log(getUniform()), 1.0 / shape);
    }

    public double getCauchy(double median, double scale) {
        if (scale <= 0) {
            String msg = String.format("Scale must be positive. Received {0}.", scale);
            throw new IllegalArgumentException(msg);
        }
        double p = getUniform();
        // Apply inverse of the Cauchy distribution function to a uniform
        return median + scale * Math.tan(Math.PI * (p - 0.5));
    }

    public double getStudentT(double degreesOfFreedom) {
        if (degreesOfFreedom <= 0) {
            String msg = String.format("Degrees of freedom must be positive. Received {0}.",
                                       degreesOfFreedom);
            throw new IllegalArgumentException(msg);
        }

        // See Seminumerical Algorithms by Knuth
        double y1 = getNormal();
        double y2 = getChiSquare(degreesOfFreedom);
        return y1 / Math.sqrt(y2 / degreesOfFreedom);
    }

    /**
     * The Laplace distribution is also known as the double exponential distribution.
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
            String msg = String.format("Beta parameters must be positive. Received {0} and {1}.",
                                       a, b);
            throw new IllegalArgumentException(msg);
        }
        double u = getGamma(a, 1.0);
        double v = getGamma(b, 1.0);
        return u / (u + v);
    }
}
