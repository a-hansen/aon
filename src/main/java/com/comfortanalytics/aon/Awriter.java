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
 * An Aobj encoder that can be used to encode large graphs with or without Aobj instances.
 * <p>
 * To simply encode an Amap or Alist, use the value(Aobj) method.  For example:
 * <ul><li>new JsonWriter(out).value(myMap).close(); </li> </ul>
 * </p>
 * <p>
 * Otherwise, you can stream data struct without using any Aobj instances:
 * <ul>
 * <li>out.newMap().key("a").value(1).key("b").value(2).key("c").value(3).endMap();</li>
 * </ul>
 * </p>
 * <p>
 * Be aware that if the underlying encoding (such as JSON) doesn't provide a mechanism to
 * differentiate between data types (such as numbers), values might not decode as the
 * same type they were encoded.
 * </p>
 *
 * @author Aaron Hansen
 */
public interface Awriter extends Aconstants, AutoCloseable {

    // Public Methods
    // --------------

    /**
     * Start a new list and return this.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter beginList();

    /**
     * Start a new map and return this.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter beginMap();

    /**
     * Close the stream. IOExceptions will be wrapped in runtime exceptions.
     */
    public void close();

    /**
     * End the current list.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter endList();

    /**
     * End the current map.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter endMap();

    /**
     * Flush the stream. IOExceptions will be wrapped in runtime exceptions.
     */
    public Awriter flush();

    /**
     * Write a key in the current map.  Cannot be called in a list, must be followed
     * by a call to one of the value methods.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter key(CharSequence key);

    /**
     * Clears the state of the writer.
     */
    public Awriter reset();

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).  This can be used to encode an entire graph.
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(Aobj arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(boolean arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(double arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(int arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(long arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(String arg);


}
