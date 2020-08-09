package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AdoubleTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Random random = new Random(System.currentTimeMillis());
        Adouble val = Adouble.valueOf(10);
        validate(val);
        validateEqual(val, Adouble.valueOf(val.toDouble()));
        Adouble val2 = Adouble.valueOf(random.nextDouble());
        validate(val2);
        validateEqual(val2, Adouble.valueOf(val2.toDouble()));
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

    private void validate(Adouble val) {
        Assertions.assertEquals(val.aonType(), Atype.DOUBLE);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Adouble.valueOf(val.toDouble()));
        Assertions.assertEquals(val.hashCode(), Adouble.valueOf(val.toDouble()).hashCode());
        Assertions.assertTrue(val.isNumber());
    }

    private void validateEqual(Adouble first, Adouble second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Adouble first, Adouble second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
