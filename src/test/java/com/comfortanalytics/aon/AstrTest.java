package com.comfortanalytics.aon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AstrTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Astr val = Astr.valueOf("abc");
        validate(val);
        Assertions.assertSame(Astr.valueOf(""), Astr.valueOf(""));
        validateEqual(val, Astr.valueOf("abc"));
        Astr val2 = Astr.valueOf("def");
        validate(val2);
        validateEqual(val2, Astr.valueOf(val2.toString()));
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

    private void validate(Astr val) {
        Assertions.assertEquals(val.aonType(), Atype.STRING);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Astr.valueOf(val.toString()));
        Assertions.assertEquals(val.hashCode(), Astr.valueOf(val.toString()).hashCode());
        Assertions.assertTrue(val.isString());
    }

    private void validateEqual(Astr first, Astr second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Astr first, Astr second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
