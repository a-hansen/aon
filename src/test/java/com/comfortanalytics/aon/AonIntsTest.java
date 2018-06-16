package com.comfortanalytics.aon;

import com.comfortanalytics.aon.Aobj.Member;
import java.io.File;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aaron Hansen
 */
public class AonIntsTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void testList() {
        Alist list = new Alist();
        list.add(Long.MIN_VALUE);
        list.add(Integer.MIN_VALUE);
        list.add(Short.MIN_VALUE);
        list.add(Byte.MIN_VALUE);
        list.add(-10);
        list.add(-1);
        list.add(0);
        list.add(1);
        list.add(10);
        list.add(Byte.MAX_VALUE);
        list.add(Short.MAX_VALUE);
        list.add(Integer.MAX_VALUE);
        list.add(Long.MAX_VALUE);
        Alist copy = Aon.decode(Aon.encode(list)).toList();
        Assert.assertEquals(list.size(), copy.size());
        Assert.assertEquals(list, copy);
        Assert.assertEquals(list.hashCode(), copy.hashCode());
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), copy.get(i));
        }
    }

    @Test
    public void testObj() throws Exception {
        Aobj obj = new Aobj();
        obj.put("0",Long.MIN_VALUE);
        obj.put("1", Integer.MIN_VALUE);
        obj.put("2", Short.MIN_VALUE);
        obj.put("3", Byte.MIN_VALUE);
        obj.put("4", -10);
        obj.put("5", -1);
        obj.put("6", 0);
        obj.put("7", 1);
        obj.put("8", 10);
        obj.put("9", Byte.MAX_VALUE);
        obj.put("10", Short.MAX_VALUE);
        obj.put("11", Integer.MAX_VALUE);
        obj.put("12", Long.MAX_VALUE);
        obj.put("13", Float.MIN_VALUE);
        obj.put("14", Float.MAX_VALUE);
        obj.put("15", Double.MIN_VALUE);
        obj.put("16", Double.MAX_VALUE);
        byte[] b = Aon.encode(obj);
        Aobj copy = Aon.decode(b).toObj();
        Assert.assertEquals(obj.size(), copy.size());
        Assert.assertEquals(obj, copy);
        Assert.assertEquals(obj.hashCode(), copy.hashCode());
        for (Member om = obj.getFirst(), cm = copy.getFirst(); om != null; ) {
            Assert.assertEquals(om.getValue(), cm.getValue());
            om = om.next();
            cm = cm.next();
        }
    }

}
