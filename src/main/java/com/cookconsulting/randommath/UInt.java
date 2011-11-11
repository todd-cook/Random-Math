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
 * A class to mimic an unsigned integer.
 * Java doesn't have native support for unsigned integers, however we can mimic the properties.
 * Unsigned integers are commonly used in network programming and random number generation.
 * <p/>
 * The method on this class are rather ugly, a scala implementation would be cleaner.
 *
 * @author Todd Cook
 * @since 9/4/2011
 */
public final class UInt implements Serializable {

    private static final long serialVersionUID = 5407738874045869976L;

    private long mValue;
    public static final long MAX_VALUE = 4294967295L;

    public UInt(long value) {
        this.mValue = value;
    }

    public UInt rightShift(int shiftValue) {
        String mValueBin = longToBinaryString(mValue, 32);
        String tmp = createZeroPad(shiftValue) + mValueBin;
        String newBinString = tmp.substring(0, 32);
        return new UInt(Long.parseLong(newBinString, 2));
    }

    private String longToBinaryString(long value, int maxLength) {
        String zeroPad = createZeroPad(maxLength);
        String tmp = zeroPad + Long.toBinaryString(value);
        return tmp.substring(tmp.length() - maxLength);
    }

    private static String createZeroPad(int length) {
        StringBuilder buf = new StringBuilder();
        for (int ii = 0; ii < length; ii++) {
            buf.append("0");
        }
        return buf.toString();
    }

    private final static String sixteenPad = createZeroPad(16);

    public UInt leftShift(int shiftValue) {
        String mValueBin = longToBinaryString(mValue, 32);
        String tmp = null;
        if (shiftValue == 16) {
            tmp = mValueBin + sixteenPad;
        }
        else {
            tmp = mValueBin + createZeroPad(shiftValue);
        }
        return new UInt(Long.parseLong((tmp.substring(tmp.length() - 32)), 2));
    }

    public UInt and(int shiftValue) {
        long tmp = mValue & shiftValue;
        if (tmp > MAX_VALUE) {
            tmp = tmp - MAX_VALUE;
        }
        return new UInt(tmp);
    }

    public UInt divide(long divisor) {
        return new UInt(this.longValue() / divisor);
    }

    public UInt add(long other) {
        return add(new UInt(other));
    }

    public UInt add(UInt other) {
        long tmp = this.longValue() + other.longValue();
        while (tmp > MAX_VALUE) {
            tmp = tmp - MAX_VALUE;
            tmp = tmp - 1;
            // subtract the extra one for the zero base index
        }
        return new UInt(tmp);
    }

    public UInt subtract(Long myLong) {
        return this.subtract(new UInt(myLong));
    }

    public UInt subtract(UInt other) {
        long tmp = this.longValue() - other.longValue();
        if (tmp < 0) {
            tmp = MAX_VALUE + tmp + 1;
        }
        return new UInt(tmp);
    }

    public UInt multiply(UInt multiplier) {
        return multiply(multiplier.longValue());
    }

    public UInt multiply(long multiplier) {
        long tmp = mValue * multiplier;
        while (tmp > MAX_VALUE) {
            tmp = tmp - MAX_VALUE;
            tmp = tmp - 1;
            // subtract the extra one for the zero base index
        }
        return new UInt(tmp);
    }


    public long longValue() {
        return mValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UInt uInt = (UInt) o;

        if (mValue != uInt.mValue) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (mValue ^ (mValue >>> 32));
    }

    @Override
    public String toString() {
        return Long.toString(mValue);
    }
}