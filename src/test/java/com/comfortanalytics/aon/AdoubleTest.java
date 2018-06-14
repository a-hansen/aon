package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertTrue(val == Adouble.valueOf(10));
        validateEqual(val, Adouble.valueOf(val.toDouble()));
        Adouble val2 = Adouble.valueOf(random.nextDouble());
        validate(val2);
        validateEqual(val2, Adouble.valueOf(val2.toDouble()));
        validateUnequal(val, val2);
        Alist list = new Alist().add(val);
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate((Adouble) list.get(0));
        validateEqual((Adouble) list.get(0), val);
        validateUnequal((Adouble) list.get(0), val2);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void validate(Adouble val) {
        Assert.assertEquals(val.aonType(), Atype.DOUBLE);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Adouble.valueOf(val.toDouble()));
        Assert.assertEquals(val.hashCode(), Adouble.valueOf(val.toDouble()).hashCode());
        Assert.assertTrue(val.isNumber());
    }

    private void validateEqual(Adouble first, Adouble second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Adouble first, Adouble second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(first.hashCode() == second.hashCode());
    }

}
