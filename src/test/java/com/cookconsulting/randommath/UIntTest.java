package com.cookconsulting.randommath;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test a range of values and operations that a Unsigned Integer should produce
 *
 * @author Todd Cook
 * @since 9/4/11
 */
public class UIntTest {

    @Test
    public void shiftRight() {
        assertTrue(new UInt(1L).rightShift(16).value() == 0);
        assertTrue(new UInt(100).rightShift(16).value() == 0);
        assertTrue(new UInt(10000).rightShift(16).value() == 0);
        assertTrue(new UInt(10000000).rightShift(16).value() == 152);
        assertTrue(new UInt(1215752192).rightShift(16).value() == 18550);
        assertTrue(new UInt(1874919424).rightShift(16).value() == 28609);
        assertTrue(new UInt(2990538752L).rightShift(16).value() == 45632);
        assertTrue(new UInt(2684354560L).rightShift(16).value() == 40960);
        assertTrue(new UInt(4294967295L).rightShift(16).value() == 65535);
    }

    @Test
    public void and() {
        assertTrue(new UInt(1).and(65535).value() == 1);
        assertTrue(new UInt(100).and(65535).value() == 100);
        assertTrue(new UInt(10000).and(65535).value() == 10000);
        assertTrue(new UInt(10000000).and(65535).value() == 38528);
        assertTrue(new UInt(1215752192).and(65535).value() == 59392);
        assertTrue(new UInt(1874919424).and(65535).value() == 0);
        assertTrue(new UInt(2990538752L).and(65535).value() == 0);
        assertTrue(new UInt(2684354560L).and(65535).value() == 0);
        assertTrue(new UInt(4294967295L).and(65535).value() == 65535);
    }

    @Test
    public void leftShift() {
        assertTrue(new UInt(1).leftShift(16).value() == 65536);
        assertTrue(new UInt(100).leftShift(16).value() == 6553600);
        assertTrue(new UInt(10000).leftShift(16).value() == 655360000);
        assertTrue(new UInt(10000000).leftShift(16).value() == 2524971008L);
        assertTrue(new UInt(1215752192).leftShift(16).value() == 3892314112L);
        assertTrue(new UInt(1874919424).leftShift(16).value() == 0);
        assertTrue(new UInt(2990538752L).leftShift(16).value() == 0);
        assertTrue(new UInt(2684354560L).leftShift(16).value() == 0);
        assertTrue(new UInt(4294967295L).leftShift(16).value() == 4294901760L);

    }

    @Test
    public void multiply() {
        assertTrue(new UInt(10).multiply(10).value() == 100);
        assertTrue(new UInt(100).multiply(100).value() == 10000);
        assertTrue(new UInt(10000).multiply(1000).value() == 10000000);
        assertTrue(new UInt(10000000).multiply(10000).value() == 1215752192);
        assertTrue(new UInt(1215752192).multiply(100000).value() == 1874919424);
        assertTrue(new UInt(1874919424).multiply(1000000).value() == 2990538752L);
        assertTrue(new UInt(2990538752L).multiply(10000000).value() == 2684354560L);
        assertTrue(new UInt(2684354560L).multiply(100000000).value() == 0);
    }

    @Test
    public void add() {
        assertTrue(new UInt(10).add(10).value() == 20);
        assertTrue(new UInt(20).add(100).value() == 120);
        assertTrue(new UInt(120).add(1000).value() == 1120);
        assertTrue(new UInt(1120).add(10000).value() == 11120);
        assertTrue(new UInt(11120).add(100000).value() == 111120);
        assertTrue(new UInt(111120).add(1000000).value() == 1111120);
        assertTrue(new UInt(1111120).add(10000000).value() == 11111120);
        assertTrue(new UInt(11111120).add(100000000).value() == 111111120);
        assertTrue(new UInt(111111120).add(1000000000).value() == 1111111120);
        assertTrue(new UInt(2684354560L).add(1000000000).value() == 3684354560L);
        assertTrue(new UInt(2684354560L).add(10000000000L).value() == 4094419968L);
    }

    // TODO add subtract test

//
//
//    @Test
//    public void test() {
//
//        UInt myUInt = new UInt(1L);
//        myUInt = new UInt(10L);
//        for (long ii = 10; ii < ( UInt.MAX_VALUE *2); ii *= 10) {
//
//            UInt tmp = new UInt(ii);
//            System.out.println("assertTrue( new UInt(" + myUInt + ").add( " + tmp +").value() == " + myUInt.add(tmp)  +");" );
//            myUInt = myUInt.add(tmp);
//        if (myUInt.value() == 0) {
//                break;
//            }
//        }
//
//    }
}