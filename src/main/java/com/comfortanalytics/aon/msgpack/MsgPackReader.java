package com.comfortanalytics.aon.msgpack;

import com.comfortanalytics.aon.AbstractReader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * MsgPack implementation of Areader.
 *
 * @author Aaron Hansen
 */
public class MsgPackReader extends AbstractReader implements MsgPack {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private ByteBuffer byteBuf;
    private byte[] bytes;
    private CharBuffer charBuf;
    private final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
    private Frame frame;
    private final InputStream in;
    private boolean wasValue = true;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public MsgPackReader(File file) {
        this.in = fis(file);
    }

    public MsgPackReader(InputStream in) {
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
        if (frame != null) {
            //check to see if we've read all the children of the parent list/map.
            if (frame.isMap) {
                //don't count keys, only values
                if (wasValue) {
                    if (!frame.next()) {
                        frame = frame.parent;
                        setEndObj();
                        return last();
                    }
                }
                wasValue = !wasValue;
            } else {
                if (!frame.next()) {
                    frame = frame.parent;
                    setEndList();
                    return last();
                }
            }
        }
        byte b;
        try {
            b = (byte) in.read();
            switch (b) {
                case NULL:
                    return setNextNull();
                case FALSE:
                    return setNext(false);
                case TRUE:
                    return setNext(true);
                case BIN8:
                case BIN16:
                case BIN32:
                    return readBytes(b);
                case EXT8:
                case EXT16:
                case EXT32:
                    return readExt(b);
                case FLOAT32:
                case FLOAT64:
                case UINT8:
                case UINT16:
                case UINT32:
                case UINT64:
                case INT8:
                case INT16:
                case INT32:
                case INT64:
                    return readNumber(b);
                case STR8:
                case STR16:
                case STR32:
                    return readString(b);
                case LIST16:
                case LIST32:
                    return readList(b);
                case MAP16:
                case MAP32:
                    return readMap(b);
            }
            if (isFixInt(b)) {
                return setNext(b);
            }
            if (isFixStr(b)) {
                return readString(b);
            }
            if (isFixedList(b)) {
                return readList(b);
            }
            if (isFixedMap(b)) {
                return readMap(b);
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        throw new IllegalStateException("Unknown type: " + b);
    }

    @Override
    public MsgPackReader reset() {
        super.reset();
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private InputStream fis(File file) {
        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Returns a byte buffer wrapping the given bytes and ready for reading (getting).  Attempts to
     * reuse the same buffer.
     */
    private ByteBuffer getByteBuffer(byte[] bytes, int len) {
        if (byteBuf == null) {
            int tmp = 1024;
            while (tmp < len) {
                tmp += 1024;
            }
            byteBuf = ByteBuffer.allocate(tmp);
        } else {
            byteBuf.clear();
            if (byteBuf.capacity() < len) {
                int tmp = 1024;
                while (tmp < len) {
                    tmp += 1024;
                }
                byteBuf = ByteBuffer.allocate(tmp);
            }
        }
        byteBuf.put(bytes, 0, len);
        byteBuf.flip();
        return byteBuf;
    }

    /**
     * Returns a char buffer with the given capacity, ready for writing (putting).  Attempts to
     * reuse the same char buffer.
     */
    private CharBuffer getCharBuffer(int size) {
        if (charBuf == null) {
            int tmp = 1024;
            while (tmp < size) {
                tmp += 1024;
            }
            charBuf = CharBuffer.allocate(tmp);
        } else {
            charBuf.clear();
            if (charBuf.remaining() < size) {
                int tmp = 1024;
                while (tmp < size) {
                    tmp += 1024;
                }
                charBuf = CharBuffer.allocate(tmp);
            }
        }
        return charBuf;
    }

    private static boolean isFixInt(byte b) {
        int v = b & 0xff;
        return v <= 0x7f || v >= 0xe0;
    }

    private static boolean isFixStr(byte b) {
        return (b & (byte) 0xe0) == FIXSTR_PREFIX;
    }

    private static boolean isFixedList(byte b) {
        return (b & (byte) 0xf0) == FIXLIST_PREFIX;
    }

    private static boolean isFixedMap(byte b) {
        return (b & (byte) 0xf0) == FIXMAP_PREFIX;
    }

    /**
     * Reads bytes into an array that is guaranteed to be at least the given size but will probably
     * be longer.
     */
    private byte[] readBytes(int size) throws IOException {
        if ((bytes == null) || (bytes.length < size)) {
            int tmp = 1024;
            while (tmp < size) {
                tmp += 1024;
            }
            bytes = new byte[tmp];
        }
        if (in.read(bytes, 0, size) != size) {
            throw new IOException("Unexpected end of input");
        }
        return bytes;
    }

    private Token readBytes(byte b) throws IOException {
        int size;
        switch (b) {
            case BIN8:
                size = in.read() & 0xFF;
                break;
            case BIN16:
                size = readShort(in);
                break;
            case BIN32:
                size = readInt(in);
                break;
            default:
                throw new IllegalStateException("Unknown bytes: " + b);
        }
        byte[] bytes = new byte[size];
        in.read(bytes);
        return setNext(bytes);
    }

    private static double readDouble(InputStream in) {
        return Double.longBitsToDouble(readLong(in));
    }

    private Token readExt(byte b) throws IOException {
        int len;
        switch (b) {
            case EXT8:
                len = in.read();
                break;
            case EXT16:
                len = readShort(in);
                break;
            default: //case EXT32 :
                len = readInt(in);
        }
        int type = in.read();
        if (type == BIG_DECIMAL_TYPE) {
            return setNext(new BigDecimal(readUTF(len)));
        }
        byte[] bytes = new byte[len];
        in.read(bytes);
        if (type == BIG_INTEGER_TYPE) {
            return setNext(new BigInteger(bytes));
        }
        //just treat as a generic byte array
        return setNext(bytes);
    }

    private static float readFloat(InputStream in) {
        return Float.intBitsToFloat(readInt(in));
    }

    private static int readInt(InputStream in) {
        try {
            return (((in.read() & 0xFF) << 24) |
                    ((in.read() & 0xFF) << 16) |
                    ((in.read() & 0xFF) << 8) |
                    ((in.read() & 0xFF)));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private Token readList(byte b) {
        int size;
        if (isFixedList(b)) {
            size = b & 0x0f;
        } else {
            switch (b) {
                case LIST16:
                    size = readShort(in);
                    break;
                case LIST32:
                    size = readInt(in);
                    break;
                default:
                    throw new IllegalStateException("Unknown list type: " + b);
            }
        }
        frame = new Frame(size, false);
        return setBeginList();
    }

    private static long readLong(InputStream in) {
        try {
            return (((long) (in.read() & 0xFF) << 56) |
                    ((long) (in.read() & 0xFF) << 48) |
                    ((long) (in.read() & 0xFF) << 40) |
                    ((long) (in.read() & 0xFF) << 32) |
                    ((long) (in.read() & 0xFF) << 24) |
                    ((in.read() & 0xFF) << 16) |
                    ((in.read() & 0xFF) << 8) |
                    ((in.read() & 0xFF)));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private Token readMap(byte b) {
        int size;
        if (isFixedMap(b)) {
            size = b & 0x0f;
        } else {
            switch (b) {
                case MAP16:
                    size = readShort(in);
                    break;
                case MAP32:
                    size = readInt(in);
                    break;
                default:
                    throw new IllegalStateException("Unknown map type: " + b);
            }
        }
        frame = new Frame(size, true);
        return setBeginObj();
    }

    private Token readNumber(byte b) throws IOException {
        switch (b) {
            case FLOAT32:
                return setNext(readFloat(in));
            case FLOAT64:
                return setNext(readDouble(in));
            case INT8:
            case UINT8:
                return setNext((short) in.read() & 0xFF);
            case INT16:
                return setNext(readShort(in));
            case UINT16:
                return setNext(readU16(in));
            case INT32:
                return setNext(readInt(in));
            case UINT32:
                return setNext(readU32(in));
            default: //INT64
                return setNext(readLong(in));
        }
    }

    private static short readShort(InputStream in) {
        try {
            return (short) (((in.read() & 0xFF) << 8) | ((in.read() & 0xFF)));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private Token readString(byte b) throws IOException {
        int size;
        if (isFixStr(b)) {
            size = b & 0x1F;
        } else {
            switch (b) {
                case STR8:
                    size = in.read() & 0xFF;
                    break;
                case STR16:
                    size = readShort(in);
                    break;
                case STR32:
                    size = readInt(in);
                    break;
                default:
                    throw new IllegalStateException("Unknown string type: " + b);
            }
        }
        setNext(readUTF(size));
        return last();
    }

    private static int readU16(InputStream in) {
        try {
            return (((in.read() & 0xFF) << 8) | ((in.read() & 0xFF)));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private static long readU32(InputStream in) {
        try {
            return (((long) (in.read() & 0xFF) << 24) |
                    ((in.read() & 0xFF) << 16) |
                    ((in.read() & 0xFF) << 8) |
                    ((in.read() & 0xFF)));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private String readUTF(int len) throws IOException {
        byte[] bytes = readBytes(len);
        ByteBuffer byteBuf = getByteBuffer(bytes, len);
        CharBuffer charBuf = getCharBuffer(len);
        decoder.decode(byteBuf, charBuf, false);
        charBuf.flip();
        return charBuf.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    private class Frame {

        final boolean isMap;
        final Frame parent;
        int size;

        Frame(int size, boolean map) {
            this.isMap = map;
            this.size = size;
            this.parent = frame;
        }

        public boolean next() {
            return --size >= 0;
        }

    }

}
