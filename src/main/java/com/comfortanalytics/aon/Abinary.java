package com.comfortanalytics.aon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Byte array value.
 *
 * @author Aaron Hansen
 */
public class Abinary extends Avalue {

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
    public String toString() {
        return AonBase64.encode(value);
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
     * Write the internal byte array to the given stream.
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(value);
    }


}
