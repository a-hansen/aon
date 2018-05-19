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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * String keyed collection of Aobjs that preserves the order of addition.  Keys and
 * values can be accessed via index.  This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Amap extends Agroup {

    // Constants
    // ---------

    // Fields
    // ------

    /**
     * For preserving order.
     */
    protected List<Entry> entryList = new ArrayList<Entry>();
    protected Map<String, Entry> entryMap = new HashMap<String, Entry>();


    // Constructors
    // ------------

    public Amap() {
    }


    // Public Methods
    // --------------

    @Override
    public Atype aonType() {
        return Atype.MAP;
    }

    @Override
    public Amap clear() {
        entryList.clear();
        entryMap.clear();
        return this;
    }

    @Override
    public Aobj copy() {
        Amap ret = new Amap();
        for (int i = 0, len = size(); i < len; i++) {
            ret.put(getKey(i), get(i).copy());
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Amap) {
            Amap other = (Amap) obj;
            if (other.size() != size()) {
                return false;
            }
            return other.entryMap.equals(entryMap);
        }
        return false;
    }

    @Override
    public Aobj get(int idx) {
        return entryList.get(idx).getValue();
    }

    /**
     * Returns the value for the given key.
     *
     * @return Possibly null.
     */
    public Aobj get(String key) {
        Entry e = entryMap.get(key);
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
        if ((ret == null) || ret.isNull()) return def;
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
        if ((ret == null) || ret.isNull()) return def;
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
        if ((ret == null) || ret.isNull()) return def;
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
        if ((ret == null) || ret.isNull()) return def;
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
        if ((ret == null) || ret.isNull()) return def;
        return ret.toString();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws NullPointerException
     */
    public boolean getBoolean(String key) {
        return get(key).toBoolean();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws NullPointerException
     */
    public double getDouble(String key) {
        return get(key).toDouble();
    }

    Entry getEntry(int idx) {
        return entryList.get(idx);
    }

    /**
     * Return the list, or null.
     *
     * @return Possibly null.
     * @throws ClassCastException
     */
    public Alist getList(String key) {
        Aobj obj = get(key);
        if (obj == null) return null;
        return obj.toList();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     * @throws NullPointerException
     */
    public long getLong(String key) {
        return get(key).toLong();
    }

    /**
     * Primitive getter.
     *
     * @throws ClassCastException
     */
    public int getInt(String key) {
        return get(key).toInt();
    }

    /**
     * Returns the key at the given index.
     *
     * @throws IndexOutOfBoundsException
     */
    public String getKey(int idx) {
        return entryList.get(idx).getKey();
    }

    /**
     * Returns the entryMap value for the given key, or null.
     *
     * @return Possibly null.
     * @throws ClassCastException
     */
    public Amap getMap(String key) {
        Aobj o = get(key);
        if (o == null) return null;
        return o.toMap();
    }

    /**
     * Returns the String value for the given key, or null.
     *
     * @return Possibly null.
     */
    public String getString(String key) {
        Aobj o = get(key);
        if (o == null) return null;
        return o.toString();
    }

    @Override
    public int hashCode() {
        return entryList.hashCode();
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
        if (o == null) return true;
        return o.aonType() == Atype.NULL;
    }

    /**
     * Index of the entry using the == operator.
     */
    private int indexOf(Entry e) {
        List<Entry> l = entryList;
        for (int i = l.size(); --i >= 0; ) {
            if (l.get(i) == e) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Index of the given key, or -1.
     */
    public int indexOf(String key) {
        if (entryMap.get(key) == null) return -1;
        List<Entry> l = entryList;
        for (int i = 0, len = l.size(); i < len; i++) {
            if (key.equals(l.get(i).getKey()))
                return i;
        }
        return -1;
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
        Entry e = entryMap.get(key);
        if (e != null) {
            //lets not err if putting same key/val pair.
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
            e = new Entry(key, val);
            entryMap.put(key, e);
            entryList.add(e);
        }
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, boolean val) {
        put(key, Abool.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, double val) {
        put(key, Adbl.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, int val) {
        put(key, Aint.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, long val) {
        put(key, Along.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     */
    public Amap put(String key, String val) {
        if (val == null) {
            put(key, Anull.NULL);
        } else {
            put(key, Astr.make(val));
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

    @Override
    public Aobj remove(int idx) {
        Entry e = entryList.remove(idx);
        entryMap.remove(e.getKey());
        Aobj ret = e.getValue();
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    /**
     * Removes the key-value pair and returns the removed value.
     *
     * @return Possibly null.
     */
    public Aobj remove(String key) {
        Entry e = entryMap.remove(key);
        if (e == null) {
            return null;
        }
        entryList.remove(indexOf(e));
        Aobj ret = e.getValue();
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    @Override
    public int size() {
        return entryList.size();
    }

    @Override
    public Amap toMap() {
        return this;
    }

    // Inner Classes
    // -------------

    /**
     * Allows values to be accessed quickly by index in the list, rather than having
     * to do a key lookup in the entryMap.
     */
    static class Entry {
        String key;
        Aobj val;

        Entry(String key, Aobj val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) obj;
            return e.getKey().equals(key);
        }

        String getKey() {
            return key;
        }

        Aobj getValue() {
            return val;
        }

        public int hashCode() {
            return key.hashCode();
        }

        void setValue(Aobj val) {
            this.val = val;
        }
    }


}
