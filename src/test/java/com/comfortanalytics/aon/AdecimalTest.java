package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(Adecimal.ZERO.valueOf(list.getValue(0).toPrimitive()));
        validateEqual(Adecimal.ZERO.valueOf(list.getValue(0).toPrimitive()), val);
        validateUnequal(Adecimal.ZERO.valueOf(list.getValue(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Adecimal val) {
        Assertions.assertEquals(val.aonType(), Atype.DECIMAL);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Adecimal.valueOf(val.toBigDecimal()));
        Assertions.assertEquals(val.hashCode(), Adecimal.valueOf(val.toBigDecimal()).hashCode());
        Assertions.assertTrue(val.isNumber());
        Assertions.assertTrue(val.isBigDecimal());
    }

    private void validateEqual(Adecimal first, Adecimal second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Adecimal first, Adecimal second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
