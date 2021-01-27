package com.comfortanalytics.aon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import javax.annotation.Nonnull;

/**
 * Byte array value.  If the an encoding format doesn't support binary, this encodes to a base 64
 * string.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Abinary implements Adata {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Abinary EMPTY = new Abinary(new byte[0]);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    final byte[] value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abinary(@Nonnull byte[] val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
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

    @Nonnull
    @Override
    public byte[] get() {
        return value;
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

    @Nonnull
    @Override
    public Aprimitive toPrimitive() {
        return Aon.ensureNotNull(Astr.valueOf(toString()));
    }

    @Nonnull
    @Override
    public String toString() {
        if (value.length == 0) {
            return "";
        }
        return AonBase64.encode(value);
    }

    /**
     * Does not copy the byte array, which means it is possible to modify the underlying data - so
     * be careful.
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
     * Decodes a base64 string.
     */
    @Override
    public Abinary valueOf(Aprimitive value) {
        return valueOf(value.toString());
    }

    /**
     * Write the internal byte array to the given stream.
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(value);
    }


}
