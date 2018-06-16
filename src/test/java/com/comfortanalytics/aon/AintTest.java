package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aaron Hansen
 */
public class AintTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Aint val = Aint.valueOf(10);
        validate(val);
        Assert.assertTrue(val == Aint.valueOf(10));
        validateEqual(val, Aint.valueOf(val.toInt()));
        Aint val2 = Aint.valueOf(random.nextInt());
        validate(val2);
        validateEqual(val2, Aint.valueOf(val2.toInt()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate((Aint) list.get(0));
        validateEqual((Aint) list.get(0), val);
        validateUnequal((Aint) list.get(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Aint val) {
        Assert.assertEquals(val.aonType(), Atype.INT);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Aint.valueOf(val.toInt()));
        Assert.assertEquals(val.hashCode(), Aint.valueOf(val.toInt()).hashCode());
        Assert.assertTrue(val.isNumber());
    }

    private void validateEqual(Aint first, Aint second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Aint first, Aint second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(first.hashCode() == second.hashCode());
    }

}
