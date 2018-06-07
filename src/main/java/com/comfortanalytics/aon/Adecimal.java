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
 * Represents a BigDecimal.
 *
 * @author Aaron Hansen
 */
public class Adecimal extends Avalue {

    // Fields
    // ------

    private BigDecimal value;

    // Constructors
    // ------------

    Adecimal(BigDecimal val) {
        value = val;
    }

    // Public Methods
    // --------------

    @Override
    public Atype aonType() {
        return Atype.DECIMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Avalue)) {
            return false;
        }
        Avalue obj = (Avalue) o;
        if (obj.isNumber()) {
            return value.equals(obj.toBigDecimal());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isBigDecimal() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf(value.longValue());
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
    public String toString() {
        return String.valueOf(value);
    }

    public static Adecimal valueOf(BigDecimal arg) {
        return new Adecimal(arg);
    }

}
