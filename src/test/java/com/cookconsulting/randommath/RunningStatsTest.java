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

/**
 * @author Todd Cook
 */

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RunningStatsTest {

    @Test
    public void testRunningStats() {
        try {
            RunningStats rs = new RunningStats();
            rs.mean();
            rs.geometricMean();
            rs.harmonicMean();
            rs.standardDeviation();
            rs.numberDataValues();
            rs.variance();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("Failure calling methods on RunningStat class uninitialized shouldn't cause " +
                     "exceptions, e.g. divide by zero");
        }
    }

    @Test
    public void testMeanStdDev() {
        RunningStats rs = new RunningStats();
        for (int ii = 1; ii < 100; ii++) {
            rs.push(ii);
        }
        assertTrue(rs.mean() == 50d);
        assertTrue(rs.standardDeviation() == 28.722813232690143d);
    }

    @Test
    public void testHarmonicMean() {
        RunningStats rs = new RunningStats();
        rs.push(1);
        rs.push(2);
        rs.push(4);
        assertTrue(rs.harmonicMean() == 1.7142857142857142d);
    }

    @Test
    public void testGeometricMean1() {
        RunningStats rs = new RunningStats();
        rs.push(2);
        rs.push(8);
        assertTrue(rs.geometricMean() == 4d);
    }

    @Test
    public void testGeometricMean2() {
        RunningStats rs = new RunningStats();
        rs.push(1);
        rs.push(4);
        rs.push(1 / 32d);
        assertTrue(rs.geometricMean() == 0.5d);
    }
}
