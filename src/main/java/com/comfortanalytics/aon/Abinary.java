package com.comfortanalytics.aon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Byte array value.  If the an encoding format doesn't support binary, this encodes to a
 * base 64 string.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Abinary implements AIvalue {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Abinary EMPTY = new Abinary(new byte[0]);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    byte[] value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abinary(byte[] val) {
        if (val == null) {
            throw new NullPointerException("Null not allowed");
        }
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public Atype aonType() {
        return Atype.BINARY;
    }

    public void copyInto(byte[] buf, int off) {
        System.arraycopy(value, 0, buf, off, value.length);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Abinary)) {
            return false;
        }
        Abinary arg = (Abinary) o;
        return Arrays.equals(value, arg.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    /**
     * The number of byte in the internal array.
     */
    public int length() {
        return value.length;
    }

    @Override
    public Abinary toBinary() {
        return this;
    }

    /**
     * Returns a copy of the internal array.
     */
    public byte[] toByteArray() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public Aprimitive toPrimitive() {
        return Astr.valueOf(toString());
    }

    @Override
    public String toString() {
        if (value.length == 0) {
            return "";
        }
        return AonBase64.encode(value);
    }

    /**
     * Decodes a base64 string.
     */
    @Override
    public Abinary valueOf(Aprimitive value) {
        return valueOf(value.toString());
    }

    /**
     * Does not copy the byte array, which means it is possible to modify the underlying data -
     * so be careful.
     */
    public static Abinary valueOf(byte[] arg) {
        if (arg == null) {
            return null;
        }
        if (arg.length == 0) {
            return EMPTY;
        }
        return new Abinary(arg);
    }

    /**
     * Attempts to decode a base64 string.
     */
    public static Abinary valueOf(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return EMPTY;
        }
        return valueOf(AonBase64.decode(str));
    }

    /**
     * Write the internal byte array to the given stream.
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(value);
    }


}
