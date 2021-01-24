package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import javax.annotation.Nonnull;

/**
 * For use with VisualVM to identify bottle necks.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Profiling {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int LARGE_OBJ_FACTOR = 200;
    private static final Aobj aobjLarge;
    private static final Aobj aobjSmall;
    private static final byte[] aonLarge;
    private static final byte[] aonSmall;
    private static final byte[] jsonLarge;
    private static final byte[] jsonSmall;
    private static final NullOutputStream nullOutputStream = new NullOutputStream();
    private static final NullWriter nullWriter = new NullWriter();

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    static {
        aobjSmall = makeSmallObj();
        aobjLarge = makeLargeObj();
        ByteArrayOutputStream out;
        encodeAon(aobjSmall, out = new ByteArrayOutputStream());
        aonSmall = out.toByteArray();
        encodeAon(aobjLarge, out = new ByteArrayOutputStream());
        aonLarge = out.toByteArray();
        encodeAonJson(aobjSmall, out = new ByteArrayOutputStream());
        jsonSmall = out.toByteArray();
        encodeAonJson(aobjLarge, out = new ByteArrayOutputStream());
        jsonLarge = out.toByteArray();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package Methods
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        while (true) {
            //decodeAon(aonLarge);
            //decodeAon(aonSmall);
            //decodeAonJson(jsonLarge);
            decodeAonJson(jsonSmall);
            //encodeAon(aobjLarge, nullOutputStream);
            //encodeAon(aobjSmall, nullOutputStream);
            //encodeAonJson(aobjLarge, nullOutputStream);
            //encodeAonJson(aobjSmall, nullOutputStream);
            Thread.yield();
        }
    }

    static Aobj makeLargeObj() {
        Aobj primitiveObj = AobjTest.makeObj();
        Alist primitiveList = AlistTest.makeList();
        Aobj complexObj = primitiveObj.copy();
        complexObj.put("list", primitiveList.copy())
                  .put("object", primitiveObj.copy());
        Alist complexList = (Alist) primitiveList.copy();
        complexList.add(primitiveList.copy());
        complexList.add(primitiveObj.copy());
        Aobj testObj = new Aobj();
        for (int i = 0; i < LARGE_OBJ_FACTOR; i++) {
            if ((i % 100) == 0) {
                Aobj tmp = new Aobj();
                tmp.put("object", testObj);
                testObj = tmp;
            }
            testObj.put("object" + i, complexObj.copy());
            testObj.put("list" + i, complexList.copy());
        }
        return testObj;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    static Aobj makeSmallObj() {
        Aobj ret = new Aobj();
        ret.put("list", AlistTest.makeList());
        return ret;
    }

    private static void decodeAon(byte[] arg) {
        try {
            AonReader reader = new AonReader(new ByteArrayInputStream(arg));
            reader.getValue();
            reader.close();
        } catch (Exception io) {
            throw new RuntimeException(io);
        }
    }

    private static void decodeAonJson(byte[] arg) {
        try {
            JsonReader reader = Aon.jsonReader(new ByteArrayInputStream(arg));
            reader.getValue();
            reader.close();
        } catch (Exception io) {
            throw new RuntimeException(io);
        }
    }

    private static void encodeAon(Aobj obj, OutputStream out) {
        new AonWriter(out).value(obj).close();
    }

    private static void encodeAonJson(Aobj obj, OutputStream out) {
        new JsonWriter(out).value(obj).close();
    }

    private static class NullOutputStream extends OutputStream {

        @Override
        public void write(@Nonnull byte[] b) {
        }

        @Override
        public void write(@Nonnull byte[] b, int off, int len) {
        }

        @Override
        public void write(int b) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Initialization
    ///////////////////////////////////////////////////////////////////////////

    private static class NullWriter extends Writer {

        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        @Override
        public void write(@Nonnull char[] cbuf, int off, int len) {
        }

    }

}
