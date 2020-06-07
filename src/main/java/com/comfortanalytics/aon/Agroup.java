package com.comfortanalytics.aon;

import com.comfortanalytics.aon.json.JsonAppender;

/**
 * A collection of values such as Alist and Aobj.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public abstract class Agroup extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private Agroup parent;

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Removes all items and returns this.
     */
    public abstract Agroup clear();

    /**
     * Returns the parent group or null.
     */
    public Agroup getParent() {
        return parent;
    }

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

    @Override
    public Agroup toGroup() {
        return this;
    }

    /**
     * Json encodes the graph, be careful.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        new JsonAppender(buf).value(this).close();
        return buf.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Sets the parent and returns this for un-parented groups, otherwise throws an
     * IllegalStateException.
     *
     * @param arg The new parent.
     * @return This
     * @throws IllegalStateException If already parented.
     */
    Agroup setParent(Agroup arg) {
        if (arg == null) {
            this.parent = null;
            return this;
        }
        if (this.parent != null) {
            throw new IllegalStateException("Already parented");
        }
        this.parent = arg;
        return this;
    }

}
