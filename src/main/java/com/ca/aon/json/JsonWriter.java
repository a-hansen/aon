/* Copyright 2017 by Aaron Hansen.
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

package com.ca.aon.json;

import com.ca.aon.Alist;
import com.ca.aon.Amap;
import com.ca.aon.Aobj;
import com.ca.aon.Awriter;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Json implementation of Awriter.
 *
 * @author Aaron Hansen
 */
public class JsonWriter implements Awriter {

    // Constants
    // ---------

    private static final char[] HEX =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };

    private static final int LAST_DONE = 0; //document complete
    private static final int LAST_END = 1; //end of map/list
    private static final int LAST_INIT = 2; //start
    private static final int LAST_KEY = 3; //object key
    private static final int LAST_LIST = 4; //started a list
    private static final int LAST_MAP = 5; //started a map
    private static final int LAST_VAL = 6; //list or object value


    // Fields
    // ------

    private StringBuilder buf = new StringBuilder();
    private int depth = 0;
    private int last = LAST_INIT;
    private boolean minify = false;
    private Appendable out;
    private boolean zip = false;
    private ZipOutputStream zout;


    // Constructors
    // ------------

    /**
     * Be sure to call one of the setOutput methods.
     */
    public JsonWriter() {
    }

    /**
     * Will write directly to the given appendable.
     */
    public JsonWriter(Appendable arg) {
        setOutput(arg);
    }

    /**
     * Creates an underlying BufferedWriter/FileWriter/arg.
     */
    public JsonWriter(File arg) {
        setOutput(arg);
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.
     */
    public JsonWriter(File file, String zipFileName) {
        setOutput(file, zipFileName);
    }

    /**
     * Creates an underlying PrintWriter.
     */
    public JsonWriter(OutputStream arg) {
        setOutput(arg);
    }

    /**
     * Will write a zip file to the given stream.
     */
    public JsonWriter(OutputStream out, String zipFileName) {
        setOutput(out, zipFileName);
    }


    // Public Methods
    // --------------

    @Override
    public JsonWriter beginList() {
        try {
            switch (last) {
                case LAST_MAP:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
            }
            buf.setLength(0);
            if ((last == LAST_END) || (last == LAST_VAL))
                buf.append(',');
            if (!minify) {
                if ((last != LAST_INIT) && (last != LAST_KEY)) {
                    newLineIndent();
                }
            }
            buf.append('[');
            out.append(buf);
            last = LAST_LIST;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter beginMap() {
        try {
            switch (last) {
                case LAST_MAP:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
            }
            buf.setLength(0);
            if ((last == LAST_END) || (last == LAST_VAL))
                buf.append(',');
            if (!minify) {
                if ((last != LAST_INIT) && (last != LAST_KEY)) {
                    newLineIndent();
                }
            }
            buf.append('{');
            out.append(buf);
            last = LAST_MAP;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public void close() {
        try {
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
    public JsonWriter endList() {
        try {
            if (depth == 0)
                throw new IllegalStateException("Nesting error.");
            if (last == LAST_MAP)
                throw new IllegalStateException("Expecting map end.");
            depth--;
            buf.setLength(0);
            if (!minify) {
                newLineIndent();
            }
            buf.append(']');
            out.append(buf);
            if (depth == 0)
                last = LAST_DONE;
            else
                last = LAST_END;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter endMap() {
        try {
            if (depth == 0)
                throw new IllegalStateException("Nesting error.");
            if (last == LAST_LIST)
                throw new IllegalStateException("Expecting list end.");
            buf.setLength(0);
            depth--;
            if (!minify) {
                newLineIndent();
            }
            buf.append('}');
            out.append(buf);
            if (depth == 0)
                last = LAST_DONE;
            else
                last = LAST_END;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter flush() {
        try {
            if (out instanceof Flushable)
                ((Flushable) out).flush();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Whether or not this is pretty printing: false by default.
     */
    public boolean isMinify() {
        return minify;
    }

    /**
     * Whether or not this is zipping the output.
     */
    public boolean isZip() {
        return zip;
    }

    @Override
    public JsonWriter key(CharSequence arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_KEY:
                case LAST_LIST:
                    throw new IllegalStateException("Not expecting key: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END))
                buf.append(',');
            if (!minify) {
                newLineIndent();
            }
            writeString(arg);
            if (minify) {
                buf.append(':');
            } else {
                buf.append(" : ");
            }
            out.append(buf);
            last = LAST_KEY;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Writes a raw new line character, useful for custom formatting.
     */
    public JsonWriter newLine() {
        try {
            out.append('\n');
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Whether or not to pretty print; false by default.
     */
    public JsonWriter setMinify(boolean arg) {
        this.minify = arg;
        return this;
    }


    @Override
    public JsonWriter reset() {
        depth = 0;
        last = LAST_INIT;
        return this;
    }

    /**
     * Will write directly to the given appendable.  Resets the state.
     *
     * @return this;
     */
    public JsonWriter setOutput(Appendable arg) {
        if (arg == null) throw new NullPointerException();
        this.out = arg;
        return reset();
    }

    /**
     * Creates an underlying BufferedWriter/FileWriter/arg.  Resets the state.
     *
     * @return this
     */
    public JsonWriter setOutput(File arg) {
        try {
            if (arg == null) throw new NullPointerException();
            this.out = new BufferedWriter(new FileWriter(arg));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Will create a zip file using the zipFileName as file name inside the zip.  Resets
     * the state.
     *
     * @return this
     */
    public JsonWriter setOutput(File file, String zipFileName) {
        try {
            if (file == null) throw new NullPointerException();
            zout = new ZipOutputStream(
                    new BufferedOutputStream(new FileOutputStream(file)));
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new PrintWriter(zout);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    /**
     * Creates an underlying PrintWriter.  Resets the state.
     *
     * @return this
     */
    public JsonWriter setOutput(OutputStream arg) {
        if (arg == null) throw new NullPointerException();
        this.out = new PrintWriter(arg);
        return reset();
    }

    /**
     * Will write a zip file to the given stream.  Resets the state.
     *
     * @return this
     */
    public JsonWriter setOutput(OutputStream out, String zipFileName) {
        try {
            if (out == null) throw new NullPointerException();
            if (zipFileName == null) throw new NullPointerException();
            ZipOutputStream zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry(zipFileName));
            this.out = new PrintWriter(zout);
            this.zip = true;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return reset();
    }

    @Override
    public JsonWriter value(Aobj arg) {
        if (arg == null)
            return value((String) null);
        switch (arg.aonType()) {
            case BOOLEAN:
                value(arg.toBoolean());
                break;
            case DOUBLE:
                value(arg.toDouble());
                break;
            case INT:
                value(arg.toInt());
                break;
            case LIST:
                beginList();
                Alist list = arg.toList();
                for (int i = 0, len = list.size(); i < len; i++)
                    value(list.get(i));
                endList();
                break;
            case LONG:
                value(arg.toLong());
                break;
            case MAP:
                beginMap();
                Amap map = arg.toMap();
                String key;
                for (int i = 0, len = map.size(); i < len; i++) {
                    key = map.getKey(i);
                    key(key).value(map.get(key));
                }
                endMap();
                break;
            case NULL:
                value((String) null);
                break;
            case STRING:
                value(arg.toString());
                break;
        }
        return this;
    }

    @Override
    public JsonWriter value(boolean arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END)) {
                buf.append(',');
            }
            if (!minify) {
                if ((last == LAST_VAL) || (last == LAST_END) || (last == LAST_LIST)) {
                    newLineIndent();
                }
            }
            if (arg)
                buf.append("true");
            else
                buf.append("false");
            out.append(buf);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter value(double arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END)) {
                buf.append(',');
            }
            if (!minify) {
                if ((last == LAST_VAL) || (last == LAST_END) || (last == LAST_LIST)) {
                    newLineIndent();
                }
            }
            if (Double.isInfinite(arg)) {
                if (arg < 0)
                    buf.append(DBL_NEG_INF);
                else
                    buf.append(DBL_POS_INF);
            } else if (Double.isNaN(arg))
                buf.append(DBL_NAN);
            else
                buf.append(String.valueOf(arg));
            out.append(buf);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter value(int arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END)) {
                buf.append(',');
            }
            if (!minify) {
                if ((last == LAST_VAL) || (last == LAST_END) || (last == LAST_LIST)) {
                    newLineIndent();
                }
            }
            buf.append(String.valueOf(arg));
            out.append(buf);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter value(long arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END)) {
                buf.append(',');
            }
            if (!minify) {
                if ((last == LAST_VAL) || (last == LAST_END) || (last == LAST_LIST)) {
                    newLineIndent();
                }
            }
            buf.append(String.valueOf(arg));
            out.append(buf);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public JsonWriter value(String arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
            }
            buf.setLength(0);
            if ((last == LAST_VAL) || (last == LAST_END)) {
                buf.append(',');
            }
            if (!minify) {
                if ((last == LAST_VAL) || (last == LAST_END) || (last == LAST_LIST)) {
                    newLineIndent();
                }
            }
            if (arg == null)
                buf.append("null");
            else
                writeString(arg);
            out.append(buf);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }


    // Protected Methods
    // -----------------

    // Package Protected Methods
    // -------------------------

    // Private Methods
    // ---------------

    /**
     * Two spaces per level.
     */
    private void newLineIndent() throws IOException {
        buf.append('\n');
        for (int i = depth; --i >= 0; ) {
            buf.append(' ').append(' ');
        }
    }

    /**
     * Encodes a string.
     */
    private void writeString(Object arg) throws IOException {
        String s = arg.toString();
        buf.append('"');
        char ch;
        for (int i = 0, len = s.length(); i < len; i++) {
            ch = s.charAt(i);
            switch (ch) {
                case '"':
                case '\\':
                    buf.append('\\');
                    buf.append(ch);
                    break;
                case '\b':
                    buf.append("\\b");
                    break;
                case '\f':
                    buf.append("\\f");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                default:
                    if (Character.isISOControl(ch)) {
                        writeUnicode(ch);
                    } else {
                        buf.append(ch);
                    }
            }
        }
        buf.append('"');
    }

    /**
     * Encode a unicode char.
     */
    private void writeUnicode(char ch) throws IOException {
        buf.append("\\u");
        buf.append(HEX[(ch >>> 12) & 0xf]);
        buf.append(HEX[(ch >>> 8) & 0xf]);
        buf.append(HEX[(ch >>> 4) & 0xf]);
        buf.append(HEX[(ch) & 0xf]);
    }


    // Inner Classes
    // -------------


}//Main
