package com.comfortanalytics.aon;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron Hansen
 */
public class AboolTest {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void test() {
        Abool val = Abool.valueOf(true);
        validate(val);
        Assertions.assertSame(val, Abool.valueOf(true));
        validateEqual(val, Abool.valueOf(val.toBoolean()));
        Abool val2 = Abool.valueOf(false);
        validate(val2);
        validateEqual(val2, Abool.valueOf(val2.toBoolean()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.aonBytes(list);
        list = Aon.readAon(bytes).toList();
        validate(list.get(0));
        validateEqual(list.get(0), val);
        validateUnequal(list.get(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Abool val) {
        Assertions.assertEquals(val.aonType(), Atype.BOOLEAN);
        Assertions.assertEquals(val, val);
        Assertions.assertEquals(val, Abool.valueOf(val.toBoolean()));
        Assertions.assertEquals(val.hashCode(), Abool.valueOf(val.toBoolean()).hashCode());
        Assertions.assertFalse(val.isNumber());
        Assertions.assertTrue(val.isBoolean());
        Assertions.assertEquals(val.toInt(), val.toBoolean() ? 1 : 0);
        Assertions.assertEquals(val.toString(), val.toBoolean() ? "true" : "false");
    }

    private void validateEqual(Abool first, Abool second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abool first, Abool second) {
        Assertions.assertEquals(first.aonType(), second.aonType());
        Assertions.assertNotEquals(second, first);
        Assertions.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
