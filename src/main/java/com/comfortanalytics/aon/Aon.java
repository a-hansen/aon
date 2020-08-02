package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
import com.comfortanalytics.aon.msgpack.MsgPackReader;
import com.comfortanalytics.aon.msgpack.MsgPackWriter;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Static convenience methods.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"ThrowFromFinallyBlock", "unused", "unchecked"})
public class Aon {

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Encode the object to a byte array.
     */
    public static byte[] aonBytes(Agroup arg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeAon(arg, out, true);
        return out.toByteArray();
    }

    public static AonReader aonReader(File in) {
        return new AonReader(in);
    }

    public static AonReader aonReader(InputStream in) {
        return new AonReader(in);
    }

    public static AonWriter aonWriter(File out) {
        return new AonWriter(out);
    }

    public static AonWriter aonWriter(OutputStream out) {
        return new AonWriter(out);
    }

    /**
     * True if the value == null or value.isNull.
     */
    public static boolean isNull(Adata value) {
        if (value == null) {
            return true;
        }
        return value.isNull();
    }

    /**
     * Encode the object to a byte array.
     */
    public static byte[] jsonBytes(Agroup arg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeJson(arg, out, true);
        return out.toByteArray();
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

    public static String jsonString(Agroup arg) {
        return arg.toString();
    }

    /**
     * Encodes using UTF-8.
     */
    public static JsonWriter jsonWriter(File out) {
        return new JsonWriter(out);
    }

    public static JsonWriter jsonWriter(File out, Charset charset) {
        return new JsonWriter(out);
    }

    /**
     * Encodes using UTF-8.
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
     * Encode the object to a byte array.
     */
    public static byte[] msgPackBytes(Agroup arg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeMsgPack(arg, out, true);
        return out.toByteArray();
    }

    public static MsgPackReader msgPackReader(File in) {
        return new MsgPackReader(in);
    }

    public static MsgPackReader msgPackReader(InputStream in) {
        return new MsgPackReader(in);
    }

    public static MsgPackWriter msgPackWriter(File out) {
        return new MsgPackWriter(out);
    }

    public static MsgPackWriter msgPackWriter(OutputStream out) {
        return new MsgPackWriter(out);
    }

    public static <T extends Agroup> T readAon(byte[] arg) {
        ByteArrayInputStream in = new ByteArrayInputStream(arg);
        return readAon(in, true);
    }

    public static <T extends Agroup> T readAon(File in) {
        try (Areader reader = aonReader(in)) {
            return (T) reader.getValue().toGroup();
        }
    }

    /**
     * Decodes an encoded list or object and optionally closes the stream.
     */
    public static <T extends Agroup> T readAon(InputStream in, boolean close) {
        Areader reader = null;
        try {
            reader = aonReader(in);
            return (T) reader.getValue().toGroup();
        } finally {
            if (close && (reader != null)) {
                reader.close();
            }
        }
    }

    public static <T extends Agroup> T readJson(byte[] arg) {
        ByteArrayInputStream in = new ByteArrayInputStream(arg);
        return readJson(in, true);
    }

    public static <T extends Agroup> T readJson(File file, Charset charset) {
        try (JsonReader in = jsonReader(file, charset)) {
            return (T) in.getValue().toGroup();
        }
    }

    /**
     * Decodes an encoded list or object and optionally closes the stream.
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
     * Decodes a JSON encoded list or object and optionally closes the stream.
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
        return readJson(new StringReader(str), true);
    }

    public static <T extends Agroup> T readMsgPack(byte[] arg) {
        ByteArrayInputStream in = new ByteArrayInputStream(arg);
        return readMsgPack(in, true);
    }

    public static <T extends Agroup> T readMsgPack(File in) {
        try (Areader reader = msgPackReader(in)) {
            return (T) reader.getValue().toGroup();
        }
    }

    /**
     * Decodes an encoded list or object and optionally closes the stream.
     */
    public static <T extends Agroup> T readMsgPack(InputStream in, boolean close) {
        Areader reader = null;
        try {
            reader = msgPackReader(in);
            return (T) reader.getValue().toGroup();
        } finally {
            if (close && (reader != null)) {
                reader.close();
            }
        }
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
    public static void writeAon(Agroup val, File out) {
        try (Awriter writer = aonWriter(out)) {
            writer.value(val);
        }
    }

    /**
     * Encodes using the Aon format and optionally closes the stream.
     */
    public static void writeAon(Agroup val, OutputStream out, boolean close) {
        AonWriter writer = null;
        try {
            writer = aonWriter(out);
            writer.value(val);
        } finally {
            if (close && (writer != null)) {
                writer.close();
            }
        }
    }

    /**
     * Encodes using the Json format.
     */
    public static void writeJson(Agroup val, File out) {
        try (Awriter writer = jsonWriter(out)) {
            writer.value(val);
        }
    }

    /**
     * Encodes using the Json format and optionally closes the stream.
     */
    public static void writeJson(Agroup val, OutputStream out, boolean close) {
        JsonWriter writer = null;
        try {
            writer = jsonWriter(out);
            writer.value(val);
        } finally {
            if (close && (writer != null)) {
                writer.close();
            }
        }
    }

    /**
     * Encodes using the MsgPack format.
     */
    public static void writeMsgPack(Agroup val, File out) {
        try (Awriter writer = msgPackWriter(out)) {
            writer.value(val);
        }
    }

    /**
     * Encodes using the MsgPack format and optionally closes the stream.
     */
    public static void writeMsgPack(Agroup val, OutputStream out, boolean close) {
        Awriter writer = null;
        try {
            writer = msgPackWriter(out);
            writer.value(val);
        } finally {
            if (close && (writer != null)) {
                writer.close();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    static Aprimitive ensureNotNull(@Nullable Aprimitive value) {
        if (value == null) {
            return Anull.NULL;
        }
        return value;
    }


}
