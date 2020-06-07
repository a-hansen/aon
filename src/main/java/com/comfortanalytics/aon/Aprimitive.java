package com.comfortanalytics.aon;

import com.comfortanalytics.aon.json.JsonAppender;

/**
 * Primitive data types that are JSON compatible.
 * Base representation for all Aon data types.  Use Alist and Aobj to create data
 * structures.
 * <p>
 * Be aware that when encoding/decoding, if the underlying format doesn't provide a mechanism
 * to differentiate between data types (such as numbers), values may not decode to the same type
 * they were encoded.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public abstract class Aprimitive implements AIvalue, Cloneable {

    @Override
    public boolean isPrimitive() {
        return true;
    }

    /**
     * Returns this.
     */
    @Override
    public Aprimitive toPrimitive() {
        return this;
    }

    /**
     * Appends the JSON encoding to the given buffer and returns the buffer.
     */
    public Appendable toString(Appendable buf) {
        new JsonAppender(buf).value(this);
        return buf;
    }


}
