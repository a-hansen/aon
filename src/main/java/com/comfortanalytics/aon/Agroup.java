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

import com.comfortanalytics.aon.json.JsonAppender;

/**
 * A collection of objects such as lists and maps.
 *
 * @author Aaron Hansen
 */
public abstract class Agroup extends Avalue {

    // Fields
    // --------------

    private Agroup parent;

    // Public Methods
    // --------------

    /**
     * Removes all items.
     *
     * @return This
     */
    public abstract Agroup clear();

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Agroup)) {
            return false;
        }
        Agroup arg = (Agroup) o;
        if (arg.aonType() != aonType()) {
            return false;
        }
        int size = arg.size();
        if (size != arg.size()) {
            return false;
        }
        Entry mine = getFirstEntry();
        Entry his = arg.getFirstEntry();
        while (mine != null) {
            if (!mine.equals(his)) {
                return false;
            }
            mine = mine.next();
            his = his.next();
        }
        return true;
    }

    /**
     * The first item of the collection or null.
     */
    public abstract Entry getFirstEntry();

    /**
     * The last item of the collection or null.
     */
    public abstract Entry getLastEntry();

    /**
     * Returns the parent group or null.
     */
    public Agroup getParent() {
        return parent;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        Entry e = getFirstEntry();
        while (e != null) {
            hashCode = 31 * hashCode + e.hashCode();
            e = e.next();
        }
        return hashCode;
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

    /**
     * Sets the parent and returns this for un-parented groups, otherwise throws an
     * IllegalStateException.
     *
     * @param arg The new parent.
     * @return This
     * @throws IllegalStateException If already parented.
     */
    protected Agroup setParent(Agroup arg) {
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

    // Inner Classes
    // -------------

    public interface Entry {

        public Avalue getValue();

        public Entry next();

    }


}
