package com.comfortanalytics.aon;

import com.comfortanalytics.aon.Aobj.Member;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * String keyed collection of values that preserves the order of addition.  To traverse
 * members in order, use {@link #iterator()}, or {@link #getFirst()} and then use
 * {@link Member#next()}.
 * <p>
 * Adding null will result in Anull.NULL being put into the obj.
 * <p>
 * This is not thread safe.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"unused"})
public class Aobj extends Agroup implements Iterable<Member> {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private volatile Member first;
    private volatile Member last;
    private final Map<String, Member> map = new HashMap<>();

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.OBJECT;
    }

    @Nonnull
    @Override
    public Aobj clear() {
        first = null;
        last = null;
        map.clear();
        return this;
    }

    @Nonnull
    @Override
    public Aobj copy() {
        Aobj ret = new Aobj();
        Member e = getFirst();
        while (e != null) {
            ret.put(e.getKey(), e.getValue().copy());
            e = e.next();
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
        if (o instanceof Aobj) {
            return map.equals(((Aobj) o).map);
        }
        return false;
    }

    /**
     * Returns the value being wrapped by the Aon data type for the given key, or null.
     */
    @Nullable
    public <T> T get(@Nonnull String key) {
        Member e = getMember(key);
        if (e == null) {
            return null;
        }
        return e.getValue().get();
    }

    /**
     * Returns the value being wrapped by the Aon data type for the given key, or null.
     */
    @Nullable
    public <T> T get(@Nonnull String key, Class<T> type) {
        Member e = getMember(key);
        if (e == null) {
            return null;
        }
        return type.cast(e.getValue().get());
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public boolean get(@Nonnull String key, boolean def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        try {
            return ret.toBoolean();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null.
     */
    public double get(@Nonnull String key, double def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        try {
            return ret.toDouble();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null.
     */
    public float get(@Nonnull String key, float def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        try {
            return ret.toFloat();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public int get(@Nonnull String key, int def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        try {
            return ret.toInt();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public long get(@Nonnull String key, long def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        try {
            return ret.toLong();
        } catch (Exception ignore) {
        }
        return def;
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null.
     */
    public String get(@Nonnull String key, String def) {
        Adata ret = get(key);
        if (Aon.isNull(ret)) {
            return def;
        }
        return ret.toString();
    }

    /**
     * Replaces the value and returns the prior value.
     */
    @Nullable
    public Adata getAndPut(@Nonnull String key, @Nullable Adata value) {
        Member m = getMember(key);
        if (m == null) {
            put(key, value);
            return null;
        }
        Adata ret = m.getValue();
        m.setValue(value == null ? Anull.NULL : value);
        return ret;
    }

    /**
     * Use this to traverse the children in order.
     */
    public Member getFirst() {
        return first;
    }

    public Member getLast() {
        return last;
    }

    @Nullable
    public Member getMember(String key) {
        return map.get(key);
    }

    /**
     * Attempts to get the current child but if there is no child, puts the new one and return it.
     */
    public <T extends Adata> T getOrPutIfAbsent(String name, Supplier<T> child) {
        T ret = get(name);
        if (ret == null) {
            ret = child.get();
            put(name, ret);
        }
        return ret;
    }

    /**
     * Returns the value for the given key or null.
     */
    @Nullable
    public <T extends Adata> T getValue(@Nonnull String key) {
        Member e = getMember(key);
        if (e == null) {
            return null;
        }
        return (T) e.getValue();
    }

    /**
     * Returns the value for the given key or null.
     */
    @Nullable
    public <T extends Adata> T getValue(@Nonnull String key, Class<T> type) {
        Member e = getMember(key);
        if (e == null) {
            return null;
        }
        return type.cast(e.getValue());
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /**
     * Returns true if the key isn't in the object, or it's value is null.
     */
    public boolean isNull(String key) {
        return Aon.isNull(get(key));
    }

    /**
     * True.
     */
    @Override
    public boolean isObj() {
        return true;
    }

    @Nonnull
    @Override
    public Iterator<Member> iterator() {
        return new Iterator<Member>() {
            private Member next = getFirst();
            private Member prev;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Nonnull
            @Override
            public Member next() {
                prev = next;
                next = next.next();
                return prev;
            }

            @Override
            public void remove() {
                Aobj.this.remove(prev.key);
            }
        };
    }

    /**
     * Puts a new list for given key and returns it.
     */
    @Nonnull
    public Alist newList(String key) {
        Alist ret = new Alist();
        put(key, ret);
        return ret;
    }

    /**
     * Creates and puts a new object for given key and returns it.
     */
    @Nonnull
    public Aobj newObj(String key) {
        Aobj ret = new Aobj();
        put(key, ret);
        return ret;
    }

    /**
     * Adds or replaces the value for the given key and returns this.
     *
     * @param key Must not be null.
     * @param val Can be null, in which case Anull.NULL will be used.
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nullable Adata val) {
        if (val == null) {
            val = Anull.NULL;
        }
        Member e = getMember(key);
        if (e != null) {
            Adata curr = e.getValue();
            if (curr != val) {
                e.setValue(val);
            }
        } else {
            e = new Member(key, val);
            map.put(key, e);
            if (first == null) {
                first = e;
            } else {
                last.setNext(e);
            }
            last = e;
        }
        return this;
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nullable BigDecimal val) {
        return put(key, Adecimal.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nullable BigInteger val) {
        return put(key, Abigint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nullable byte[] val) {
        return put(key, Abinary.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, boolean val) {
        return put(key, Abool.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, double val) {
        return put(key, Adouble.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, int val) {
        return put(key, Aint.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, long val) {
        return put(key, Along.valueOf(val));
    }

    /**
     * @return this
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nullable String val) {
        return put(key, Astr.valueOf(val));
    }

    /**
     * Puts a String representing the stack trace into the object.
     */
    @Nonnull
    public Aobj put(@Nonnull String key, @Nonnull Throwable val) {
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        val.printStackTrace(out);
        out.flush();
        put(key, str.toString());
        try {
            out.close();
        } catch (Exception ignore) {
        }
        try {
            str.close();
        } catch (Exception ignore) {
        }
        return this;
    }

    /**
     * Does not {@link #copy()} the contents.
     *
     * @return this
     */
    @Nonnull
    public Aobj putAll(Aobj arg) {
        Member m = arg.getFirst();
        while (m != null) {
            put(m.getKey(), m.getValue());
            m = m.next;
        }
        return this;
    }

    /**
     * Puts a null value for given key and returns this.
     */
    @Nonnull
    public Aobj putNull(@Nonnull String key) {
        return put(key, Anull.NULL);
    }

    /**
     * Removes the key-value pair and returns the removed value.
     *
     * @return Possibly null.
     */
    @Nullable
    public Adata remove(@Nonnull String key) {
        Member e = map.remove(key);
        if (e == null) {
            return null;
        }
        if (size() == 0) {
            first = null;
            last = null;
        } else if (e == first) {
            first = first.next();
        } else {
            Member prev = getPrev(key);
            prev.setNext(e.next());
            if (e == last) {
                last = prev;
            }
        }
        return e.getValue();
    }

    @Nullable
    public Adata removeFirst() {
        if (first != null) {
            return remove(first.getKey());
        }
        return null;
    }

    @Nullable
    public Adata removeLast() {
        if (last != null) {
            return remove(last.getKey());
        }
        return null;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Nonnull
    @Override
    public Aobj toObj() {
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private Member getPrev(String key) {
        Member prev = null;
        Member entry = getFirst();
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return prev;
            }
            prev = entry;
            entry = entry.next();
        }
        return prev;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Object member which provides access to the next member in the object.
     */
    public static class Member {

        private final String key;
        private Member next;
        private Adata val;

        Member(@Nonnull String key, @Nonnull Adata val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Member) {
                Member e = (Member) obj;
                return e.getKey().equals(key) && e.getValue().equals(val);
            }
            return false;
        }

        @Nonnull
        public String getKey() {
            return key;
        }

        @Nonnull
        public Adata getValue() {
            return val;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ val.hashCode();
        }

        @Nonnull
        public Member next() {
            return next;
        }

        void setNext(@Nullable Member entry) {
            next = entry;
        }

        void setValue(@Nonnull Adata val) {
            this.val = val;
        }

    }

}
