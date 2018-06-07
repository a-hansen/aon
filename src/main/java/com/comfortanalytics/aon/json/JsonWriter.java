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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Json implementation of Awriter intended for OutputStreams and Writers.  While
 * JsonAppender can also handle OutputStreams and Writer, this is more performant.
 * <p>The same instance can be reused with the setOutput methods.</p>
 * <p>This class is not thread safe.</p>
 *
 * @author Aaron Hansen
 * @see com.comfortanalytics.aon.Awriter
 */
public class JsonWriter extends AbstractJsonWriter {

    // Constants
    // ---------

    // Fields
    // ------

    private char[] buf = new char[BUF_SIZE];
    private int buflen = 0;
    private Writer out;
    private ZipOutputStream zout;

    // Constructors
    // ------------

    /**
     * Be sure to call one of the setOutput methods.
     */
    public JsonWriter() {
    }

    public JsonWriter(File arg) {
        setOutput(arg);
    }

    public JsonWriter(File file, String charset) {
        setOutput(file, charset);
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.
     */
    public JsonWriter(File file, String charset, String zipFileName) {
        setOutput(file, charset, zipFileName);
    }

    public JsonWriter(OutputStream arg) {
        setOutput(arg);
    }

    public JsonWriter(OutputStream out, String charset) {
        setOutput(out, charset);
    }

    // Public Methods
    // --------------

    /**
     * Append the char and return this.  Can be used for custom formatting.
     */
    public Appendable append(char ch) {
        if (buflen + 1 >= BUF_SIZE) {
            flush();
        }
        buf[buflen++] = ch;
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    public AbstractJsonWriter append(char[] ch, int off, int len) {
        if (buflen + len >= BUF_SIZE) {
            flush();
        }
        System.arraycopy(ch, off, buf, buflen, len);
        buflen += len;
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    public Appendable append(CharSequence csq) {
        for (int i = 0, len = csq.length(); i < len; i++) {
            append(csq.charAt(i));
        }
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    public Appendable append(CharSequence csq, int start, int end) {
        for (int i = start; i < end; i++) {
            append(csq.charAt(i));
        }
        return this;
    }

    public void close() {
        try {
            flush();
            if (getDepth() > 0) {
                throw new IllegalStateException("Nesting error.");
            }
            if (zout != null) {
                try {
                    zout.closeEntry();
                } catch (Exception x) {
                }
                zout.close();
            }
            out.close();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    public JsonWriter flush() {
        try {
            if (buflen > 0) {
                out.write(buf, 0, buflen);
                buflen = 0;
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter reset() {
        buflen = 0;
        return (JsonWriter) super.reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonWriter setOutput(File arg) {
        try {
            if (arg == null) {
                throw new NullPointerException();
            }
            this.out = new FileWriter(arg);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonWriter setOutput(File file, String charset) {
        try {
            this.out = new OutputStreamWriter(new FileOutputStream(file), charset);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.  Resets
     * the state and returns this.
     */
    public JsonWriter setOutput(File file, String charset, String zipFileName) {
        try {
            if (file == null) {
                throw new NullPointerException();
            }
            zout = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new OutputStreamWriter(zout, charset);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonWriter setOutput(OutputStream arg) {
        if (arg == null) {
            throw new NullPointerException();
        }
        this.out = new OutputStreamWriter(arg);
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonWriter setOutput(OutputStream out, String charset) {
        try {
            if (out == null) {
                throw new NullPointerException();
            }
            this.out = new OutputStreamWriter(out, charset);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonWriter setOutput(Writer out) {
        this.out = out;
        return reset();
    }

}
