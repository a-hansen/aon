/* Copyright 2017 by Aaron Hansen.
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

package com.ca.aon.json;

import java.io.*;
import java.util.zip.ZipInputStream;

/**
 * Allows unreading of a byte for JsonReader.
 */
class JsonInput implements JsonReader.Input {

    private Reader in;
    private char[] buf;
    private int len = 0;
    private int next = 0;

    public JsonInput(InputStream in)
            throws IOException {
        this(in, "UTF-8");
    }

    public JsonInput(InputStream in, String charset)
            throws IOException {
        //check if it is zip
        if (in.markSupported()) {
            byte[] zip = new byte[4];
            in.mark(4);
            in.read(zip);
            in.reset();
            if ((zip[0] == 0x50) &&
                    (zip[1] == 0x4b) &&
                    (zip[2] == 0x03) &&
                    (zip[3] == 0x04)) {
                ZipInputStream unzip = new ZipInputStream(new BufferedInputStream(in));
                unzip.getNextEntry();
                in = unzip;
            }
        }
        if (charset == null)
            this.in = new InputStreamReader(in);
        else
            this.in = new InputStreamReader(in, charset);
        this.buf = new char[8192];
        this.len = buf.length;
    }

    public JsonInput(Reader in) {
        this.in = in;
        this.buf = new char[8192];
        this.len = buf.length;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    private void fill() throws IOException {
        if (in == null) return;
        len = in.read(buf, 0, buf.length);
        next = 0;
    }

    public int read() {
        if (next >= len) {
            try {
                fill();
            } catch (IOException x) {
                throw new RuntimeException(x);
            }
            if (next >= len)
                return -1;
        }
        return buf[next++];
    }

    public int read(char[] buf, int off, int len) throws IOException {
        throw new IllegalStateException("Will never be called");
    }

    public void unread() {
        if (next > 0) --next;
    }


}
