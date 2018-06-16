package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * BigInteger value.
 *
 * @author Aaron Hansen
 */
public class Abigint extends Avalue {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private BigInteger value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abigint(BigInteger val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.BIGINT;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Avalue)) {
            return false;
        }
        Avalue obj = (Avalue) o;
        if (obj.isNumber()) {
            return value.equals(obj.toBigInt());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isBigInteger() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public BigInteger toBigInt() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return value.intValue() != 0;
    }

    @Override
    public double toDouble() {
        return value.doubleValue();
    }

    @Override
    public float toFloat() {
        return value.floatValue();
    }

    @Override
    public int toInt() {
        return value.intValue();
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public Number toNumber() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Abigint valueOf(BigInteger arg) {
        return new Abigint(arg);
    }

}
