package com.comfortanalytics.aon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AobjTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Aobj obj1 = makeObj();
        Aobj obj2 = Aon.readAon(Aon.aonBytes(obj1));
        Assertions.assertEquals(obj1, obj2);
        obj2 = Aon.readJson(Aon.jsonBytes(obj1));
        Assertions.assertEquals(obj1.toString(), obj2.toString());
        obj2 = Aon.readMsgPack(Aon.msgPackBytes(obj1));
        Assertions.assertEquals(obj1, obj2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package Methods
    ///////////////////////////////////////////////////////////////////////////

    static Aobj makeObj() {
        return new Aobj()
                .put("true", true)
                .put("false", false)
                .put("dbl0", 0d)
                .put("dbl1234", 1234.1234d)
                .put("dblMin", Double.MIN_VALUE)
                .put("dblMax", Double.MAX_VALUE)
                .put("flt0", 0f)
                .put("flt1234", 1234.1234f)
                .put("fltMin", Float.MIN_VALUE)
                .put("fltMax", Float.MAX_VALUE)
                .put("int0", 0)
                .put("int1234", 1234)
                .put("intMin", Integer.MIN_VALUE)
                .put("intMax", Integer.MAX_VALUE)
                .put("long0", 0L)
                .put("long1234", 1234L)
                .put("longMin", Long.MIN_VALUE)
                .put("longMax", Long.MAX_VALUE)
                .put("string", "abc")
                .put("stringEmpty", "")
                .put("stringUni", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .putNull("null");
    }

}
