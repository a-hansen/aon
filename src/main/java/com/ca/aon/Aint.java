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
package com.ca.aon;

/**
 * @author Aaron Hansen
 */
class Aint extends Aobj {

    // Constants
    // ---------

    // Fields
    // ------

    private int value;


    // Constructors
    // ------------

    private Aint(int val) {
        value = val;
    }

    // Public Methods
    // --------------

    @Override
    public Atype aonType() {
        return Atype.INT;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Aobj)) return false;
        Aobj obj = (Aobj) o;
        switch (obj.aonType()) {
            case DOUBLE:
                return obj.toDouble() == value;
            case INT:
                return obj.toInt() == value;
            case LONG:
                return obj.toLong() == value;
        }
        return false;
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    /**
     * Attempts to reuse some common values before creating a new instance.
     */
    public static Aint make(int arg) {
        Aint ret = null;
        int i = (int) arg;
        if (arg == i) ret = IntCache.get(i);
        if (ret == null) ret = new Aint(arg);
        return ret;
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
    public String toString() {
        return String.valueOf(value);
    }


    // Inner Classes
    // --------------

    private static class IntCache {
        private static final Aint[] cache = new Aint[101];
        private static final Aint NEG_ONE = new Aint(-1);

        public static Aint get(int i) {
            if ((i < 0) || (i > 100)) {
                if (i == -1) return NEG_ONE;
                return null;
            }
            return cache[i];
        }

        static {
            for (int i = 101; --i >= 0; )
                cache[i] = new Aint(i);
        }
    }

}
