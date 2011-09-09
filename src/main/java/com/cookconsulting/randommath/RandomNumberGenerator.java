package com.cookconsulting.randommath;

/**
 * Interface for plugging in Random Number Generators into the Random Number Factory
 *
 * @author todd
 * @since 9/6/11 9:42 PM
 */
public interface RandomNumberGenerator {

    void setSeed(long u, long v);

    void setSeed(long u);

    void setSeedFromSystemTime();

    long getLong();

    long getUInt();
}
