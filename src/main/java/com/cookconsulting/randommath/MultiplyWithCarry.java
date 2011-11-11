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

/**
 * a simple random number generator based on
 * George Marsaglia's MWC (multiply with carry) generator.
 * Although it is very simple, it passes Marsaglia's DIEHARD
 * series of random number generator tests.
 * <p/>
 *
 * @author Todd Cook - Java port, refactoring
 * @author John D. Cook - C# version
 * @since 8/21/11
 */
public class MultiplyWithCarry implements RandomNumberGenerator, Serializable {

    private static final long serialVersionUID = -2661046007335935932L;

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
        return tmp.add(m_w).longValue();
    }

    public long getUInt() {
        return getLong();
    }

    @Override
    public int nextInt() {
        return 0;  //TODO
    }

    @Override
    public int nextInt(int limit) {
             //TODO check

        if ((limit & -limit) == limit)     {
            return (int)((limit * (long)getLong()) >> 31);
        }

        int bits, val;
        do
            {
            bits =(int) getLong();
            val = bits % limit;
            }
        while(bits - val + (limit-1) < 0);
        return val;
    }
}

