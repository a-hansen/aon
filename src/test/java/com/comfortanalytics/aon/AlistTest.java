package com.comfortanalytics.aon;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * @author Aaron Hansen
 */
public class AlistTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Alist list1 = makeList();
        System.out.println(list1);
        Alist list2 = Aon.readAon(Aon.aonBytes(list1));
        Assertions.assertEquals(list1, list2);
        list2 = Aon.readJson(Aon.jsonBytes(list1));
        Assertions.assertEquals(list1.toString(), list2.toString());
        list2 = Aon.readMsgPack(Aon.msgPackBytes(list1));
        Assertions.assertEquals(list1, list2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package Methods
    ///////////////////////////////////////////////////////////////////////////

    static Alist makeList() {
        return new Alist()
                .add(true)
                .add(false)
                .add(0d)
                .add(1234.1234d)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .add(0f)
                .add(1234.1234f)
                .add(Float.MIN_VALUE)
                .add(Float.MAX_VALUE)
                .add(0)
                .add(1234)
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(0L)
                .add(1234L)
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add("abc")
                .add("")
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .addNull();
    }

}
