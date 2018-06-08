package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Double value.
 *
 * @author Aaron Hansen
 */
public class Adouble extends Avalue {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private double value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Adouble(double val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.DOUBLE;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Avalue)) {
            return false;
        }
        Avalue obj = (Avalue) o;
        switch (obj.aonType()) {
            case DECIMAL:
                return obj.equals(this);
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
            if (i > MAX) {
                return null;
            }
            if (cache[i] == null) {
                cache[i] = new Adouble(i);
            }
            return cache[i];
        }

    }

}
