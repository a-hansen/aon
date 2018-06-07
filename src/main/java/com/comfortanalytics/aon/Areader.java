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
 * the first key of the map (or END_OBJ if empty).
 * <li>END_INPUT - Parsing is finished, close the reader.
 * <li>END_LIST - The current list is complete.
 * <li>END_OBJ - The current map is complete.
 * <li>Everything else - Call the corresponding getter.
 * </ul>
 * <p>
 * Be aware that if the underlying encoding (such as JSON) doesn't provide a mechanism to
 * differentiate between data types (such as numbers), values might not decode as the same
 * type they were encoded.
 *
 * @author Aaron Hansen
 */
public interface Areader {

    /**
     * Close the input.
     */
    public void close();

    /**
     * Returns the value when last() == DECIMAL.
     */
    public BigDecimal getBigDecimal();

    /**
     * Returns the value when last() == BIGINT.
     */
    public BigInteger getBigInt();

    /**
     * Returns the value when last() == BINARY.
     */
    public byte[] getBinary();

    /**
     * Returns the value when last() == BOOLEAN.
     */
    public boolean getBoolean();

    /**
     * Returns the value when last() == DOUBLE.
     */
    public double getDouble();

    /**
     * Returns the value when last() == FLOAT.
     */
    public float getFloat();

    /**
     * Returns the value when last() == INT.
     */
    public int getInt();

    /**
     * This should only be called when last() == BEGIN_LIST and it will decode the
     * entire list.  Call next rather than this method to get the list in pieces.
     */
    public Alist getList();

    /**
     * Returns the value when last() == LONG.
     */
    public long getLong();

    /**
     * This should only be called when last() == BEGIN_OBJ and it will decode the
     * entire map.  Call next rather than this method get the map in pieces.
     */
    public Aobj getObj();

    /**
     * Returns the value when last() == STRING or KEY.
     */
    public String getString();

    /**
     * Returns the Avalue when last() == raw type, KEY or ROOT.
     */
    public Avalue getValue();

    /**
     * The last value returned from next(). At the beginning of a document, before
     * next has been called, this will return ROOT.
     */
    public Token last();

    /**
     * Advances the reader to the next item and returns the token representing it's
     * current state.
     */
    public Token next();

    /**
     * Sets last() == ROOT.
     */
    public Areader reset();

    /**
     * Represents the state of the reader, and determines which getter should be
     * called next.
     */
    public enum Token {
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
