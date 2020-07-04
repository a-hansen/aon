package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Static conveniences.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"ThrowFromFinallyBlock", "unused", "unchecked"})
public class Aon {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Decodes an Aon encoded list or object.
     */
    public static <T extends Agroup> T decode(byte[] arg) {
        ByteArrayInputStream in = new ByteArrayInputStream(arg);
        return (T) read(in, true);
    }

    /**
     * Encodes using Aon format.
     */
    public static byte[] encode(Agroup arg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        write(arg, out, true);
        return out.toByteArray();
    }

    /**
     * True if the value == null or isNull.
     */
    public static boolean isNull(AIvalue value) {
        if (value == null) {
            return true;
        }
        return value.isNull();
    }

    /**
     * Returns a reader for a UTF-8 encoded file.
     */
    public static JsonReader jsonReader(File in) {
        return new JsonReader(in);
    }

    public static JsonReader jsonReader(File in, Charset charset) {
        return new JsonReader(in, charset);
    }

    /**
     * Returns a reader for a UTF-8 encoded stream.
     */
    public static JsonReader jsonReader(InputStream in) {
        return new JsonReader(in);
    }

    public static JsonReader jsonReader(InputStream in, Charset charset) {
        return new JsonReader(in, charset);
    }

    public static JsonReader jsonReader(Reader in) {
        return new JsonReader(in);
    }

    /**
     * Encodes in UTF-8.
     */
    public static JsonWriter jsonWriter(File out) {
        return new JsonWriter(out);
    }

    public static JsonWriter jsonWriter(File out, Charset charset) {
        return new JsonWriter(out);
    }

    /**
     * Encodes in UTF-8.
     */
    public static JsonWriter jsonWriter(OutputStream out) {
        return new JsonWriter(out);
    }

    public static JsonWriter jsonWriter(OutputStream out, Charset charset) {
        return new JsonWriter(out, charset);
    }

    public static JsonWriter jsonWriter(Writer out) {
        return new JsonWriter(out);
    }

    /**
     * Decodes an Aon encoded list or object.
     */
    public static <T extends Agroup> T read(File in) {
        try (Areader reader = reader(in)) {
            return (T) reader.getValue().toGroup();
        }
    }

    /**
     * Decodes an Aon encoded list or object and optionally closes the stream.
     */
    public static <T extends Agroup> T read(InputStream in, boolean close) {
        Areader reader = null;
        try {
            reader = reader(in);
            return (T) reader.getValue().toGroup();
        } finally {
            if (close && (reader != null)) {
                reader.close();
            }
        }
    }

    public static <T extends Agroup> T readJson(File file, Charset charset) {
        try (JsonReader in = jsonReader(file, charset)) {
            return (T) in.getValue().toGroup();
        }
    }

    /**
     * Decodes an JSON encoded list or object and optionally closes the stream.
     */
    public static <T extends Agroup> T readJson(InputStream in, boolean close) {
        JsonReader reader;
        try {
            reader = jsonReader(in);
            return (T) reader.getValue().toGroup();
        } finally {
            if (close) {
                try {
                    in.close();
                } catch (Exception x) {
                    throw new RuntimeException(x);
                }
            }
        }
    }

    /**
     * Decodes an JSON encoded list or object and optionally closes the stream.
     */
    public static <T extends Agroup> T readJson(Reader in, boolean close) {
        JsonReader reader;
        try {
            reader = jsonReader(in);
            return (T) reader.getValue().toGroup();
        } finally {
            if (close) {
                try {
                    in.close();
                } catch (Exception x) {
                    throw new RuntimeException(x);
                }
            }
        }

    }

    public static <T extends Agroup> T readJson(String str) {
        return (T) readJson(new StringReader(str), true);
    }

    public static AonReader reader(File in) {
        return new AonReader(in);
    }

    public static AonReader reader(InputStream in) {
        return new AonReader(in);
    }

    public static Adecimal valueOf(BigDecimal val) {
        return Adecimal.valueOf(val);
    }

    public static Abigint valueOf(BigInteger val) {
        return Abigint.valueOf(val);
    }

    public static Abool valueOf(boolean val) {
        return Abool.valueOf(val);
    }

    public static Adouble valueOf(double val) {
        return Adouble.valueOf(val);
    }

    public static Afloat valueOf(float val) {
        return Afloat.valueOf(val);
    }

    public static Aint valueOf(int val) {
        return Aint.valueOf(val);
    }

    public static Along valueOf(long val) {
        return Along.valueOf(val);
    }

    public static Astr valueOf(String val) {
        return Astr.valueOf(val);
    }

    /**
     * Encodes using the Aon format.
     */
    public static void write(Agroup val, File out) {
        try (Awriter writer = writer(out)) {
            writer.value(val);
        }
    }

    /**
     * Encodes using the Aon format and optionally closes the stream.
     */
    public static void write(Agroup val, OutputStream out, boolean close) {
        AonWriter writer = null;
        try {
            writer = writer(out);
            writer.value(val);
        } finally {
            if (close && (writer != null)) {
                writer.close();
            }
        }
    }

    public static AonWriter writer(File out) {
        return new AonWriter(out);
    }

    public static AonWriter writer(OutputStream out) {
        return new AonWriter(out);
    }

}
