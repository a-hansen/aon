package com.comfortanalytics.aon;

import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Aaron Hansen
 */
public class AfloatTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Afloat val = Afloat.valueOf(10);
        validate(val);
        Assert.assertSame(val, Afloat.valueOf(10));
        validateEqual(val, Afloat.valueOf(val.toFloat()));
        Afloat val2 = Afloat.valueOf(random.nextFloat());
        validate(val2);
        validateEqual(val2, Afloat.valueOf(val2.toFloat()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()));
        validateEqual(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()), val);
        validateUnequal(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Afloat val) {
        Assert.assertEquals(val.aonType(), Atype.FLOAT);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Afloat.valueOf(val.toFloat()));
        Assert.assertEquals(val.hashCode(), Afloat.valueOf(val.toFloat()).hashCode());
        Assert.assertTrue(val.isNumber());
    }

    private void validateEqual(Afloat first, Afloat second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Afloat first, Afloat second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertNotEquals(second, first);
        Assert.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
