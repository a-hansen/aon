/* ISC License
 *
 * Copyright 2017 by Comfort Analytics, LLC.
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with
 * or without fee is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN
 * NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.comfortanalytics.aon;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Byte array wrapper.
 *
 * @author Aaron Hansen
 */
public class Abinary extends Avalue {

    ///////////////////////////////////////////////////////////
    // Fields
    ///////////////////////////////////////////////////////////

    public static final Abinary EMPTY = new Abinary(new byte[0]);

    private byte[] value;

    ///////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////

    Abinary(byte[] val) {
        if (val == null) {
            throw new NullPointerException("Null not allowed");
        }
        value = val;
    }

    ///////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////

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
        return value.hashCode();
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
