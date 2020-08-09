package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AlongTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Along val = Along.valueOf(Long.MAX_VALUE);
        validate(val);
        Assertions.assertSame(Along.valueOf(10), Along.valueOf(10));
        validateEqual(val, Along.valueOf(val.toLong()));
        Along val2 = Along.valueOf(5000000000L);
        validate(val2);
        validateEqual(val2, Along.valueOf(val2.toLong()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(list.getValue(0));
        validateEqual(list.getValue(0), val);
        validateUnequal(list.getValue(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Along val) {
        Assertions.assertEquals(val.aonType(), Atype.LONG);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Along.valueOf(val.toLong()));
        Assertions.assertEquals(val.hashCode(), Along.valueOf(val.toLong()).hashCode());
        Assertions.assertTrue(val.isNumber());
    }

    private void validateEqual(Along first, Along second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Along first, Along second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
