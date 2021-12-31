package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * Marker interface for all Aon compatible data types.  Aprimitives map to the JSON type system,
 * however there may be types that serialize themselves as Aprimitives but represent different Java
 * classes.  For example, JSON can't distinguish between a float and a double, so Aon uses Adouble
 * as the primitive while Afloat is a non-primitive that implements this interface.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"unused", "unchecked"})
public interface Adata {

    /**
     * The value type for the value returned by toValue.
     */
    @Nonnull
    Atype aonType();

    /**
     * If an object is mutable (list or object) then this should clone it, immutable objects can
     * simply return themselves.
     */
    @Nonnull
    default Adata copy() {
        return this;
    }

    /**
     * A convenience for getting the value being wrapped.  Agroups will return themselves.
     *
     * @throws ClassCastException Be careful
     */
    default <T> T get() {
        return (T) this;
    }

    /**
     * Whether or not the object represents a BigDecimal.
     */
    default boolean isBigDecimal() {
        return false;
    }

    /**
     * Whether or not the object represents a BigDecimal.
     */
    default boolean isBigInteger() {
        return false;
    }

    /**
     * Whether or not the object represents a byte[].
     */
    default boolean isBinary() {
        return false;
    }

    /**
     * Whether or not the object represents a boolean.
     */
    default boolean isBoolean() {
        return false;
    }

    /**
     * Whether or not the object represents a double.
     */
    default boolean isDouble() {
        return false;
    }

    /**
     * Whether or not the object represents a double.
     */
    default boolean isFloat() {
        return false;
    }

    /**
     * Whether or not the object represents a list or object.
     */
    default boolean isGroup() {
        return false;
    }

    /**
     * Whether or not the object represents an int.
     */
    default boolean isInt() {
        return false;
    }

    /**
     * Whether or not the object represents a list.
     */
    default boolean isList() {
        return false;
    }

    /**
     * Whether or not the object represents a long.  Be careful, longs can deserialize as ints.
     */
    default boolean isLong() {
        return false;
    }

    /**
     * Whether or not the object represents null.
     */
    default boolean isNull() {
        return false;
    }

    /**
     * Whether or not the object represents a number.
     */
    default boolean isNumber() {
        return false;
    }

    /**
     * Whether or not the object represents an object.
     */
    default boolean isObj() {
        return false;
    }

    /**
     * The primitives are JSON compatible data types.
     */
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Whether or not the object represents a string.
     */
    default boolean isString() {
        return false;
    }

    /**
     * Attempts to return a BigDecimal.  Will convert other numeric types.
     *
     * @throws ClassCastException If not convertible.
     */
    default BigDecimal toBigDecimal() {
        throw new ClassCastException(getClass().getName() + " not big decimal");
    }

    /**
     * Attempts to return a BigInteger.  Will convert other numeric types.
     *
     * @throws ClassCastException If not convertible.
     */
    default BigInteger toBigInt() {
        throw new ClassCastException(getClass().getName() + " not big int");
    }

    /**
     * Binaries return themselves, everything else results in a class cast exception.
     *
     * @throws ClassCastException If not convertible.
     */
    default Abinary toBinary() {
        throw new ClassCastException(getClass().getName() + " not binary");
    }

    /**
     * Attempts to return a boolean value.  Numerics will return false for 0 and true for anything
     * else.  Strings should return true for "true" or "1" and false for "false" or "0".  Anything
     * else will throws a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default boolean toBoolean() {
        throw new ClassCastException(getClass().getName() + " not boolean");
    }

    /**
     * Attempts to return a double value.  Numerics of other types will cast the results. Booleans
     * will return 0 for false and 1 for true. Strings will attempt to parse the numeric which may
     * result in a parse exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default double toDouble() {
        throw new ClassCastException(getClass().getName() + " not double");
    }

    /**
     * Attempts to return a float value.  Numerics of other types will cast the results. Booleans
     * will return 0 for false and 1 for true. Strings will attempt to parse the numeric which may
     * result in a parse exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default float toFloat() {
        throw new ClassCastException(getClass().getName() + " not float");
    }

    /**
     * Lists and objects return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    default Agroup toGroup() {
        throw new ClassCastException(getClass().getName() + " not list");
    }

    /**
     * Attempts to return an int value.  Numerics of other types will cast the results. Booleans
     * will return 0 for false and 1 for true. Strings will attempt to parse the numeric which may
     * result in a parse exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default int toInt() {
        throw new ClassCastException(getClass().getName() + " not int");
    }

    /**
     * Lists return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    default Alist toList() {
        throw new ClassCastException(getClass().getName() + " not list");
    }

    /**
     * Attempts to return a long value.  Numerics of other types will cast the results. Booleans
     * will return 0 for false and 1 for true. Strings will attempt to parse the numeric which may
     * result in a parse exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default long toLong() {
        throw new ClassCastException(getClass().getName() + " not long");
    }

    /**
     * Attempts to return number.  Actual numbers return their value. Booleans will return 0 for
     * false and 1 for true. Strings will attempt to parse the numeric which may result in a parse
     * exception.  Anything else will throw a ClassCastException.
     *
     * @throws ClassCastException If not convertible.
     */
    default Number toNumber() {
        throw new ClassCastException(getClass().getName() + " not number");
    }

    /**
     * Objects return themselves, everything else results in an exception.
     *
     * @throws ClassCastException If not convertible.
     */
    default Aobj toObj() {
        throw new ClassCastException(getClass().getName() + " not object");
    }

    /**
     * Values that are not Aprimitives must be able to convert themselves.
     */
    @Nonnull
    Aprimitive toPrimitive();

    @Nonnull
    @Override
    String toString();

    /**
     * Convert the arg to the proper type.
     *
     * @return Anull.NULL if null, or the arg.
     */
    default Adata valueOf(Aprimitive arg) {
        if (arg == null) {
            return Anull.NULL;
        }
        return arg;
    }

}
