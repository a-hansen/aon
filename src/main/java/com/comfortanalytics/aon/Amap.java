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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * String keyed collection of Aobjs that preserves the order of addition. This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Amap extends Agroup {

    // Fields
    // ------

    /**
     * For preserving order.
     */
    private Map<String, MapEntry> entryMap = new TreeMap<String, MapEntry>();
    private MapEntry first;
    private MapEntry last;

    // Methods
    // -------

    @Override
    public Atype aonType() {
        return Atype.MAP;
    }

    @Override
    public Amap clear() {
        first = null;
        last = null;
        entryMap.clear();
        return this;
    }

    @Override
    public Aobj copy() {
        Amap ret = new Amap();
        MapEntry e = getFirstEntry();
        while (e != null) {
            ret.put(e.getKey(), e.getValue().copy());
            e = e.next();
        }
        return ret;
    }

    /**
     * Returns the value for the given key or null.
     */
    public Aobj get(String key) {
        MapEntry e = entryMap.get(key);
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
        Aobj ret = get(key);
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
        Aobj ret = get(key);
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
        Aobj ret = get(key);
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
        Aobj ret = get(key);
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
        Aobj ret = get(key);
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
    public MapEntry getFirstEntry() {
        return first;
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public int getInt(String key) {
        return get(key).toInt();
    }

    @Override
    public MapEntry getLastEntry() {
        return last;
    }

    /**
     * Returns the value, null or throws a ClassCastException.
     */
    public Alist getList(String key) {
        Aobj obj = get(key);
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
    public Amap getMap(String key) {
        Aobj o = get(key);
        if (o == null) {
            return null;
        }
        return o.toMap();
    }

    private MapEntry getPrev(String key) {
        MapEntry prev = null;
        MapEntry entry = getFirstEntry();
        while (entry != null) {
            if (entry.getKey().equals(key)) {
                return prev;
            }
            prev = entry;
            entry = entry.next();
        }
        return prev;
    }

    /**
     * Returns the String value for the given key, or null.
     */
    public String getString(String key) {
        Aobj o = get(key);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    /**
     * Returns true.
     */
    public boolean isMap() {
        return true;
    }

    /**
     * Returns true if the key isn't in the entryMap, or it's value is null.
     */
    public boolean isNull(String key) {
        Aobj o = get(key);
        if (o == null) {
            return true;
        }
        return o.aonType() == Atype.NULL;
    }

    /**
     * Adds or replaces the value for the given key and returns this.
     *
     * @param key Must not be null.
     * @param val Can be null, and can not be an already parented group.
     * @return this
     */
    public Amap put(String key, Aobj val) {
        if (val == null) {
            val = Anull.NULL;
        }
        MapEntry e = entryMap.get(key);
        if (e != null) {
            Aobj curr = e.getValue();
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
            e = new MapEntry(key, val);
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
    public Amap put(String key, boolean val) {
        put(key, Abool.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, double val) {
        put(key, Adouble.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, int val) {
        put(key, Aint.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, long val) {
        put(key, Along.valueOf(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, String val) {
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
    public Amap put(String key, Throwable val) {
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
    public Amap putMap(String key) {
        Amap ret = new Amap();
        put(key, ret);
        return ret;
    }

    /**
     * Puts a null value for given key and returns this.
     */
    public Amap putNull(String key) {
        return put(key, Anull.NULL);
    }

    /**
     * Removes the key-value pair and returns the removed value.
     *
     * @return Possibly null.
     */
    public Aobj remove(String key) {
        MapEntry e = entryMap.remove(key);
        if (e == null) {
            return null;
        }
        if (size() == 0) {
            first = null;
            last = null;
        } else if (e == first) {
            first = first.next();
        } else {
            MapEntry prev = getPrev(key);
            prev.setNext(e.next());
            if (e == last) {
                last = prev;
            }
        }
        Aobj ret = e.getValue();
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    public Aobj removeFirst() {
        if (first != null) {
            return remove(first.getKey());
        }
        return null;
    }

    public Aobj removeLast() {
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
    public Amap toMap() {
        return this;
    }

    // Inner Classes
    // -------------

    public static class MapEntry implements Entry {

        private String key;
        private MapEntry next;
        private Aobj val;

        MapEntry(String key, Aobj val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof MapEntry)) {
                return false;
            }
            MapEntry e = (MapEntry) obj;
            if (!e.getKey().equals(key)) {
                return false;
            }
            if (!e.getValue().equals(val)) {
                return false;
            }
            return true;
        }

        public String getKey() {
            return key;
        }

        public Aobj getValue() {
            return val;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ val.hashCode();
        }

        public MapEntry next() {
            return next;
        }

        void setNext(MapEntry entry) {
            next = entry;
        }

        void setValue(Aobj val) {
            this.val = val;
        }

    }


}
