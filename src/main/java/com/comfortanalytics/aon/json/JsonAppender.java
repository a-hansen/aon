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

package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.Alist;
import com.comfortanalytics.aon.Amap;
import com.comfortanalytics.aon.Aobj;
import com.comfortanalytics.aon.Awriter;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Json implementation of Awriter intended for Appendables such as StringBuilders.
 * This can be used for OutputStreams and Writers as well, but JsonWriter will be faster.
 * <p>The same instance can be reused with the setOutput methods.</p>
 * <p>This class is not thread safe.</p>
 *
 * @author Aaron Hansen
 */
public class JsonAppender extends AbstractJsonWriter {

    // Constants
    // ---------

    // Fields
    // ------

    private StringBuilder buf = new StringBuilder(BUF_SIZE);
    private Appendable out;
    private boolean zip = false;
    private ZipOutputStream zout;


    // Constructors
    // ------------

    /**
     * Be sure to call one of the setOutput methods.
     */
    public JsonAppender() {
    }

    /**
     * Will write directly to the given appendable.
     */
    public JsonAppender(Appendable arg) {
        setOutput(arg);
    }

    /**
     * Creates an underlying FileWriter.
     */
    public JsonAppender(File arg) {
        setOutput(arg);
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.
     */
    public JsonAppender(File file, String zipFileName) {
        setOutput(file, zipFileName);
    }

    /**
     * Creates an underlying OutputStreamWriter.
     */
    public JsonAppender(OutputStream arg) {
        setOutput(arg);
    }

    /**
     * Will write a zip file to the given stream.
     */
    public JsonAppender(OutputStream out, String zipFileName) {
        setOutput(out, zipFileName);
    }


    // Public Methods
    // --------------

    /**
     * Append the char and return this.  Can be used for custom formatting.
     */
    @Override
    public Appendable append(char ch) {
        try {
            buf.append(ch);
            if (buf.length() >= BUF_SIZE) {
                out.append(buf);
                buf.setLength(0);
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    @Override
    public JsonAppender append(char[] ch, int off, int len) {
        try {
            buf.append(ch, off, len);
            if (buf.length() >= BUF_SIZE) {
                out.append(buf);
                buf.setLength(0);
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    @Override
    public Appendable append(CharSequence csq) {
        try {
            buf.append(csq);
            if (buf.length() >= BUF_SIZE) {
                out.append(buf);
                buf.setLength(0);
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    @Override
    public Appendable append(CharSequence csq, int start, int end) {
        try {
            buf.append(csq, start, end);
            if (buf.length() >= BUF_SIZE) {
                out.append(buf);
                buf.setLength(0);
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public void close() {
        try {
            flush();
            if (depth > 0)
                throw new IllegalStateException("Nesting error.");
            if (zout != null) {
                try {
                    zout.closeEntry();
                } catch (Exception x) {
                }
                zout = null;
            }
            if (out instanceof Closeable) {
                ((Closeable) out).close();
                out = null;
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public JsonAppender flush() {
        try {
            if (buf.length() > 0) {
                out.append(buf);
                buf.setLength(0);
            }
            if (out instanceof Flushable) {
                ((Flushable) out).flush();
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Whether or not this is zipping the output.
     */
    public boolean isZip() {
        return zip;
    }

    @Override
    public JsonAppender reset() {
        buf.setLength(0);
        return (JsonAppender) super.reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonAppender setOutput(Appendable arg) {
        if (arg == null) throw new NullPointerException();
        this.out = arg;
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonAppender setOutput(File arg) {
        try {
            if (arg == null) throw new NullPointerException();
            this.out = new FileWriter(arg);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.  Resets
     * the state and returns this.
     */
    public JsonAppender setOutput(File file, String zipFileName) {
        try {
            if (file == null) throw new NullPointerException();
            zout = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new OutputStreamWriter(zout);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonAppender setOutput(OutputStream arg) {
        if (arg == null) throw new NullPointerException();
        this.out = new OutputStreamWriter(arg);
        return reset();
    }

    /**
     * Will write a zip file to the given stream.  Resets the state and returns this.
     */
    public JsonAppender setOutput(OutputStream out, String zipFileName) {
        try {
            if (out == null) throw new NullPointerException();
            if (zipFileName == null) throw new NullPointerException();
            ZipOutputStream zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new OutputStreamWriter(zout);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    // Protected Methods
    // -----------------

    // Package Protected Methods
    // -------------------------

    // Private Methods
    // ---------------

    // Inner Classes
    // -------------


}//Main
