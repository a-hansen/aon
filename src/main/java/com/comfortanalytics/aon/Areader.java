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
 * An Aobj decoder that can be used to decode an entire graph in pieces, or one large
 * Aobj, or somewhere in between. To decode an entire graph, call getObj(), getMap()
 * or getList(). Otherwise, use the next() method to iterate the elements of the input
 * document.
 * <p>
 * When next() returns:
 * <ul>
 * <li>ROOT - The initial state, not in a list or map, call next() or getObj().
 * <li>BEGIN_LIST - Call getList() to decode the entire list, or call next again to get
 * the first element of the list (or END_LIST if empty).
 * <li>BEGIN_MAP - Call getMap() to decode the entire map, or call next again to get
 * the first key of the map (or END_MAP if empty).
 * <li>END_INPUT - Parsing is finished, close the reader.
 * <li>END_LIST - The current list is complete, call next again.
 * <li>END_MAP - The current map is complete, call next again.
 * <li>KEY - Call getString() to get the next key, then call
 * next to determine its value.
 * <li>BOOLEAN,DOUBLE,INT,LONG,NULL,STRING - Call the corresponding getter.
 * </ul>
 * <p>
 * Be aware that if the underlying encoding (such as JSON) doesn't provide a mechanism to
 * differentiate between data types (such as numbers), values might
 * not decode as the same type they were encoded.
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
     * Returns the value when last() == BOOLEAN.
     */
    public boolean getBoolean();

    /**
     * Returns the value when last() == DOUBLE.
     */
    public double getDouble();

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
     * This should only be called when last() == BEGIN_MAP and it will decode the
     * entire map.  Call next rather than this method get the map in pieces.
     */
    public Amap getMap();

    /**
     * Returns the Aobj when last() == raw type, KEY or ROOT.
     */
    public Aobj getObj();

    /**
     * Returns the value when last() == STRING or KEY.
     */
    public String getString();

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
        BEGIN_MAP,
        BIGINT,
        BOOLEAN,
        DECIMAL,
        DOUBLE,
        END_INPUT,
        END_LIST,
        END_MAP,
        FLOAT,
        INT,
        KEY,
        LONG,
        NULL,
        ROOT,
        STRING,
    }


}
