package com.comfortanalytics.aon.json;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Json implementation of Awriter intended for OutputStreams and Writers.  While
 * JsonAppender can also handle OutputStreams and Writer, this is more performant.
 * <p>The same instance can be reused with the setOutput methods.</p>
 * <p>This class is not thread safe.</p>
 *
 * @author Aaron Hansen
 * @see com.comfortanalytics.aon.Awriter
 */
@SuppressWarnings("unused")
public class JsonWriter extends AbstractJsonWriter {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int BUF_SIZE = 2048;

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final char[] buf = new char[BUF_SIZE];
    private int buflen = 0;
    private final Writer out;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public JsonWriter(File file) {
        this(fos(file), StandardCharsets.UTF_8);
    }

    public JsonWriter(File file, Charset charset) {
        this(fos(file), charset);
    }

    public JsonWriter(OutputStream out) {
        this(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public JsonWriter(OutputStream out, Charset charset) {
        this(new OutputStreamWriter(out, charset));
    }

    public JsonWriter(Writer out) {
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
        if (buflen + 1 >= BUF_SIZE) {
            flush();
        }
        buf[buflen++] = ch;
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    @Override
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
    @Override
    public Appendable append(CharSequence csq) {
        int len = csq.length();
        if (buflen + len < BUF_SIZE) {
            for (int i = 0; i < len; i++) {
                buf[buflen++] = csq.charAt(i);
            }
        } else if (len <= BUF_SIZE) {
            flush();
            for (int i = 0; i < len; i++) {
                buf[buflen++] = csq.charAt(i);
            }
        } else {
            for (int i = 0; i < len; i++) {
                append(csq.charAt(i));
            }
        }
        return this;
    }

    /**
     * Append the chars and return this.  Can be used for custom formatting.
     */
    @Override
    public Appendable append(CharSequence csq, int start, int end) {
        int len = end - start;
        if (buflen + len < BUF_SIZE) {
            for (int i = start; i < end; i++) {
                buf[buflen++] = csq.charAt(i);
            }
        } else if (len <= BUF_SIZE) {
            flush();
            for (int i = start; i < end; i++) {
                buf[buflen++] = csq.charAt(i);
            }
        } else {
            for (int i = start; i < end; i++) {
                append(csq.charAt(i));
            }
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
            out.close();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public void flush() {
        try {
            if (buflen > 0) {
                out.write(buf, 0, buflen);
                buflen = 0;
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public JsonWriter reset() {
        buflen = 0;
        return (JsonWriter) super.reset();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private static OutputStream fos(File file) {
        try {
            return new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException x) {
            throw new RuntimeException(x);
        }
    }


}
