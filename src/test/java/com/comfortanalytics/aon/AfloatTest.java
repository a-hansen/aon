package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        Assertions.assertSame(val, Afloat.valueOf(10));
        validateEqual(val, Afloat.valueOf(val.toFloat()));
        Afloat val2 = Afloat.valueOf(random.nextFloat());
        validate(val2);
        validateEqual(val2, Afloat.valueOf(val2.toFloat()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()));
        validateEqual(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()), val);
        validateUnequal(Afloat.ZERO.valueOf(list.getValue(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Afloat val) {
        Assertions.assertEquals(val.aonType(), Atype.FLOAT);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Afloat.valueOf(val.toFloat()));
        Assertions.assertEquals(val.hashCode(), Afloat.valueOf(val.toFloat()).hashCode());
        Assertions.assertTrue(val.isNumber());
    }

    private void validateEqual(Afloat first, Afloat second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Afloat first, Afloat second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
