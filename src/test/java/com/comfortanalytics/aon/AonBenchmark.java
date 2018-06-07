/* ISC License
 *
 * Copyright 2017 by Comfort Analytics, LLC.
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with
 * or without fee is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN
 * NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.comfortanalytics.aon;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import org.boon.json.JsonFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

/**
 * Compares the performance of various JSON libraries.  Also a nice test of
 * Aons compatibility.
 *
 * @author Aaron Hansen
 */
public class AonBenchmark {

    ///////////////////////////////////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////////////////////////////////

    private static final boolean AON_ONLY = false;
    private static final int LARGE_ITERATIONS = 25;
    private static final int LARGE_OBJ_FACTOR = 1000;
    private static final int SMALL_ITERATIONS = 10000;

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // Methods
    ///////////////////////////////////////////////////////////////////////////

    @Test
    public void run() throws Exception {
        Aobj smallObj = makeSmallMap();
        Aobj largeObj = makeLargeMap();
        byte[] smallBytes = encodeJson(smallObj);
        byte[] largeBytes = encodeJson(largeObj);
        System.out.println("Aon vs Json sizes");
        printAonSizes(smallBytes, largeBytes);
        System.out.println("Configuring benchmark");
        Target aon = new AonTarget();
        Target aonJson = new AonJsonTarget();
        Target boon = new BoonTarget();
        Target flex = new FlexjsonTarget();
        Target genson = new GensonTarget();
        Target gson = new GsonTarget();
        Target jackson = new JacksonTarget();
        Target jsoniter = new JsoniterTarget();
        Target simple = new JsonSimpleTarget();
        System.out.println("Small document size = " + smallBytes.length + " bytes");
        System.out.println("Large document size = " + largeBytes.length + " bytes");
        //warm up
        System.out.println("Warming up benchmark");
        runAon(aon, largeObj, LARGE_ITERATIONS, false);
        run(aonJson, largeBytes, LARGE_ITERATIONS, false);
        printField(aonJson, 15);
        System.out.println(" complete.");
        if (!AON_ONLY) {
            run(boon, largeBytes, LARGE_ITERATIONS, false);
            printField(boon, 15);
            System.out.println(" complete.");
            run(flex, largeBytes, LARGE_ITERATIONS, false);
            printField(flex, 15);
            System.out.println(" complete.");
            run(genson, largeBytes, LARGE_ITERATIONS, false);
            printField(genson, 15);
            System.out.println(" complete.");
            run(gson, largeBytes, LARGE_ITERATIONS, false);
            printField(gson, 15);
            System.out.println(" complete.");
            run(jackson, largeBytes, LARGE_ITERATIONS, false);
            printField(jackson, 15);
            System.out.println(" complete.");
            run(jsoniter, largeBytes, LARGE_ITERATIONS, false);
            printField(jsoniter, 15);
            System.out.println(" complete.");
            run(simple, largeBytes, LARGE_ITERATIONS, false);
            printField(simple, 15);
            System.out.println(" complete.");
        }
        //actual benchmark
        System.out.println("\nBegin large document benchmark");
        runAon(aonJson, largeObj, LARGE_ITERATIONS, true);
        run(aonJson, largeBytes, LARGE_ITERATIONS, true);
        if (!AON_ONLY) {
            run(boon, largeBytes, LARGE_ITERATIONS, true);
            run(flex, largeBytes, LARGE_ITERATIONS, true);
            run(genson, largeBytes, LARGE_ITERATIONS, true);
            run(gson, largeBytes, LARGE_ITERATIONS, true);
            run(jackson, largeBytes, LARGE_ITERATIONS, true);
            run(jsoniter, largeBytes, LARGE_ITERATIONS, true);
            run(simple, largeBytes, LARGE_ITERATIONS, true);
        }
        System.out.println("\nBegin small document benchmark");
        runAon(aon, smallObj, SMALL_ITERATIONS, true);
        run(aonJson, smallBytes, SMALL_ITERATIONS, true);
        if (!AON_ONLY) {
            run(boon, smallBytes, SMALL_ITERATIONS, true);
            run(flex, smallBytes, SMALL_ITERATIONS, true);
            run(genson, smallBytes, SMALL_ITERATIONS, true);
            run(gson, smallBytes, SMALL_ITERATIONS, true);
            run(jackson, smallBytes, SMALL_ITERATIONS, true);
            run(jsoniter, smallBytes, SMALL_ITERATIONS, true);
            run(simple, smallBytes, SMALL_ITERATIONS, true);
        }
        System.out.println("End benchmark\n");
    }

    public void run(Target target, byte[] doc, int iterations, boolean print) {
        long decode = runDecode(target, doc, iterations);
        long encode = runEncode(target, doc, iterations);
        if (print) {
            printResults(target, encode, decode, encode + decode);
        }
    }

    public void runAon(Target target, Aobj doc, int iterations, boolean print) {
        byte[] bytes = encodeAon(doc);
        long decode = runDecode(target, bytes, iterations);
        long encode = runEncode(target, bytes, iterations);
        if (print) {
            printResults(target, encode, decode, encode + decode);
        }
    }

    public long runDecode(Target target, byte[] doc, int iterations) {
        target.setInput(doc);
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            target.decode();
        }
        return System.currentTimeMillis() - start;
    }

    public long runEncode(Target target, byte[] doc, int iterations) {
        Object map = target.decode(new ByteArrayInputStream(doc));
        long start = System.currentTimeMillis();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < iterations; i++) {
            out.reset();
            target.encode(map, out);
        }
        return System.currentTimeMillis() - start;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private byte[] encodeAon(Aobj obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AonWriter(out).value(obj).close();
        return out.toByteArray();
    }

    private byte[] encodeJson(Aobj obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new JsonWriter(out).value(obj).close();
        return out.toByteArray();
    }


    private Aobj makeLargeMap() {
        Aobj primitiveMap = new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("int", 100001)
                .put("long", 100001l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n");
        Alist primitiveList = new Alist()
                .add(true)
                .add(100.001d)
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

    private Aobj makeSmallMap() {
        return new Aobj()
                .put("boolean", true)
                .put("double", 100.001d)
                .put("int", 100001)
                .put("long", 100001l)
                .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
                .put("list", new Alist()
                        .add(true)
                        .add(100.001d)
                        .add(100001)
                        .add(100001l)
                        .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n"));
    }

    private void printAonSizes(byte[] small, byte[] large) throws Exception {
        printField("Json ", 7);
        System.out.print(", Small obj:");
        printField(String.valueOf(small.length), 4);
        System.out.print(", Large obj:");
        printField(String.valueOf(large.length), 7);
        System.out.println();
        Aobj smallObj = new JsonReader(new ByteArrayInputStream(small), "UTF-8").getObj();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new AonWriter(out).value(smallObj).close();
        small = out.toByteArray();
        Aobj largeObj = new JsonReader(new ByteArrayInputStream(large), "UTF-8").getObj();
        out = new ByteArrayOutputStream();
        new AonWriter(out).value(largeObj).close();
        large = out.toByteArray();
        printField("Aon ", 7);
        System.out.print(", Small obj:");
        printField(String.valueOf(small.length), 4);
        System.out.print(", Large obj:");
        printField(String.valueOf(large.length), 7);
        System.out.println();
    }

    private void printField(Object obj, int len) {
        String str = obj.toString();
        for (int i = len - str.length(); --i >= 0; ) {
            System.out.print(' ');
        }
        System.out.print(str);
    }

    private void printResults(Target target, long encode, long decode, long total) {
        printField(target, 15);
        System.out.print(", Encode:");
        printField(encode + "ms", 7);
        System.out.print(", Decode:");
        printField(decode + "ms", 7);
        System.out.print(", TOTAL:");
        printField(total + "ms", 7);
        System.out.println();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Interface for the various implementations
     */
    public abstract class Target {

        private byte[] input;

        public Object decode() {
            return decode(new ByteArrayInputStream(input));
        }

        public abstract Object decode(InputStream in);

        public abstract void encode(Object map, OutputStream out);

        public void setInput(byte[] input) {
            this.input = input;
        }

    }

    /**
     * Aon
     */
    public class AonTarget extends Target {

        private JsonReader reader = new JsonReader();
        private JsonWriter writer = new JsonWriter();

        public Object decode(InputStream in) {
            try {
                return reader.setInput(new InputStreamReader(in, "UTF-8")).getObj();
            } catch (Exception io) {
                throw new RuntimeException(io);
            }
        }

        public void encode(Object map, OutputStream out) {
            Awriter aout = new AonWriter(out);
            aout.value((Aobj) map);
            aout.close();
        }

        public String toString() {
            return "Aon";
        }
    }

    /**
     * Aon json
     */
    public class AonJsonTarget extends Target {

        private JsonReader reader = new JsonReader();
        private JsonWriter writer = new JsonWriter();

        public Object decode(InputStream in) {
            try {
                return reader.setInput(new InputStreamReader(in, "UTF-8")).getObj();
            } catch (Exception io) {
                throw new RuntimeException(io);
            }
        }

        public void encode(Object map, OutputStream out) {
            writer.setOutput(new OutputStreamWriter(out)).value((Aobj) map);
        }

        public String toString() {
            return "Aon-Json";
        }
    }

    /**
     * Boon
     */
    public class BoonTarget extends Target {

        public Object decode(InputStream in) {
            return JsonFactory.fromJson(new InputStreamReader(in));
        }

        public void encode(Object map, OutputStream out) {
            JsonFactory.toJson(map, new OutputStreamWriter(out));
        }

        public String toString() {
            return "Boon";
        }
    }

    /**
     * Flexjson
     */
    public class FlexjsonTarget extends Target {

        private JSONDeserializer deserializer = new JSONDeserializer();
        private JSONSerializer serializer = new JSONSerializer();

        public Object decode(InputStream in) {
            return deserializer.deserialize(new InputStreamReader(in));
        }

        public void encode(Object map, OutputStream out) {
            serializer.serialize(map, new OutputStreamWriter(out));
        }

        public String toString() {
            return "Flexjson";
        }
    }

    /**
     * Genson
     */
    public class GensonTarget extends Target {

        private Genson genson = new Genson();

        public Object decode(InputStream in) {
            return genson.deserialize(in, Map.class);
        }

        public void encode(Object map, OutputStream out) {
            genson.serialize(map, new OutputStreamWriter(out));
        }

        public String toString() {
            return "Genson";
        }
    }

    /**
     * Gson
     */
    public class GsonTarget extends Target {

        private Gson gson = new Gson();
        private JsonParser parser = new JsonParser();

        public Object decode(InputStream in) {
            return parser.parse(new InputStreamReader(in));
        }

        public void encode(Object map, OutputStream out) {
            gson.toJson(map, new OutputStreamWriter(out));
        }

        public String toString() {
            return "Gson";
        }
    }

    /**
     * Jackson
     */
    public class JacksonTarget extends Target {

        ObjectMapper mapper = new ObjectMapper();

        public Object decode(InputStream in) {
            try {
                return mapper.readTree(in);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        public void encode(Object map, OutputStream out) {
            try {
                mapper.writeValue(out, map);
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        public String toString() {
            return "Jackson";
        }
    }

    /**
     * Json-Simple
     */
    public class JsonSimpleTarget extends Target {

        private JSONParser parser = new JSONParser();

        public Object decode(InputStream in) {
            try {
                return parser.parse(new InputStreamReader(in));
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        public void encode(Object map, OutputStream out) {
            try {
                OutputStreamWriter oow = new OutputStreamWriter(out);
                oow.write(((JSONObject) map).toJSONString());
                oow.close();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        public String toString() {
            return "Json-Simple";
        }
    }

    /**
     * Json Iterator
     */
    public class JsoniterTarget extends Target {


        public Object decode(InputStream in) {
            try {
                return JsonIterator.parse(in, 8192).readAny();
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }

        public void encode(Object map, OutputStream out) {
            try {
                JsonStream stream = new JsonStream(out, 8192);
                stream.writeVal((Any) map);
                stream.close();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
        }

        public String toString() {
            return "Json Iterator";
        }
    }

}
