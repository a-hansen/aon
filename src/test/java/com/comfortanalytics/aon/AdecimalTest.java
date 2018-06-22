package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Aaron Hansen
 */
public class AdecimalTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Adecimal val = Adecimal.valueOf(new BigDecimal(new BigInteger(128, random)));
        validate(val);
        validateEqual(val, Adecimal.valueOf(val.toBigDecimal()));
        Adecimal val2 = Adecimal.valueOf(new BigDecimal(new BigInteger(128, random)));
        validate(val2);
        validateEqual(val2, Adecimal.valueOf(val2.toBigDecimal()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate((Adecimal) list.get(0));
        validateEqual((Adecimal) list.get(0), val);
        validateUnequal((Adecimal) list.get(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Adecimal val) {
        Assert.assertEquals(val.aonType(), Atype.DECIMAL);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Adecimal.valueOf(val.toBigDecimal()));
        Assert.assertEquals(val.hashCode(), Adecimal.valueOf(val.toBigDecimal()).hashCode());
        Assert.assertTrue(val.isNumber());
        Assert.assertTrue(val.isBigDecimal());
    }

    private void validateEqual(Adecimal first, Adecimal second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Adecimal first, Adecimal second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(first.hashCode() == second.hashCode());
    }

}
