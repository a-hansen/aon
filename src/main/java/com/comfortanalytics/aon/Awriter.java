package com.comfortanalytics.aon;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * An Aobj encoder that can be used to encode large graphs with or without Aobj instances.
 * <p>
 * To simply encode an Aobj or Alist, use the value(Aobj) method.  For example:
 * <ul><li>new JsonWriter(out).value(myMap).close(); </li> </ul>
 * <p>
 * Otherwise, you can stream data struct without using any Aobj instances:
 * <ul>
 * <li>out.newMap().key("a").value(1).key("b").value(2).key("c").value(3).endMap();</li>
 * </ul>
 * <p>
 * Be aware that if the underlying encoding (such as JSON) doesn't provide a mechanism to
 * differentiate between data types (such as numbers), values might not decode as the
 * same type they were encoded.
 *
 * @author Aaron Hansen
 */
public interface Awriter extends Closeable, Flushable {

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
    public void flush();

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
    public Awriter value(Avalue arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(BigDecimal arg);

    /**
     * Write a value to the map or list.  If in a map, this must have been preceeded
     * by a call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    public Awriter value(BigInteger arg);

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
    public Awriter value(float arg);

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
