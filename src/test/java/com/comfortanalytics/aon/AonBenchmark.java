package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.owlike.genson.Genson;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.boon.json.JsonFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * Benchmarks how much time applications spend submitting log records to various async
 * log handlers.
 *
 * @author Aaron Hansen
 */
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
    private static NullOutputStream nullOutputStream = new NullOutputStream();
    private static NullWriter nullWriter = new NullWriter();

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void run() throws Exception {
        int TEST_SIZE = 2;
        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                //.mode(Mode.Throughput)
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupIterations(TEST_SIZE)
                .warmupTime(TimeValue.seconds(TEST_SIZE))
                .measurementIterations(TEST_SIZE)
                .measurementTime(TimeValue.seconds(TEST_SIZE))
                .forks(TEST_SIZE)
                .threads(TEST_SIZE)
                .shouldDoGC(true)
                .jvmArgs("")
                .build();
        new Runner(opt).run();
        aobjSmall = makeSmallMap();
        aobjLarge = makeLargeMap();
        ByteArrayOutputStream out = null;
        encodeAon(aobjSmall, out = new ByteArrayOutputStream());
        aonSmall = out.toByteArray();
        encodeAon(aobjLarge, out = new ByteArrayOutputStream());
        aonLarge = out.toByteArray();
        encodeAonJson(aobjSmall, out = new ByteArrayOutputStream());
        jsonSmall = out.toByteArray();
        encodeAonJson(aobjLarge, out = new ByteArrayOutputStream());
        jsonLarge = out.toByteArray();
        System.out.println(" AON small doc size: " + aonSmall.length);
        System.out.println("JSON small doc size: " + jsonSmall.length);
        System.out.println(" AON large doc size: " + aonLarge.length);
        System.out.println("JSON large doc size: " + jsonLarge.length);
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
            JsonReader reader = new JsonReader(new ByteArrayInputStream(arg), "UTF-8");
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
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    static Aobj makeLargeMap() {
        Aobj primitiveMap = new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("float", 100.001f)
                .put("int", 100001)
                .put("long", 100001l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(true)
                .add(100.001d)
                .add(100.001f)
                .add(100001)
                .add(100001l)
                .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Aobj complexMap = (Aobj) primitiveMap.copy();
        complexMap.put("list", primitiveList.copy())
                  .put("map", primitiveMap.copy());
        Alist complexList = (Alist) primitiveList.copy();
        complexList.add(primitiveList.copy());
        complexList.add(primitiveMap.copy());
        Aobj testMap = new Aobj();
        for (int i = 0; i < LARGE_OBJ_FACTOR; i++) {
            if ((i % 100) == 0) {
                Aobj tmp = new Aobj();
                tmp.put("map", testMap);
                testMap = tmp;
            }
            testMap.put("map" + i, complexMap.copy());
            testMap.put("list" + i, complexList.copy());
        }
        return testMap;
    }

    static Aobj makeSmallMap() {
        return new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("float", 100.001f)
                .put("int", 100001)
                .put("long", 100001l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .put("list", new Alist()
                        .add(true)
                        .add(100.001d)
                        .add(100.001f)
                        .add(100001)
                        .add(100001l)
                        .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    @State(Scope.Benchmark)
    public static class DecodeLargeDoc {

        private JSONDeserializer flexjson = new JSONDeserializer();
        private Genson genson = new Genson();
        private Gson gson = new Gson();
        private ObjectMapper jackson = new ObjectMapper();
        private JSONParser jsonSimple = new JSONParser();

        @Benchmark
        public void Aon() {
            decodeAon(aonLarge);
        }

        @Benchmark
        public void AonJson() {
            decodeAonJson(jsonLarge);
        }

        @Benchmark
        public void Boon() {
            try {
                JsonFactory.fromJson(new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (Exception io) {
                throw new RuntimeException(io);
            }
        }

        @Benchmark
        public void Flexjson() {
            flexjson.deserialize(new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
        }

        @Benchmark
        public void Genson() {
            genson.deserialize(new ByteArrayInputStream(jsonLarge), Map.class);
        }

        @Benchmark
        public void Gson() {
            gson.toJson(new ByteArrayInputStream(jsonLarge), nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                jackson.readTree(new ByteArrayInputStream(jsonLarge));
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                jsonSimple.parse(new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Jsoniter() {
            try {
                JsonIterator.parse(new ByteArrayInputStream(jsonLarge), 8192).readAny();
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Separator____() {
        }

    }

    @State(Scope.Benchmark)
    public static class DecodeSmallDoc {

        private JSONDeserializer flexjson = new JSONDeserializer();
        private Genson genson = new Genson();
        private Gson gson = new Gson();
        private ObjectMapper jackson = new ObjectMapper();
        private JSONParser jsonSimple = new JSONParser();

        @Benchmark
        public void Aon() {
            decodeAon(aonSmall);
        }

        @Benchmark
        public void AonJson() {
            decodeAonJson(jsonSmall);
        }

        @Benchmark
        public void Boon() {
            try {
                JsonFactory.fromJson(new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (Exception io) {
                throw new RuntimeException(io);
            }
        }

        @Benchmark
        public void Flexjson() {
            flexjson.deserialize(new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
        }

        @Benchmark
        public void Genson() {
            genson.deserialize(new ByteArrayInputStream(jsonSmall), Map.class);
        }

        @Benchmark
        public void Gson() {
            gson.toJson(new ByteArrayInputStream(jsonSmall), nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                jackson.readTree(new ByteArrayInputStream(jsonSmall));
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            try {
                jsonSimple.parse(new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Jsoniter() {
            try {
                JsonIterator.parse(new ByteArrayInputStream(jsonSmall), 8192).readAny();
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Separator____() {
        }

    }

    @State(Scope.Benchmark)
    public static class EncodeLargeDoc {

        private Object boonObj;
        private JSONSerializer flexjson = new JSONSerializer();
        private Object flexjsonObj;
        private Genson genson = new Genson();
        private Object gensonObj;
        private Gson gson = new Gson();
        private Object gsonObj;
        private ObjectMapper jackson = new ObjectMapper();
        private Object jacksonObj;
        private JSONObject jsonSimpleObj;
        private Object jsoniterObj;

        @Benchmark
        public void Aon() {
            encodeAon(aobjLarge, nullOutputStream);
        }

        @Benchmark
        public void AonJson() {
            encodeAonJson(aobjLarge, nullOutputStream);
        }

        @Benchmark
        public void Boon() {
            JsonFactory.toJson(boonObj, nullWriter);
        }

        @Benchmark
        public void Flexjson() {
            flexjson.serialize(flexjsonObj, nullWriter);
        }

        @Benchmark
        public void Genson() {
            genson.serialize(gensonObj, nullWriter);
        }

        @Benchmark
        public void Gson() {
            gson.toJson(gsonObj, nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                jackson.writeValue(nullOutputStream, jacksonObj);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            jsonSimpleObj.toJSONString();
        }

        @Benchmark
        public void Jsoniter() {
            try {
                JsonStream stream = new JsonStream(nullOutputStream, 8192);
                stream.writeVal((Any) jsoniterObj);
                stream.close();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Separator____() {
        }

        @Setup
        public void start() {
            try {
                boonObj = JsonFactory.fromJson(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
                flexjsonObj = new JSONDeserializer().deserialize(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
                gensonObj = genson.deserialize(
                        new ByteArrayInputStream(jsonLarge), Map.class);
                gsonObj = new JsonParser().parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
                jacksonObj = jackson.readTree(
                        new ByteArrayInputStream(jsonLarge));
                jsoniterObj = JsonIterator.parse(
                        new ByteArrayInputStream(jsonLarge), 8192).readAny();
                jsonSimpleObj = (JSONObject) new JSONParser().parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonLarge)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    @State(Scope.Benchmark)
    public static class EncodeSmallDoc {

        private Object boonObj;
        private JSONSerializer flexjson = new JSONSerializer();
        private Object flexjsonObj;
        private Genson genson = new Genson();
        private Object gensonObj;
        private Gson gson = new Gson();
        private Object gsonObj;
        private ObjectMapper jackson = new ObjectMapper();
        private Object jacksonObj;
        private JSONObject jsonSimpleObj;
        private Object jsoniterObj;

        @Benchmark
        public void Aon() {
            encodeAon(aobjSmall, nullOutputStream);
        }

        @Benchmark
        public void AonJson() {
            encodeAonJson(aobjSmall, nullOutputStream);
        }

        @Benchmark
        public void Boon() {
            JsonFactory.toJson(boonObj, nullWriter);
        }

        @Benchmark
        public void Flexjson() {
            flexjson.serialize(flexjsonObj, nullWriter);
        }

        @Benchmark
        public void Genson() {
            genson.serialize(gensonObj, nullWriter);
        }

        @Benchmark
        public void Gson() {
            gson.toJson(gsonObj, nullWriter);
        }

        @Benchmark
        public void Jackson() {
            try {
                jackson.writeValue(nullOutputStream, jacksonObj);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void JsonSimple() {
            jsonSimpleObj.toJSONString();
        }

        @Benchmark
        public void Jsoniter() {
            try {
                JsonStream stream = new JsonStream(nullOutputStream, 8192);
                stream.writeVal((Any) jsoniterObj);
                stream.close();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        @Benchmark
        public void Separator____() {
        }

        @Setup
        public void start() {
            try {
                boonObj = JsonFactory.fromJson(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
                flexjsonObj = new JSONDeserializer().deserialize(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
                gensonObj = genson.deserialize(
                        new ByteArrayInputStream(jsonSmall), Map.class);
                gsonObj = new JsonParser().parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
                jacksonObj = jackson.readTree(
                        new ByteArrayInputStream(jsonSmall));
                jsoniterObj = JsonIterator.parse(
                        new ByteArrayInputStream(jsonSmall), 8192).readAny();
                jsonSimpleObj = (JSONObject) new JSONParser().parse(
                        new InputStreamReader(new ByteArrayInputStream(jsonSmall)));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    private static class NullOutputStream extends OutputStream {

        public void write(byte[] b) {
        }

        public void write(byte[] b, int off, int len) {
        }

        public void write(int b) {
        }
    }

    private static class NullWriter extends Writer {

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
        }

    }

    ///////////////////////////////////////////////////////////////////////////
    // Initialization
    ///////////////////////////////////////////////////////////////////////////

    static {
        aobjSmall = makeSmallMap();
        aobjLarge = makeLargeMap();
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
