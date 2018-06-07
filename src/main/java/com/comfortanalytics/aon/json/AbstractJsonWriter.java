package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.AbstractWriter;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Aaron Hansen
 */
public abstract class AbstractJsonWriter extends AbstractWriter implements Appendable, Closeable {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

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

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Append the characters and return this.
     */
    public abstract AbstractJsonWriter append(char[] ch, int off, int len);

    /**
     * Pretty printing is disabled by default, use this to enable it.
     *
     * @return This
     */
    public AbstractJsonWriter setPrettyPrint(boolean arg) {
        prettyPrint = arg;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void write(BigDecimal arg) throws IOException {
        append(arg.toString());
    }

    @Override
    protected void write(BigInteger arg) throws IOException {
        append(arg.toString());
    }

    @Override
    protected void write(boolean arg) throws IOException {
        if (arg) {
            append(C_TRUE, 0, 4);
        } else {
            append(C_FALSE, 0, 5);
        }
    }

    @Override
    protected void write(double arg) throws IOException {
        if ((arg % 1) == 0) {
            write((long) arg);
        } else {
            append(String.valueOf(arg));
        }
    }

    @Override
    protected void write(float arg) throws IOException {
        if ((arg % 1) == 0) {
            write((long) arg);
        } else {
            append(String.valueOf(arg));
        }
    }

    @Override
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

    @Override
    protected void write(long arg) throws IOException {
        if (arg < 100000) {
            write((int) arg);
        } else {
            append(String.valueOf(arg));
        }
    }

    @Override
    protected void writeKey(CharSequence arg) throws IOException {
        writeValue(arg);
    }

    @Override
    protected void writeKeyValueSeparator() throws IOException {
        if (prettyPrint) {
            append(C_S, 0, 2);
        } else {
            append(':');
        }
    }

    @Override
    protected void writeListEnd() throws IOException {
        append(']');
    }

    @Override
    protected void writeListStart() throws IOException {
        append('[');
    }

    @Override
    protected void writeMapEnd() throws IOException {
        append('}');
    }

    @Override
    protected void writeMapStart() throws IOException {
        append('{');
    }

    @Override
    protected void writeNewLineIndent() throws IOException {
        append('\n');
        for (int i = getDepth(); --i >= 0; ) {
            append(C_INDENT, 0, 2);
        }
    }

    @Override
    protected void writeNull() throws IOException {
        append(C_NULL, 0, 4);
    }

    @Override
    protected void writeSeparator() throws IOException {
        append(',');
    }

    @Override
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

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

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

}
