package com.comfortanalytics.aon;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * An encoder that can be used to encode large graphs with or without Aval instances.
 * <p>
 * To simply encode an Aobj or Alist, use the value(Aobj) method.  For example:
 * <ul><li>new JsonWriter(out).value(myObj).close(); </li> </ul>
 * <p>
 * Otherwise, you can stream data struct without using any Aval instances:
 * <ul>
 * <li>out.newMap().key("a").value(1).key("b").value(2).key("c").value(3).endMap();</li>
 * </ul>
 * <p>
 * Be aware that if the underlying encoding doesn't provide a mechanism to * differentiate between
 * data types (such as numbers), values might not decode as the same type they were encoded.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public interface Awriter extends Closeable, Flushable {

    /**
     * Start a new list and return this.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter beginList();

    /**
     * Start a new object and return this.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter beginObj();

    /**
     * Close the stream. IOExceptions will be wrapped in runtime exceptions.
     */
    @Override
    void close();

    /**
     * End the current list.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter endList();

    /**
     * End the current object.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter endObj();

    /**
     * Flush the stream. IOExceptions will be wrapped in runtime exceptions.
     */
    @Override
    void flush();

    /**
     * Write a key in the current object.  Cannot be called in a list, must be followed by a call to
     * one of the value methods.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter key(CharSequence key);

    /**
     * Clears the state of the writer.
     */
    Awriter reset();

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).  This can be used to encode an entire graph.
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(Adata arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(BigDecimal arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(BigInteger arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(boolean arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(byte[] arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(double arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(float arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(int arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(long arg);

    /**
     * Write a value to the object or list.  If in a object, this must have been preceded by a call
     * to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    Awriter value(String arg);

    /**
     * Write a null value to the object or list.  If in a object, this must have been preceded by a
     * call to key(String).
     *
     * @throws IllegalStateException when improperly called.
     */
    default Awriter valueNull() {
        return value(Anull.NULL);
    }


}
