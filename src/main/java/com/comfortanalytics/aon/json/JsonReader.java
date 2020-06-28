package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.AbstractReader;
import com.comfortanalytics.aon.Areader;
import com.comfortanalytics.aon.Astr;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Json implementation of Areader.  The same instance can be re-used with the
 * setInput methods.  This class is not thread safe.
 *
 * @author Aaron Hansen
 * @see Areader
 */
@SuppressWarnings("unused")
public class JsonReader extends AbstractReader {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int[] alse = new int[]{'a', 'l', 's', 'e'};
    private static final int[] rue = new int[]{'r', 'u', 'e'};
    private static final int[] ull = new int[]{'u', 'l', 'l'};

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private char[] bufChars = new char[512];
    private int bufLen;
    private final Reader in;
    private final char[] inChars = new char[4096];
    private int inLen;
    private int inOff;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public JsonReader(File file) {
        this(file, StandardCharsets.UTF_8);
    }

    public JsonReader(File file, Charset charset) {
        this(new BufferedReader(new InputStreamReader(fis(file), charset)));
    }

    public JsonReader(InputStream in) {
        this(in, StandardCharsets.UTF_8);
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
                        return setBeginObj();
                    case ']':
                        return setEndList();
                    case '}':
                        return setEndObj();
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
                    case '+':
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
                        return readNumber(ch);
                }
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private void bufAppend(char b) {
        if (bufLen >= bufChars.length) {
            bufGrow();
        }
        bufChars[bufLen] = b;
        bufLen++;
    }

    private void bufGrow() {
        int size = bufChars.length;
        if (size < 131072) {
            size *= 2;
        } else {
            size += 131072;
        }
        char[] tmp = new char[size];
        System.arraycopy(bufChars, inOff, tmp, 0, inLen);
        bufChars = tmp;
    }

    private static long decodeLong(char[] buf, int idx, int end) {
        boolean neg = false;
        if (buf[idx] == '+') {
            idx++;
        } else if (buf[idx] == '-') {
            neg = true;
            idx++;
        }
        long ret = 0;
        while (idx < end) {
            ret = (ret * 10L) + (buf[idx++] - '0');
        }
        if (neg) {
            return -ret;
        }
        return ret;
    }

    private Token decodeNumber(char[] buf, int len, int decIdx) {
        int curIdx = 0;
        //parse whole
        int endIdx = len;
        if (decIdx >= 0) {
            endIdx = decIdx;
        }
        long theLong = decodeLong(buf, curIdx, endIdx);
        curIdx = ++endIdx;
        //parse fraction
        double theFrac;
        if (decIdx >= 0) {
            long num = decodeLong(buf, curIdx, len);
            theFrac = num / Math.pow(10, len - curIdx);
            if (theFrac != 0) {
                theFrac += theLong;
                return setNext(theFrac);
            }
        }
        return setNext(theLong);
    }

    private static InputStream fis(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException x) {
            throw new RuntimeException(x);
        }
    }

    private int readChar() throws IOException {
        if (inLen == 0) {
            inOff = 0;
            inLen = in.read(inChars, 0, inChars.length);
            if (inLen <= 0) {
                return -1;
            }
        }
        inLen--;
        return inChars[inOff++];
    }

    private Token readNumber(int ch) throws IOException {
        bufLen = 0;
        int decIndex = -1;
        boolean hasExp = false;
        boolean more = true;
        while (more) {
            switch (ch) {
                case -1 :
                    return setEndInput();
                case '.':
                    decIndex = bufLen - 1;
                    ch = readChar();
                    break;
                case 'e':
                case 'E':
                    hasExp = true;
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
                    bufAppend((char) ch);
                    ch = readChar();
                    break;
                default:
                    inOff--;
                    inLen++;
                    more = false;
                    break;
            }
        }
        if (hasExp) {
            String s = new String(bufChars, 0, bufLen);
            if (decIndex >= 0) {
                return setNext(Double.parseDouble(s));
            }
            return setNext(Long.parseLong(s));
        }
        return decodeNumber(bufChars, bufLen, decIndex);
    }

    private String readString() throws IOException {
        bufLen = 0;
        int ch = readChar();
        while (ch != '"') {
            if (ch == '\\') {
                ch = readChar();
                switch (ch) {
                    case -1 :
                        throw new EOFException();
                    case 'u': //case 'U' :
                        bufAppend(readUnicode());
                        break;
                    case 'b':
                        bufAppend('\b');
                        break;
                    case 'f':
                        bufAppend('\f');
                        break;
                    case 'n':
                        bufAppend('\n');
                        break;
                    case 'r':
                        bufAppend('\r');
                        break;
                    case 't':
                        bufAppend('\t');
                        break;
                    case '"':
                        bufAppend('"');
                        break;
                    case '\\':
                        bufAppend('\\');
                    case '/':
                        bufAppend('/');
                        break;
                    default:
                        throw new IOException("Unexpected char: " + ch);
                }
            } else {
                bufAppend((char) ch);
            }
            ch = readChar();
        }
        if (bufLen == 0) {
            return Astr.EMPTY.get();
        }
        return new String(bufChars, 0, bufLen);
    }

    private char readUnicode() throws IOException {
        int ret = 0;
        int ch;
        for (int i = 0; i < 4; ++i) {
            switch (ch = readChar()) {
                case -1 :
                    throw new EOFException();
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
        for (int aChar : chars) {
            ch = readChar();
            if (ch == -1) {
                throw new EOFException();
            }
            if (ch != aChar) {
                throw new IllegalStateException("Expecting " + aChar + ", but got " + ch);
            }
        }
    }

}
