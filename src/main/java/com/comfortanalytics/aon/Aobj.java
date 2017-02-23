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
 * Base representation for all Aon data types.  Use Alist and Amap to create data
 * structures.
 * <p>
 * Be aware that when encoding/decoding, if the underlying format (such as JSON)
 * doesn't provide a mechanism to differentiate between data types (such as numbers),
 * values might not decode as the same type they were encoded.
 * </p>
 *
 * @author Aaron Hansen
 */
public abstract class Aobj {

    // Constants
    // ---------

    // Fields
    // ------

    // Constructors
    // ------------

    // Methods
    // -------

    /**
     * For switch statements.
     */
    public abstract Atype aonType();

    /**
     * If an object is mutable (list or map) then this should clone it,
     * immutable objects can simply return themselves.
     */
    public Aobj copy() {
        return this;
    }

    /**
     * Whether or not the object represents a boolean.
     */
    public boolean isBoolean() {
        return false;
    }

    /**
     * Whether or not the object represents a double.
     */
    public boolean isDouble() {
        return false;
    }

    /**
     * Whether or not the object represents an int.
     */
    public boolean isInt() {
        return false;
    }

    /**
     * Whether or not the object represents a list or map.
     */
    public boolean isGroup() {
        return false;
    }

    /**
     * Whether or not the object represents a list.
     */
    public boolean isList() {
        return false;
    }

    /**
     * Whether or not the object represents a long.  Be careful, longs can deserialize
     * as ints.
     */
    public boolean isLong() {
        return false;
    }

    /**
     * Whether or not the object represents a amp.
     */
    public boolean isMap() {
        return false;
    }

    /**
     * Whether or not the object represents null.
     */
    public boolean isNull() {
        return false;
    }

    /**
     * Whether or not the object represents a number.
     */
    public boolean isNumber() {
        return false;
    }

    /**
     * Whether or not the object represents a string.
     */
    public boolean isString() {
        return false;
    }

    /**
     * Creates an Aobj representation of the the primitive.
     */
    public static Aobj make(boolean arg) {
        return Abool.make(arg);
    }

    /**
     * Creates an Aobj representation of the the primitive.
     */
    public static Aobj make(double arg) {
        return Adbl.make(arg);
    }

    /**
     * Creates an Aobj representation of the the primitive.
     */
    public static Aobj make(int arg) {
        return Aint.make(arg);
    }

    /**
     * Creates an Aobj representation of the the primitive.
     */
    public static Aobj make(long arg) {
        return Along.make(arg);
    }

    /**
     * Creates an Aobj representation of the the primitive.
     */
    public static Aobj make(String arg) {
        if (arg == null) {
            return makeNull();
        }
        return Astr.make(arg);
    }

    /**
     * Creates an Aobj representation of null.
     */
    public static Aobj makeNull() {
        return Anull.NULL;
    }

    /**
     * Attempts to return a boolean value.  Numerics will return false for 0 and true for
     * anything else.  Strings should return true for "true" or "1" and false for
     * "false" or "0".  Anything else will throws a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    public boolean toBoolean() {
        throw new ClassCastException(getClass().getName() + " not boolean");
    }

    /**
     * Attempts to return a double value.  Numerics of other types will cast the results.
     * Booleans will return 0 for false and 1 for true.
     * Strings will attempt to parse the numeric which may result in a parse
     * exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    public double toDouble() {
        throw new ClassCastException(getClass().getName() + " not double");
    }

    /**
     * Attempts to return a float value.  Numerics of other types will cast the results.
     * Booleans will return 0 for false and 1 for true.
     * Strings will attempt to parse the numeric which may result in a parse
     * exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    public float toFloat() {
        throw new ClassCastException(getClass().getName() + " not float");
    }

    /**
     * Lists and maps return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    public Agroup toGroup() {
        throw new ClassCastException(getClass().getName() + " not list");
    }

    /**
     * Attempts to return an int value.  Numerics of other types will cast the results.
     * Booleans will return 0 for false and 1 for true.
     * Strings will attempt to parse the numeric which may result in a parse
     * exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    public int toInt() {
        throw new ClassCastException(getClass().getName() + " not int");
    }

    /**
     * Lists return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    public Alist toList() {
        throw new ClassCastException(getClass().getName() + " not list");
    }

    /**
     * Attempts to return a long value.  Numerics of other types will cast the results.
     * Booleans will return 0 for false and 1 for true.
     * Strings will attempt to parse the numeric which may result in a parse
     * exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    public long toLong() {
        throw new ClassCastException(getClass().getName() + " not long");
    }

    /**
     * Maps return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    public Amap toMap() {
        throw new ClassCastException(getClass().getName() + " not map");
    }


    // Inner Classes
    // -------------


}
