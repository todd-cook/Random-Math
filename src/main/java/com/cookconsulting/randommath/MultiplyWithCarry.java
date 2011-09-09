package com.cookconsulting.randommath;

/**
 * a simple random number generator based on
 * George Marsaglia's MWC (multiply with carry) generator.
 * Although it is very simple, it passes Marsaglia's DIEHARD
 * series of random number generator tests.
 * <p/>
 *
 * @author Todd Cook
 * @author John D. Cook
 * @since 8/21/11
 */
public class MultiplyWithCarry implements RandomNumberGenerator {

    private UInt m_w;
    private UInt m_z;

    public MultiplyWithCarry() {
        // These values are not magical, just the default values Marsaglia used.
        // Any pair of unsigned integers should be fine.
        m_w = new UInt(521288629L);
        m_z = new UInt(362436069L);
    }

    /**
     * The random generator seed can be set three ways:
     * 1) specifying two non-zero unsigned integers
     * 2) specifying one non-zero unsigned integer and taking a default value for the second
     * 3) setting the seed from the system time
     *
     * @param u
     * @param v
     */
    public void setSeed(long u, long v) {
        if (u != 0) {
            m_w = new UInt(u);
        }
        if (v != 0) {
            m_z = new UInt(v);
        }
    }

    public void setSeed(long u) {
        setSeed((int) (u >> 16), (int) (u % 4294967296d));
    }

    public void setSeedFromSystemTime() {
        long x = System.currentTimeMillis();
        setSeed((int) (x >> 16), (int) (x % 4294967296d));
    }

    /**
     * This is the heart of the generator.
     * It uses George Marsaglia's MWC algorithm to produce an unsigned integer.
     * See http://www.bobwheeler.com/statistics/Password/MarsagliaPost.txt
     *
     * @return
     */

    public long getLong() {
        m_z = m_z.and(65535).multiply(36969)
            .add(m_z.rightShift(16));
        m_w = (new UInt(18000).multiply(m_w.and(65535)))
            .add(m_w.rightShift(16));
        UInt tmp = m_z.leftShift(16);
        return tmp.add(m_w).value();
    }

    public long getUInt() {
        return getLong();
    }
}

