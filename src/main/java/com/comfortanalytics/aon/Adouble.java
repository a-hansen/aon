/* ISC License
 *
 * Copyright 2017 by Comfort Analytics, LLC.
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with
 * or without fee is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN
 * NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents a double value.
 *
 * @author Aaron Hansen
 */
public class Adouble extends Avalue {

    // Constants
    // ---------

    // Fields
    // ------

    private double value;

    // Constructors
    // ------------

    Adouble(double val) {
        value = val;
    }

    // Public Methods
    // --------------

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

    // Inner Classes
    // --------------

    private static class DblCache {

        private static final Adouble NEG_ONE = new Adouble(-1);
        private static final Adouble[] cache = new Adouble[101];

        public static Adouble get(int i) {
            if ((i < 0) || (i > 100)) {
                if (i == -1) {
                    return NEG_ONE;
                }
                return null;
            }
            return cache[i];
        }

        static {
            for (int i = 101; --i >= 0; ) {
                cache[i] = new Adouble(i);
            }
        }
    }

}//Adbl
