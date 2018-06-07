package com.comfortanalytics.aon;

import com.comfortanalytics.aon.io.AonConstants;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Thread-safe Base64 encoder and decoder.  This only exists because to be compatible with
 * Java 6 and to avoid a dependency.
 */
class AonBase64 {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int[] fromBase64 = new int[256];

    private final static char[] toBase64 = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '+', '/'
    };

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Decodes a base 64 encoded string.  Will decodeKeys both url safe and unsafe.
     *
     * @param str Most not be null.
     * @return Never null.
     * @throws IllegalArgumentException If anything is wrong with the parameter.
     */
    public static byte[] decode(String str) {
        byte[] src = null;
        try {
            src = str.getBytes(AonConstants.UTF8);
        } catch (Exception willNotHappen) {
            throw new RuntimeException(willNotHappen);
        }
        int len = src.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream((len * 6) / 8);
        int[] base64 = fromBase64;
        int bits = 0;
        int start = 18;
        int pos = 0;
        while (pos < len) {
            int b = src[pos++] & 0xff;
            if ((b = base64[b]) < 0) {
                if (b == -2) {
                    if (start == 6 && (pos == len || src[pos++] != '=') || start == 18) {
                        throw new IllegalArgumentException("Input byte array has wrong ending");
                    }
                    break;
                }
                throw new IllegalArgumentException(
                        "Illegal character " + Integer.toString(src[pos - 1], 16));
            }
            bits |= (b << start);
            start -= 6;
            if (start < 0) {
                out.write(bits >> 16);
                out.write(bits >> 8);
                out.write(bits);
                start = 18;
                bits = 0;
            }
        }
        if (start == 6) {
            out.write(bits >> 16);
        } else if (start == 0) {
            out.write(bits >> 16);
            out.write(bits >> 8);
        } else if (start == 12) {
            throw new IllegalArgumentException("Not enough valid bits");
        }
        if (pos < len) {
            throw new IllegalArgumentException("Invalid byte at " + pos);
        }
        return out.toByteArray();
    }

    /**
     * Encodes the bytes into a single line string with no padding.
     *
     * @param buf The bytes to encode.
     * @return The encoding.
     */
    public static String encode(byte[] buf) {
        return encode(buf, 0, buf.length, -1, false);
    }

    /**
     * Encodes the buffer into a String with the given line length.  Lines will be padded to the
     * given length.
     *
     * @param buf     The bytes to encode.
     * @param linelen A number greater than zero limits the number of characters before an
     *                interleaving newline.
     * @return The encoding.
     */
    public static String encode(byte[] buf, int linelen) {
        return encode(buf, 0, buf.length, linelen, true);
    }

    /**
     * Encodes to a URL safe base64 string.  Replaces / with _ and + with -.
     */
    public static String encodeUrl(byte[] bytes) {
        String enc = encode(bytes);
        return enc.replace('+', '-').replace('/', '_');
    }

    ///////////////////////////////////////////////////////////////////////////
    // Package/Private Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Encodes a buffer into a base 64 string.
     *
     * @param bytes   The bytes to encode.
     * @param offset  The starting index in the buffer,
     * @param length  The number bytes from the buffer to encode.
     * @param linelen A number greater than zero limits the number of characters before an
     *                interleaving newline.
     * @param pad     Whether or not pad lines to the given line length.
     * @return The encoding.
     */
    private static String encode(byte[] bytes,
                                 int offset,
                                 int length,
                                 int linelen,
                                 boolean pad) {
        StringBuilder buf = new StringBuilder((int) (length * 1.33));
        char[] base64 = toBase64;
        int pos = offset;
        int end = offset + length;
        int slen = (end - offset) / 3 * 3;
        int len = offset + slen;
        if (linelen > 0 && slen > linelen / 4 * 3) {
            slen = linelen / 4 * 3;
        }
        while (pos < len) {
            int sl0 = Math.min(pos + slen, len);
            for (int sp0 = pos; sp0 < sl0; ) {
                int bits = (bytes[sp0++] & 0xff) << 16 |
                        (bytes[sp0++] & 0xff) << 8 |
                        (bytes[sp0++] & 0xff);
                buf.append(base64[(bits >>> 18) & 0x3f]);
                buf.append(base64[(bits >>> 12) & 0x3f]);
                buf.append(base64[(bits >>> 6) & 0x3f]);
                buf.append(base64[bits & 0x3f]);
            }
            int dlen = (sl0 - pos) / 3 * 4;
            pos = sl0;
            if (dlen == linelen && pos < end) {
                buf.append('\n');
            }
        }
        if (pos < end) {
            int b0 = bytes[pos++] & 0xff;
            buf.append(base64[b0 >> 2]);
            if (pos == end) {
                buf.append(base64[(b0 << 4) & 0x3f]);
                if (pad) {
                    buf.append('=').append('=');
                }
            } else {
                int b1 = bytes[pos++] & 0xff;
                buf.append(base64[(b0 << 4) & 0x3f | (b1 >> 4)]);
                buf.append(base64[(b1 << 2) & 0x3f]);
                if (pad) {
                    buf.append('=');
                }
            }
        }
        return buf.toString();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Initialization
    ///////////////////////////////////////////////////////////////////////////

    static {
        Arrays.fill(fromBase64, -1);
        for (int i = 0; i < toBase64.length; i++) {
            fromBase64[toBase64[i]] = i;
        }
        fromBase64['-'] = fromBase64['+'];
        fromBase64['_'] = fromBase64['/'];
        fromBase64['='] = -2;
    }

}
