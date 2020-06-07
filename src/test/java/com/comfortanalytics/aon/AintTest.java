package com.comfortanalytics.aon;

import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Aaron Hansen
 */
public class AintTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Aint val = Aint.valueOf(10);
        validate(val);
        Assert.assertSame(val, Aint.valueOf(10));
        validateEqual(val, Aint.valueOf(val.toInt()));
        Aint val2 = Aint.valueOf(random.nextInt());
        validate(val2);
        validateEqual(val2, Aint.valueOf(val2.toInt()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate(Aint.ZERO.valueOf(list.get(0).toPrimitive()));
        validateEqual(Aint.ZERO.valueOf(list.get(0).toPrimitive()), val);
        validateUnequal(Aint.ZERO.valueOf(list.get(0).toPrimitive()), val2);
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
        Assert.assertNotEquals(second, first);
        Assert.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
