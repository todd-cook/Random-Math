package com.cookconsulting.randommath;

import java.io.Serializable;

/**
 * Utility class for efficiently calculating mean, variance and standard deviation from a
 * stream of numbers.
 * <p/>
 * see: http://www.johndcook.com/standard_deviation.html
 * See Donald Knuth's Art of Computer Programming, Vol 2, page 232, 3rd edition
 *
 * @author Todd Cook - Java port
 * @author John D. Cook - original C# code
 * @since 9/4/11
 */

public class RunningStats implements Serializable {

    private static final long serialVersionUID = 2278858881964145770L;

    private long m_n;
    private double m_oldM;
    private double m_newM;
    private double m_oldS;
    private double m_newS;

    public RunningStats() {
        m_n = 0;
    }

    public void clear() {
        m_n = 0;
    }

    public void push(double x) {
        m_n++;

        // See Knuth TAOCP vol 2, 3rd edition, page 232
        if (m_n == 1) {
            m_oldM = m_newM = x;
            m_oldS = 0.0;
        }
        else {
            m_newM = m_oldM + (x - m_oldM) / m_n;
            m_newS = m_oldS + (x - m_oldM) * (x - m_newM);

            // set up for next iteration
            m_oldM = m_newM;
            m_oldS = m_newS;
        }
    }

    public long numberDataValues() {
        return m_n;
    }

    public double mean() {
        return (m_n > 0) ? m_newM : 0.0;
    }

    public double variance() {
        return ((m_n > 1) ? m_newS / (m_n - 1) : 0.0);
    }

    public double standardDeviation() {
        return Math.sqrt(variance());
    }
}
