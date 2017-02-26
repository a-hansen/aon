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
import java.io.IOException;

/**
 * @author Aaron Hansen
 */
public abstract class AbstractJsonWriter implements Appendable, AutoCloseable, Awriter {

    // Constants
    // ---------

    static final int BUF_SIZE = 8192;
    private static final char[] C_B = new char[]{'\\', 'b'};
    private static final char[] C_F = new char[]{'\\', 'f'};
    private static final char[] C_FALSE = new char[]{'f', 'a', 'l', 's', 'e'};
    private static final char[] C_INDENT = new char[]{' ', ' '};
    private static final char[] C_N = new char[]{'\\', 'n'};
    private static final char[] C_NULL = new char[]{'n', 'u', 'l', 'l'};
    private static final char[] C_R = new char[]{'\\', 'r'};
    private static final char[] C_T = new char[]{'\\', 't'};
    private static final char[] C_TRUE = new char[]{'t', 'r', 'u', 'e'};
    private static final char[] C_U = new char[]{'\\', 'u'};
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

    int depth = 0;
    int last = LAST_INIT;
    boolean minify = false;

    // Constructors
    // ------------

    // Public Methods
    // --------------

    /**
     * Append the characters and return this.
     */
    public abstract AbstractJsonWriter append(char[] ch, int off, int len);

    @Override
    public AbstractJsonWriter beginList() {
        try {
            switch (last) {
                case LAST_MAP:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    append(',');
                default:
                    if (!minify && (last != LAST_INIT) && (last != LAST_KEY)) {
                        newLineIndent();
                    }
            }
            append('[');
            last = LAST_LIST;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter beginMap() {
        try {
            switch (last) {
                case LAST_MAP:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    append(',');
                default:
                    if (!minify && (last != LAST_INIT) && (last != LAST_KEY)) {
                        newLineIndent();
                    }
            }
            append('{');
            last = LAST_MAP;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter endList() {
        try {
            if (depth == 0)
                throw new IllegalStateException("Nesting error.");
            if (last == LAST_MAP)
                throw new IllegalStateException("Expecting map end.");
            depth--;
            if (!minify) {
                newLineIndent();
            }
            append(']');
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
    public AbstractJsonWriter endMap() {
        try {
            if (depth == 0)
                throw new IllegalStateException("Nesting error.");
            if (last == LAST_LIST)
                throw new IllegalStateException("Expecting list end.");
            depth--;
            if (!minify) {
                newLineIndent();
            }
            append('}');
            if (depth == 0)
                last = LAST_DONE;
            else
                last = LAST_END;
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

    @Override
    public AbstractJsonWriter key(CharSequence arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_KEY:
                case LAST_LIST:
                    throw new IllegalStateException("Not expecting key: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                default:
                    if (!minify) newLineIndent();
            }
            writeString(arg);
            if (minify) {
                append(':');
            } else {
                append(" : ");
            }
            last = LAST_KEY;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    /**
     * Whether or not to pretty print; false by default.
     */
    public AbstractJsonWriter setMinify(boolean arg) {
        this.minify = arg;
        return this;
    }


    @Override
    public AbstractJsonWriter reset() {
        depth = 0;
        last = LAST_INIT;
        return this;
    }

    @Override
    public AbstractJsonWriter value(Aobj arg) {
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
                    key(map.getKey(i)).value(map.get(i));
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
    public AbstractJsonWriter value(boolean arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                    if (!minify) newLineIndent();
                    break;
                case LAST_LIST:
                    if (!minify) newLineIndent();
            }
            if (arg)
                append(C_TRUE, 0, 4);
            else
                append(C_FALSE, 0, 5);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter value(double arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                    if (!minify) newLineIndent();
                    break;
                case LAST_LIST:
                    if (!minify) newLineIndent();
            }
            if (Double.isInfinite(arg)) {
                if (arg < 0)
                    append(DBL_NEG_INF);
                else
                    append(DBL_POS_INF);
            } else if (Double.isNaN(arg))
                append(DBL_NAN);
            else
                append(String.valueOf(arg));
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter value(int arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                    if (!minify) newLineIndent();
                    break;
                case LAST_LIST:
                    if (!minify) newLineIndent();
            }
            append(String.valueOf(arg));
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter value(long arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                    if (!minify) newLineIndent();
                    break;
                case LAST_LIST:
                    if (!minify) newLineIndent();
            }
            append(String.valueOf(arg));
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractJsonWriter value(String arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                case LAST_MAP:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    append(',');
                    if (!minify) newLineIndent();
                    break;
                case LAST_LIST:
                    if (!minify) newLineIndent();
            }
            if (arg == null)
                append(C_NULL, 0, 4);
            else
                writeString(arg);
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
        append('\n');
        for (int i = depth; --i >= 0; ) {
            append(C_INDENT, 0, 2);
        }
    }

    /**
     * Encodes a string.
     */
    private void writeString(Object arg) throws IOException {
        String s = String.valueOf(arg);
        append('"');
        char ch;
        for (int i = 0, len = s.length(); i < len; i++) {
            ch = s.charAt(i);
            switch (ch) {
                case '"':
                case '\\':
                    append('\\');
                    append(ch);
                    break;
                case '\b':
                    append(C_B, 0, 2);
                    break;
                case '\f':
                    append(C_F, 0, 2);
                    break;
                case '\n':
                    append(C_N, 0, 2);
                    break;
                case '\r':
                    append(C_R, 0, 2);
                    break;
                case '\t':
                    append(C_T, 0, 2);
                    break;
                default:
                    if (Character.isISOControl(ch)) {
                        writeUnicode(ch);
                    } else {
                        append(ch);
                    }
            }
        }
        append('"');
    }

    /**
     * Encode a unicode char.
     */
    private void writeUnicode(char ch) throws IOException {
        append(C_U, 0, 2);
        append(HEX[(ch >>> 12) & 0xf]);
        append(HEX[(ch >>> 8) & 0xf]);
        append(HEX[(ch >>> 4) & 0xf]);
        append(HEX[(ch) & 0xf]);
    }

    // Inner Classes
    // -------------


}//Main
