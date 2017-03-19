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

import java.util.ArrayList;

/**
 * Indexed collection of Aobjs.  This is not thread safe.
 *
 * @author Aaron Hansen
 */
public class Alist extends Agroup {

    // Fields
    // ------

    protected ArrayList<Aobj> list = new ArrayList<Aobj>();


    // Constructors
    // ------------

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
        } else if (val.isGroup()) {
            val.toGroup().setParent(this);
        }
        list.add(val);
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(boolean val) {
        list.add(Abool.make(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(double val) {
        list.add(Adbl.make(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(long val) {
        list.add(Along.make(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(String val) {
        if (val == null) {
            return addNull();
        }
        list.add(Astr.make(val));
        return this;
    }

    /**
     * Appends the primitive and returns this.
     */
    public Alist add(int val) {
        list.add(Aint.make(val));
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
        list.add(Anull.NULL);
        return this;
    }

    @Override
    public Atype aonType() {
        return Atype.LIST;
    }

    @Override
    public Agroup clear() {
        list.clear();
        return this;
    }

    @Override
    public Aobj copy() {
        Alist ret = new Alist();
        for (int i = 0, len = list.size(); i < len; i++) {
            ret.add(get(i).copy());
        }
        return ret;
    }

    @Override
    public Aobj get(int idx) {
        return (Aobj) list.get(idx);
    }

    /**
     * Returns true.
     */
    public boolean isList() {
        return true;
    }

    /**
     * Replaces a value and returns this.
     *
     * @param val Can be null.
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, Aobj val) {
        if (idx == list.size()) {
            add(val);
            return this;
        }
        if (val == null) val = Anull.NULL;
        list.set(idx, val);
        return this;
    }

    /**
     * Primitive setter, returns this.
     *
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, boolean val) {
        put(idx, Abool.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     *
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, double val) {
        put(idx, Adbl.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     *
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, int val) {
        put(idx, Aint.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     *
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, long val) {
        put(idx, Along.make(val));
        return this;
    }

    /**
     * Primitive setter, returns this.
     *
     * @throws IndexOutOfBoundsException
     */
    public Alist put(int idx, String val) {
        put(idx, Astr.make(val));
        return this;
    }

    @Override
    public Aobj remove(int idx) {
        Aobj ret = (Aobj) list.remove(idx);
        if (ret.isGroup()) {
            ret.toGroup().setParent(null);
        }
        return ret;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Alist toList() {
        return this;
    }


}//Alist
