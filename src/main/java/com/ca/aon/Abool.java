/* Copyright 2017 by Aaron Hansen.
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

package com.ca.aon;


/**
 * Represents a boolean value.
 *
 * @author Aaron Hansen
 */
class Abool extends Aobj {

    // Constants
    // ---------

    public static final Abool TRUE = new Abool(true);
    public static final Abool FALSE = new Abool(false);

    // Fields
    // ------

    private boolean value;


    // Constructors
    // ------------

    private Abool(boolean val) {
        value = val;
    }

    // Public Methods
    // --------------

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
     * Will return either TRUE or FALSE.
     */
    public static Abool make(boolean arg) {
        if (arg) return TRUE;
        return FALSE;
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
        if (value) return 1;
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public float toFloat() {
        if (value) return 1;
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public int toInt() {
        if (value) return 1;
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public long toLong() {
        if (value) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }


}
