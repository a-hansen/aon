package com.comfortanalytics.aon;


import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Boolean value.
 *
 * @author Aaron Hansen
 */
public class Abool extends Avalue {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Abool FALSE = new Abool(false);
    public static final Abool TRUE = new Abool(true);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private boolean value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abool(boolean val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.BOOLEAN;
    }

    @Override
    public boolean equals(Object arg) {
        return arg == this;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    /**
     * 0 or 1.
     */
    @Override
    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(toInt());
    }

    /**
     * 0 or 1.
     */
    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf(toInt());
    }

    @Override
    public boolean toBoolean() {
        return value;
    }

    /**
     * 0 or 1.
     */
    @Override
    public double toDouble() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public float toFloat() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public int toInt() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public long toLong() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public Number toNumber() {
        return toInt();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Will return either TRUE or FALSE.
     */
    public static Abool valueOf(boolean arg) {
        if (arg) {
            return TRUE;
        }
        return FALSE;
    }


}
