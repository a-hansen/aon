package com.comfortanalytics.aon.json;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private StringBuilder buf = new StringBuilder(BUF_SIZE);
    private Appendable out;
    private boolean zip = false;
    private ZipOutputStream zout;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Be sure to call one of the setOutput methods.
     */
    public JsonAppender() {
    }

    public JsonAppender(Appendable arg) {
        setOutput(arg);
    }

    public JsonAppender(File arg) {
        setOutput(arg);
    }

    public JsonAppender(File file, String charset) {
        setOutput(file, charset);
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.
     */
    public JsonAppender(File file, String charset, String zipFileName) {
        setOutput(file, charset, zipFileName);
    }

    public JsonAppender(OutputStream arg) {
        setOutput(arg);
    }

    public JsonAppender(OutputStream out, String charset) {
        setOutput(out, charset);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

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
        if (arg == null) {
            throw new NullPointerException();
        }
        this.out = arg;
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonAppender setOutput(File arg) {
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
     * Will create a zip file using the zipFileName as file name inside the zip.  Resets
     * the state and returns this.
     */
    public JsonAppender setOutput(File file, String charset) {
        try {
            if (file == null) {
                throw new NullPointerException();
            }
            this.out = new OutputStreamWriter(new FileOutputStream(file), charset);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.  Resets
     * the state and returns this.
     */
    public JsonAppender setOutput(File file, String charset, String zipFileName) {
        try {
            if (file == null) {
                throw new NullPointerException();
            }
            zout = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new OutputStreamWriter(zout, charset);
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
        if (arg == null) {
            throw new NullPointerException();
        }
        this.out = new OutputStreamWriter(arg);
        return reset();
    }

    /**
     * Sets the sink, resets the state and returns this.
     */
    public JsonAppender setOutput(OutputStream out, String charset) {
        try {
            if (out == null) {
                throw new NullPointerException();
            }
            this.out = new OutputStreamWriter(zout, charset);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

}
