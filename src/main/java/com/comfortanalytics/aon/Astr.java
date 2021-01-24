package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * String value.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Astr extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Astr EMPTY = new Astr("");

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final String value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Astr(@Nonnull String val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Aprimitive)) {
            return false;
        }
        Aprimitive obj = (Aprimitive) o;
        if (obj.aonType() != Atype.STRING) {
            return false;
        }
        return value.equals(obj.toString());
    }

    @Nonnull
    @Override
    public String get() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isString() {
        return true;
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public BigInteger toBigInt() {
        return new BigInteger(value);
    }

    @Override
    public boolean toBoolean() {
        return Abool.valueOf(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public double toDouble() {
        return Double.parseDouble(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public float toFloat() {
        return Float.parseFloat(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public int toInt() {
        return Integer.parseInt(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public long toLong() {
        return Long.parseLong(value);
    }

    /**
     * Attempts to parse the number so may throw an exception.
     */
    @Override
    public Number toNumber() {
        return toBigDecimal();
    }

    @Nonnull
    @Override
    public String toString() {
        return value;
    }

    public static Astr valueOf(String arg) {
        if (arg == null) {
            return null;
        }
        if (arg.isEmpty()) {
            return EMPTY;
        }
        return new Astr(arg);
    }

}
