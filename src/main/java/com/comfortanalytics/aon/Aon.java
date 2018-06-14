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
import java.nio.charset.Charset;

/**
 * Static conveniences.
 *
 * @author Aaron Hansen
 */
public class Aon {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Charset UTF8 = Charset.forName("UTF-8");

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Decodes an Aon encoded list or object.
     */
    public static Agroup decode(byte[] arg) {
        ByteArrayInputStream in = new ByteArrayInputStream(arg);
        return read(in, true);
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

    /**
     * Decodes an Aon encoded list or object.
     */
    public static Agroup read(File in) {
        Areader reader = null;
        try {
            reader = reader(in);
            return reader.getValue().toGroup();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Decodes an Aon encoded list or object and optionally closes the stream.
     */
    public static Agroup read(InputStream in, boolean close) {
        Areader reader = null;
        try {
            reader = reader(in);
            return reader.getValue().toGroup();
        } finally {
            if (close && (reader != null)) {
                reader.close();
            }
        }
    }

    public static AonReader reader(File in) {
        return new AonReader(in);
    }

    public static AonReader reader(InputStream in) {
        return new AonReader(in);
    }

    /**
     * Encodes using the Aon format.
     */
    public static void write(Agroup val, File out) {
        Awriter writer = null;
        try {
            writer = writer(out);
            writer.value(val);
        } finally {
            if (writer != null) {
                writer.close();
            }
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
