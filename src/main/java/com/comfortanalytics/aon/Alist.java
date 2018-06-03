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
 * Indexed collection of Aobjs implemented as a linked list.  This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Alist extends Agroup {

    // Fields
    // ------

    private ListEntry first;
    private ListEntry last;
    private int size;

    // Public Methods
    // --------------

    /**
     * Adds the value and returns this.
     *
     * @param val Can be null, and can not be an already parented group.
     * @return this
     */
    public Alist add(Aobj val) {
        if (val == null) {
            return addNull();
        }
        if (val.isGroup()) {
            val.toGroup().setParent(this);
        }
        ListEntry entry = new ListEntry(val);
        if (first == null) {
            first = entry;
            last = entry;
        } else {
            last.setNext(entry);
            last = entry;
        }
        size++;
        return this;
    }

    /**
     * Appends the arg and returns this.
     */
    public Alist add(BigDecimal val) {
        add(Adecimal.valueOf(val));
        return this;
    }

    /**
     * Appends the arg and returns this.
     */
    public Alist add(BigInteger val) {
        add(Abigint.valueOf(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(boolean val) {
        add(Abool.valueOf(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(double val) {
        add(Adouble.valueOf(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(int val) {
        add(Aint.valueOf(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(long val) {
        add(Along.valueOf(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(String val) {
        if (val == null) {
            return addNull();
        }
        add(Astr.valueOf(val));
        return this;
    }

    /**
     * Appends a new list and returns it.  This is going to cause trouble, but the
     * the primary usage won't be to add an empty list.
     */
    public Alist addList() {
        Alist ret = new Alist();
        add(ret);
        return ret;
    }

    /**
     * Appends a new map and returns it.
     */
    public Amap addMap() {
        Amap ret = new Amap();
        add(ret);
        return ret;
    }

    /**
     * Appends null and returns this.
     */
    public Alist addNull() {
        add(Anull.NULL);
        return this;
    }

    @Override
    public Atype aonType() {
        return Atype.LIST;
    }

    @Override
    public Agroup clear() {
        size = 0;
        first = null;
        last = null;
        return this;
    }

    @Override
    public Aobj copy() {
        Alist ret = new Alist();
        ListEntry e = first;
        while (e != null) {
            ret.add(e.getValue().copy());
            e = e.next();
        }
        return ret;
    }

    /**
     * Value at the given index or throws an IndexOutOfBounds exception.
     */
    public Aobj get(int idx) {
        return getEntry(idx).getValue();
    }

    /**
     * Optional getter.
     */
    public boolean get(int idx, boolean def) {
        if (idx >= size()) {
            return def;
        }
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
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
        if (idx >= size()) {
            return def;
        }
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
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
        if (idx >= size()) {
            return def;
        }
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
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
        if (idx >= size()) {
            return def;
        }
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
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
        if (idx >= size()) {
            return def;
        }
        Aobj ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        return ret.toString();
    }

    /**
     * Primitive getter.
     */
    public boolean getBoolean(int idx) {
        return get(idx).toBoolean();
    }

    /**
     * Primitive getter.
     */
    public double getDouble(int idx) {
        return get(idx).toDouble();
    }

    /**
     * Entry at the given index or throws an IndexOutOfBounds exception.
     */
    public ListEntry getEntry(int idx) {
        ListEntry entry = getFirstEntry();
        for (int i = 0; i < idx; i++) {
            if (entry == null) {
                break;
            }
            entry = entry.next();
        }
        if (entry == null) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
        }
        return entry;
    }

    /**
     * Returns the item at index 0 or null.
     */
    public Aobj getFirst() {
        if (isEmpty()) {
            return null;
        }
        return get(0);
    }

    /**
     * The entry at index 0.
     */
    public ListEntry getFirstEntry() {
        return first;
    }

    /**
     * Primitive getter.
     */
    public float getFloat(int idx) {
        return get(idx).toFloat();
    }

    /**
     * Primitive getter.
     */
    public int getInt(int idx) {
        return get(idx).toInt();
    }

    /**
     * Returns the item at the highest index.
     *
     * @return Null if empty.
     */
    public Aobj getLast() {
        if (last == null) {
            return null;
        }
        return last.getValue();
    }

    @Override
    public ListEntry getLastEntry() {
        return last;
    }

    /**
     * Primitive getter.
     */
    public Alist getList(int idx) {
        return get(idx).toList();
    }

    /**
     * Primitive getter.
     */
    public long getLong(int idx) {
        return get(idx).toLong();
    }

    /**
     * Primitive getter.
     */
    public Amap getMap(int idx) {
        return get(idx).toMap();
    }

    /**
     * Primitive getter.
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
            if (obj == tmp) {
                return i;
            }
            if (isNull && tmp.isNull()) {
                return i;
            }
            if (obj.equals(tmp)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isList() {
        return true;
    }

    /**
     * Whether or not the object at the given index is null.  Will return
     * true if the index is out of bounds.
     */
    public boolean isNull(int idx) {
        if (idx >= size()) {
            return true;
        }
        if (idx < 0) {
            return true;
        }
        return get(idx).isNull();
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
            if (obj == tmp) {
                return i;
            }
            if (isNull && tmp.isNull()) {
                return i;
            }
            if (obj.equals(tmp)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Replaces a value and returns this.
     *
     * @param val Can be null.
     */
    public Alist put(int idx, Aobj val) {
        if (idx == size()) {
            add(val);
            return this;
        }
        ListEntry e = getEntry(idx);
        Aobj old = e.getValue();
        if (old.isGroup()) {
            old.toGroup().setParent(null);
        }
        if (val == null) {
            val = Anull.NULL;
        } else if (val.isGroup()) {
            val.toGroup().setParent(this);
        }
        e.setValue(val);
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, BigDecimal val) {
        put(idx, Adecimal.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, BigInteger val) {
        put(idx, Abigint.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, boolean val) {
        put(idx, Abool.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, double val) {
        put(idx, Adouble.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, int val) {
        put(idx, Aint.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, long val) {
        put(idx, Along.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Alist put(int idx, String val) {
        put(idx, Astr.valueOf(val));
        return this;
    }

    /**
     * Removes the value at the given index and returns it.
     *
     * @return The value removed.
     */
    public Aobj remove(int idx) {
        if (idx >= size()) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
        }
        ListEntry e = null;
        if (size() == 1) {
            e = first;
            first = null;
            last = null;
        } else if (idx == 0) {
            e = first;
            first = first.next();
        } else {
            ListEntry pred = getEntry(--idx);
            e = pred.next();
            pred.setNext(e.next());
            if (e == last) {
                last = pred;
            }
        }
        size--;
        Aobj ret = e.getValue();
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    /**
     * Remove and return the item at index 0.
     *
     * @return The value removed.
     */
    public Aobj removeFirst() {
        return remove(0);
    }

    /**
     * Remove and return the item at the highest index.
     *
     * @return The value removed.
     */
    public Aobj removeLast() {
        return remove(size() - 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Alist toList() {
        return this;
    }

    // Inner Classes
    // -------------

    public class ListEntry implements Entry {

        private ListEntry next;
        private Aobj value;

        ListEntry(Aobj value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ListEntry)) {
                return false;
            }
            ListEntry e = (ListEntry) obj;
            return e.getValue().equals(value);
        }

        public Aobj getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        public ListEntry next() {
            return next;
        }

        /**
         * Returns this.
         */
        void setNext(ListEntry arg) {
            next = arg;
        }

        /**
         * Returns this.
         */
        void setValue(Aobj arg) {
            value = arg;
        }

    }


}
