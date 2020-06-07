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
@SuppressWarnings("unused")
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
    private final boolean aonFormat = true;

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
        test(Along.valueOf(0L), 0L);
        test(Along.valueOf(101L), 101L);
        Assert.assertTrue(Along.valueOf(101L).isLong());
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
                .add(100001L)
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
                .put("long", 123L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .putNull("null");
    }

    private void test(Aprimitive obj, boolean value) {
        Assert.assertSame(obj.aonType(), Atype.BOOLEAN);
        Assert.assertSame(Abool.valueOf(value), obj);
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
        Assert.assertEquals(value, obj.toBoolean());
        int num = value ? 1 : 0;
        Assert.assertEquals(num, obj.toDouble());
        Assert.assertEquals(num, obj.toFloat());
        Assert.assertEquals(num, obj.toInt());
        Assert.assertEquals(num, obj.toLong());
        Assert.assertEquals(value + "", obj.toString());
    }

    private void test(Aprimitive obj, double value) {
        Assert.assertSame(obj.aonType(), Atype.DOUBLE);
        if (value < 100) {
            Assert.assertSame(Adouble.valueOf(value), obj);
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
        Assert.assertEquals(value, obj.toDouble());
        Assert.assertEquals(value, obj.toFloat());
    }

    private void test(Aprimitive obj, int value) {
        Assert.assertSame(obj.aonType(), Atype.INT);
        if (value <= 100) {
            Assert.assertSame(Aint.valueOf(value), obj);
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
        Assert.assertEquals(value, obj.toDouble());
        Assert.assertEquals(value, obj.toFloat());
        Assert.assertEquals(value, obj.toInt());
        Assert.assertEquals(value, obj.toLong());
    }

    private void test(Aprimitive obj, long value) {
        Assert.assertSame(obj.aonType(), Atype.LONG);
        if (value <= 100) {
            Assert.assertSame(Along.valueOf(value), obj);
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
        Assert.assertEquals(value, obj.toDouble());
        Assert.assertEquals(value, obj.toFloat());
        Assert.assertEquals(value, obj.toInt());
        Assert.assertEquals(value, obj.toLong());
    }

    private void test(Aprimitive obj, String value) {
        Assert.assertSame(obj.aonType(), Atype.STRING);
        if (value.length() == 0) {
            Assert.assertSame(Astr.valueOf(""), obj);
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
        Assert.assertEquals(value, obj.toString());
    }

    private void testList(Alist list) {
        Assert.assertSame(list.aonType(), Atype.LIST);
        Assert.assertTrue(list.isList());
        int size = list.size();
        if (size == 0) {
            Assert.assertTrue(list.isEmpty());
        }
        if (list.isEmpty()) {
            Assert.assertEquals(size, 0);
        }
        Aprimitive tmp = Astr.valueOf("mustNotContain");
        list.add(tmp);
        Assert.assertFalse(list.isEmpty());
        Assert.assertEquals((size + 1), list.size());
        Assert.assertSame(list.remove(size), tmp);
        Assert.assertEquals(size, list.size());
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
        Assert.assertSame(list.removeLast(), tmp);
    }

    @SuppressWarnings("SameParameterValue")
    private void testNull(Aprimitive obj) {
        Assert.assertSame(Anull.NULL, obj);
        Assert.assertTrue(obj.isNull());
        Assert.assertSame(obj.aonType(), Atype.NULL);
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
        Assert.assertSame(object.aonType(), Atype.OBJECT);
        Assert.assertTrue(object.isObj());
        int size = object.size();
        if (size == 0) {
            Assert.assertTrue(object.isEmpty());
        }
        if (object.isEmpty()) {
            Assert.assertEquals(size, 0);
        }
        Assert.assertNull(object.get("mustNotContain", null));
        Assert.assertTrue(object.isNull("mustNotContain"));
        object.put("mustNotContain", 10);
        Assert.assertFalse(object.isNull("mustNotContain"));
        Assert.assertFalse(object.isEmpty());
        Assert.assertEquals((size + 1), object.size());
        Assert.assertEquals(object.remove("mustNotContain").toInt(), 10);
        Assert.assertEquals(size, object.size());
        object.putNull("mustNotContain");
        Assert.assertFalse(object.isEmpty());
        Assert.assertEquals((size + 1), object.size());
        Assert.assertTrue(object.remove("mustNotContain").isNull());
        Assert.assertEquals(size, object.size());
        object.putNull("mustNotContain");
        object.put("mustNotContain", 10);
        Assert.assertEquals((size + 1), object.size());
        Assert.assertEquals(object.remove("mustNotContain").toInt(), 10);
        Assert.assertEquals(size, object.size());
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
        Assert.assertSame(object.removeLast(), tmp);
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
        Assert.assertEquals(object.getDouble("double"), 105.001d);
        Assert.assertEquals(object.getInt("int"), 100001);
        Assert.assertEquals(object.getInt("long"), 123L);
        Assert.assertEquals(object.getString("string"),
                            "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Assert.assertTrue(object.get("null").isNull());
    }

}
