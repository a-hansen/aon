package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import javax.annotation.Nonnull;

/**
 * Float value.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"unused"})
public class Afloat implements Adata {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Afloat MAX = new Afloat(Float.MAX_VALUE);
    public static final Afloat MIN = new Afloat(Float.MIN_VALUE);
    public static final Afloat ZERO = new Afloat(0);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final float value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Afloat(float val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.FLOAT;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Adata)) {
            return false;
        }
        Adata obj = (Adata) o;
        switch (obj.aonType()) {
            case DECIMAL:
            case BIGINT:
            case FLOAT:
                return obj.toFloat() == value;
            case DOUBLE:
                return obj.toDouble() == value;
            case INT:
                return obj.toInt() == value;
            case LONG:
                return obj.toLong() == value;
        }
        return false;
    }

    /**
     * Converts the float to a double such that the String value of both would be equal.
     */
    public static double formatDouble(float value) {
        return new BigDecimal(value, MathContext.DECIMAL32).doubleValue();
    }

    @Nonnull
    @Override
    public Float get() {
        return value;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(value);
    }

    @Override
    public boolean isFloat() {
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
        return value;
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

    /**
     * Returns Adouble
     */
    @Nonnull
    @Override
    public Aprimitive toPrimitive() {
        return Adouble.valueOf(value);
    }

    @Nonnull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Will convert numbers, otherwise returns null.
     */
    @Override
    public Afloat valueOf(Aprimitive value) {
        try {
            switch (value.aonType()) {
                case DOUBLE:
                    return valueOf((float) value.toDouble());
                case INT:
                    return valueOf(value.toInt());
                case LONG:
                    return valueOf(value.toLong());
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public static Afloat valueOf(float arg) {
        if (arg == 0) {
            return ZERO;
        } else if (arg == Float.MIN_VALUE) {
            return MIN;
        } else if (arg == Float.MAX_VALUE) {
            return MAX;
        }
        return new Afloat(arg);
    }

}
