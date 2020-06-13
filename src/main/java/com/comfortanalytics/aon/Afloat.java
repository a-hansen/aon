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
@SuppressWarnings({"CatchMayIgnoreException", "unused"})
public class Afloat implements AIvalue {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Afloat ZERO = FltCache.get(0);

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
        if (!(o instanceof AIvalue)) {
            return false;
        }
        AIvalue obj = (AIvalue) o;
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
        } catch (Exception x) {
        }
        return null;
    }

    /**
     * Attempts to reuse some common values before creating a new instance.
     */
    public static Afloat valueOf(float arg) {
        Afloat ret = null;
        int i = (int) arg;
        if (arg == i) {
            ret = FltCache.get(i);
        }
        if (ret == null) {
            ret = new Afloat(arg);
        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    private static class FltCache {

        private static final int MAX = 100;
        private static final Afloat NEG_ONE = new Afloat(-1);
        private static final Afloat[] cache = new Afloat[MAX + 1];

        public static Afloat get(int i) {
            if (i == -1) {
                return NEG_ONE;
            }
            if ((i < 0) || (i > MAX)) {
                return null;
            }
            if (cache[i] == null) {
                cache[i] = new Afloat(i);
            }
            return cache[i];
        }
    }

}
