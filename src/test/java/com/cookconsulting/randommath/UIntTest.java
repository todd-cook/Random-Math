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

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test a range of values and operations that a Unsigned Integer should produce
 *
 * @author Todd Cook
 * @since 2011/4/9
 */
public class UIntTest {

    @Test
    public void shiftRight() {
        assertTrue(new UInt(1L).rightShift(16).longValue() == 0);
        assertTrue(new UInt(100).rightShift(16).longValue() == 0);
        assertTrue(new UInt(10000).rightShift(16).longValue() == 0);
        assertTrue(new UInt(10000000).rightShift(16).longValue() == 152);
        assertTrue(new UInt(1215752192).rightShift(16).longValue() == 18550);
        assertTrue(new UInt(1874919424).rightShift(16).longValue() == 28609);
        assertTrue(new UInt(2990538752L).rightShift(16).longValue() == 45632);
        assertTrue(new UInt(2684354560L).rightShift(16).longValue() == 40960);
        assertTrue(new UInt(4294967295L).rightShift(16).longValue() == 65535);
    }

    @Test
    public void and() {
        assertTrue(new UInt(1).and(65535).longValue() == 1);
        assertTrue(new UInt(100).and(65535).longValue() == 100);
        assertTrue(new UInt(10000).and(65535).longValue() == 10000);
        assertTrue(new UInt(10000000).and(65535).longValue() == 38528);
        assertTrue(new UInt(1215752192).and(65535).longValue() == 59392);
        assertTrue(new UInt(1874919424).and(65535).longValue() == 0);
        assertTrue(new UInt(2990538752L).and(65535).longValue() == 0);
        assertTrue(new UInt(2684354560L).and(65535).longValue() == 0);
        assertTrue(new UInt(4294967295L).and(65535).longValue() == 65535);
    }

    @Test
    public void leftShift() {
        assertTrue(new UInt(1).leftShift(16).longValue() == 65536);
        assertTrue(new UInt(100).leftShift(16).longValue() == 6553600);
        assertTrue(new UInt(10000).leftShift(16).longValue() == 655360000);
        assertTrue(new UInt(10000000).leftShift(16).longValue() == 2524971008L);
        assertTrue(new UInt(1215752192).leftShift(16).longValue() == 3892314112L);
        assertTrue(new UInt(1874919424).leftShift(16).longValue() == 0);
        assertTrue(new UInt(2990538752L).leftShift(16).longValue() == 0);
        assertTrue(new UInt(2684354560L).leftShift(16).longValue() == 0);
        assertTrue(new UInt(4294967295L).leftShift(16).longValue() == 4294901760L);
    }

    @Test
    public void multiply() {
        assertTrue(new UInt(10).multiply(10).longValue() == 100);
        assertTrue(new UInt(100).multiply(100).longValue() == 10000);
        assertTrue(new UInt(10000).multiply(1000).longValue() == 10000000);
        assertTrue(new UInt(10000000).multiply(10000).longValue() == 1215752192);
        assertTrue(new UInt(1215752192).multiply(100000).longValue() == 1874919424);
        assertTrue(new UInt(1874919424).multiply(1000000).longValue() == 2990538752L);
        assertTrue(new UInt(2990538752L).multiply(10000000).longValue() == 2684354560L);
        assertTrue(new UInt(2684354560L).multiply(100000000).longValue() == 0);
    }

    @Test
    public void add() {
        assertTrue(new UInt(10).add(10).longValue() == 20);
        assertTrue(new UInt(20).add(100).longValue() == 120);
        assertTrue(new UInt(120).add(1000).longValue() == 1120);
        assertTrue(new UInt(1120).add(10000).longValue() == 11120);
        assertTrue(new UInt(11120).add(100000).longValue() == 111120);
        assertTrue(new UInt(111120).add(1000000).longValue() == 1111120);
        assertTrue(new UInt(1111120).add(10000000).longValue() == 11111120);
        assertTrue(new UInt(11111120).add(100000000).longValue() == 111111120);
        assertTrue(new UInt(111111120).add(1000000000).longValue() == 1111111120);
        assertTrue(new UInt(2684354560L).add(1000000000).longValue() == 3684354560L);
        assertTrue(new UInt(2684354560L).add(10000000000L).longValue() == 4094419968L);
    }

    @Test
    public void testSubtract() {
        UInt myUInt = new UInt(1L);
        System.out.println(myUInt.subtract(UInt.MAX_VALUE).longValue());
        myUInt = new UInt(2L);
        System.out.println(myUInt.subtract(UInt.MAX_VALUE).longValue());
        assertTrue(myUInt.subtract(UInt.MAX_VALUE).equals(new UInt(3L)));
        myUInt = new UInt(3L);
        System.out.println(myUInt.subtract(UInt.MAX_VALUE).longValue());
        assertTrue(myUInt.subtract(UInt.MAX_VALUE).equals(new UInt(4L)));
        UInt three = new UInt(3L);
        UInt two = new UInt(2L);
        System.out.println(two.subtract(three).longValue());
        assertTrue(two.subtract(three).equals(new UInt(4294967295L)));
    }
}