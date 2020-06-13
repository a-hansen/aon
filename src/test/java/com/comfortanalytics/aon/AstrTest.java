package com.comfortanalytics.aon;

import org.testng.Assert;
import org.testng.annotations.Test;

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
        Assert.assertSame(Astr.valueOf(""), Astr.valueOf(""));
        validateEqual(val, Astr.valueOf("abc"));
        Astr val2 = Astr.valueOf("def");
        validate(val2);
        validateEqual(val2, Astr.valueOf(val2.toString()));
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

    private void validate(Astr val) {
        Assert.assertEquals(val.aonType(), Atype.STRING);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Astr.valueOf(val.toString()));
        Assert.assertEquals(val.hashCode(), Astr.valueOf(val.toString()).hashCode());
        Assert.assertTrue(val.isString());
    }

    private void validateEqual(Astr first, Astr second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Astr first, Astr second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertNotEquals(second, first);
        Assert.assertNotEquals(second.hashCode(), first.hashCode());
    }

}
