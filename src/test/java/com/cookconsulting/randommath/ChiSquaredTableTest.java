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
 *
 *  @author Todd Cook
 */

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ChiSquaredTableTest {

    public boolean nearEquivalent(double expected, double actual, double tolerance) {
        Double difference = Math.abs(expected - actual);
        System.out.println(String.format("expected: %f actual: %f difference: %f" +
                                             " relative error: %f ", expected, actual, difference,
                                         difference / expected));
        return (difference / expected < tolerance);
    }

    @Test
    public void testFirst30ChiSquaredValues() {
        ChiSquaredTable cst = new ChiSquaredTable.Builder().build();
        List<Double> columnPercentages = Arrays.asList(.995, .990, .975, .950, .900, .750, .500,
                                                       .250, .100, .050, .025, .010, .005);
        List<List<Double>> expectedValues = new ArrayList<List<Double>>();

        expectedValues.add(Arrays.asList(0.00004, 0.00016, 0.00098, 0.00393, 0.01579, 0.10153, 0.45494, 1.32330, 2.70554, 3.84146, 5.02389, 6.63490, 7.87944));
        expectedValues.add(Arrays.asList(0.01003, 0.02010, 0.05064, 0.10259, 0.21072, 0.57536, 1.38629, 2.77259, 4.60517, 5.99146, 7.37776, 9.21034, 10.59663));
        expectedValues.add(Arrays.asList(0.07172, 0.11483, 0.21580, 0.35185, 0.58437, 1.21253, 2.36597, 4.10834, 6.25139, 7.81473, 9.34840, 11.34487, 12.83816));
        expectedValues.add(Arrays.asList(0.20699, 0.29711, 0.48442, 0.71072, 1.06362, 1.92256, 3.35669, 5.38527, 7.77944, 9.48773, 11.14329, 13.27670, 14.86026));
        expectedValues.add(Arrays.asList(0.41174, 0.55430, 0.83121, 1.14548, 1.61031, 2.67460, 4.35146, 6.62568, 9.23636, 11.07050, 12.83250, 15.08627, 16.74960));
        expectedValues.add(Arrays.asList(0.67573, 0.87209, 1.23734, 1.63538, 2.20413, 3.45460, 5.34812, 7.84080, 10.64464, 12.59159, 14.44938, 16.81189, 18.54758));
        expectedValues.add(Arrays.asList(0.98926, 1.23904, 1.68987, 2.16735, 2.83311, 4.25485, 6.34581, 9.03715, 12.01704, 14.06714, 16.01276, 18.47531, 20.27774));
        expectedValues.add(Arrays.asList(1.34441, 1.64650, 2.17973, 2.73264, 3.48954, 5.07064, 7.34412, 10.21885, 13.36157, 15.50731, 17.53455, 20.09024, 21.95495));
        expectedValues.add(Arrays.asList(1.73493, 2.08790, 2.70039, 3.32511, 4.16816, 5.89883, 8.34283, 11.38875, 14.68366, 16.91898, 19.02277, 21.66599, 23.58935));
        expectedValues.add(Arrays.asList(2.15586, 2.55821, 3.24697, 3.94030, 4.86518, 6.73720, 9.34182, 12.54886, 15.98718, 18.30704, 20.48318, 23.20925, 25.18818));
        expectedValues.add(Arrays.asList(2.60322, 3.05348, 3.81575, 4.57481, 5.57778, 7.58414, 10.34100, 13.70069, 17.27501, 19.67514, 21.92005, 24.72497, 26.75685));
        expectedValues.add(Arrays.asList(3.07382, 3.57057, 4.40379, 5.22603, 6.30380, 8.43842, 11.34032, 14.84540, 18.54935, 21.02607, 23.33666, 26.21697, 28.29952));
        expectedValues.add(Arrays.asList(3.56503, 4.10692, 5.00875, 5.89186, 7.04150, 9.29907, 12.33976, 15.98391, 19.81193, 22.36203, 24.73560, 27.68825, 29.81947));
        expectedValues.add(Arrays.asList(4.07467, 4.66043, 5.62873, 6.57063, 7.78953, 10.16531, 13.33927, 17.11693, 21.06414, 23.68479, 26.11895, 29.14124, 31.31935));
        expectedValues.add(Arrays.asList(4.60092, 5.22935, 6.26214, 7.26094, 8.54676, 11.03654, 14.33886, 18.24509, 22.30713, 24.99579, 27.48839, 30.57791, 32.80132));
        expectedValues.add(Arrays.asList(5.14221, 5.81221, 6.90766, 7.96165, 9.31224, 11.91222, 15.33850, 19.36886, 23.54183, 26.29623, 28.84535, 31.99993, 34.26719));
        expectedValues.add(Arrays.asList(5.69722, 6.40776, 7.56419, 8.67176, 10.08519, 12.79193, 16.33818, 20.48868, 24.76904, 27.58711, 30.19101, 33.40866, 35.71847));
        expectedValues.add(Arrays.asList(6.26480, 7.01491, 8.23075, 9.39046, 10.86494, 13.67529, 17.33790, 21.60489, 25.98942, 28.86930, 31.52638, 34.80531, 37.15645));
        expectedValues.add(Arrays.asList(6.84397, 7.63273, 8.90652, 10.11701, 11.65091, 14.56200, 18.33765, 22.71781, 27.20357, 30.14353, 32.85233, 36.19087, 38.58226));
        expectedValues.add(Arrays.asList(7.43384, 8.26040, 9.59078, 10.85081, 12.44261, 15.45177, 19.33743, 23.82769, 28.41198, 31.41043, 34.16961, 37.56623, 39.99685));
        expectedValues.add(Arrays.asList(8.03365, 8.89720, 10.28290, 11.59131, 13.23960, 16.34438, 20.33723, 24.93478, 29.61509, 32.67057, 35.47888, 38.93217, 41.40106));
        expectedValues.add(Arrays.asList(8.64272, 9.54249, 10.98232, 12.33801, 14.04149, 17.23962, 21.33704, 26.03927, 30.81328, 33.92444, 36.78071, 40.28936, 42.79565));
        expectedValues.add(Arrays.asList(9.26042, 10.19572, 11.68855, 13.09051, 14.84796, 18.13730, 22.33688, 27.14134, 32.00690, 35.17246, 38.07563, 41.63840, 44.18128));
        expectedValues.add(Arrays.asList(9.88623, 10.85636, 12.40115, 13.84843, 15.65868, 19.03725, 23.33673, 28.24115, 33.19624, 36.41503, 39.36408, 42.97982, 45.55851));
        expectedValues.add(Arrays.asList(10.51965, 11.52398, 13.11972, 14.61141, 16.47341, 19.93934, 24.33659, 29.33885, 34.38159, 37.65248, 40.64647, 44.31410, 46.92789));
        expectedValues.add(Arrays.asList(11.16024, 12.19815, 13.84390, 15.37916, 17.29188, 20.84343, 25.33646, 30.43457, 35.56317, 38.88514, 41.92317, 45.64168, 48.28988));
        expectedValues.add(Arrays.asList(11.80759, 12.87850, 14.57338, 16.15140, 18.11390, 21.74940, 26.33634, 31.52841, 36.74122, 40.11327, 43.19451, 46.96294, 49.64492));
        expectedValues.add(Arrays.asList(12.46134, 13.56471, 15.30786, 16.92788, 18.93924, 22.65716, 27.33623, 32.62049, 37.91592, 41.33714, 44.46079, 48.27824, 50.99338));
        expectedValues.add(Arrays.asList(13.12115, 14.25645, 16.04707, 17.70837, 19.76774, 23.56659, 28.33613, 33.71091, 39.08747, 42.55697, 45.72229, 49.58788, 52.33562));
        expectedValues.add(Arrays.asList(13.78672, 14.95346, 16.79077, 18.49266, 20.59923, 24.47761, 29.33603, 34.79974, 40.25602, 43.77297, 46.97924, 50.89218, 53.67196));

        for (int degreesOfFreedom = 1; degreesOfFreedom < 30; degreesOfFreedom++) {
            System.out.print(String.format("%d degrees of freedom ", degreesOfFreedom));
            for (int i = 0; i < columnPercentages.size(); i++) {
                double actual = cst.getChiSquared(degreesOfFreedom, columnPercentages.get(i));
                System.out.println(String.format(" %f percent", columnPercentages.get(i)));
                assertTrue(nearEquivalent(expectedValues.get(degreesOfFreedom - 1).get(i), actual, 0.03));
            }
        }
    }

    @Test
    public void knuthTable() {
        ChiSquaredTable cst = new ChiSquaredTable.Builder().build();
        assertTrue(nearEquivalent(0.4549, cst.getChiSquared(1, .50), 0.01));
        assertTrue(nearEquivalent(1.386, cst.getChiSquared(2, .50), 0.01));
        assertTrue(nearEquivalent(2.366, cst.getChiSquared(3, .50), 0.01));
        assertTrue(nearEquivalent(3.357, cst.getChiSquared(4, .50), 0.01));
    }
}
