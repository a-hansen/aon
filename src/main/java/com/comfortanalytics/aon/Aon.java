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

    public Avalue decodeAon(File in) {
        Areader reader = reader(in);
        Avalue ret = reader.getValue();
        reader.close();
        return ret;
    }

    public Avalue decodeAon(InputStream in) {
        Areader reader = reader(in);
        Avalue ret = reader.getValue();
        reader.close();
        return ret;
    }

    public void encodeAon(Agroup val, File out) {
        Awriter writer = writer(out);
        writer.value(val);
        writer.close();
    }

    /**
     * @param val   What to encode.
     * @param out   Where to encode it.
     * @param close Whether or not to close the stream.
     */
    public void encodeAon(Agroup val, OutputStream out, boolean close) {
        Awriter writer = writer(out);
        writer.value(val);
        if (close) {
            writer.close();
        }
    }

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

    public AonReader reader(File in) {
        return new AonReader(in);
    }

    public AonReader reader(InputStream in) {
        return new AonReader(in);
    }

    public AonWriter writer(File out) {
        return new AonWriter(out);
    }

    public AonWriter writer(OutputStream out) {
        return new AonWriter(out);
    }

}
