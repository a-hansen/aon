package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Indexed collection of values implemented.  This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Alist extends Agroup implements Iterable<Avalue> {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private ArrayList<Avalue> values = new ArrayList<Avalue>();

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Adds the value and returns this.
     *
     * @param val Can be null, and can not be an already parented group.
     * @return this
     */
    public Alist add(Avalue val) {
        if (val == null) {
            return addNull();
        }
        if (val.isGroup()) {
            val.toGroup().setParent(this);
        }
        values.add(val);
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
    public Alist add(byte[] val) {
        add(Abinary.valueOf(val));
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
    public Alist add(float val) {
        add(Afloat.valueOf(val));
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
        for (Avalue val : values) {
            if (val.isGroup()) {
                val.toGroup().setParent(null);
            }
        }
        values.clear();
        return this;
    }

    @Override
    public Avalue copy() {
        Alist ret = new Alist();
        for (Avalue val : values) {
            ret.add(val.copy());
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof Alist) {
            Alist arg = (Alist) o;
            int size = arg.size();
            if (size() != arg.size()) {
                return false;
            }
            return hashCode() == arg.hashCode();
        }
        return false;
    }

    /**
     * Value at the given index or throws an IndexOutOfBounds exception.
     */
    public Avalue get(int idx) {
        return values.get(idx);
    }

    /**
     * Optional getter.
     */
    public boolean get(int idx, boolean def) {
        if (idx >= size()) {
            return def;
        }
        Avalue ret = get(idx);
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
        Avalue ret = get(idx);
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
        Avalue ret = get(idx);
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
        Avalue ret = get(idx);
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
        Avalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        return ret.toString();
    }

    public Abinary getBinary(int idx) {
        return get(idx).toBinary();
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
     * Returns the item at index 0 or null.
     */
    public Avalue getFirst() {
        if (isEmpty()) {
            return null;
        }
        return get(0);
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
    public Avalue getLast() {
        if (values.isEmpty()) {
            return null;
        }
        return values.get(values.size() - 1);
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

    public Aobj getObj(int idx) {
        return get(idx).toObj();
    }

    /**
     * Primitive getter.
     */
    public String getString(int idx) {
        return get(idx).toString();
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (Avalue val : values) {
            hashCode = 31 * hashCode + val.hashCode();
        }
        return hashCode;
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int indexOf(Avalue obj) {
        boolean isNull = ((obj == null) || obj.isNull());
        Avalue tmp;
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

    @Override
    public Iterator<Avalue> iterator() {
        return values.iterator();
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int lastIndexOf(Avalue obj) {
        boolean isNull = ((obj == null) || obj.isNull());
        Avalue tmp;
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
     * Creates a new Alist, adds it to this list and returns the new list.
     */
    public Alist newList() {
        Alist list = new Alist();
        add(list);
        return list;
    }

    /**
     * Creates a new Aobj, adds it to this list and returns it.
     */
    public Aobj newObj() {
        Aobj obj = new Aobj();
        add(obj);
        return obj;
    }

    /**
     * Replaces a value and returns this.
     *
     * @param val Can be null.
     */
    public Alist put(int idx, Avalue val) {
        Avalue old = get(idx);
        if (old.isGroup()) {
            old.toGroup().setParent(null);
        }
        if (val.isGroup()) {
            val.toGroup().setParent(this);
        }
        values.set(idx, val);
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
    public Alist put(int idx, byte[] val) {
        put(idx, Abinary.valueOf(val));
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
    public Alist put(int idx, float val) {
        put(idx, Afloat.valueOf(val));
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
    public Avalue remove(int idx) {
        Avalue ret = values.remove(idx);
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
    public Avalue removeFirst() {
        return remove(0);
    }

    /**
     * Remove and return the item at the highest index.
     *
     * @return The value removed.
     */
    public Avalue removeLast() {
        return remove(size() - 1);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Alist toList() {
        return this;
    }

}
