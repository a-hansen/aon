package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Aaron Hansen
 */
public class Atest {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Field
    ///////////////////////////////////////////////////////////////////////////

    private static final String test = "[\n"
            + "  {\n"
            + "    \"proto_index\": null,\n"
            + "    \"room\": 1,\n"
            + "    \"dew_point\": 28.3,\n"
            + "    \"battery\": 37,\n"
            + "    \"name\": \"Partition Sensor\",\n"
            + "    \"external_temp_1\": null,\n"
            + "    \"external_temp_2\": null,\n"
            + "    \"humidity\": 19,\n"
            + "    \"error_flags\": 0,\n"
            + "    \"id\": 587202561,\n"
            + "    \"temperature\": 73\n"
            + "  }\n"
            + "]";
    private boolean aonFormat = true;

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        //aonFormat = true;
        //allTests();
        //aonFormat = false;
        //allTests();
        Aon.jsonReader(new StringReader(test)).getList();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void allTests() {
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
        testObj();
    }

    private Areader newReader(InputStream in) {
        if (aonFormat) {
            return new AonReader(in);
        }
        return new JsonReader(in);
    }

    private Awriter newWriter(OutputStream out) {
        if (aonFormat) {
            return new AonWriter(out);
        }
        return new JsonWriter(out);
    }

    private Alist primitiveList() {
        return new Alist()
                .add(true)
                .add(100.001d)
                .add(100001)
                .add(100001l)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .addNull()
                .add(5)
                .add("abcde");
    }

    private Aobj primitiveObj() {
        return new Aobj()
                .put("boolean", true)
                .put("double", 105.001d)
                .put("int", 100001)
                .put("long", (long) 123l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .putNull("null");
    }

    private void test(Avalue obj, boolean value) {
        Assert.assertTrue(obj.aonType() == Atype.BOOLEAN);
        Assert.assertTrue(Abool.valueOf(value) == obj);
        Assert.assertTrue(obj.isBoolean());
        Assert.assertFalse(obj.isDouble());
        Assert.assertFalse(obj.isGroup());
        Assert.assertFalse(obj.isInt());
        Assert.assertFalse(obj.isList());
        Assert.assertFalse(obj.isLong());
        Assert.assertFalse(obj.isObj());
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

    private void test(Avalue obj, double value) {
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
        Assert.assertFalse(obj.isObj());
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

    private void test(Avalue obj, int value) {
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
        Assert.assertFalse(obj.isObj());
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

    private void test(Avalue obj, long value) {
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
        Assert.assertFalse(obj.isObj());
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

    private void test(Avalue obj, String value) {
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
        Assert.assertFalse(obj.isObj());
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
        Avalue tmp = Astr.valueOf("mustNotContain");
        list.add(tmp);
        Assert.assertFalse(list.isEmpty());
        Assert.assertTrue(list.size() == (size + 1));
        Assert.assertTrue(list.remove(size) == tmp);
        Assert.assertTrue(list.size() == size);
        boolean fail = true;
        try {
            tmp = new Aobj();
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

    private void testNull(Avalue obj) {
        Assert.assertTrue(Anull.NULL == obj);
        Assert.assertTrue(obj.isNull());
        Assert.assertTrue(obj.aonType() == Atype.NULL);
    }

    private void testObj() {
        testObj(new Aobj());
        Aobj object = primitiveObj();
        testPrimitiveObj(object);
        Alist list = primitiveList();
        testPrimitiveList(list);
        object.put("object", primitiveObj());
        object.put("list", primitiveList());
        Assert.assertTrue(object.get("object").isObj());
        Assert.assertTrue(object.get("list").isList());
        testObj(object);
        //encode and reconstitute
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        newWriter(out).value(object).close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        object = newReader(in).getObj();
        Assert.assertTrue(object.get("object").isObj());
        Assert.assertTrue(object.get("list").isList());
        testPrimitiveObj(object.getObj("object"));
        list.add(primitiveObj());
        list.add(primitiveList());
        testList(list);
        Assert.assertTrue(list.get(8).isObj());
        Assert.assertTrue(list.get(9).isList());
        out = new ByteArrayOutputStream();
        newWriter(out).value(object).close();
        in = new ByteArrayInputStream(out.toByteArray());
        object = newReader(in).getValue().toObj();
        Assert.assertTrue(object.get("object").isObj());
        Assert.assertTrue(object.get("list").isList());
        testPrimitiveList(list.get(9).toList());
    }

    private void testObj(Aobj object) {
        Assert.assertTrue(object.aonType() == Atype.OBJECT);
        Assert.assertTrue(object.isObj());
        int size = object.size();
        if (size == 0) {
            Assert.assertTrue(object.isEmpty());
        }
        if (object.isEmpty()) {
            Assert.assertTrue(size == 0);
        }
        Assert.assertTrue(object.get("mustNotContain", (String) null) == null);
        Assert.assertTrue(object.isNull("mustNotContain"));
        object.put("mustNotContain", 10);
        Assert.assertFalse(object.isNull("mustNotContain"));
        Assert.assertFalse(object.isEmpty());
        Assert.assertTrue(object.size() == (size + 1));
        Assert.assertTrue(object.remove("mustNotContain").toInt() == 10);
        Assert.assertTrue(object.size() == size);
        object.putNull("mustNotContain");
        Assert.assertFalse(object.isEmpty());
        Assert.assertTrue(object.size() == (size + 1));
        Assert.assertTrue(object.remove("mustNotContain").isNull());
        Assert.assertTrue(object.size() == size);
        object.putNull("mustNotContain");
        object.put("mustNotContain", 10);
        Assert.assertTrue(object.size() == (size + 1));
        Assert.assertTrue(object.remove("mustNotContain").toInt() == 10);
        Assert.assertTrue(object.size() == size);
        boolean fail = true;
        Aobj tmp = new Aobj();
        try {
            object.put("mustNotContain", tmp);
            Alist list = new Alist();
            list.add(tmp);
        } catch (Exception ignore) {
            fail = false;
        }
        if (fail) {
            throw new IllegalStateException("Parenting failure");
        }
        Assert.assertTrue(object.removeLast() == tmp);
    }

    private void testPrimitiveList(Alist list) {
        testList(list);
        Assert.assertTrue(list.get(0).isBoolean());
        Assert.assertTrue(list.get(1).isDouble());
        Assert.assertTrue(list.get(2).isInt());
        Assert.assertTrue(list.get(3).isNumber()); //Deserializes as an int
        Assert.assertTrue(list.get(4).isString());
        Assert.assertTrue(list.get(5).isNull());
        Assert.assertEquals(list.get(6).toInt(), 5);
        Assert.assertEquals(list.get(7).toString(), "abcde");
    }

    private void testPrimitiveObj(Aobj object) {
        testObj(object);
        Assert.assertTrue(object.get("boolean").isBoolean());
        Assert.assertTrue(object.get("double").isDouble());
        Assert.assertTrue(object.get("int").isNumber());
        Assert.assertTrue(object.get("long").isNumber());
        Assert.assertTrue(object.get("string").isString());
        Assert.assertTrue(object.get("null").isNull());
        Assert.assertTrue(object.getBoolean("boolean"));
        Assert.assertTrue(object.getDouble("double") == 105.001d);
        Assert.assertTrue(object.getInt("int") == 100001);
        Assert.assertTrue(object.getInt("long") == 123l);
        Assert.assertTrue(
                object.getString("string")
                      .equals("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
        Assert.assertTrue(object.get("null").isNull());
    }

}
