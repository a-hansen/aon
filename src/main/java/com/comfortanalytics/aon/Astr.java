package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    private Astr(String val) {
        if (val == null) {
            throw new NullPointerException("Null not allowed");
        }
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

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
        if (value.equals("0")) {
            return false;
        } else if (value.equals("1")) {
            return true;
        } else if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return true;
        } else if (value.equalsIgnoreCase("on")) {
            return true;
        } else if (value.equalsIgnoreCase("off")) {
            return false;
        }
        return false;
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

    @Override
    public String toString() {
        return value;
    }

    public static Astr valueOf(String arg) {
        if (arg == null) {
            return null;
        }
        if (arg.length() == 0) {
            return EMPTY;
        }
        return new Astr(arg);
    }

}
