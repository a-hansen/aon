package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.owlike.genson.Genson;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import org.json.simple.JSONValue;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

/**
 * Benchmarks how much time applications spend submitting log records to various async
 * log handlers.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class AonBenchmark {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int LARGE_OBJ_FACTOR = 200;
    private static Aobj aobjLarge;
    private static Aobj aobjSmall;
    private static byte[] aonLarge;
    private static byte[] aonSmall;
    private static byte[] jsonLarge;
    private static byte[] jsonSmall;
    private static final NullOutputStream nullOutputStream = new NullOutputStream();
    private static final NullWriter nullWriter = new NullWriter();

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    static Aobj makeLargeObj() {
        Aobj primitiveObj = new Aobj()
                .put("boolean", true)
                .put("double", 100001.001d)
                .put("float", 100001.001f)
                .put("int", 100001)
                .put("long", 100001L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(true)
                .add(100001.001d)
                .add(100001.001f)
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

    static Aobj makeSmallObj() {
        return new Aobj()
                .put("boolean", true)
                .put("double", 10001.001d)
                .put("float", 10001.001f)
                .put("int", 100001)
                .put("long", 100001L)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .put("list", new Alist()
                        .add(true)
                        .add(100001.001d)
                        .add(100001.001f)
                        .add(100001)
                        .add(100001L)
                        .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
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
            JsonReader reader = new JsonReader(new ByteArrayInputStream(arg));
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

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    @State(Scope.Benchmark)
    public static class DecodeLargeDoc {

        @Benchmark
        public void Aon() {
            Aon.reader(new ByteArrayInputStream(aonLarge)).getValue();
        }

        @Benchmark
        public void AonJson() {
            Aon.jsonReader(new ByteArrayInputStream(jsonLarge)).getValue();
        }

        @Benchmark
        public void Genson() {
            new Genson().deserialize(jsonLarge, Map.class);
        }

        @Benchmark
        public void Gson() {
            JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
        }

        @Benchmark
        public void Jackson() {
            try {
                new ObjectMapper().readTree(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                JSONValue.parse(new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

    }

    @State(Scope.Benchmark)
    public static class DecodeSmallDoc {

        @Benchmark
        public void Aon() {
            Aon.reader(new ByteArrayInputStream(aonSmall)).getValue();
        }

        @Benchmark
        public void AonJson() {
            Aon.jsonReader(new ByteArrayInputStream(jsonSmall)).getValue();
        }

        @Benchmark
        public void Genson() {
            new Genson().deserialize(jsonSmall, Map.class);
        }

        @Benchmark
        public void Gson() {
            JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
        }

        @Benchmark
        public void Jackson() {
            try {
                new ObjectMapper().readTree(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                JSONValue.parse(new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

    }

    @State(Scope.Benchmark)
    public static class EncodeLargeDoc {

        private Object gensonObj;
        private JsonElement gsonObj;
        private Object jacksonObj;
        private Object jsonSimpleObj;

        @Benchmark
        public void Aon() {
            Aon.writer(nullOutputStream).value(aobjLarge);
        }

        @Benchmark
        public void AonJson() {
            Aon.jsonWriter(nullWriter).value(aobjLarge);
        }

        @Benchmark
        public void Genson() {
            new Genson().serialize(gensonObj, nullWriter);
        }

        @Benchmark
        public void Gson() {
            new Gson().toJson(gsonObj, nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                new ObjectMapper().writeValue(nullWriter, jacksonObj);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                JSONValue.writeJSONString(jsonSimpleObj, nullWriter);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Setup
        public void start() {
            try {
                gensonObj = new Genson().deserialize(jsonLarge, Map.class);
                gsonObj = JsonParser.parseReader(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
                jacksonObj = new ObjectMapper().readTree(
                        new ByteArrayInputStream(jsonLarge));
                jsonSimpleObj = JSONValue.parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    @State(Scope.Benchmark)
    public static class EncodeSmallDoc {

        private Object gensonObj;
        private Object gsonObj;
        private Object jacksonObj;
        private Object jsonSimpleObj;

        @Benchmark
        public void Aon() {
            Aon.writer(nullOutputStream).value(aobjSmall);
        }

        @Benchmark
        public void AonJson() {
            Aon.jsonWriter(nullWriter).value(aobjSmall);
        }

        @Benchmark
        public void Genson() {
            new Genson().serialize(gensonObj, nullWriter);
        }

        @Benchmark
        public void Gson() {
            new Gson().toJson(gsonObj, nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                new ObjectMapper().writeValue(nullWriter, jacksonObj);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                JSONValue.writeJSONString(jsonSimpleObj, nullWriter);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Setup
        public void start() {
            try {
                gensonObj = new Genson().deserialize(jsonSmall, Map.class);
                gsonObj = JsonParser.parseReader(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
                jacksonObj = new ObjectMapper().readTree(
                        new ByteArrayInputStream(jsonSmall));
                jsonSimpleObj = JSONValue.parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

    }

    private static class NullOutputStream extends OutputStream {

        @Override
        public void write(byte[] b) {
        }

        @Override
        public void write(byte[] b, int off, int len) {
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
        public void write(char[] cbuf, int off, int len) {
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
        /*
        System.out.println(" AON small doc size: " + aonSmall.length);
        System.out.println("JSON small doc size: " + jsonSmall.length);
        System.out.println(" AON large doc size: " + aonLarge.length);
        System.out.println("JSON large doc size: " + jsonLarge.length);
        */
    }

}
