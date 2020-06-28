package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * Long value.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Along extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Along ZERO = valueOf(0);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final long value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Along(long val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.LONG;
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
    public Long get() {
        return value;
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public boolean isLong() {
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
        return (int) value;
    }

    @Override
    public long toLong() {
        return value;
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
    public static Along valueOf(long arg) {
        Along ret = LongCache.get(arg);
        if (ret == null) {
            ret = new Along(arg);
        }
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    private static class LongCache {

        private static final int MAX = 100;
        private static final Along NEG_ONE = new Along(-1);
        private static Along[] cache = null;

        public static Along get(long l) {
            if ((l < 0) || (l > MAX)) {
                if (l == -1) {
                    return NEG_ONE;
                }
                return null;
            }
            int i = (int) l;
            Along ret;
            if (cache == null) {
                cache = new Along[MAX + 1];
            }
            ret = cache[i];
            if (ret == null) {
                ret = new Along(l);
                cache[i] = ret;
            }
            return ret;
        }

    }

}
