package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Integer value.
 *
 * @author Aaron Hansen
 */
public class Aint extends Avalue {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private int value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Aint(int val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.INT;
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
        return value;
    }

    @Override
    public boolean isInt() {
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
        return BigInteger.valueOf(value);
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
        return value;
    }

    @Override
    public long toLong() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Attempts to reuse some common values before creating a new instance.
     */
    public static Aint valueOf(int arg) {
        Aint ret = IntCache.get(arg);
        if (ret == null) {
            ret = new Aint(arg);
        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    private static class IntCache {

        private static final int MAX = 100;
        private static final Aint NEG_ONE = new Aint(-1);
        private static final Aint[] cache = new Aint[MAX + 1];

        public static Aint get(int i) {
            if (i == -1) {
                return NEG_ONE;
            }
            if (i > MAX) {
                return null;
            }
            if (cache[i] == null) {
                cache[i] = new Aint(i);
            }
            return cache[i];
        }

    }

}
