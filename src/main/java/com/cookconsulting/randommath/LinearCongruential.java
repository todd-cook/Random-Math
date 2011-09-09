package com.cookconsulting.randommath;

import java.util.Random;

/**
 * The linear congruential formula. (See Donald Knuth,
 * <i>The Art of Computer Programming, Volume 3</i>, Section 3.2.1.)
 * is used by java.util.Random.
 *
 * @author Todd Cook
 * @since 9/4/11
 */
public class LinearCongruential implements RandomNumberGenerator {

    private Random random = new Random();

    @Override
    public void setSeed(long u, long v) {
        random.setSeed(u);
    }

    @Override
    public void setSeed(long u) {
        random.setSeed(u);
    }

    @Override
    public void setSeedFromSystemTime() {
        random.setSeed(System.currentTimeMillis());
    }

    @Override
    public long getLong() {
        return random.nextLong();
    }

    @Override
    public long getUInt() {
        long tmp = Math.abs(random.nextLong());
        if (Long.toBinaryString(tmp).length() > 32) {
            tmp = Long.parseLong(Long.toBinaryString(tmp).substring(0, 31), 2);
        }
        return tmp;
    }
}
