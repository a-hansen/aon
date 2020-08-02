package com.comfortanalytics.aon.msgpack;

import com.comfortanalytics.aon.AbstractWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * MsgPack implementation of Awriter.
 *
 * @author Aaron Hansen
 */
public class MsgPackWriter extends AbstractWriter implements MsgPack {

    ///////////////////////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////////////////////

    private final MsgPackBuffer byteBuffer = new MsgPackBuffer();
    private CharBuffer charBuffer;
    private final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
    private Frame frame;
    private final OutputStream out;
    private ByteBuffer strBuffer;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public MsgPackWriter(File out) {
        this.out = fos(out);
    }

    public MsgPackWriter(OutputStream out) {
        this.out = out;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void close() {
        try {
            out.close();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public void flush() {
        try {
            out.flush();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void write(BigDecimal arg) {
        byte[] b = arg.toString().getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len < (1 << 8)) {
            byteBuffer.put(EXT8);
            byteBuffer.put((byte) len);
            byteBuffer.put(BIG_DECIMAL_TYPE);
        } else if (len < (1 << 16)) {
            byteBuffer.put(EXT16);
            byteBuffer.putShort((short) len);
            byteBuffer.put(BIG_DECIMAL_TYPE);
        } else {
            byteBuffer.put(EXT32);
            byteBuffer.putInt(len);
            byteBuffer.put(BIG_DECIMAL_TYPE);
        }
        byteBuffer.put(b);
    }

    @Override
    protected void write(BigInteger arg) {
        byte[] b = arg.toByteArray();
        int len = b.length;
        if (len < (1 << 8)) {
            byteBuffer.put(EXT8);
            byteBuffer.put((byte) len);
            byteBuffer.put(BIG_INTEGER_TYPE);
        } else if (len < (1 << 16)) {
            byteBuffer.put(EXT16);
            byteBuffer.putShort((short) len);
            byteBuffer.put(BIG_INTEGER_TYPE);
        } else {
            byteBuffer.put(EXT32);
            byteBuffer.putInt(len);
            byteBuffer.put(BIG_INTEGER_TYPE);
        }
        byteBuffer.put(b);
    }

    @Override
    protected void write(boolean arg) {
        if (frame != null) {
            frame.increment();
        }
        byteBuffer.put(arg ? TRUE : FALSE);
    }

    @Override
    protected void write(byte[] arg) {
        if (frame != null) {
            frame.increment();
        }
        if (arg == null) {
            writeNull();
            return;
        }
        int len = arg.length;
        if (len < (1 << 8)) {
            byteBuffer.put(BIN8);
            byteBuffer.put((byte) len);
        } else if (len < (1 << 16)) {
            byteBuffer.put(BIN16);
            byteBuffer.putShort((short) len);
        } else {
            byteBuffer.put(BIN32);
            byteBuffer.putInt(len);
        }
        byteBuffer.put(arg);
    }

    @Override
    protected void write(double arg) {
        if (frame != null) {
            frame.increment();
        }
        byteBuffer.put(FLOAT64);
        byteBuffer.putDouble(arg);
    }

    @Override
    protected void write(float arg) {
        if (frame != null) {
            frame.increment();
        }
        byteBuffer.put(FLOAT32);
        byteBuffer.putFloat(arg);
    }

    @Override
    protected void write(int arg) {
        write((long) arg);
    }

    @Override
    protected void write(long arg) {
        if (frame != null) {
            frame.increment();
        }
        if (arg < -(1 << 5)) {
            if (arg < -(1 << 15)) {
                if (arg < -(1L << 31)) {
                    byteBuffer.put(INT64);
                    byteBuffer.putLong(arg);
                } else {
                    byteBuffer.put(INT32);
                    byteBuffer.putInt((int) arg);
                }
            } else {
                if (arg < -(1 << 7)) {
                    byteBuffer.put(INT16);
                    byteBuffer.putShort((short) arg);
                } else {
                    byteBuffer.put(INT8);
                    byteBuffer.put((byte) arg);
                }
            }
        } else if (arg < (1 << 7)) { //negative & positive fixint
            byteBuffer.put((byte) arg);
        } else {
            if (arg < (1 << 16)) {
                if (arg < (1 << 8)) {
                    byteBuffer.put(UINT8);
                    byteBuffer.put((byte) arg);
                } else {
                    byteBuffer.put(UINT16);
                    byteBuffer.putU16((int) arg);
                }
            } else {
                if (arg < (1L << 32)) {
                    byteBuffer.put(UINT32);
                    byteBuffer.putU32((int) arg);
                } else {
                    byteBuffer.put(INT64);
                    byteBuffer.putLong(arg);
                }
            }
        }
    }

    @Override
    protected void write(CharSequence arg) {
        if (frame != null) {
            frame.increment();
        }
        writeString(arg);
    }

    /**
     * A negative size implies dynamic and will be written when the list is closed.
     */
    @Override
    protected void writeBeginList() {
        if (frame != null) {
            frame.increment();
        }
        this.frame = new Frame(false);
        byteBuffer.put(LIST32);
        frame.offset = byteBuffer.length();
        byteBuffer.putInt(0);
    }

    /**
     * A negative size implies dynamic and will be written when the map is closed.
     */
    @Override
    protected void writeBeginObj() {
        if (frame != null) {
            frame.increment();
        }
        this.frame = new Frame(true);
        byteBuffer.put(MAP32);
        frame.offset = byteBuffer.length();
        byteBuffer.putInt(0);
    }

    @Override
    protected void writeEndList() {
        frame.writeSize();
        frame = frame.parent;
        if (frame == null) {
            byteBuffer.sendTo(out);
        }
    }

    @Override
    protected void writeEndObj() {
        frame.writeSize();
        frame = frame.parent;
        if (frame == null) {
            byteBuffer.sendTo(out);
        }
    }

    @Override
    protected void writeKey(CharSequence arg) {
        writeString(arg);
    }

    @Override
    protected void writeKeyValueSeparator() {
    }

    @Override
    protected void writeNull() {
        if (frame != null) {
            frame.increment();
        }
        byteBuffer.put(NULL);
    }

    @Override
    protected void writeSeparator() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private Methods
    ///////////////////////////////////////////////////////////////////////////

    private FileOutputStream fos(File f) {
        try {
            return new FileOutputStream(f);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Used by writeString(), returns the string wrapped in a charbuffer that is ready for reading
     * (getting).  Attempts to reuse the same buffer as much as possible.
     */
    private CharBuffer getCharBuffer(CharSequence arg) {
        int len = arg.length();
        if (charBuffer == null) {
            int tmp = 1024;
            while (tmp < len) {
                tmp += 1024;
            }
            charBuffer = CharBuffer.allocate(tmp);
        } else {
            charBuffer.clear();
            if (charBuffer.capacity() < len) {
                int tmp = charBuffer.capacity();
                while (tmp < len) {
                    tmp += 1024;
                }
                charBuffer = CharBuffer.allocate(tmp);
            }
        }
        charBuffer.append(arg);
        charBuffer.flip();
        return charBuffer;
    }

    /**
     * Called by writeString(), returns a bytebuffer for the given capacity ready for writing
     * (putting).  Attempts to reuse the same buffer as much as possible.
     */
    private ByteBuffer getStringBuffer(int len) {
        if (strBuffer == null) {
            int tmp = 1024;
            while (tmp < len) {
                tmp += 1024;
            }
            strBuffer = ByteBuffer.allocate(tmp);
        } else {
            strBuffer.clear();
            if (strBuffer.capacity() < len) {
                int tmp = strBuffer.capacity();
                while (tmp < len) {
                    tmp += 1024;
                }
                strBuffer = ByteBuffer.allocate(tmp);
            }
        }
        return strBuffer;
    }

    private void writeString(CharSequence arg) {
        if (arg.length() == 0) {
            byteBuffer.put(FIXSTR_PREFIX);
            return;
        }
        CharBuffer chars = getCharBuffer(arg);
        ByteBuffer strBuffer = getStringBuffer(
                chars.remaining() * (int) encoder.maxBytesPerChar());
        encoder.encode(chars, strBuffer, false);
        encoder.reset();
        strBuffer.flip();
        int len = strBuffer.remaining();
        if (len < (1 << 5)) {
            byteBuffer.put((byte) (FIXSTR_PREFIX | len));
        } else if (len < (1 << 8)) {
            byteBuffer.put(STR8);
            byteBuffer.put((byte) len);
        } else if (len < (1 << 16)) {
            byteBuffer.put(STR16);
            byteBuffer.putShort((short) len);
        } else {
            byteBuffer.put(STR32);
            byteBuffer.putInt(len);
        }
        byteBuffer.put(strBuffer);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Inner Classes
    ///////////////////////////////////////////////////////////////////////////

    class Frame {

        int offset = -1;
        final Frame parent;
        int size = 0;

        Frame(boolean map) {
            this.parent = frame;
        }

        void increment() {
            size++;
        }

        void writeSize() {
            if (offset >= 0) {
                byteBuffer.replaceInt(offset, size);
            }
        }
    }

}
