package com.comfortanalytics.aon;

import com.comfortanalytics.aon.Aobj.Member;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Basic implementation of Awriter.  Subclasses must implement the
 * abstract methods which all start with 'write'.
 *
 * @author Aaron Hansen
 */
public abstract class AbstractWriter implements Closeable, Awriter {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int LAST_DONE = 0; //document complete
    private static final int LAST_END = 1;  //end of map/list
    private static final int LAST_INIT = 2; //start
    private static final int LAST_KEY = 3;  //object key
    private static final int LAST_LIST = 4; //started a list
    private static final int LAST_OBJ = 5;  //started a map
    private static final int LAST_VAL = 6;  //list or object value

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private int depth = 0;
    private int last = LAST_INIT;

    /**
     * Subclasses can use this if applicable.
     */
    protected boolean prettyPrint = false;

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    public AbstractWriter beginList() {
        try {
            switch (last) {
                case LAST_OBJ:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                default:
                    if (prettyPrint && (last != LAST_INIT) && (last != LAST_KEY)) {
                        writeNewLineIndent();
                    }
            }
            writeListStart();
            last = LAST_LIST;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter beginMap() {
        try {
            switch (last) {
                case LAST_OBJ:
                    throw new IllegalStateException("Expecting map key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                default:
                    if (prettyPrint && (last != LAST_INIT) && (last != LAST_KEY)) {
                        writeNewLineIndent();
                    }
            }
            writeMapStart();
            last = LAST_OBJ;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter endList() {
        try {
            if (depth == 0) {
                throw new IllegalStateException("Nesting error.");
            }
            depth--;
            if (prettyPrint) {
                writeNewLineIndent();
            }
            writeListEnd();
            if (depth == 0) {
                last = LAST_DONE;
            } else {
                last = LAST_END;
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter endMap() {
        try {
            if (depth == 0) {
                throw new IllegalStateException("Nesting error.");
            }
            depth--;
            if (prettyPrint) {
                writeNewLineIndent();
            }
            writeMapEnd();
            if (depth == 0) {
                last = LAST_DONE;
            } else {
                last = LAST_END;
            }
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter key(CharSequence arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting: " + arg + " (" + last + ")");
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                default:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            writeKey(arg);
            last = LAST_KEY;
            writeKeyValueSeparator();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter reset() {
        depth = 0;
        last = LAST_INIT;
        return this;
    }

    public AbstractWriter value(Avalue arg) {
        if (arg == null) {
            return value((String) null);
        }
        switch (arg.aonType()) {
            case DECIMAL:
                value(arg.toBigDecimal());
                break;
            case BIGINT:
                value(arg.toBigInt());
                break;
            case BOOLEAN:
                value(arg.toBoolean());
                break;
            case DOUBLE:
                value(arg.toDouble());
                break;
            case FLOAT:
                value(arg.toFloat());
                break;
            case INT:
                value(arg.toInt());
                break;
            case LIST:
                beginList();
                for (Avalue val : arg.toList()) {
                    value(val);
                }
                endList();
                break;
            case LONG:
                value(arg.toLong());
                break;
            case OBJECT:
                beginMap();
                Aobj map = arg.toObj();
                Member e = map.getFirst();
                while (e != null) {
                    key(e.getKey()).value(e.getValue());
                    e = e.next();
                }
                endMap();
                break;
            case NULL:
                value((String) null);
                break;
            case STRING:
                value(arg.toString());
                break;
        }
        return this;
    }

    public AbstractWriter value(BigDecimal arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(BigInteger arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(boolean arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(double arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(float arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(int arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(long arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    public AbstractWriter value(String arg) {
        try {
            switch (last) {
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error: " + arg);
                case LAST_INIT:
                    throw new IllegalStateException("Not expecting value: " + arg);
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
                    break;
                case LAST_LIST:
                    if (prettyPrint) {
                        writeNewLineIndent();
                    }
            }
            if (arg == null) {
                writeNull();
            } else {
                writeValue(arg);
            }
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Current depth in the tree, will be needed by writeNewLineIndent.
     */
    protected int getDepth() {
        return depth;
    }

    /**
     * Write the value.
     */
    protected abstract void write(BigDecimal arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(BigInteger arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(boolean arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(double arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(float arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(int arg) throws IOException;

    /**
     * Write the value.
     */
    protected abstract void write(long arg) throws IOException;

    /**
     * Write string key of a map entry.
     */
    protected abstract void writeKey(CharSequence arg) throws IOException;

    /**
     * Separate the key from the value in a map.
     */
    protected abstract void writeKeyValueSeparator() throws IOException;

    /**
     * End the current list.
     */
    protected abstract void writeListEnd() throws IOException;

    /**
     * Start a new list.
     */
    protected abstract void writeListStart() throws IOException;

    /**
     * End the current map.
     */
    protected abstract void writeMapEnd() throws IOException;

    /**
     * Start a new map.
     */
    protected abstract void writeMapStart() throws IOException;

    /**
     * Override point for subclasses which perform use pretty printing, such as json.
     * Does nothing by default.
     *
     * @see #getDepth()
     */
    protected void writeNewLineIndent() throws IOException {
    }

    /**
     * Write a null value.
     */
    protected abstract void writeNull() throws IOException;

    /**
     * Write a list value or object entry separator, such as the comma in json.
     */
    protected abstract void writeSeparator() throws IOException;

    /**
     * Write the value, which will never be null.
     */
    protected abstract void writeValue(CharSequence arg) throws IOException;

}
