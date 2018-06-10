package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.AbstractReader;
import com.comfortanalytics.aon.Areader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Json implementation of Areader.  The same instance can be re-used with the
 * setInput methods.  This class is not thread safe.
 *
 * @author Aaron Hansen
 * @see Areader
 */
public class JsonReader extends AbstractReader {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int[] alse = new int[]{'a', 'l', 's', 'e'};
    private static final int[] rue = new int[]{'r', 'u', 'e'};
    private static final int[] ull = new int[]{'u', 'l', 'l'};

    private static final Charset UTF8 = Charset.forName("UTF-8");

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private StringBuilder buf = new StringBuilder(512);
    private char[] chars = new char[512];
    private int charsLen = 0;
    private int charsPos = 0;
    private Reader in;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public JsonReader(File file) {
        this(file, UTF8);
    }

    public JsonReader(File file, Charset charset) {
        this(new BufferedReader(new InputStreamReader(fis(file), charset)));
    }

    public JsonReader(InputStream in) {
        this(in, UTF8);
    }

    public JsonReader(InputStream in, Charset charset) {
        this(new InputStreamReader(in, charset));
    }

    public JsonReader(Reader in) {
        this.in = in;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
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
            while (true) {
                ch = readChar();
                switch (ch) {
                    case '[':
                        return setBeginList();
                    case '{':
                        return setBeginMap();
                    case ']':
                        return setEndList();
                    case '}':
                        return setEndMap();
                    case -1:
                        return setEndInput();
                    //values
                    case '"':
                        return setNext(readString());
                    case 't':
                        validateNextChars(rue);
                        return setNext(true);
                    case 'f':
                        validateNextChars(alse);
                        return setNext(false);
                    case 'n':
                        validateNextChars(ull);
                        return setNextNull();
                    case '-':
                    case '+':  //can number start with '+'?
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
                        return readNumber(ch);
                }
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package / Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private static InputStream fis(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException x) {
            throw new RuntimeException(x);
        }
    }

    private int readChar() throws IOException {
        if (charsPos >= charsLen) {
            if (charsLen == -1) {
                return -1;
            }
            charsLen = in.read(chars);
            if (charsLen < 0) {
                return -1;
            }
            charsPos = 0;
        }
        return chars[charsPos++];
    }

    private Token readNumber(int ch) throws IOException {
        boolean hasDecimal = false;
        boolean more = true;
        while (more) {
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
                    buf.append((char) ch);
                    ch = readChar();
                    break;
                default:
                    more = false;
                    break;
            }
        }
        String s = buf.toString();
        buf.setLength(0);
        if (hasDecimal) {
            return setNext(Double.parseDouble(s));
        }
        return setNext(Long.parseLong(s));
    }

    private String readString() throws IOException {
        int ch = readChar();
        while (ch != '"') {
            if (ch == '\\') {
                ch = readChar();
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
            buf.append((char) ch);
            ch = readChar();
        }
        String s = buf.toString();
        buf.setLength(0);
        return s;
    }

    private char readUnicode() throws IOException {
        int ret = 0;
        int ch;
        for (int i = 0; i < 4; ++i) {
            switch (ch = readChar()) {
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

    private void validateNextChars(int[] chars) throws IOException {
        int ch;
        for (int i = 0, len = chars.length; i < len; i++) {
            ch = readChar();
            if (ch != chars[i]) {
                throw new IllegalStateException("Expecting " + chars[i] + ", but got " + ch);
            }
        }
    }

}
