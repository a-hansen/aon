/* Copyright 2017 by Aaron Hansen.
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

package com.ca.aon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * String keyed collection of Aobjs that preserves the order of addition.
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
    protected List keys = new ArrayList();
    protected Map map = new HashMap();


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
        keys.clear();
        map.clear();
        return this;
    }

    @Override
    public Aobj copy() {
        Amap ret = new Amap();
        ret.keys.addAll(keys);
        ret.map.putAll(map);
        return ret;
    }

    @Override
    public Aobj get(int idx) {
        return (Aobj) map.get(keys.get(idx));
    }

    /**
     * Returns the object for the given key.
     *
     * @return Possibly null.
     */
    public Aobj get(String key) {
        return (Aobj) map.get(key);
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
        return (String) keys.get(idx);
    }

    /**
     * Returns the map value for the given key, or null.
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

    /**
     * Returns true.
     */
    public boolean isMap() {
        return true;
    }

    /**
     * Returns true if the key isn't in the map, or it's value is null.
     */
    public boolean isNull(String key) {
        Aobj o = get(key);
        if (o == null) return true;
        return o.aonType() == Atype.NULL;
    }

    /**
     * Index of the given key, or -1.
     */
    public int indexOf(String key) {
        if (map.get(key) == null) return -1;
        List l = keys;
        for (int i = 0, len = l.size(); i < len; i++) {
            if (key.equals((String) l.get(i)))
                return i;
        }
        return -1;
    }

    /**
     * Adds or replaces the value for the given key and returns this.
     *
     * @param key Must not be null.
     * @param val Can be null.
     * @return this
     */
    public Amap put(String key, Aobj val) {
        if (val == null) val = Anull.NULL;
        if (map.put(key, val) == null) {
            keys.add(key);
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
        put(key, Astr.make(val));
        return this;
    }

    /**
     * Puts a String representing the stack trace into the map.
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
     * Puts a new map for given key and returns it.
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
        Aobj ret = Anull.NULL;
        map.put(key, ret);
        keys.add(key);
        return this;
    }

    @Override
    public Aobj remove(int idx) {
        String key = (String) keys.remove(idx);
        return (Aobj) map.remove(key);
    }

    /**
     * Removes the key-value pair and returns the removed value.
     *
     * @return Possibly null.
     */
    public Aobj remove(String key) {
        Aobj obj = (Aobj) map.remove(key);
        if (obj != null) {
            keys.remove(key);
        }
        return obj;
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public Amap toMap() {
        return this;
    }


}
