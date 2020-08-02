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

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Exception {
        while (true) {
            //decodeAon(aonLarge);
            //decodeAon(aonSmall);
            decodeAonJson(jsonLarge);
            decodeAonJson(jsonSmall);
            //encodeAon(aobjLarge, nullOutputStream);
            //encodeAon(aobjSmall, nullOutputStream);
            //encodeAonJson(aobjLarge, nullOutputStream);
            //encodeAonJson(aobjSmall, nullOutputStream);
            Thread.sleep(1);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package Methods
    ///////////////////////////////////////////////////////////////////////////

    static Aobj makeLargeObj() {
        Aobj primitiveObj = new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("float", 100.001f)
                .put("int", 100001)
                .put("long", 100001L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(true)
                .add(100.001d)
                .add(100.001f)
                .add(100001)
                .add(100001L)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
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

    private static Aobj makeSmallObj() {
        return new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("float", 100.001f)
                .put("int", 100001)
                .put("long", 100001L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .put("list", new Alist()
                        .add(true)
                        .add(100.001d)
                        .add(100.001f)
                        .add(100001)
                        .add(100001L)
                        .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
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

    ///////////////////////////////////////////////////////////////////////////
    // Initialization
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

}
