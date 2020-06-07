package com.comfortanalytics.aon;

import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A decoder that can be used to decode an entire graph in pieces, or one large
 * Avalue, or somewhere in between. To decode an entire graph, call getObj(), getList()
 * or getValue(). Otherwise, use the next() method to iterate the elements of the input
 * document.
 * <p>
 * When next()/last() returns:
 * <ul>
 * <li>ROOT - The root state, not in a list or obj, call next() to start iterating
 * or getList(), getObj() or getValue() to decode the entire document.
 * <li>BEGIN_LIST - Call getList() to decode the entire list, or call next() again to get
 * the first element of the list (or END_LIST if empty).
 * <li>BEGIN_OBJ - Call getObj() to decode the entire object, or call next again to get
 * the first key of the object (or END_OBJ if empty).
 * <li>END_INPUT - Parsing is finished, close the reader.
 * <li>END_LIST - The current list is complete.
 * <li>END_OBJ - The current object is complete.
 * <li>Everything else - Call the corresponding getter.
 * </ul>
 * <p>
 * Be aware that numbers may not decode to the same type they were encoded from.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public interface Areader extends Closeable {

    @Override
    void close();

    /**
     * Returns the value when last() == DECIMAL.
     */
    BigDecimal getBigDecimal();

    /**
     * Returns the value when last() == BIGINT.
     */
    BigInteger getBigInt();

    /**
     * Returns the value when last() == BINARY.
     */
    byte[] getBinary();

    /**
     * Returns the value when last() == BOOLEAN.
     */
    boolean getBoolean();

    /**
     * Returns the value when last() == DOUBLE.
     */
    double getDouble();

    /**
     * Returns the value when last() == FLOAT.
     */
    float getFloat();

    /**
     * Returns the value when last() == INT.
     */
    int getInt();

    /**
     * This should only be called when last() == BEGIN_LIST and it will decode the
     * entire list.  Call next rather than this method to get the list in pieces.
     */
    Alist getList();

    /**
     * Returns the value when last() == LONG.
     */
    long getLong();

    /**
     * This should only be called when last() == BEGIN_OBJ and it will decode the
     * entire object.  Call next rather than this method get the object in pieces.
     */
    Aobj getObj();

    /**
     * Returns the value when last() == STRING or KEY.
     */
    String getString();

    /**
     * Returns the Avalue when last() == raw type, KEY or ROOT.
     */
    AIvalue getValue();

    /**
     * The last value returned from next(). At the beginning of a document, before
     * next has been called, this will return ROOT.
     */
    Token last();

    /**
     * Advances the reader to the next item and returns the token representing it's
     * current state.
     */
    Token next();

    /**
     * Sets last() == ROOT.
     */
    Areader reset();

    /**
     * Represents the state of the reader, and determines which getter should be
     * called next.
     */
    enum Token {
        BEGIN_LIST,
        BEGIN_OBJ,
        BIGINT,
        BINARY,
        BOOLEAN,
        DECIMAL,
        DOUBLE,
        END_INPUT,
        END_LIST,
        END_OBJ,
        FLOAT,
        INT,
        LONG,
        NULL,
        ROOT,
        STRING,
    }


}
