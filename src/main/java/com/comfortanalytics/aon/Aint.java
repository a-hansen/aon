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

    private static final Aint[] CACHE = new Aint[256];
    public static final Aint MAX = new Aint(Integer.MAX_VALUE);
    public static final Aint MIN = new Aint(Integer.MIN_VALUE);
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
        if (!(o instanceof Adata)) {
            return false;
        }
        Adata obj = (Adata) o;
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
        int idx = arg + 128;
        if ((idx < 0) || (idx > 255)) {
            if (arg == Integer.MAX_VALUE) {
                return MAX;
            }
            if (arg == Integer.MIN_VALUE) {
                return MIN;
            }
            return new Aint(arg);
        }
        Aint ret = CACHE[idx];
        if (ret == null) {
            ret = new Aint(arg);
            CACHE[idx] = ret;
        }
        return ret;
    }

}
