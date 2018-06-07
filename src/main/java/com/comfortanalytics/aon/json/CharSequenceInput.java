package com.comfortanalytics.aon.json;

import java.io.Closeable;
import java.io.IOException;

/**
 * Wraps a CharSequence for JsonReader.
 *
 * @author Aaron Hansen
 */
class CharSequenceInput implements JsonReader.Input {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private CharSequence in;
    private int len = 0;
    private int next = 0;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    public CharSequenceInput(CharSequence in) {
        this.in = in;
        this.len = in.length();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void close() throws IOException {
        if (in instanceof Closeable) {
            ((Closeable) in).close();
        }
    }

    @Override
    public int read() {
        if (next >= len) {
            return -1;
        }
        return in.charAt(next++);
    }

    @Override
    public void unread() {
        next--;
    }

}
