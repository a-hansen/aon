package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.AbstractWriter;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Aaron Hansen
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class AbstractJsonWriter extends AbstractWriter implements Appendable, Closeable {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

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
    private static final char[] HEX = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
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
    @SuppressWarnings("unused")
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
    protected void write(boolean arg) {
        if (arg) {
            append(C_TRUE, 0, 4);
        } else {
            append(C_FALSE, 0, 5);
        }
    }

    @Override
    protected void write(double arg) throws IOException {
        if (arg == StrictMath.rint(arg)) {
            write((long) arg);
            append('.').append('0');
        } else {
            if (Double.isInfinite(arg) || Double.isNaN(arg)) {
                append(C_NULL, 0, 4);
            } else {
                append(Double.toString(arg));
            }
        }
    }

    @Override
    protected void write(float arg) throws IOException {
        if (arg == StrictMath.rint(arg)) {
            write((int) arg);
            append('.').append('0');
        } else {
            if (Float.isInfinite(arg) || Float.isNaN(arg)) {
                append(C_NULL, 0, 4);
            } else {
                append(Float.toString(arg));
            }
        }
    }

    protected void write(int val) throws IOException {
        if (val == 0) {
            append('0');
            return;
        }
        append(Integer.toString(val));
    }

    @Override
    protected void write(long val) throws IOException {
        if (val == 0) {
            append('0');
            return;
        }
        append(Long.toString(val));
    }

    @Override
    protected void write(CharSequence buf) throws IOException {
        append('"');
        char ch;
        for (int i = 0, len = buf.length(); i < len; ) {
            ch = buf.charAt(i++);
            if (Character.isISOControl(ch)) {
                switch (ch) {
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
                        writeUnicode(ch);
                }
            } else {
                if ((ch == '"') || (ch == '\\')) {
                    append('\\');
                }
                append(ch);
            }
        }
        append('"');
    }

    @Override
    protected void writeBeginList() throws IOException {
        append('[');
    }

    @Override
    protected void writeBeginObj() throws IOException {
        append('{');
    }

    @Override
    protected void writeEndList() throws IOException {
        append(']');
    }

    @Override
    protected void writeEndObj() throws IOException {
        append('}');
    }

    @Override
    protected void writeKey(CharSequence arg) throws IOException {
        write(arg);
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
    protected void writeNewLineIndent() throws IOException {
        append('\n');
        for (int i = getDepth(); --i >= 0; ) {
            append(C_INDENT, 0, 2);
        }
    }

    @Override
    protected void writeNull() {
        append(C_NULL, 0, 4);
    }

    @Override
    protected void writeSeparator() throws IOException {
        append(',');
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
