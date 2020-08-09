package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SerializationTest {

    final Aobj largeObj = makeLargeObj();

    @Test
    public void testAon() {
        byte[] buf = Aon.aonBytes(largeObj);
        Aobj neu = Aon.readAon(buf);
        Assertions.assertEquals(largeObj, neu);
    }

    @Test
    public void testJson() {
        Aobj orig = makeJsonObj();
        byte[] buf = Aon.jsonBytes(orig);
        Aobj neu = Aon.readJson(buf);
        Assertions.assertEquals(orig, neu);
    }

    @Test
    public void testMsgPack() {
        Aobj orig = Profiling.makeLargeObj();
        byte[] buf = Aon.msgPackBytes(orig);
        Aobj neu = Aon.readMsgPack(buf);
        Assertions.assertEquals(orig, neu);
    }

    @Test
    public void testObjCompare() {
        Aobj neu = largeObj.copy();
        Assertions.assertEquals(largeObj, neu);
        neu.put("foo", "bar");
        Assertions.assertNotEquals(largeObj, neu);
    }

    static Aobj makeJsonObj() {
        Aobj primitiveObj = new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("long", 100001L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(true)
                .add(100.001d)
                .add(100001L)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Aobj complexObj = primitiveObj.copy();
        complexObj.put("list", primitiveList.copy())
                  .put("object", primitiveObj.copy());
        Alist complexList = (Alist) primitiveList.copy();
        complexList.add(primitiveList.copy());
        complexList.add(primitiveObj.copy());
        Aobj testObj = new Aobj();
        for (int i = 0; i < 100; i++) {
            if ((i % 100) == 0) {
                Aobj tmp = new Aobj();
                tmp.put("object", testObj);
                testObj = tmp;
            }
            testObj.put("object" + i, complexObj.copy());
            testObj.put("list" + i, complexList.copy());
        }
        return testObj;
    }

    static Aobj makeLargeObj() {
        Aobj primitiveObj = new Aobj()
                .put("decimal", new BigDecimal(Double.MAX_VALUE))
                .put("bigint", new BigInteger("" + Long.MAX_VALUE))
                .put("boolean", true)
                .put("double", 100.001d)
                .put("float", 100.001f)
                .put("smallInt", 1)
                .put("int", 100001)
                .put("long", 100001L)
                .put("bytes", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n".getBytes())
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(new BigDecimal(Double.MIN_VALUE))
                .add(new BigInteger("" + Long.MIN_VALUE))
                .add(true)
                .add(100.001d)
                .add(100.001f)
                .add(1)
                .add(100001)
                .add(100001L)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n".getBytes())
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Aobj complexObj = primitiveObj.copy();
        complexObj.put("list", primitiveList.copy())
                  .put("object", primitiveObj.copy());
        Alist complexList = (Alist) primitiveList.copy();
        complexList.add(primitiveList.copy());
        complexList.add(primitiveObj.copy());
        Aobj testObj = new Aobj();
        for (int i = 0; i < 100; i++) {
            if ((i % 100) == 0) {
                Aobj tmp = new Aobj();
                tmp.put("object", testObj);
                testObj = tmp;
            }
            testObj.put("object" + i, complexObj.copy());
            testObj.put("list" + i, complexList.copy());
        }
        return testObj;
    }


}
