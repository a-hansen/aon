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
 * Static convenience methods.
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

    public AonReader readAon(File in) {
        return new AonReader(in);
    }

    public AonReader readAon(InputStream in) {
        return new AonReader(in);
    }

    /**
     * Returns a reader for a UTF-8 encoded file.
     */
    public JsonReader readJson(File in) {
        return new JsonReader(in);
    }

    public JsonReader readJson(File in, Charset charset) {
        return new JsonReader(in, charset);
    }

    /**
     * Returns a reader for a UTF-8 encoded stream.
     */
    public JsonReader readJson(InputStream in) {
        return new JsonReader(in);
    }

    public JsonReader readJson(InputStream in, Charset charset) {
        return new JsonReader(in, charset);
    }

    public JsonReader readJson(Reader in) {
        return new JsonReader(in);
    }

    public AonWriter writeAon(File out) {
        return new AonWriter(out);
    }

    public AonWriter writeAon(OutputStream out) {
        return new AonWriter(out);
    }

    /**
     * Encodes in UTF-8.
     */
    public JsonWriter writeJson(File out) {
        return new JsonWriter(out);
    }

    public JsonWriter writeJson(File out, Charset charset) {
        return new JsonWriter(out);
    }

    /**
     * Encodes in UTF-8.
     */
    public JsonWriter writeJson(OutputStream out) {
        return new JsonWriter(out);
    }

    public JsonWriter writeJson(OutputStream out, Charset charset) {
        return new JsonWriter(out, charset);
    }

}
