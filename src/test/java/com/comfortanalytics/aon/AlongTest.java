package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aaron Hansen
 */
public class AlongTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Along val = Along.valueOf(Long.MAX_VALUE);
        validate(val);
        Assert.assertTrue(Along.valueOf(10) == Along.valueOf(10));
        validateEqual(val, Along.valueOf(val.toLong()));
        Along val2 = Along.valueOf(5000000000l);
        validate(val2);
        validateEqual(val2, Along.valueOf(val2.toLong()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate((Along) list.get(0));
        validateEqual((Along) list.get(0), val);
        validateUnequal((Along) list.get(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Along val) {
        Assert.assertEquals(val.aonType(), Atype.LONG);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Along.valueOf(val.toLong()));
        Assert.assertEquals(val.hashCode(), Along.valueOf(val.toLong()).hashCode());
        Assert.assertTrue(val.isNumber());
    }

    private void validateEqual(Along first, Along second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Along first, Along second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(first.hashCode() == second.hashCode());
    }

}
