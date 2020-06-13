package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Indexed collection of values.  Adding null will result in Anull.NULL being added.
 * Not thread safe.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Alist extends Agroup implements Iterable<AIvalue> {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private final ArrayList<AIvalue> values = new ArrayList<>();

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Adds the value and returns this.
     *
     * @param val Can be null, which will then add Anull.NULL.
     * @return this
     */
    @Nonnull
    public Alist add(@Nullable AIvalue val) {
        if (val == null) {
            val = Anull.NULL;
        }
        values.add(val);
        return this;
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(@Nullable BigDecimal val) {
        return add(Adecimal.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(@Nullable BigInteger val) {
        return add(Abigint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(boolean val) {
        return add(Abool.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(byte[] val) {
        return add(Abinary.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(double val) {
        return add(Adouble.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(float val) {
        return add(Afloat.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(int val) {
        return add(Aint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(long val) {
        return add(Along.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist add(@Nullable String val) {
        return add(Astr.valueOf(val));
    }

    /**
     * Appends Anull.NULL and returns this.
     */
    public Alist addNull() {
        add(Anull.NULL);
        return this;
    }

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.LIST;
    }

    @Nonnull
    @Override
    public Agroup clear() {
        values.clear();
        return this;
    }

    @Nonnull
    @Override
    public AIvalue copy() {
        Alist ret = new Alist();
        for (AIvalue val : values) {
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
            if (size() != arg.size()) {
                return false;
            }
            return values.equals(arg.values);
        }
        return false;
    }

    /**
     * Returns the value being wrapped by the Aon data type at the given index, unless
     * the value is Agroup.
     *
     * @see AIvalue#get()
     */
    public <T> T get(int idx) {
        return (T) values.get(idx).get();
    }

    /**
     * The value at the given index unless the index is out of bounds or the value is null.
     */
    public boolean get(int idx, boolean def) {
        if (isOutOfBounds(idx)) {
            return def;
        }
        AIvalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        try {
            return ret.toBoolean();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * The value at the given index unless the index is out of bounds or the value is null.
     */
    public double get(int idx, double def) {
        if (isOutOfBounds(idx)) {
            return def;
        }
        AIvalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        try {
            return ret.toDouble();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * The value at the given index unless the index is out of bounds or the value is null.
     */
    public int get(int idx, int def) {
        if (isOutOfBounds(idx)) {
            return def;
        }
        AIvalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        try {
            return ret.toInt();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * The value at the given index unless the index is out of bounds or the value is null.
     */
    public long get(int idx, long def) {
        if (isOutOfBounds(idx)) {
            return def;
        }
        AIvalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        try {
            return ret.toLong();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * The value at the given index unless the index is out of bounds or the value is null.
     */
    public String get(int idx, String def) {
        if (isOutOfBounds(idx)) {
            return def;
        }
        AIvalue ret = get(idx);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        return ret.toString();
    }

    /**
     * Returns the item at index 0 or null (not Anull.NULL).
     *
     * @return Null (not Anull.NULL) if empty.
     */
    public AIvalue getFirst() {
        if (isEmpty()) {
            return null;
        }
        return get(0);
    }

    /**
     * Returns the item at the highest index or null (not Anull.NULL).
     *
     * @return Null (not Anull.NULL) if empty.
     */
    public AIvalue getLast() {
        if (values.isEmpty()) {
            return null;
        }
        return values.get(values.size() - 1);
    }

    /**
     * Returns the AIvalue at the given index.
     */
    public <T extends AIvalue> T getValue(int idx) {
        return (T) values.get(idx);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int indexOf(AIvalue obj) {
        for (int i = 0, len = size(); i < len; i++) {
            if (obj.equals(get(i))) {
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
        if (isOutOfBounds(idx)) {
            return true;
        }
        if (idx < 0) {
            return true;
        }
        return getValue(idx).isNull();
    }

    public boolean isOutOfBounds(int index) {
        if (index < 0) {
            return true;
        }
        return index > lastIndex();
    }

    @Nonnull
    @Override
    public Iterator<AIvalue> iterator() {
        return values.iterator();
    }

    /**
     * The last index, or -1 if empty.
     */
    public int lastIndex() {
        return size() - 1;
    }

    /**
     * Scans the collection and returns the first index that equals the arg.
     *
     * @return -1 if not found.
     */
    public int lastIndexOf(AIvalue obj) {
        for (int i = size(); --i >= 0; ) {
            if (obj.equals(get(i))) {
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
    @Nonnull
    public Aobj newObj() {
        Aobj obj = new Aobj();
        add(obj);
        return obj;
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, @Nullable AIvalue val) {
        if (val == null) {
            val = Anull.NULL;
        }
        values.set(idx, val);
        return this;
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, @Nullable BigDecimal val) {
        return put(idx, Adecimal.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, @Nullable BigInteger val) {
        return put(idx, Abigint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, boolean val) {
        return put(idx, Abool.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, @Nullable byte[] val) {
        return put(idx, Abinary.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, double val) {
        return put(idx, Adouble.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, float val) {
        return put(idx, Afloat.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, int val) {
        return put(idx, Aint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, long val) {
        return put(idx, Along.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Alist put(int idx, @Nullable String val) {
        return put(idx, Astr.valueOf(val));
    }

    /**
     * Removes the value at the given index and returns it.
     *
     * @return The value removed.
     */
    @Nullable
    public AIvalue remove(int idx) {
        return values.remove(idx);
    }

    /**
     * Remove and return the item at index 0.
     *
     * @return The value removed.
     */
    @Nullable
    public AIvalue removeFirst() {
        if (isEmpty()) {
            return null;
        }
        return remove(0);
    }

    /**
     * Remove and return the item at the highest index.
     *
     * @return The value removed.
     */
    @Nullable
    public AIvalue removeLast() {
        if (isEmpty()) {
            return null;
        }
        return remove(size() - 1);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Nonnull
    @Override
    public Alist toList() {
        return this;
    }

}
