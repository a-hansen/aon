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

import com.comfortanalytics.aon.AbstractReader;
import com.comfortanalytics.aon.Areader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Json implementation of Areader.  The same instance can be re-used with the
 * setInput methods.  This class is not thread safe.
 *
 * @author Aaron Hansen
 * @see com.comfortanalytics.aon.Areader
 */
public class JsonReader extends AbstractReader implements Closeable, JsonConstants {

    // Constants
    // ---------

    private static final int BUFLEN = 8192;

    private static final char[] alse = new char[]{'a', 'l', 's', 'e'};
    private static final char[] rue = new char[]{'r', 'u', 'e'};
    private static final char[] ull = new char[]{'u', 'l', 'l'};

    // Fields
    // ---------

    private char[] buf = new char[BUFLEN];
    private int buflen = 0;
    private Input in;

    // Constructors
    // ------------

    public JsonReader() {
    }

    public JsonReader(CharSequence in) {
        setInput(in);
    }

    public JsonReader(File file) {
        setInput(file);
    }

    public JsonReader(InputStream in, String charset) {
        setInput(in, charset);
    }

    public JsonReader(java.io.Reader in) {
        setInput(in);
    }

    // Public Methods
    // --------------

    public void close() {
        try {
            in.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public Token next() {
        try {
            int ch;
            boolean hasValue = false;
            while (true) {
                ch = readNextClue();
                switch (ch) {
                    case '[':
                        return setBeginList();
                    case '{':
                        return setBeginMap();
                    case ':':
                        if (last() != Token.STRING) {
                            throw new IllegalStateException("Invalid key in map");
                        }
                        return last();
                    case ',':
                        if ((last() == Token.END_LIST) || (last() == Token.END_OBJ)) {
                            break;
                        }
                        return last();
                    case ']':
                        if (!hasValue) {
                            return setEndList();
                        }
                        in.unread();
                        return last();
                    case '}':
                        if (!hasValue) {
                            return setEndMap();
                        }
                        in.unread();
                        return last();
                    case -1:
                        if (!hasValue) {
                            return setEndInput();
                        }
                        in.unread();
                        return last();
                    //values
                    case '"':
                        String str = readString();
                        if ((str.length() > 0) && (str.charAt(0) == '\u001B')) {
                            if (str.equals(DBL_NEG_INF)) {
                                setNext(Double.NEGATIVE_INFINITY);
                                hasValue = true;
                                break;
                            }
                            if (str.equals(DBL_POS_INF)) {
                                setNext(Double.POSITIVE_INFINITY);
                                hasValue = true;
                                break;
                            }
                            if (str.equals(DBL_NAN)) {
                                setNext(Double.NaN);
                                hasValue = true;
                                break;
                            }
                        }
                        setNext(str);
                        hasValue = true;
                        break;
                    case 't':
                        validateNextChars(rue);
                        setNext(true);
                        hasValue = true;
                        break;
                    case 'f':
                        validateNextChars(alse);
                        setNext(false);
                        hasValue = true;
                        break;
                    case 'n':
                        validateNextChars(ull);
                        setNextNull();
                        hasValue = true;
                        break;
                    case '-':
                    case '.':  //can number start with '.'?
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        readNumber((char) ch);
                        hasValue = true;
                }
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public Areader setInput(CharSequence in) {
        this.in = new CharSequenceInput(in);
        return reset();
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public Areader setInput(File file) {
        try {
            if (in instanceof JsonInput) {
                ((JsonInput) in).setInput(new FileReader(file));
            } else {
                in = new JsonInput(new FileReader(file));
            }
        } catch (Exception x) {
            throw new IllegalStateException(x);
        }
        return reset();
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public Areader setInput(InputStream inputStream, String charset) {
        try {
            if (this.in instanceof JsonInput) {
                ((JsonInput) this.in).setInput(inputStream, charset);
            } else {
                this.in = new JsonInput(inputStream, charset);
            }
            return reset();
        } catch (IOException x) {
            throw new IllegalStateException("IOException: " + x.getMessage(), x);
        }
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public Areader setInput(Reader reader) {
        if (this.in instanceof JsonInput) {
            ((JsonInput) this.in).setInput(reader);
        } else {
            this.in = new JsonInput(reader);
        }
        return reset();
    }

    // Private Methods
    // ---------------

    private void bufAppend(char ch) {
        if (buflen == buf.length) {
            char[] tmp = new char[buflen * 2];
            System.arraycopy(buf, 0, tmp, 0, buflen);
            buf = tmp;
        }
        buf[buflen++] = ch;
    }

    private String bufToString() {
        String ret = new String(buf, 0, buflen);
        buflen = 0;
        return ret;
    }

    /**
     * Scans for the next relevant character, skipping over whitespace etc.
     */
    private int readNextClue() throws IOException {
        int ch = in.read();
        while (ch >= 0) {
            switch (ch) {
                case '{':
                case '}':
                case '[':
                case ']':
                case ':':
                case ',':
                case '"':
                case 't':
                case 'f':
                case 'n':
                case '-':
                case '.':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return ch;
            }
            ch = in.read();
        }
        return ch;
    }

    private Token readNumber(char clue) throws IOException {
        char ch = clue;
        boolean hasDecimal = false;
        boolean hasMore = true;
        while (hasMore) {
            switch (ch) {
                case '.':
                case 'e':
                case 'E':
                    hasDecimal = true;
                case '-':
                case '+':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    bufAppend(ch);
                    ch = (char) in.read();
                    break;
                default:
                    in.unread();
                    hasMore = false;
            }
        }
        if (hasDecimal) {
            return setNext(Double.parseDouble(bufToString()));
        }
        long l = Long.parseLong(bufToString());
        if ((l < Integer.MIN_VALUE) || (l > Integer.MAX_VALUE)) {
            return setNext(l);
        }
        return setNext((int) l);
    }

    private String readString() throws IOException {
        char ch = (char) in.read();
        while (ch != '"') {
            if (ch == '\\') {
                ch = (char) in.read();
                switch (ch) {
                    case 'u': //case 'U' :
                        ch = readUnicode();
                        break;
                    case 'b':
                        ch = '\b';
                        break;
                    case 'f':
                        ch = '\f';
                        break;
                    case 'n':
                        ch = '\n';
                        break;
                    case 'r':
                        ch = '\r';
                        break;
                    case 't':
                        ch = '\t';
                        break;
                    case '"':
                    case '\\':
                    case '/':
                        break;
                    default:
                        throw new IOException("Unexpected escape: \\" + ch);
                }
            }
            bufAppend(ch);
            ch = (char) in.read();
        }
        return bufToString();
    }

    private char readUnicode() throws IOException {
        int ret = 0;
        int ch;
        for (int i = 0; i < 4; ++i) {
            switch (ch = in.read()) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    ret = (ret << 4) + ch - '0';
                    break;
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    ret = (ret << 4) + (ch - 'a') + 10;
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                    ret = (ret << 4) + (ch - 'A') + 10;
                    break;
                default:
                    throw new IllegalStateException(
                            "Illegal character in unicode escape: " + (char) ch);
            }
        }
        return (char) ret;
    }

    private static void validate(int ch1, int ch2) {
        if (ch1 != ch2) {
            throw new IllegalStateException("Expecting " + ch2 + ", but got " + ch1);
        }
    }

    private void validateNextChars(char[] chars) throws IOException {
        int ch;
        for (int i = 0, len = chars.length; i < len; i++) {
            ch = in.read();
            if (ch != chars[i]) {
                throw new IllegalStateException(
                        "Expecting " + chars[i] + ", but got " + ch);
            }
        }
    }

    // Inner Classes
    // -------------

    /**
     * Needed for the ability to unread (pushback) a char.
     */
    static interface Input extends Closeable {

        public int read() throws IOException;

        public void unread();
    }


}//Aon
