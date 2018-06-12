package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonReader;
import com.comfortanalytics.aon.io.AonWriter;
import com.comfortanalytics.aon.json.JsonReader;
import com.comfortanalytics.aon.json.JsonWriter;
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
     * Returns a reader for a UTF-8 encoded file.
     */
    public JsonReader jsonReader(File in) {
        return new JsonReader(in);
    }

    public JsonReader jsonReader(File in, Charset charset) {
        return new JsonReader(in, charset);
    }

    /**
     * Returns a reader for a UTF-8 encoded stream.
     */
    public JsonReader jsonReader(InputStream in) {
        return new JsonReader(in);
    }

    public JsonReader jsonReader(InputStream in, Charset charset) {
        return new JsonReader(in, charset);
    }

    public JsonReader jsonReader(Reader in) {
        return new JsonReader(in);
    }

    /**
     * Encodes in UTF-8.
     */
    public JsonWriter jsonWriter(File out) {
        return new JsonWriter(out);
    }

    public JsonWriter jsonWriter(File out, Charset charset) {
        return new JsonWriter(out);
    }

    /**
     * Encodes in UTF-8.
     */
    public JsonWriter jsonWriter(OutputStream out) {
        return new JsonWriter(out);
    }

    public JsonWriter jsonWriter(OutputStream out, Charset charset) {
        return new JsonWriter(out, charset);
    }

    public Avalue read(File in) {
        Areader reader = null;
        try {
            reader = reader(in);
            return reader.getValue();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Decodes the next value in the stream and optionally closes the stream.
     *
     * @param in    Where to read from.
     * @param close Whether or not to close the input.
     */
    public Avalue read(InputStream in, boolean close) {
        Areader reader = null;
        try {
            reader = reader(in);
            return reader.getValue();
        } finally {
            if (close && (reader != null)) {
                reader.close();
            }
        }
    }

    public AonReader reader(File in) {
        return new AonReader(in);
    }

    public AonReader reader(InputStream in) {
        return new AonReader(in);
    }

    public void write(Agroup val, File out) {
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
     * Encodes the next value in the stream and optionally closes the stream.
     *
     * @param val   What to encode.
     * @param out   Where to encode it.
     * @param close Whether or not to close the stream.
     */
    public void write(Agroup val, OutputStream out, boolean close) {
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

    public AonWriter writer(File out) {
        return new AonWriter(out);
    }

    public AonWriter writer(OutputStream out) {
        return new AonWriter(out);
    }

}
