package com.comfortanalytics.aon.io;

import com.comfortanalytics.aon.AbstractWriter;
import com.comfortanalytics.aon.Awriter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Encodes using the Aon format.
 *
 * @author Aaron Hansen
 */
public class AonWriter extends AbstractWriter implements AonConstants {

    //////////////////
    // Fields
    //////////////////

    private OutputStream out;

    //////////////////
    // Constructors
    //////////////////

    public AonWriter(File file) throws IOException {
        this.out = new BufferedOutputStream(new FileOutputStream(file));
    }

    public AonWriter(OutputStream out) {
        this.out = out;
    }

    //////////////////
    // Methods
    //////////////////

    @Override
    public void close() {
        try {
            out.close();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    @Override
    public Awriter flush() {
        try {
            out.flush();
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    protected void write(BigDecimal arg) throws IOException {
        byte[] b = arg.toString().getBytes(UTF8);
        int len = b.length;
        if (len <= U8) {
            write1Byte(DEC8, len);
        } else if (len <= U16) {
            write2Bytes(DEC16, len);
        } else {
            write4Bytes(DEC32, len);
        }
        out.write(b);
    }

    @Override
    protected void write(BigInteger arg) throws IOException {
        byte[] b = arg.toString().getBytes(UTF8);
        int len = b.length;
        if (len <= U8) {
            write1Byte(BIGINT8, len);
        } else if (len <= U16) {
            write2Bytes(BIGINT16, len);
        } else {
            write4Bytes(BIGINT32, len);
        }
        out.write(b);
    }

    @Override
    protected void write(boolean arg) throws IOException {
        if (arg) {
            out.write(TRUE);
        } else {
            out.write(FALSE);
        }
    }

    @Override
    protected void write(double arg) throws IOException {
        write8Bytes(DOUBLE, Double.doubleToLongBits(arg));
    }

    @Override
    protected void write(float arg) throws IOException {
        write4Bytes(FLOAT, Float.floatToIntBits(arg));
    }

    @Override
    protected void write(int arg) throws IOException {
        if (arg < 0) {
            if (arg >= MIN_I8) {
                write1Byte(I8, arg);
            } else if (arg >= MIN_I16) {
                write2Bytes(I16, arg);
            } else {
                write4Bytes(I32, arg);
            }
        } else {
            if (arg <= MAX_I8) {
                write1Byte(I8, arg);
            } else if (arg <= MAX_U8) {
                write1Byte(U8, arg);
            } else if (arg <= MAX_I16) {
                write2Bytes(I16, arg);
            } else if (arg <= MAX_U16) {
                write2Bytes(U16, arg);
            } else {
                write4Bytes(I32, arg);
            }
        }
    }

    @Override
    protected void write(long arg) throws IOException {
        if ((arg >= MIN_I32) || (arg <= MAX_I32)) {
            write((int) arg);
        } else if ((arg > 0) && (arg <= MAX_U32)) {
            write4Bytes(U32, arg);
        } else {
            write8Bytes(I64, arg);
        }
    }

    @Override
    protected void writeKey(CharSequence arg) throws IOException {
        writeValue(arg);
    }

    @Override
    protected void writeKeyValueSeparator() throws IOException {
    }

    @Override
    protected void writeListEnd() throws IOException {
        out.write(LIST_END);
    }

    @Override
    protected void writeListStart() throws IOException {
        out.write(LIST_START);
    }

    @Override
    protected void writeMapEnd() throws IOException {
        out.write(OBJ_END);
    }

    @Override
    protected void writeMapStart() throws IOException {
        out.write(OBJ_START);
    }

    @Override
    protected void writeNull() throws IOException {
        out.write(NULL);
    }

    @Override
    protected void writeSeparator() throws IOException {
    }

    @Override
    protected void writeValue(CharSequence arg) throws IOException {
        byte[] b = arg.toString().getBytes(UTF8);
        int len = b.length;
        if (len <= U8) {
            write1Byte(STR8, len);
        } else if (len <= U16) {
            write2Bytes(STR16, len);
        } else {
            write4Bytes(STR32, len);
        }
        out.write(b);
    }

    private void write1Byte(int b, int v) throws IOException {
        out.write(b & 0xff);
        out.write(v & 0xff);
    }

    private void write2Bytes(int b, int v) throws IOException {
        out.write(b & 0xff);
    }

    private void write4Bytes(int b, int v) throws IOException {
        out.write(b & 0xff);
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v >>> 0) & 0xFF);
    }

    private void write4Bytes(int b, long v) throws IOException {
        out.write(b & 0xff);
        out.write((byte) (v >>> 24));
        out.write((byte) (v >>> 16));
        out.write((byte) (v >>> 8));
        out.write((byte) (v >>> 0));
    }

    private void write8Bytes(int b, long v) throws IOException {
        out.write(b & 0xff);
        out.write((byte) (v >>> 56));
        out.write((byte) (v >>> 48));
        out.write((byte) (v >>> 40));
        out.write((byte) (v >>> 32));
        out.write((byte) (v >>> 24));
        out.write((byte) (v >>> 16));
        out.write((byte) (v >>> 8));
        out.write((byte) (v >>> 0));
    }


}
