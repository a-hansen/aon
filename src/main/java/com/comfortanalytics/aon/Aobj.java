package com.comfortanalytics.aon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * String keyed collection of values that preserves the order of addition. This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Aobj extends Agroup {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private Map<String, ObjEntry> entryMap = new HashMap<String, ObjEntry>();
    private ObjEntry first;
    private ObjEntry last;

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.OBJECT;
    }

    @Override
    public Aobj clear() {
        first = null;
        last = null;
        entryMap.clear();
        return this;
    }

    @Override
    public Avalue copy() {
        Aobj ret = new Aobj();
        ObjEntry e = getFirstEntry();
        while (e != null) {
            ret.put(e.getKey(), e.getValue().copy());
            e = e.next();
        }
        return ret;
    }

    /**
     * Returns the value for the given key or null.
     */
    public Avalue get(String key) {
        ObjEntry e = entryMap.get(key);
        if (e == null) {
            return null;
        }
        return e.getValue();
    }

    /**
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public boolean get(String key, boolean def) {
        Avalue ret = get(key);
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
     * Optional getter, returns the provided default if the value mapped to the key is
     * null.
     */
    public double get(String key, double def) {
        Avalue ret = get(key);
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
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public int get(String key, int def) {
        Avalue ret = get(key);
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
     * Optional getter, returns the provided default if the value mapped to the key is
     * null or not convertible.
     */
    public long get(String key, long def) {
        Avalue ret = get(key);
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
     * Optional getter, returns the provided default if the value mapped to the key is
     * null.
     */
    public String get(String key, String def) {
        Avalue ret = get(key);
        if ((ret == null) || ret.isNull()) {
            return def;
        }
        return ret.toString();
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public boolean getBoolean(String key) {
        return get(key).toBoolean();
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public double getDouble(String key) {
        return get(key).toDouble();
    }

    @Override
    public ObjEntry getFirstEntry() {
        return first;
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public int getInt(String key) {
        return get(key).toInt();
    }

    @Override
    public ObjEntry getLastEntry() {
        return last;
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public Alist getList(String key) {
        Avalue obj = get(key);
        if (obj == null) {
            return null;
        }
        return obj.toList();
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public long getLong(String key) {
        return get(key).toLong();
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public Aobj getObj(String key) {
        Avalue o = get(key);
        if (o == null) {
            return null;
        }
        return o.toObj();
    }

    /**
     * Returns the String value for the given key, or null.
     */
    public String getString(String key) {
        Avalue o = get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * Returns true if the key isn't in the entryMap, or it's value is null.
     */
    public boolean isNull(String key) {
        Avalue o = get(key);
        if (o == null) {
            return true;
        }
        return o.aonType() == Atype.NULL;
    }

    /**
     * Returns true.
     */
    public boolean isObj() {
        return true;
    }

    /**
     * Adds or replaces the value for the given key and returns this.
     *
     * @param key Must not be null.
     * @param val Can be null, and can not be an already parented group.
     * @return this
     */
    public Aobj put(String key, Avalue val) {
        if (val == null) {
            val = Anull.NULL;
        }
        ObjEntry e = entryMap.get(key);
        if (e != null) {
            Avalue curr = e.getValue();
            if (curr != val) {
                if (val.isGroup()) {
                    val.toGroup().setParent(this);
                }
                if (curr.isGroup()) {
                    curr.toGroup().setParent(null);
                }
                e.setValue(val);
            }
        } else {
            if (val.isGroup()) {
                val.toGroup().setParent(this);
            }
            e = new ObjEntry(key, val);
            entryMap.put(key, e);
            if (first == null) {
                first = e;
                last = e;
            } else {
                last.setNext(e);
                last = e;
            }
        }
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Aobj put(String key, boolean val) {
        put(key, Abool.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Aobj put(String key, double val) {
        put(key, Adouble.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Aobj put(String key, int val) {
        put(key, Aint.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Aobj put(String key, long val) {
        put(key, Along.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Aobj put(String key, String val) {
        if (val == null) {
            put(key, Anull.NULL);
        } else {
            put(key, Astr.valueOf(val));
        }
        return this;
    }

    /**
     * Puts a String representing the stack trace into the entryMap.
     */
    public Aobj put(String key, Throwable val) {
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        val.printStackTrace(out);
        out.flush();
        put(key, str.toString());
        try {
            out.close();
        } catch (Exception x) {
        }
        try {
            str.close();
        } catch (Exception x) {
        }
        return this;
    }

    /**
     * Puts a new list for given key and returns it.
     */
    public Alist putList(String key) {
        Alist ret = new Alist();
        put(key, ret);
        return ret;
    }

    /**
     * Puts a new entryMap for given key and returns it.
     */
    public Aobj putMap(String key) {
        Aobj ret = new Aobj();
        put(key, ret);
        return ret;
    }

    /**
     * Puts a null value for given key and returns this.
     */
    public Aobj putNull(String key) {
        return put(key, Anull.NULL);
    }

    /**
     * Removes the key-value pair and returns the removed value.
     *
     * @return Possibly null.
     */
    public Avalue remove(String key) {
        ObjEntry e = entryMap.remove(key);
        if (e == null) {
            return null;
        }
        if (size() == 0) {
            first = null;
            last = null;
        } else if (e == first) {
            first = first.next();
        } else {
            ObjEntry prev = getPrev(key);
            prev.setNext(e.next());
            if (e == last) {
                last = prev;
            }
        }
        Avalue ret = e.getValue();
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    public Avalue removeFirst() {
        if (first != null) {
            return remove(first.getKey());
        }
        return null;
    }

    public Avalue removeLast() {
        if (last != null) {
            return remove(last.getKey());
        }
        return null;
    }

    @Override
    public int size() {
        return entryMap.size();
    }

    @Override
    public Aobj toObj() {
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private ObjEntry getPrev(String key) {
        ObjEntry prev = null;
        ObjEntry entry = getFirstEntry();
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
     * Object element which provides access to the next entry in the object.
     */
    public static class ObjEntry implements Entry {

        private String key;
        private ObjEntry next;
        private Avalue val;

        ObjEntry(String key, Avalue val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ObjEntry)) {
                return false;
            }
            ObjEntry e = (ObjEntry) obj;
            if (!e.getKey().equals(key)) {
                return false;
            }
            return e.getValue().equals(val);
        }

        public String getKey() {
            return key;
        }

        @Override
        public Avalue getValue() {
            return val;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ val.hashCode();
        }

        @Override
        public ObjEntry next() {
            return next;
        }

        void setNext(ObjEntry entry) {
            next = entry;
        }

        void setValue(Avalue val) {
            this.val = val;
        }

    }

}
