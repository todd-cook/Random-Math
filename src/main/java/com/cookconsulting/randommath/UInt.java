package com.cookconsulting.randommath;

/**
 * A class to mimic an unsigned integer.
 * Java doesn't have native support for unsigned integers, however we can mimic the properties.
 * Unsigned integers are commonly used in network programming and random number generation.
 * <p/>
 * The method on this class are rather ugly, see the scala implementation for a prettier version.
 *
 * @author Todd Cook
 * @since 9/4/2011
 */
public final class UInt {

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
        return new UInt(this.value() / divisor);
    }

    public UInt add(long other) {
        return add(new UInt(other));
    }

    public UInt add(UInt other) {
        long tmp = this.value() + other.value();
        while (tmp > MAX_VALUE) {
            tmp = tmp - MAX_VALUE;
            tmp = tmp - 1;
            // subtract the extra one for the zero base index
        }
        return new UInt(tmp);
    }

    public UInt subtract(UInt other) {
        long tmp = this.value() - other.value();
        if (tmp < 0) {
            tmp = MAX_VALUE - tmp;
        }
        return new UInt(tmp);
    }

    public UInt multiply(UInt multiplier) {
        return multiply(multiplier.value());
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

    public long value() {
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