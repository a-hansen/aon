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
import com.comfortanalytics.aon.Areader;
import java.io.*;

/**
 * Json implementation of Areader.  The same instance can be re-used with the
 * setInput methods.  This class is not thread safe.
 *
 * @author Aaron Hansen
 * @see com.comfortanalytics.aon.Areader
 */
public class JsonReader implements Areader, AutoCloseable {

    // Constants
    // ---------

    private static final int BUFLEN = 8192;

    // Fields
    // ---------

    private char[] buf = new char[BUFLEN];
    private int buflen = 0;
    private Input in;
    private Token last = Token.ROOT;
    private boolean valBoolean;
    private double valDouble;
    private int valInt;
    private long valLong;
    private String valString;


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
        setInput(in,charset);
    }

    public JsonReader(java.io.Reader in) {
        setInput(in);
    }


    // Public Methods
    // --------------

    @Override
    public void close() {
        try {
            in.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public boolean getBoolean() {
        if (last != Token.BOOLEAN) {
            throw new IllegalStateException("Not a boolean");
        }
        return valBoolean;
    }

    @Override
    public double getDouble() {
        switch (last) {
            case DOUBLE:
                break;
            case INT:
                return (double) valInt;
            case LONG:
                return (double) valLong;
            default:
                throw new IllegalStateException("Not a double");
        }
        return valDouble;
    }

    @Override
    public int getInt() {
        switch (last) {
            case DOUBLE:
                return (int) valDouble;
            case INT:
                break;
            case LONG:
                return (int) valLong;
            default:
                throw new IllegalStateException("Not a long");
        }
        return valInt;
    }

    @Override
    public Alist getList() {
        if (last == Token.ROOT) {
            next();
        }
        if (last != Token.BEGIN_LIST) {
            throw new IllegalStateException("Not a list");
        }
        Alist ret = new Alist();
        int token = 0;
        while (true) {
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    return ret;
                case END_MAP:
                    throw new IllegalStateException("Unexpected end of map in list");
                case KEY:
                    throw new IllegalStateException("Unexpected key in list");
                case BOOLEAN:
                    ret.add(valBoolean);
                    break;
                case DOUBLE:
                    ret.add(valDouble);
                    break;
                case INT:
                    ret.add(valInt);
                    break;
                case LONG:
                    ret.add(valLong);
                    break;
                case BEGIN_LIST:
                    ret.add(getList());
                    break;
                case BEGIN_MAP:
                    ret.add(getMap());
                    break;
                case NULL:
                    ret.addNull();
                    break;
                case STRING:
                    ret.add(valString);
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected token in list: " + last);
            }
        }
    }

    @Override
    public long getLong() {
        switch (last) {
            case DOUBLE:
                return (long) valDouble;
            case INT:
                return (long) valInt;
            case LONG:
                break;
            default:
                throw new IllegalStateException("Not an int");
        }
        return valLong;
    }

    @Override
    public Amap getMap() {
        if (last == Token.ROOT) {
            next();
        }
        if (last != Token.BEGIN_MAP) {
            throw new IllegalStateException("Not a map");
        }
        Amap ret = new Amap();
        String key = null;
        while (true) {
            switch (next()) {
                case KEY:
                    key = valString;
                    break;
                case END_MAP:
                case END_INPUT:
                    return ret;
                default:
                    throw new IllegalStateException("Expecting a key or map end");
            }
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    throw new IllegalStateException("Unexpected end of list in map");
                case END_MAP:
                    return ret;
                case KEY:
                    throw new IllegalStateException("Unexpected key in map");
                case BOOLEAN:
                    ret.put(key, Aobj.make(valBoolean));
                    break;
                case DOUBLE:
                    ret.put(key, Aobj.make(valDouble));
                    break;
                case INT:
                    ret.put(key, Aobj.make(valInt));
                    break;
                case LONG:
                    ret.put(key, Aobj.make(valLong));
                    break;
                case BEGIN_LIST:
                    ret.put(key, getList());
                    break;
                case BEGIN_MAP:
                    ret.put(key, getMap());
                    break;
                case NULL:
                    ret.putNull(key);
                    break;
                case STRING:
                    ret.put(key, Aobj.make(valString));
                    break;
                default:
                    throw new IllegalStateException("Unexpected token in map: " + last);
            }
        }
    }

    @Override
    public Aobj getObj() {
        if (last == Token.ROOT) {
            next();
        }
        switch (last) {
            case KEY:
                return Aobj.make(valString);
            case BOOLEAN:
                return Aobj.make(valBoolean);
            case DOUBLE:
                return Aobj.make(valDouble);
            case INT:
                return Aobj.make(valInt);
            case LONG:
                return Aobj.make(valLong);
            case BEGIN_LIST:
                return getList();
            case BEGIN_MAP:
                return getMap();
            case NULL:
                return Aobj.makeNull();
            case STRING:
                return Aobj.make(valString);
        }
        throw new IllegalStateException("Not a value");
    }

    @Override
    public String getString() {
        if ((last != Token.STRING) && (last != Token.KEY)) {
            throw new IllegalStateException("Not a string");
        }
        return valString;
    }

    @Override
    public Token last() {
        return last;
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
                        return last = Token.BEGIN_LIST;
                    case '{':
                        return last = Token.BEGIN_MAP;
                    case ':':
                        if (last != Token.STRING)
                            throw new IllegalStateException("Invalid key");
                        return last = Token.KEY;
                    case ',':
                        if ((last == Token.END_LIST) || (last == Token.END_MAP))
                            break;
                        return last;
                    case ']':
                        if (!hasValue) return last = Token.END_LIST;
                        in.unread();
                        return last;
                    case '}':
                        if (!hasValue) return last = Token.END_MAP;
                        in.unread();
                        return last;
                    case -1:
                        if (!hasValue) return last = Token.END_INPUT;
                        in.unread();
                        return last;
                    //values
                    case '"':
                        readString();
                        String str = bufToString();
                        if ((str.length() > 0) && (str.charAt(0) == '\u001B')) {
                            if (str.equals(DBL_NEG_INF)) {
                                valDouble = Double.NEGATIVE_INFINITY;
                                last = Token.DOUBLE;
                                break;
                            }
                            if (str.equals(DBL_POS_INF)) {
                                valDouble = Double.POSITIVE_INFINITY;
                                last = Token.DOUBLE;
                                break;
                            }
                            if (str.equals(DBL_NAN)) {
                                valDouble = Double.NaN;
                                last = Token.DOUBLE;
                                break;
                            }
                        }
                        last = Token.STRING;
                        hasValue = true;
                        break;
                    case 't':
                        validate(in.read(), 'r');
                        validate(in.read(), 'u');
                        validate(in.read(), 'e');
                        valBoolean = true;
                        last = Token.BOOLEAN;
                        hasValue = true;
                        break;
                    case 'f':
                        validate(in.read(), 'a');
                        validate(in.read(), 'l');
                        validate(in.read(), 's');
                        validate(in.read(), 'e');
                        valBoolean = false;
                        last = Token.BOOLEAN;
                        hasValue = true;
                        break;
                    case 'n':
                        validate(in.read(), 'u');
                        validate(in.read(), 'l');
                        validate(in.read(), 'l');
                        last = Token.NULL;
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

    @Override
    public JsonReader reset() {
        last = Token.ROOT;
        return this;
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public JsonReader setInput(CharSequence in) {
        this.in = new CharSequenceInput(in);
        return reset();
    }

    /**
     * Sets the input source, resets to ROOT, and returns this.
     */
    public JsonReader setInput(File file) {
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
    public JsonReader setInput(InputStream inputStream, String charset) {
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
    public JsonReader setInput(Reader reader) {
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
            System.arraycopy(buf,0,tmp,0,buflen);
            buf = tmp;
        }
        buf[buflen++] = ch;
    }

    private String bufToString() {
        String ret = new String(buf,0,buflen);
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
            valDouble = Double.parseDouble(bufToString());
            return last = Token.DOUBLE;
        }
        long l = Long.parseLong(bufToString());
        if ((l < Integer.MIN_VALUE) || (l > Integer.MAX_VALUE)) {
            valLong = l;
            return last = Token.LONG;
        }
        valInt = (int) l;
        return last = Token.INT;
    }

    private void readString() throws IOException {
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
        valString = bufToString();
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


    // Inner Classes
    // -------------

    /**
     * Needed for the ability to unread (pushback) a char.
     */
    static interface Input extends AutoCloseable {
        public int read() throws IOException;

        public void unread();
    }


}//Aon
