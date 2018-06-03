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

import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aaron Hansen
 */
public class AobjTest {

    // Constants
    // ---------

    // Fields
    // ------

    // Constructors
    // ------------

    // Methods
    // -------

    private Alist primitiveList() {
        return new Alist()
                .add(true)
                .add(100.001d)
                .add(100001)
                .add(100001l)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .addNull();
    }

    private Amap primitiveMap() {
        return new Amap()
                .put("boolean", true)
                .put("double", 105.001d)
                .put("int", 100001)
                .put("long", (long) 123l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .putNull("null");
    }

    @Test
    public void test() throws Exception {
        test(Abool.TRUE, true);
        test(Abool.FALSE, false);
        test(Adouble.valueOf(0d), 0d);
        test(Adouble.valueOf(100.5d), 100.5d);
        test(Aint.valueOf(0), 0);
        test(Aint.valueOf(101), 101);
        test(Along.valueOf(0l), 0l);
        test(Along.valueOf(101l), 101l);
        Assert.assertTrue(Along.valueOf(101l).isLong());
        test(Astr.valueOf(""), "");
        test(Astr.valueOf("0"), "0");
        test(Astr.valueOf("abc"), "abc");
        testNull(Anull.NULL);
        testMap();
    }

    private void test(Aobj obj, boolean value) {
        Assert.assertTrue(obj.aonType() == Atype.BOOLEAN);
        Assert.assertTrue(Abool.valueOf(value) == obj);
        Assert.assertTrue(obj.isBoolean());
        Assert.assertFalse(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertFalse(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertFalse(obj.isLong());
        Assert.assertFalse(obj.isMap());
        Assert.assertFalse(obj.isNull());
        Assert.assertFalse(obj.isNumber());
        Assert.assertFalse(obj.isString());
        Assert.assertTrue(obj.toBoolean() == value);
        int num = value ? 1 : 0;
        Assert.assertTrue(obj.toDouble() == num);
        Assert.assertTrue(obj.toFloat() == num);
        Assert.assertTrue(obj.toInt() == num);
        Assert.assertTrue(obj.toLong() == num);
        Assert.assertTrue(obj.toString().equals(value + ""));
    }

    private void test(Aobj obj, double value) {
        Assert.assertTrue(obj.aonType() == Atype.DOUBLE);
        if (value < 100) {
            Assert.assertTrue(Adouble.valueOf(value) == obj);
        }
        Assert.assertFalse(obj.isBoolean());
        Assert.assertTrue(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertFalse(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertFalse(obj.isLong());
        Assert.assertFalse(obj.isMap());
        Assert.assertFalse(obj.isNull());
        Assert.assertTrue(obj.isNumber());
        Assert.assertFalse(obj.isString());
        if (value == 0) {
            Assert.assertFalse(obj.toBoolean());
        } else {
            Assert.assertTrue(obj.toBoolean());
        }
        Assert.assertTrue(obj.toDouble() == value);
        Assert.assertTrue(obj.toFloat() == value);
    }

    private void test(Aobj obj, int value) {
        Assert.assertTrue(obj.aonType() == Atype.INT);
        if (value <= 100) {
            Assert.assertTrue(Aint.valueOf(value) == obj);
        }
        Assert.assertFalse(obj.isBoolean());
        Assert.assertFalse(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertTrue(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertFalse(obj.isLong());
        Assert.assertFalse(obj.isMap());
        Assert.assertFalse(obj.isNull());
        Assert.assertTrue(obj.isNumber());
        Assert.assertFalse(obj.isString());
        if (value == 0) {
            Assert.assertFalse(obj.toBoolean());
        } else {
            Assert.assertTrue(obj.toBoolean());
        }
        Assert.assertTrue(obj.toDouble() == value);
        Assert.assertTrue(obj.toFloat() == value);
        Assert.assertTrue(obj.toInt() == value);
        Assert.assertTrue(obj.toLong() == value);
    }

    private void test(Aobj obj, long value) {
        Assert.assertTrue(obj.aonType() == Atype.LONG);
        if (value <= 100) {
            Assert.assertTrue(Along.valueOf(value) == obj);
        }
        Assert.assertFalse(obj.isBoolean());
        Assert.assertFalse(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertFalse(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertTrue(obj.isLong());
        Assert.assertFalse(obj.isMap());
        Assert.assertFalse(obj.isNull());
        Assert.assertTrue(obj.isNumber());
        Assert.assertFalse(obj.isString());
        if (value == 0) {
            Assert.assertFalse(obj.toBoolean());
        } else {
            Assert.assertTrue(obj.toBoolean());
        }
        Assert.assertTrue(obj.toDouble() == value);
        Assert.assertTrue(obj.toFloat() == value);
        Assert.assertTrue(obj.toInt() == value);
        Assert.assertTrue(obj.toLong() == value);
    }

    private void test(Aobj obj, String value) {
        Assert.assertTrue(obj.aonType() == Atype.STRING);
        if (value.length() == 0) {
            Assert.assertTrue(Astr.valueOf("") == obj);
        }
        Assert.assertFalse(obj.isBoolean());
        Assert.assertFalse(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertFalse(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertFalse(obj.isLong());
        Assert.assertFalse(obj.isMap());
        Assert.assertFalse(obj.isNull());
        Assert.assertFalse(obj.isNumber());
        Assert.assertTrue(obj.isString());
        if (value.equals("0")) {
            Assert.assertFalse(obj.toBoolean());
        } else if (value.equals("1")) {
            Assert.assertTrue(obj.toBoolean());
        }
        Assert.assertTrue(obj.toString().equals(value));
    }

    private void testList(Alist list) {
        Assert.assertTrue(list.aonType() == Atype.LIST);
        Assert.assertTrue(list.isList());
        int size = list.size();
        if (size == 0) {
            Assert.assertTrue(list.isEmpty());
        }
        if (list.isEmpty()) {
            Assert.assertTrue(size == 0);
        }
        Aobj tmp = Astr.valueOf("mustNotContain");
        list.add(tmp);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == (size + 1));
        Assert.assertTrue(list.remove(size) == tmp);
        Assert.assertTrue(list.size() == size);
        boolean fail = true;
        try {
            tmp = new Amap();
            list.add(tmp);
            list.add(tmp);
        } catch (Exception ignore) {
            fail = false;
        }
        if (fail) {
            throw new IllegalStateException("Parenting failure");
        }
        Assert.assertTrue(list.removeLast() == tmp);
    }

    private void testMap() {
        testMap(new Amap());
        Amap map = primitiveMap();
        testPrimitiveMap(map);
        Alist list = primitiveList();
        testPrimitiveList(list);
        map.put("map", primitiveMap());
        map.put("list", primitiveList());
        Assert.assertTrue(map.get("map").isMap());
        Assert.assertTrue(map.get("list").isList());
        testMap(map);
        //encode and reconstitute
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new JsonWriter(out, "UTF-8").value(map).close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        map = new JsonReader(in, "UTF-8").getObj().toMap();
        Assert.assertTrue(map.get("map").isMap());
        Assert.assertTrue(map.get("list").isList());
        testPrimitiveMap(map.getMap("map"));
        list.add(primitiveMap());
        list.add(primitiveList());
        testList(list);
        Assert.assertTrue(list.get(6).isMap());
        Assert.assertTrue(list.get(7).isList());
        out = new ByteArrayOutputStream();
        new JsonWriter(out, "UTF-8").value(map).close();
        in = new ByteArrayInputStream(out.toByteArray());
        map = new JsonReader(in, "UTF-8").getObj().toMap();
        Assert.assertTrue(map.get("map").isMap());
        Assert.assertTrue(map.get("list").isList());
        testPrimitiveList(list.get(7).toList());
    }

    private void testMap(Amap map) {
        Assert.assertTrue(map.aonType() == Atype.MAP);
        Assert.assertTrue(map.isMap());
        int size = map.size();
        if (size == 0) {
            Assert.assertTrue(map.isEmpty());
        }
        if (map.isEmpty()) {
            Assert.assertTrue(size == 0);
        }
        Assert.assertTrue(map.get("mustNotContain", null) == null);
        Assert.assertTrue(map.isNull("mustNotContain"));
        map.put("mustNotContain", 10);
        Assert.assertFalse(map.isNull("mustNotContain"));
        Assert.assertFalse(map.isEmpty());
        Assert.assertTrue(map.size() == (size + 1));
        Assert.assertTrue(map.remove("mustNotContain").toInt() == 10);
        Assert.assertTrue(map.size() == size);
        map.putNull("mustNotContain");
        Assert.assertFalse(map.isEmpty());
        Assert.assertTrue(map.size() == (size + 1));
        Assert.assertTrue(map.remove("mustNotContain").isNull());
        Assert.assertTrue(map.size() == size);
        map.putNull("mustNotContain");
        map.put("mustNotContain", 10);
        Assert.assertTrue(map.size() == (size + 1));
        Assert.assertTrue(map.remove("mustNotContain").toInt() == 10);
        Assert.assertTrue(map.size() == size);
        boolean fail = true;
        Amap tmp = new Amap();
        try {
            map.put("mustNotContain", tmp);
            Alist list = new Alist();
            list.add(tmp);
        } catch (Exception ignore) {
            fail = false;
        }
        if (fail) {
            throw new IllegalStateException("Parenting failure");
        }
        Assert.assertTrue(map.removeLast() == tmp);
    }

    private void testNull(Aobj obj) {
        Assert.assertTrue(Anull.NULL == obj);
        Assert.assertTrue(obj.isNull());
        Assert.assertTrue(obj.aonType() == Atype.NULL);
    }

    private void testPrimitiveList(Alist list) {
        testList(list);
        Assert.assertTrue(list.get(0).isBoolean());
        Assert.assertTrue(list.get(1).isDouble());
        Assert.assertTrue(list.get(2).isInt());
        Assert.assertTrue(list.get(3).isNumber()); //Deserializes as an int
        Assert.assertTrue(list.get(4).isString());
        Assert.assertTrue(list.get(5).isNull());
    }

    private void testPrimitiveMap(Amap map) {
        testMap(map);
        Assert.assertTrue(map.get("boolean").isBoolean());
        Assert.assertTrue(map.get("double").isDouble());
        Assert.assertTrue(map.get("int").isInt());
        Assert.assertTrue(map.get("long").isNumber()); //Deserializes as an int
        Assert.assertTrue(map.get("string").isString());
        Assert.assertTrue(map.get("null").isNull());
        Assert.assertTrue(map.getBoolean("boolean"));
        Assert.assertTrue(map.getDouble("double") == 105.001d);
        Assert.assertTrue(map.getInt("int") == 100001);
        Assert.assertTrue(map.getInt("long") == 123l);
        Assert.assertTrue(
                map.getString("string")
                   .equals("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
        Assert.assertTrue(map.get("null").isNull());
    }

}
