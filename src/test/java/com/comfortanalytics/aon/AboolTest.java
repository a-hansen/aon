package com.comfortanalytics.aon;

import org.testng.Assert;
import org.testng.annotations.Test;

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
        Assert.assertSame(val, Abool.valueOf(true));
        validateEqual(val, Abool.valueOf(val.toBoolean()));
        Abool val2 = Abool.valueOf(false);
        validate(val2);
        validateEqual(val2, Abool.valueOf(val2.toBoolean()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate(list.getValue(0));
        validateEqual(list.getValue(0), val);
        validateUnequal(list.getValue(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Abool val) {
        Assert.assertEquals(val.aonType(), Atype.BOOLEAN);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Abool.valueOf(val.toBoolean()));
        Assert.assertEquals(val.hashCode(), Abool.valueOf(val.toBoolean()).hashCode());
        Assert.assertFalse(val.isNumber());
        Assert.assertTrue(val.isBoolean());
        Assert.assertEquals(val.toInt(), val.toBoolean() ? 1 : 0);
        Assert.assertEquals(val.toString(), val.toBoolean() ? "true" : "false");
    }

    private void validateEqual(Abool first, Abool second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abool first, Abool second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertNotEquals(second, first);
        Assert.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
