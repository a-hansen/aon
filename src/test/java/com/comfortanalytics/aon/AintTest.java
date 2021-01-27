package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertSame(val, Aint.valueOf(10));
        validateEqual(val, Aint.valueOf(val.toInt()));
        Aint val2 = Aint.valueOf(random.nextInt());
        validate(val2);
        validateEqual(val2, Aint.valueOf(val2.toInt()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(Aint.ZERO.valueOf(list.get(0).toPrimitive()));
        validateEqual(Aint.ZERO.valueOf(list.get(0).toPrimitive()), val);
        validateUnequal(Aint.ZERO.valueOf(list.get(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Aint val) {
        Assertions.assertEquals(val.aonType(), Atype.INT);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Aint.valueOf(val.toInt()));
        Assertions.assertEquals(val.hashCode(), Aint.valueOf(val.toInt()).hashCode());
        Assertions.assertTrue(val.isNumber());
    }

    private void validateEqual(Aint first, Aint second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Aint first, Aint second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
