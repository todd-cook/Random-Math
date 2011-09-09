package com.cookconsulting.randommath;

/**
 * Wrapper class for Mersenne_Twister
 *
 * @author todd
 * @since 9/6/11 10:03 PM
 */
public class Mersenne_Twister implements RandomNumberGenerator {

    private ec.util.MersenneTwister mersenneTwister = new ec.util.MersenneTwister();

    public void setSeed(long u, long v) {
        setSeed(u);
    }

    public void setSeed(long u) {
        mersenneTwister.setSeed(u);
    }

    public void setSeedFromSystemTime() {
        mersenneTwister.setSeed(System.currentTimeMillis());
    }

    public long getLong() {
        return mersenneTwister.nextLong();
    }

    public long getUInt() {
        return Math.abs(mersenneTwister.nextLong(UInt.MAX_VALUE));
    }

}
