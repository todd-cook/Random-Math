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
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for efficiently calculating mean, variance and standard deviation from a
 * stream of numbers.
 * <p/>
 * see: http://www.johndcook.com/standard_deviation.html
 * See Donald Knuth's Art of Computer Programming, Vol 2, page 232, 3rd edition
 *
 * @author Todd Cook - Java port, added Thread safety, harmonic and geometric means
 * @author John D. Cook - original C# code
 * @author Donald Knuth - Algorithm
 * @since 9/4/11
 */

public class RunningStats implements Serializable {

    private static final long serialVersionUID = 2278858881964145770L;

    private AtomicLong m_n = new AtomicLong(0L);
    private double m_oldM = 0d;
    private double m_newM = 0d;
    private double m_oldS = 0d;
    private double m_newS = 0d;
    private Double mMin = null;
    private Double mMax = null;

    private double mHarmonicDenominator = 0d;

    public RunningStats() {
    }

    public void clear() {
        m_n.set(0L);
    }

    public synchronized void push(double x) {
        m_n.getAndIncrement();
        // check if first pass, if so, initialize min and max
        if (mMin == null) {
            mMin = x;
            mMax = x;
        }
        else {
            mMin = Math.min(mMin, x);
            mMax = Math.max(mMax, x);
        }
        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if (m_n.get() == 1) {
            m_oldM = m_newM = x;
            m_oldS = 0.0;
        }
        else {
            m_newM = m_oldM + (x - m_oldM) / m_n.get();
            m_newS = m_oldS + (x - m_oldM) * (x - m_newM);
            // set up for next iteration
            m_oldM = m_newM;
            m_oldS = m_newS;
        }
        if (x != 0.0) {
            mHarmonicDenominator += (1 / x);
        }
    }

    /**
     * @return
     */
    public long numberDataValues() {
        return m_n.get();
    }

    /**
     * @return the arithmetic mean
     */
    public synchronized double mean() {
        return (m_n.get() > 0) ? m_newM : 0.0;
    }

    /**
     * @return
     */
    public synchronized double variance() {
        return ((m_n.get() > 1) ? m_newS / (m_n.get() - 1) : 0.0);
    }

    /**
     * @return
     */
    public synchronized double standardDeviation() {
        return Math.sqrt(variance());
    }

    /**
     * The Harmonic Mean
     * Typically, it is appropriate for situations when the average of rates is desired.
     * see http://en.wikipedia.org/wiki/Harmonic_mean
     *
     * @return
     */
    public synchronized double harmonicMean() {
        return (mHarmonicDenominator == 0) ? 0 : m_n.get() / mHarmonicDenominator;
    }

    /**
     * Calculate power value using double value; since
     * X^(A+B)=X^A*X^B
     *
     * @param base the base number for the power operation
     * @param exp the double value for the power
     * @return BigDecimal power product
     */
    private BigDecimal pow(BigDecimal base, double exp) {
        BigDecimal n2 = new BigDecimal(exp);
        // Perform X^(A+B)=X^A*X^B (B = remainder)
        double dn1 = base.doubleValue();
        BigDecimal remainderOf2 = n2.remainder(BigDecimal.ONE);
        BigDecimal n2IntPart = n2.subtract(remainderOf2);
        BigDecimal intPow = base.pow(n2IntPart.intValueExact(), MathContext.DECIMAL128);
        BigDecimal doublePow = new BigDecimal(Math.pow(dn1, remainderOf2.doubleValue()));
        return intPow.multiply(doublePow);
    }
}
