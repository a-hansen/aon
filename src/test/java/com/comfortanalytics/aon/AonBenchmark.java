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

import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.owlike.genson.Genson;
import java.io.*;
import java.util.Map;
import org.boon.json.JsonFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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

    private static int ITERATIONS = 100;
    private static int SIZE_FACTOR = 500;

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
        System.out.println("Configuring benchmark");
        byte[] test = makeMap();
        Target aon = new AonTarget();
        Target boon = new BoonTarget();
        Target flex = new FlexjsonTarget();
        Target genson = new GensonTarget();
        Target gson = new GsonTarget();
        Target jackson = new JacksonTarget();
        Target simple = new JsonSimpleTarget();
        System.out.println("Test Size = " + test.length + " bytes");
        //warm up
        System.out.println("Warming up benchmark");
        run(aon, test, false);
        run(boon, test, false);
        run(flex, test, false);
        run(genson, test, false);
        run(gson, test, false);
        run(jackson, test, false);
        run(simple, test, false);
        //actual benchmark
        System.out.println("Begin benchmark");
        run(aon, test, true);
        run(boon, test, true);
        run(flex, test, true);
        run(genson, test, true);
        run(gson, test, true);
        run(jackson, test, true);
        run(simple, test, true);
        System.out.println("End benchmark");
    }

    public void run(Target target, byte[] doc, boolean print) {
        long start = System.currentTimeMillis();
        //decode
        long decode = runDecode(target, doc);
        long encode = runEncode(target, doc);
        if (print) {
            printResults(target,encode,decode,encode+decode);
        }
    }

    public long runDecode(Target target, byte[] doc) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            target.decode(new ByteArrayInputStream(doc));
        }
        return System.currentTimeMillis() - start;
    }

    public long runEncode(Target target, byte[] doc) {
        long start = System.currentTimeMillis();
        Object map = target.decode(new ByteArrayInputStream(doc));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < ITERATIONS; i++) {
            out.reset();
            target.encode(map, out);
        }
        return System.currentTimeMillis() - start;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private byte[] makeMap() {
        Amap primitiveMap = new Amap()
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
        Amap complexMap = (Amap) primitiveMap.copy();
        complexMap.put("list", primitiveList.copy())
                  .put("map", primitiveMap.copy());
        Alist complexList = (Alist) primitiveList.copy();
        complexList.add(primitiveList.copy());
        complexList.add(primitiveMap.copy());
        Amap testMap = new Amap();
        for (int i = 0; i < SIZE_FACTOR; i++) {
            testMap.put("map" + i, complexMap.copy());
            testMap.put("list" + i, complexList.copy());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new JsonWriter(out).value(testMap).close();
        return out.toByteArray();
    }

    private void printField(Object obj, int len) {
        String str = obj.toString();
        for (int i = len - str.length(); --i >= 0; ) {
            System.out.print(' ');
        }
        System.out.print(str);
    }

    private void printResults(Target target, long encode, long decode, long total) {
        printField(target,15);
        System.out.print(", Encode:");
        printField(encode+"ms",7);
        System.out.print(", Decode:");
        printField(decode+"ms",7);
        System.out.print(", TOTAL:");
        printField(total+"ms",7);
        System.out.println();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Interface for the various implementations
     */
    public static interface Target {

        public Object decode(InputStream in);

        public void encode(Object map, OutputStream out);
    }

    /**
     * Aon
     */
    public static class AonTarget implements Target {

        private JsonReader reader = new JsonReader();
        private JsonWriter writer = new JsonWriter().setMinify(true);

        public Object decode(InputStream in) {
            return reader.setInput(in, "UTF-8").getMap();
        }

        public void encode(Object map, OutputStream out) {
            writer.setOutput(out).value((Amap) map);
        }

        public String toString() {
            return "Aon";
        }
    }

    /**
     * Boon
     */
    public static class BoonTarget implements Target {

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
    public static class FlexjsonTarget implements Target {

        private JSONSerializer serializer = new JSONSerializer();
        private JSONDeserializer deserializer = new JSONDeserializer();

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
    public static class GensonTarget implements Target {

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
    public static class GsonTarget implements Target {

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
    public static class JacksonTarget implements Target {

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
    public static class JsonSimpleTarget implements Target {

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

}
