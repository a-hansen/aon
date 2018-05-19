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

import com.comfortanalytics.aon.AbstractWriter;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author Aaron Hansen
 */
public abstract class AbstractJsonWriter
        extends AbstractWriter
        implements Appendable, Closeable {

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
    private static final char[] C_S = new char[]{' ', ':', ' '};
    private static final char[] C_T = new char[]{'\\', 't'};
    private static final char[] C_TRUE = new char[]{'t', 'r', 'u', 'e'};
    private static final char[] C_U = new char[]{'\\', 'u'};
    private static final char[] HEX =
            {
                    '0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };


    // Fields
    // ------

    // Constructors
    // ------------

    // Public Methods
    // --------------

    /**
     * Append the characters and return this.
     */
    public abstract AbstractJsonWriter append(char[] ch, int off, int len);

    // Protected Methods
    // -----------------

    public AbstractJsonWriter setPrettyPrint(boolean arg) {
        prettyPrint = arg;
        return this;
    }

    /**
     * Write the value.
     */
    protected void write(boolean arg) throws IOException {
        if (arg) {
            append(C_TRUE, 0, 4);
        } else {
            append(C_FALSE, 0, 5);
        }
    }

    /**
     * Write the value.
     */
    protected void write(double arg) throws IOException {
        if ((arg % 1) == 0) {
            write((long) arg);
        } else {
            append(String.valueOf(arg));
        }
    }

    /**
     * Write the value.
     */
    protected void write(int arg) throws IOException {
        if (arg < 10) {
            append((char) (arg + '0'));
        } else if (arg < 100) {
            append((char) ((arg / 10) + '0'));
            append((char) ((arg % 10) + '0'));
        } else if (arg < 1000) {
            append((char) ((arg / 100) + '0'));
            arg = arg % 100;
            append((char) ((arg / 10) + '0'));
            append((char) ((arg % 10) + '0'));
        } else if (arg < 10000) {
            append((char) ((arg / 1000) + '0'));
            arg = arg % 1000;
            append((char) ((arg / 100) + '0'));
            arg = arg % 100;
            append((char) ((arg / 10) + '0'));
            append((char) ((arg % 10) + '0'));
        } else if (arg < 100000) {
            append((char) ((arg / 10000) + '0'));
            arg = arg % 10000;
            append((char) ((arg / 1000) + '0'));
            arg = arg % 1000;
            append((char) ((arg / 100) + '0'));
            arg = arg % 100;
            append((char) ((arg / 10) + '0'));
            append((char) ((arg % 10) + '0'));
        } else {
            append(String.valueOf(arg));
        }
    }

    /**
     * Write the value.
     */
    protected void write(long arg) throws IOException {
        if (arg < 100000) {
            write((int) arg);
        } else {
            append(String.valueOf(arg));
        }
    }

    /**
     * Write string key of a map entry.
     */
    protected void writeKey(CharSequence arg) throws IOException {
        writeValue(arg);
    }

    /**
     * Separate the key from the value in a map.
     */
    protected void writeKeyValueSeparator() throws IOException {
        if (prettyPrint)
            append(C_S, 0, 2);
        else
            append(':');
    }

    /**
     * End the current list.
     */
    protected void writeListEnd() throws IOException {
        append(']');
    }

    /**
     * Start a new list.
     */
    protected void writeListStart() throws IOException {
        append('[');
    }

    /**
     * End the current map.
     */
    protected void writeMapEnd() throws IOException {
        append('}');
    }

    /**
     * Start a new map.
     */
    protected void writeMapStart() throws IOException {
        append('{');
    }

    /**
     * Two spaces per level.
     */
    protected void writeNewLineIndent() throws IOException {
        append('\n');
        for (int i = getDepth(); --i >= 0; ) {
            append(C_INDENT, 0, 2);
        }
    }

    /**
     * Write a null value.
     */
    protected void writeNull() throws IOException {
        append(C_NULL, 0, 4);
    }

    /**
     * Write a value separator, such as the comma in json.
     */
    protected void writeSeparator() throws IOException {
        append(',');
    }

    /**
     * Encodes a string.
     */
    protected void writeValue(CharSequence buf) throws IOException {
        append('"');
        char ch;
        for (int i = 0, len = buf.length(); i < len; i++) {
            ch = buf.charAt(i);
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