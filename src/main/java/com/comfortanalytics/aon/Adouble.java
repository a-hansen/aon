package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * IEEE 754 floating-point "double format" bit layout.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Adouble extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Adouble MAX = new Adouble(Double.MAX_VALUE);
    public static final Adouble MIN = new Adouble(Double.MIN_VALUE);
    public static final Adouble ZERO = new Adouble(0);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final double value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Adouble(double val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.DOUBLE;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Aprimitive) {
            Aprimitive obj = (Aprimitive) o;
            switch (obj.aonType()) {
                case DECIMAL:
                case BIGINT:
                    return obj.equals(this);
                case DOUBLE:
                    return obj.toDouble() == value;
                case FLOAT:
                    return obj.toFloat() == value;
                case INT:
                    return obj.toInt() == value;
                case LONG:
                    return obj.toLong() == value;
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public Double get() {
        return value;
    }

    @Override
    public int hashCode() {
        long v = Double.doubleToLongBits(value);
        return (int) (v ^ (v >>> 32));
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(value);
    }

    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf((long) value);
    }

    @Override
    public boolean toBoolean() {
        return value != 0;
    }

    @Override
    public double toDouble() {
        return value;
    }

    @Override
    public float toFloat() {
        return (float) value;
    }

    @Override
    public int toInt() {
        return (int) value;
    }

    @Override
    public long toLong() {
        return (long) value;
    }

    @Override
    public Number toNumber() {
        return value;
    }

    @Nonnull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Attempts to reuse some common values before creating a new instance.
     */
    public static Adouble valueOf(double arg) {
        if (arg == 0) {
            return ZERO;
        } else if (arg == Double.MIN_VALUE) {
            return MIN;
        } else if (arg == Double.MAX_VALUE) {
            return MAX;
        }
        return new Adouble(arg);
    }

}
