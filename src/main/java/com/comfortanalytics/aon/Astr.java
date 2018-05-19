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

/**
 * String wrapper.
 *
 * @author Aaron Hansen
 */
class Astr extends Aobj {

    // Constants
    // ---------

    public static final Astr EMPTY = new Astr("");

    // Fields
    // ------

    private String value;


    // Constructors
    // ------------

    Astr(String val) {
        if (val == null) throw new NullPointerException("Null not allowed");
        value = val;
    }

    // Public Methods
    // --------------

    @Override
    public Atype aonType() {
        return Atype.STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Aobj)) return false;
        Aobj obj = (Aobj) o;
        if (obj.aonType() != Atype.STRING) return false;
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

    public static Astr make(String arg) {
        if (arg.isEmpty()) return EMPTY;
        return new Astr(arg);
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

    @Override
    public String toString() {
        return value;
    }


}