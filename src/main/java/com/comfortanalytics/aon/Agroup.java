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
 * A index accessible collection of objects such as lists and maps.
 *
 * @author Aaron Hansen
 */
public abstract class Agroup extends Aobj {

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
        if (o == this) return true;
        if (!(o instanceof Agroup)) return false;
        Agroup arg = (Agroup) o;
        if (arg.aonType() != aonType()) {
            return false;
        }
        int size = arg.size();
        if (size != arg.size()) return false;
        for (int i = size; --i >= 0; ) {
            if (!get(i).equals(arg.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (int i = size(); --i >= 0; ) {
            hashCode = 31 * hashCode + get(i).hashCode();
        }
        return hashCode;
    }

    /**
     * Returns the item at index 0.
     *
     * @return Null if empty.
     */
    public Aobj first() {
        if (isEmpty()) {
            return null;
        }
        return get(0);
    }

    /**
     * Returns the value at the given index.
     *
     * @throws IndexOutOfBoundsException
     */
    public abstract Aobj get(int idx);

    /**
     * Optional getter.
     */
    public boolean get(int idx, boolean def) {
        if (idx >= size()) return def;
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) return def;
        try {
            return ret.toBoolean();
        } catch (Exception x) {
        }
        return def;
    }

    /**
     * Optional getter.
     */
    public double get(int idx, double def) {
        if (idx >= size()) return def;
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) return def;
        try {
            return ret.toDouble();
        } catch (Exception x) {
        }
        return def;
    }

    /**
     * Optional getter.
     */
    public int get(int idx, int def) {
        if (idx >= size()) return def;
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) return def;
        try {
            return ret.toInt();
        } catch (Exception x) {
        }
        return def;
    }

    /**
     * Optional getter.
     */
    public long get(int idx, long def) {
        if (idx >= size()) return def;
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) return def;
        try {
            return ret.toLong();
        } catch (Exception x) {
        }
        return def;
    }

    /**
     * Optional getter.
     */
    public String get(int idx, String def) {
        if (idx >= size()) return def;
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) return def;
        return ret.toString();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public boolean getBoolean(int idx) {
        return get(idx).toBoolean();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public double getDouble(int idx) {
        return get(idx).toDouble();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public double getFloat(int idx) {
        return get(idx).toFloat();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public int getInt(int idx) {
        return get(idx).toInt();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public Alist getList(int idx) {
        return get(idx).toList();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public long getLong(int idx) {
        return get(idx).toLong();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws IndexOutOfBoundsException
     */
    public Amap getMap(int idx) {
        return get(idx).toMap();
    }

    /**
     * Returns the parent group or null.
     */
    public Agroup getParent() {
        return parent;
    }

    /**
     * Primitive getter.
     *
     * @throws IndexOutOfBoundsException
     */
    public String getString(int idx) {
        return get(idx).toString();
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int indexOf(Aobj obj) {
        boolean isNull = ((obj == null) || obj.isNull());
        Aobj tmp;
        int len = size();
        for (int i = 0; i < len; i++) {
            tmp = get(i);
            if (obj == tmp)
                return i;
            if (isNull && tmp.isNull())
                return i;
            if (obj.equals(tmp))
                return i;
        }
        return -1;
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
     * Whether or not the object at the given index is null.  Will return
     * true if the index is out of bounds.
     */
    public boolean isNull(int idx) {
        if (idx >= size()) return true;
        if (idx < 0) return true;
        return get(idx).isNull();
    }

    /**
     * Returns the item at the highest index.
     *
     * @return Null if empty.
     */
    public Aobj last() {
        if (isEmpty()) {
            return null;
        }
        return get(size() - 1);
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int lastIndexOf(Aobj obj) {
        boolean isNull = ((obj == null) || obj.isNull());
        Aobj tmp;
        int len = size();
        for (int i = len; --i >= 0; ) {
            tmp = get(i);
            if (obj == tmp)
                return i;
            if (isNull && tmp.isNull())
                return i;
            if (obj.equals(tmp))
                return i;
        }
        return -1;
    }

    /**
     * Removes the value at the given index and returns it.
     *
     * @return The value removed.
     * @throws IndexOutOfBoundsException
     */
    public abstract Aobj remove(int idx);

    /**
     * Remove and return the item at index 0.
     *
     * @return The value removed.
     * @throws IndexOutOfBoundsException
     */
    public Aobj removeFirst() {
        return remove(0);
    }

    /**
     * Remove and return the item at the highest index.
     *
     * @return The value removed.
     * @throws IndexOutOfBoundsException
     */
    public Aobj removeLast() {
        return remove(size() - 1);
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

    @Override
    public Agroup toGroup() {
        return this;
    }

    /**
     * The number of items is the group.
     */
    public abstract int size();

    /**
     * Json encodes the graph, be careful.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        new JsonAppender(buf).value(this).close();
        return buf.toString();
    }


}