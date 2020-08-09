package com.comfortanalytics.aon;

import com.comfortanalytics.aon.json.JsonAppender;
import javax.annotation.Nonnull;

/**
 * A collection of values such as Alist and Aobj.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class Agroup extends Aprimitive {

    /**
     * Removes all items and returns this.
     */
    @Nonnull
    public abstract Agroup clear();

    /**
     * Returns true when size() == 0.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    /**
     * The number of items is the group.
     */
    public abstract int size();

    @Nonnull
    @Override
    public Agroup toGroup() {
        return this;
    }

    /**
     * Json encodes the graph, be careful.
     */
    @Nonnull
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        new JsonAppender(buf).value(this).close();
        return buf.toString();
    }

}
