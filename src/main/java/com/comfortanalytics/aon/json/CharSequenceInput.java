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

package com.comfortanalytics.aon.json;

import java.io.Closeable;
import java.io.IOException;

/**
 * Wraps a CharSequence for JsonReader.
 *
 * @author Aaron Hansen
 */
class CharSequenceInput implements JsonReader.Input {

    private CharSequence in;
    private int len = 0;
    private int next = 0;

    public CharSequenceInput(CharSequence in) {
        this.in = in;
        this.len = in.length();
    }

    public void close() throws IOException {
        if (in instanceof Closeable) {
            ((Closeable) in).close();
        }
    }

    public int read() {
        if (next >= len) {
            return -1;
        }
        return in.charAt(next++);
    }

    public void unread() {
        next--;
    }

}