package com.comfortanalytics.aon;

import com.comfortanalytics.aon.json.JsonAppender;
import javax.annotation.Nonnull;

/**
 * Primitive data types are JSON compatible.
 * <p>
 * Be aware that when encoding/decoding, if the underlying format doesn't provide a mechanism to
 * differentiate between data types (such as numbers in JSON), values may not decode to the same
 * type they were encoded.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public abstract class Aprimitive implements Adata {

    @Override
    public boolean isPrimitive() {
        return true;
    }

    /**
     * Returns this.
     */
    @Nonnull
    @Override
    public Aprimitive toPrimitive() {
        return this;
    }

    @Nonnull
    @Override
    public abstract String toString();

    /**
     * Appends the JSON encoding to the given buffer and returns the buffer.
     */
    @Nonnull
    public Appendable toString(Appendable buf) {
        new JsonAppender(buf).value(this);
        return buf;
    }

}
