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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertTrue(Along.valueOf(101L).isLong());
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
        Assertions.assertSame(obj.aonType(), Atype.BOOLEAN);
        Assertions.assertSame(Abool.valueOf(value), obj);
        Assertions.assertTrue(obj.isBoolean());
        Assertions.assertFalse(obj.isDouble());
        Assertions.assertFalse(obj.isGroup());
        Assertions.assertFalse(obj.isInt());
        Assertions.assertFalse(obj.isList());
        Assertions.assertFalse(obj.isLong());
        Assertions.assertFalse(obj.isObj());
        Assertions.assertFalse(obj.isNull());
        Assertions.assertFalse(obj.isNumber());
        Assertions.assertFalse(obj.isString());
        Assertions.assertEquals(value, obj.toBoolean());
        int num = value ? 1 : 0;
        Assertions.assertEquals(num, obj.toDouble());
        Assertions.assertEquals(num, obj.toFloat());
        Assertions.assertEquals(num, obj.toInt());
        Assertions.assertEquals(num, obj.toLong());
        Assertions.assertEquals(value + "", obj.toString());
    }

    private void test(Aprimitive obj, double value) {
        Assertions.assertSame(obj.aonType(), Atype.DOUBLE);
        if (value < 100) {
            Assertions.assertSame(Adouble.valueOf(value), obj);
        }
        Assertions.assertFalse(obj.isBoolean());
        Assertions.assertTrue(obj.isDouble());
        Assertions.assertFalse(obj.isGroup());
        Assertions.assertFalse(obj.isInt());
        Assertions.assertFalse(obj.isList());
        Assertions.assertFalse(obj.isLong());
        Assertions.assertFalse(obj.isObj());
        Assertions.assertFalse(obj.isNull());
        Assertions.assertTrue(obj.isNumber());
        Assertions.assertFalse(obj.isString());
        if (value == 0) {
            Assertions.assertFalse(obj.toBoolean());
        } else {
            Assertions.assertTrue(obj.toBoolean());
        }
        Assertions.assertEquals(value, obj.toDouble());
        Assertions.assertEquals(value, obj.toFloat());
    }

    private void test(Aprimitive obj, int value) {
        Assertions.assertSame(obj.aonType(), Atype.INT);
        if (value <= 100) {
            Assertions.assertSame(Aint.valueOf(value), obj);
        }
        Assertions.assertFalse(obj.isBoolean());
        Assertions.assertFalse(obj.isDouble());
        Assertions.assertFalse(obj.isGroup());
        Assertions.assertTrue(obj.isInt());
        Assertions.assertFalse(obj.isList());
        Assertions.assertFalse(obj.isLong());
        Assertions.assertFalse(obj.isObj());
        Assertions.assertFalse(obj.isNull());
        Assertions.assertTrue(obj.isNumber());
        Assertions.assertFalse(obj.isString());
        if (value == 0) {
            Assertions.assertFalse(obj.toBoolean());
        } else {
            Assertions.assertTrue(obj.toBoolean());
        }
        Assertions.assertEquals(value, obj.toDouble());
        Assertions.assertEquals(value, obj.toFloat());
        Assertions.assertEquals(value, obj.toInt());
        Assertions.assertEquals(value, obj.toLong());
    }

    private void test(Aprimitive obj, long value) {
        Assertions.assertSame(obj.aonType(), Atype.LONG);
        if (value <= 100) {
            Assertions.assertSame(Along.valueOf(value), obj);
        }
        Assertions.assertFalse(obj.isBoolean());
        Assertions.assertFalse(obj.isDouble());
        Assertions.assertFalse(obj.isGroup());
        Assertions.assertFalse(obj.isInt());
        Assertions.assertFalse(obj.isList());
        Assertions.assertTrue(obj.isLong());
        Assertions.assertFalse(obj.isObj());
        Assertions.assertFalse(obj.isNull());
        Assertions.assertTrue(obj.isNumber());
        Assertions.assertFalse(obj.isString());
        if (value == 0) {
            Assertions.assertFalse(obj.toBoolean());
        } else {
            Assertions.assertTrue(obj.toBoolean());
        }
        Assertions.assertEquals(value, obj.toDouble());
        Assertions.assertEquals(value, obj.toFloat());
        Assertions.assertEquals(value, obj.toInt());
        Assertions.assertEquals(value, obj.toLong());
    }

    private void test(Aprimitive obj, String value) {
        Assertions.assertSame(obj.aonType(), Atype.STRING);
        if (value.length() == 0) {
            Assertions.assertSame(Astr.valueOf(""), obj);
        }
        Assertions.assertFalse(obj.isBoolean());
        Assertions.assertFalse(obj.isDouble());
        Assertions.assertFalse(obj.isGroup());
        Assertions.assertFalse(obj.isInt());
        Assertions.assertFalse(obj.isList());
        Assertions.assertFalse(obj.isLong());
        Assertions.assertFalse(obj.isObj());
        Assertions.assertFalse(obj.isNull());
        Assertions.assertFalse(obj.isNumber());
        Assertions.assertTrue(obj.isString());
        if (value.equals("0")) {
            Assertions.assertFalse(obj.toBoolean());
        } else if (value.equals("1")) {
            Assertions.assertTrue(obj.toBoolean());
        }
        Assertions.assertEquals(value, obj.toString());
    }

    private void testList(Alist list) {
        Assertions.assertSame(list.aonType(), Atype.LIST);
        Assertions.assertTrue(list.isList());
        int size = list.size();
        if (size == 0) {
            Assertions.assertTrue(list.isEmpty());
        }
        if (list.isEmpty()) {
            Assertions.assertEquals(size, 0);
        }
        Aprimitive tmp = Astr.valueOf("mustNotContain");
        list.add(tmp);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals((size + 1), list.size());
        Assertions.assertSame(list.remove(size), tmp);
        Assertions.assertEquals(size, list.size());
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
        Assertions.assertSame(list.removeLast(), tmp);
    }

    @SuppressWarnings("SameParameterValue")
    private void testNull(Aprimitive obj) {
        Assertions.assertSame(Anull.NULL, obj);
        Assertions.assertTrue(obj.isNull());
        Assertions.assertSame(obj.aonType(), Atype.NULL);
    }

    private void testObj() {
        testObj(new Aobj());
        Aobj object = primitiveObj();
        testPrimitiveObj(object);
        Alist list = primitiveList();
        testPrimitiveList(list);
        object.put("object", primitiveObj());
        object.put("list", primitiveList());
        Assertions.assertTrue(object.getValue("object").isObj());
        Assertions.assertTrue(object.getValue("list").isList());
        testObj(object);
        //encode and reconstitute
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        newWriter(out).value(object).close();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        object = newReader(in).getObj();
        Assertions.assertTrue(object.getValue("object").isObj());
        Assertions.assertTrue(object.getValue("list").isList());
        testPrimitiveObj(object.getValue("object"));
        list.add(primitiveObj());
        list.add(primitiveList());
        testList(list);
        Assertions.assertTrue(list.getValue(8).isObj());
        Assertions.assertTrue(list.getValue(9).isList());
        out = new ByteArrayOutputStream();
        newWriter(out).value(object).close();
        in = new ByteArrayInputStream(out.toByteArray());
        object = newReader(in).getValue().toObj();
        Assertions.assertTrue(object.getValue("object").isObj());
        Assertions.assertTrue(object.getValue("list").isList());
        testPrimitiveList(list.getValue(9).toList());
    }

    private void testObj(Aobj object) {
        Assertions.assertSame(object.aonType(), Atype.OBJECT);
        Assertions.assertTrue(object.isObj());
        int size = object.size();
        if (size == 0) {
            Assertions.assertTrue(object.isEmpty());
        }
        if (object.isEmpty()) {
            Assertions.assertEquals(size, 0);
        }
        Assertions.assertNull(object.get("mustNotContain", (String) null));
        Assertions.assertTrue(object.isNull("mustNotContain"));
        object.put("mustNotContain", 10);
        Assertions.assertFalse(object.isNull("mustNotContain"));
        Assertions.assertFalse(object.isEmpty());
        Assertions.assertEquals((size + 1), object.size());
        Assertions.assertEquals(object.remove("mustNotContain").toInt(), 10);
        Assertions.assertEquals(size, object.size());
        object.putNull("mustNotContain");
        Assertions.assertFalse(object.isEmpty());
        Assertions.assertEquals((size + 1), object.size());
        Assertions.assertTrue(object.remove("mustNotContain").isNull());
        Assertions.assertEquals(size, object.size());
        object.putNull("mustNotContain");
        object.put("mustNotContain", 10);
        Assertions.assertEquals((size + 1), object.size());
        Assertions.assertEquals(object.remove("mustNotContain").toInt(), 10);
        Assertions.assertEquals(size, object.size());
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
        Assertions.assertSame(object.removeLast(), tmp);
    }

    private void testPrimitiveList(Alist list) {
        testList(list);
        Assertions.assertTrue(list.getValue(0).isBoolean());
        Assertions.assertTrue(list.getValue(1).isDouble());
        Assertions.assertTrue(list.getValue(2).isInt());
        Assertions.assertTrue(list.getValue(3).isNumber()); //Deserializes as an int
        Assertions.assertTrue(list.getValue(4).isString());
        Assertions.assertTrue(list.getValue(5).isNull());
        Assertions.assertEquals(list.getValue(6).toInt(), 5);
        Assertions.assertEquals(list.getValue(7).toString(), "abcde");
    }

    private void testPrimitiveObj(Aobj object) {
        testObj(object);
        Assertions.assertTrue(object.getValue("boolean").isBoolean());
        Assertions.assertTrue(object.getValue("double").isDouble());
        Assertions.assertTrue(object.getValue("int").isNumber());
        Assertions.assertTrue(object.getValue("long").isNumber());
        Assertions.assertTrue(object.getValue("string").isString());
        Assertions.assertTrue(object.getValue("null").isNull());
        Assertions.assertTrue(object.getValue("boolean").toBoolean());
        Assertions.assertEquals(object.getValue("double").toDouble(), 105.001d);
        Assertions.assertEquals(object.getValue("int").toInt(), 100001);
        Assertions.assertEquals(object.getValue("long").toLong(), 123L);
        Assertions.assertEquals(object.getValue("string").toString(),
                                "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Assertions.assertTrue(object.getValue("null").isNull());
    }

}
