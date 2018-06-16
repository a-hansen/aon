package com.comfortanalytics.aon;

import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

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
        byte[] bytes = Aon.encode(list);
        list = Aon.decode(bytes).toList();
        validate((Abinary) list.get(0));
        validateEqual((Abinary) list.get(0), val);
        validateUnequal((Abinary) list.get(0), val2);
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
        Assert.assertEquals(val.aonType(), Atype.BINARY);
        Assert.assertEquals(val, val);
        Assert.assertEquals(val, Abinary.valueOf(val.toByteArray()));
        Assert.assertEquals(val.hashCode(), Abinary.valueOf(val.toByteArray()).hashCode());
        Assert.assertFalse(val.isNumber());
        Assert.assertTrue(val.isBinary());
    }

    private void validateEqual(Abinary first, Abinary second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertEquals(first, second);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    private void validateUnequal(Abinary first, Abinary second) {
        Assert.assertEquals(first.aonType(), second.aonType());
        Assert.assertFalse(first.equals(second));
        Assert.assertFalse(first.hashCode() == second.hashCode());
    }

}