package com.comfortanalytics.aon.json;

import com.comfortanalytics.aon.AbstractReader;
import com.comfortanalytics.aon.Astr;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Json implementation of Areader.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class JsonReader extends AbstractReader {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int[] ALSE = new int[]{'a', 'l', 's', 'e'};
    private static final int[] RUE = new int[]{'r', 'u', 'e'};
    private static final int[] ULL = new int[]{'u', 'l', 'l'};

    static final long[] POWS = new long[]{
            1,
            10,
            100,
            1000,
            10000,
            100000,
            1000000,
            10000000,
            100000000,
            1000000000,
            10000000000L,
            100000000000L,
            1000000000000L,
            10000000000000L,
            100000000000000L,
            1000000000000000L,
            10000000000000000L,
            100000000000000000L,
            1000000000000000000L};

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private char[] bufChars = new char[256];
    private int bufLen;
    private final Reader in;
    private final char[] inChars = new char[2048];
    private int inLen;
    private int inOff;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public JsonReader(File file) {
        this(file, StandardCharsets.UTF_8);
    }

    public JsonReader(File file, Charset charset) {
        this(new InputStreamReader(fis(file), charset));
    }

    public JsonReader(InputStream in) {
        this(in, StandardCharsets.UTF_8);
    }

    public JsonReader(InputStream in, Charset charset) {
        this(new InputStreamReader(in, charset));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    public JsonReader(Reader in) {
        this.in = in;
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

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
                        validateNextChars(RUE);
                        return setNext(true);
                    case 'f':
                        validateNextChars(ALSE);
                        return setNext(false);
                    case 'n':
                        validateNextChars(ULL);
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

    private void bufAppend(char b) {
        int len = bufLen;
        if (len == bufChars.length) {
            bufGrow();
        }
        bufChars[len] = b;
        ++bufLen;
    }

    private void bufGrow() {
        if (bufChars.length < 131072) {
            bufChars = Arrays.copyOf(bufChars, bufChars.length + bufChars.length);
        } else {
            bufChars = Arrays.copyOf(bufChars, bufChars.length + 131072);
        }
    }

    private static long decodeLong(char[] buf, int idx, int end) {
        char ch = buf[idx];
        long ret = 0;
        if (ch == '-') {
            ++idx;
            while (idx < end) {
                ret = ((ret << 1) + (ret << 3)) + buf[idx++] - '0';
            }
            return -ret;
        }
        if (ch == '+') {
            ++idx;
        }
        while (idx < end) {
            ret = ((ret << 1) + (ret << 3)) + buf[idx++] - '0';
        }
        return ret;
    }

    private Token decodeNumber(char[] buf, int len, int decIdx) {
        if (decIdx < 0) {
            return setNext(decodeLong(buf, 0, len));
        }
        return setNext(
                decodeLong(buf, 0, decIdx) +
                        ((double) decodeLong(buf, ++decIdx, len) / POWS[len - decIdx]));
    }

    private static InputStream fis(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException x) {
            throw new RuntimeException(x);
        }
    }

    private int readChar() throws IOException {
        if (inLen == 0) {
            inLen = in.read(inChars, 0, inChars.length);
            if (inLen <= 0) {
                return -1;
            }
            inOff = 0;
        }
        --inLen;
        return inChars[inOff++];
    }

    private Token readNumber(int ch) throws IOException {
        bufLen = 0;
        int dec = -1;
        boolean exp = false;
        while (true) {
            if (ch < '0') {
                if (ch == '.') {
                    dec = bufLen;
                } else if (ch == -1) {
                    return setEndInput();
                } else if (ch != '-' && ch != '+') {
                    unreadChar();
                    break;
                }
            } else if (ch > '9') {
                if (ch == 'e' || ch == 'E') {
                    exp = true;
                } else {
                    unreadChar();
                    break;
                }
            }
            bufAppend((char) ch);
            ch = readChar();
        }
        try {
            if (exp) {
                if (dec < 0) {
                    return setNext(Long.parseLong(new String(bufChars, 0, bufLen)));
                }
                return setNext(Double.parseDouble(new String(bufChars, 0, bufLen)));
            }
            return decodeNumber(bufChars, bufLen, dec);
        } catch (Exception x) {
            return setNext(new BigDecimal(new String(bufChars, 0, bufLen)));
        }
    }

    private String readString() throws IOException {
        bufLen = 0;
        int ch = readChar();
        while (ch != '"') {
            if (ch < 0) {
                throw new EOFException();
            } else if (ch == '\\') {
                ch = readChar();
                switch (ch) {
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
                        if (ch < 0) {
                            throw new EOFException();
                        }
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
        for (int i = 0; i < 4; i++) {
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
                    if (ch < 0) {
                        throw new EOFException();
                    }
                    throw new IllegalStateException(
                            "Illegal character in unicode escape: " + (char) ch);
            }
        }
        return (char) ret;
    }

    private void unreadChar() {
        ++inLen;
        --inOff;
    }

    private void validateNextChars(int[] chars) throws IOException {
        for (int aChar : chars) {
            int ch = readChar();
            if (ch != aChar) {
                if (ch == -1) {
                    throw new EOFException();
                }
                throw new IllegalStateException("Expecting " + aChar + ", but got " + ch);
            }
        }
    }

}
