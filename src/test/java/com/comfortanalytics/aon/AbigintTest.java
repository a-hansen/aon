package com.comfortanalytics.aon;

import java.math.BigInteger;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Aaron Hansen
 */
public class AbigintTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Abigint val = Abigint.valueOf(new BigInteger(128, random));
        validate(val);
        validateEqual(val, Abigint.valueOf(val.toBigInt()));
        Abigint val2 = Abigint.valueOf(new BigInteger(128, random));
        validate(val2);
        validateEqual(val2, Abigint.valueOf(val2.toBigInt()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate(Abigint.ZERO.valueOf(list.get(0).toPrimitive()));
        validateEqual(Abigint.ZERO.valueOf(list.get(0).toPrimitive()), val);
        validateUnequal(Abigint.ZERO.valueOf(list.get(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Abigint val) {
        Assert.assertEquals(val.aonType(), Atype.BIGINT);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Abigint.valueOf(val.toBigInt()));
        Assert.assertEquals(val.hashCode(), Abigint.valueOf(val.toBigInt()).hashCode());
        Assert.assertTrue(val.isNumber());
        Assert.assertTrue(val.isBigInteger());
    }

    private void validateEqual(Abigint first, Abigint second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abigint first, Abigint second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertNotEquals(second, first);
        Assert.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
