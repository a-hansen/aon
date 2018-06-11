package com.comfortanalytics.aon;

import com.comfortanalytics.aon.json.JsonAppender;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Base representation for all Aon data types.  Use Alist and Aobj to create data
 * structures.
 * <p>
 * Be aware that when encoding/decoding, if the underlying format (such as JSON)
 * doesn't provide a mechanism to differentiate between data types (such as numbers),
 * values might not decode as the same type they were encoded.
 * </p>
 *
 * @author Aaron Hansen
 */
public abstract class Avalue {

    /**
     * For switch statements.
     */
    public abstract Atype aonType();

    /**
     * If an object is mutable (list or map) then this should clone it,
     * immutable objects can simply return themselves.
     */
    public Avalue copy() {
        return this;
    }

    /**
     * Whether or not the object represents a BigDecimal.
     */
    public boolean isBigDecimal() {
        return false;
    }

    /**
     * Whether or not the object represents a BigDecimal.
     */
    public boolean isBigInteger() {
        return false;
    }

    /**
     * Whether or not the object represents a byte[].
     */
    public boolean isBinary() {
        return false;
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
     * Whether or not the object represents a double.
     */
    public boolean isFloat() {
        return false;
    }

    /**
     * Whether or not the object represents a list or map.
     */
    public boolean isGroup() {
        return false;
    }

    /**
     * Whether or not the object represents an int.
     */
    public boolean isInt() {
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
     * Whether or not the object represents an object.
     */
    public boolean isObj() {
        return false;
    }

    /**
     * Whether or not the object represents a string.
     */
    public boolean isString() {
        return false;
    }

    /**
     * Attempts to return a BigDecimal.  Will convert other numeric types.
     *
     * @throws ClassCastException If not convertible.
     */
    public BigDecimal toBigDecimal() {
        throw new ClassCastException(getClass().getName() + " not big decimal");
    }

    /**
     * Attempts to return a BigInteger.  Will convert other numeric types.
     *
     * @throws ClassCastException If not convertible.
     */
    public BigInteger toBigInt() {
        throw new ClassCastException(getClass().getName() + " not big int");
    }

    /**
     * Binaries return themselves, everything else results in a class cast exception.
     *
     * @throws ClassCastException If not convertible.
     */
    public Abinary toBinary() {
        throw new ClassCastException(getClass().getName() + " not binary");
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
    public Aobj toObj() {
        throw new ClassCastException(getClass().getName() + " not map");
    }

    /**
     * Appends the JSON encoding to the given buffer.
     */
    public void toString(Appendable buf) {
        new JsonAppender(buf).value(this);
    }


}
