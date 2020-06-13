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

    public static final Adouble ZERO = DblCache.get(0);

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
        if (!(o instanceof Aprimitive)) {
            return false;
        }
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
        Adouble ret = null;
        int i = (int) arg;
        if (arg == i) {
            ret = DblCache.get(i);
        }
        if (ret == null) {
            ret = new Adouble(arg);
        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    private static class DblCache {

        private static final int MAX = 100;
        private static final Adouble NEG_ONE = new Adouble(-1);
        private static final Adouble[] cache = new Adouble[MAX + 1];

        public static Adouble get(int i) {
            if (i == -1) {
                return NEG_ONE;
            }
            if ((i < 0) || (i > MAX)) {
                return null;
            }
            if (cache[i] == null) {
                cache[i] = new Adouble(i);
            }
            return cache[i];
        }

    }

}
