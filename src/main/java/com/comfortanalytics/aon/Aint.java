package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * Integer value.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Aint extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Aint ZERO = valueOf(0);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final int value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Aint(int val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.INT;
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
            case INT:
                return obj.toInt() == value;
            case DOUBLE:
                return obj.toDouble() == value;
            case FLOAT:
                return obj.toFloat() == value;
            case LONG:
                return obj.toLong() == value;
        }
        return false;
    }

    @Nonnull
    @Override
    public Integer get() {
        return value;
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
    public Number toNumber() {
        return value;
    }

    /**
     * Returns Along
     */
    @Nonnull
    @Override
    public Aprimitive toPrimitive() {
        return Along.valueOf(value);
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
    public Aint valueOf(Aprimitive value) {
        if (value.isNumber()) {
            return valueOf(value.toNumber().intValue());
        }
        return null;
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
        private static Aint[] cache;

        public static Aint get(int i) {
            if ((i < 0) || (i > MAX)) {
                if (i == -1) {
                    return NEG_ONE;
                }
                return null;
            }
            Aint ret;
            if (cache == null) {
                cache = new Aint[MAX + 1];
            }
            ret = cache[i];
            if (ret == null) {
                ret = new Aint(i);
                cache[i] = ret;
            }
            return ret;
        }

    }

}
