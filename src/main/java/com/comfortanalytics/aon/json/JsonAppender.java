package com.comfortanalytics.aon.json;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Json implementation of Awriter intended for Appendables such as StringBuilders.
 * This can be used for OutputStreams and Writers as well, but JsonWriter will be faster.
 *
 * @author Aaron Hansen
 */
public class JsonAppender extends AbstractJsonWriter implements Appendable {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int BUF_SIZE = 512;

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final StringBuilder buf = new StringBuilder(BUF_SIZE);
    private Appendable out;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public JsonAppender(Appendable out) {
        this.out = out;
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
            if (getDepth() > 0) {
                throw new IllegalStateException("Nesting error.");
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
    public void flush() {
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
    }

    @Override
    public JsonAppender reset() {
        buf.setLength(0);
        return (JsonAppender) super.reset();
    }

}
