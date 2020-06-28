package com.comfortanalytics.aon;

import java.math.BigInteger;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        validate(Abigint.ZERO.valueOf(list.getValue(0).toPrimitive()));
        validateEqual(Abigint.ZERO.valueOf(list.getValue(0).toPrimitive()), val);
        validateUnequal(Abigint.ZERO.valueOf(list.getValue(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Abigint val) {
        Assertions.assertEquals(val.aonType(), Atype.BIGINT);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Abigint.valueOf(val.toBigInt()));
        Assertions.assertEquals(val.hashCode(), Abigint.valueOf(val.toBigInt()).hashCode());
        Assertions.assertTrue(val.isNumber());
        Assertions.assertTrue(val.isBigInteger());
    }

    private void validateEqual(Abigint first, Abigint second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abigint first, Abigint second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
