package com.cookconsulting.randommath;

/**
 * Interface for plugging in Random Number Generators into the Random Number Factory
 *
 * @author Todd Cook
 * @since 9/4/11
 */
public interface RandomNumberGenerator {

    void setSeed(long u, long v);

    void setSeed(long u);

    void setSeedFromSystemTime();

    long getLong();

    long getUInt();
}
