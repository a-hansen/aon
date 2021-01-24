package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AbinaryTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Abinary val = Abinary.valueOf(random());
        validate(val);
        validateEqual(val, Abinary.valueOf(val.toByteArray()));
        Abinary val2 = Abinary.valueOf(random());
        validate(val2);
        validateEqual(val2, Abinary.valueOf(val2.toByteArray()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(Abinary.EMPTY.valueOf(list.get(0).toPrimitive()));
        validateEqual(Abinary.EMPTY.valueOf(list.get(0).toPrimitive()), val);
        validateUnequal(Abinary.EMPTY.valueOf(list.get(0).toPrimitive()), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private byte[] random() {
        Random random = new Random();
        int len = random.nextInt(1000);
        byte[] ret = new byte[len];
        random.nextBytes(ret);
        return ret;
    }

    private void validate(Abinary val) {
        Assertions.assertEquals(val.aonType(), Atype.BINARY);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Abinary.valueOf(val.toByteArray()));
        Assertions.assertEquals(val.hashCode(), Abinary.valueOf(val.toByteArray()).hashCode());
        Assertions.assertFalse(val.isNumber());
        Assertions.assertTrue(val.isBinary());
    }

    private void validateEqual(Abinary first, Abinary second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abinary first, Abinary second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
