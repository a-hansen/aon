package com.comfortanalytics.aon.io;

import com.comfortanalytics.aon.AbstractReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Areader implementation that uses the Aon format.
 *
 * @author Aaron Hansen
 */
public class AonReader extends AbstractReader implements AonConstants {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private byte[] buffer;
    private InputStream in;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public AonReader(File file) {
        this.in = new BufferedInputStream(fis(file));
    }

    public AonReader(InputStream in) {
        this.in = in;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public Token next() {
        try {
            int ch = in.read();
            switch (ch) {
                case NULL:
                    return setNextNull();
                case DOUBLE:
                    return setNext(Double.longBitsToDouble(readLong(in)));
                case FLOAT:
                    return setNext(Float.intBitsToFloat(readInt(in)));
                case FALSE:
                    return setNext(false);
                case TRUE:
                    return setNext(true);
                case OBJ_START:
                    return setBeginMap();
                case OBJ_END:
                    return setEndMap();
                case LIST_START:
                    return setBeginList();
                case LIST_END:
                    return setEndList();
                case BIGINT8:
                    return setNext(readBigInteger(readU8(in)));
                case BIGINT16:
                    return setNext(readBigInteger(readU16(in)));
                case BIGINT32:
                    return setNext(readBigInteger(readInt(in)));
                case BIN8:
                    return setNext(new byte[readU8(in)]);
                case BIN16:
                    return setNext(new byte[readU16(in)]);
                case BIN32:
                    return setNext(new byte[readInt(in)]);
                case DEC8:
                    return setNext(readBigDecimal(readU8(in)));
                case DEC16:
                    return setNext(readBigDecimal(readU16(in)));
                case DEC32:
                    return setNext(readBigDecimal(readInt(in)));
                case I8:
                    return setNext(in.read());
                case I16:
                    return setNext(readShort(in));
                case I32:
                    return setNext(readInt(in));
                case I64:
                    return setNext(readLong(in));
                case S8:
                    return setNext(readString(readU8(in)));
                case S16:
                    return setNext(readString(readU16(in)));
                case S32:
                    return setNext(readString(readInt(in)));
                case U8:
                    return setNext(readU8(in));
                case U16:
                    return setNext(readU16(in));
                case U32:
                    return setNext(readU32(in));
                default:
                    if ((ch & S5) == S5) {
                        return setNext(ch & 0x1F);
                    }
                    if ((ch & U5) == U5) {
                        return setNext(ch & 0x1F);
                    }
                    if ((ch & I5) == I5) {
                        if ((ch & 0x10) == 0x10) { //neg
                            return setNext(ch | 0xFFFFFFF0);
                        } else { //pos
                            return setNext(ch & 0x0F);
                        }
                    }
                    throw new IllegalStateException("Unexpected symbol: 0x"
                                                            + Integer.toHexString(ch));
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

    private byte[] getBuffer(int len) {
        if ((buffer == null) || (buffer.length < len)) {
            buffer = new byte[len + 1000];
        }
        return buffer;
    }

    private BigDecimal readBigDecimal(int len) throws IOException {
        return new BigDecimal(readString(len));
    }

    private BigInteger readBigInteger(int len) throws IOException {
        return new BigInteger(readString(len));
    }

    private static int readInt(InputStream in) throws IOException {
        return (((in.read() & 0xFF) << 24) |
                ((in.read() & 0xFF) << 16) |
                ((in.read() & 0xFF) << 8) |
                ((in.read() & 0xFF)));
    }

    private static long readLong(InputStream in) throws IOException {
        return (((long) (in.read() & 0xFF) << 56) |
                ((long) (in.read() & 0xFF) << 48) |
                ((long) (in.read() & 0xFF) << 40) |
                ((long) (in.read() & 0xFF) << 32) |
                ((long) (in.read() & 0xFF) << 24) |
                ((in.read() & 0xFF) << 16) |
                ((in.read() & 0xFF) << 8) |
                ((in.read() & 0xFF)));
    }

    private static short readShort(InputStream in) throws IOException {
        return (short) (((in.read() & 0xFF) << 8) | in.read());
    }

    private static int readU16(InputStream in) throws IOException {
        return ((in.read() & 0xFF) << 8) | in.read();
    }

    private static long readU32(InputStream in) throws IOException {
        return ((long) (in.read() & 0xFF) << 24) |
                ((in.read() & 0xFF) << 16) |
                ((in.read() & 0xFF) << 8) |
                ((in.read() & 0xFF));
    }

    private static int readU8(InputStream in) throws IOException {
        return in.read() & 0xFF;
    }

    private String readString(int len) throws IOException {
        byte[] buf = getBuffer(len);
        if (in.read(buf, 0, len) != len) {
            throw new IOException("Unexpected end of stream");
        }
        return new String(buf, 0, len);
    }

}
