package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * For integers that exceed the min and max values of 64 signed bits.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"CatchMayIgnoreException", "unused"})
public class Abigint implements AIvalue {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Abigint ZERO = new Abigint(BigInteger.ZERO);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final BigInteger value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abigint(BigInteger val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.BIGINT;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AIvalue)) {
            return false;
        }
        return value.equals(((AIvalue) o).toBigInt());
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isBigInteger() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public BigInteger toBigInt() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return value.intValue() != 0;
    }

    @Override
    public double toDouble() {
        return value.doubleValue();
    }

    @Override
    public float toFloat() {
        return value.floatValue();
    }

    @Override
    public int toInt() {
        return value.intValue();
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public Number toNumber() {
        return value;
    }

    /**
     * Attempts to return Along, but if out of bounds returns Astr.
     */
    @Override
    public Aprimitive toPrimitive() {
        if (value.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            return Astr.valueOf(toString());
        }
        if (value.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
            return Astr.valueOf(toString());
        }
        return Along.valueOf(value.longValue());
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Will convert attempt to convert numbers and strings, otherwise returns null.
     */
    @Override
    public Abigint valueOf(Aprimitive value) {
        try {
            switch (value.aonType()) {
                case DOUBLE:
                    double d = value.toDouble();
                    if (d % 1 == 0) {
                        return valueOf(BigInteger.valueOf(value.toLong()));
                    }
                    return null;
                case FLOAT:
                    float f = value.toFloat();
                    if (f % 1 == 0) {
                        return valueOf(BigInteger.valueOf(value.toLong()));
                    }
                    return null;
                case INT:
                case LONG:
                    return valueOf(BigInteger.valueOf(value.toLong()));
                case STRING:
                    return valueOf(new BigInteger(value.toString()));
            }
        } catch (Exception x) {
        }
        return null;
    }

    public static Abigint valueOf(BigInteger arg) {
        if (arg.equals(BigInteger.ZERO)) {
            return ZERO;
        }
        return new Abigint(arg);
    }

}
