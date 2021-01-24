package com.comfortanalytics.aon;

import com.comfortanalytics.aon.Aobj.Member;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Basic implementation of Awriter.  Subclasses must implement the abstract methods which all start
 * with 'write'.
 *
 * @author Aaron Hansen
 */
public abstract class AbstractWriter implements Awriter {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    private static final int LAST_INIT = 0; //start
    private static final int LAST_DONE = 1; //document complete
    private static final int LAST_END = 2;  //end of object/list
    private static final int LAST_VAL = 3;  //list or object value
    private static final int LAST_LIST = 4; //started a list
    private static final int LAST_OBJ = 5;  //started a object
    private static final int LAST_KEY = 6;  //object key

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

    @Override
    public AbstractWriter beginList() {
        try {
            switch (last) {
                case LAST_OBJ:
                    throw new IllegalStateException("Expecting object key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
            }
            if (prettyPrint && (last != LAST_INIT) && (last != LAST_KEY)) {
                writeNewLineIndent();
            }
            writeBeginList();
            last = LAST_LIST;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter beginObj() {
        try {
            switch (last) {
                case LAST_OBJ:
                    throw new IllegalStateException("Expecting object key.");
                case LAST_DONE:
                    throw new IllegalStateException("Nesting error.");
                case LAST_VAL:
                case LAST_END:
                    writeSeparator();
            }
            if (prettyPrint && (last != LAST_INIT) && (last != LAST_KEY)) {
                writeNewLineIndent();
            }
            writeBeginObj();
            last = LAST_OBJ;
            depth++;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter endList() {
        try {
            if (depth == 0) {
                throw new IllegalStateException("Nesting error.");
            }
            depth--;
            if (prettyPrint) {
                writeNewLineIndent();
            }
            writeEndList();
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

    @Override
    public AbstractWriter endObj() {
        try {
            if (depth == 0) {
                throw new IllegalStateException("Nesting error.");
            }
            depth--;
            if (prettyPrint) {
                writeNewLineIndent();
            }
            writeEndObj();
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

    @Override
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
            }
            if (prettyPrint) {
                writeNewLineIndent();
            }
            writeKey(arg);
            last = LAST_KEY;
            writeKeyValueSeparator();
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter reset() {
        depth = 0;
        last = LAST_INIT;
        return this;
    }

    @Override
    public AbstractWriter value(Adata arg) {
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
            case BINARY:
                value(((Abinary) arg).value);
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
                for (Adata val : arg.toList()) {
                    value(val);
                }
                endList();
                break;
            case LONG:
                value(arg.toLong());
                break;
            case OBJECT:
                beginObj();
                Aobj object = arg.toObj();
                Member e = object.getFirst();
                while (e != null) {
                    key(e.getKey()).value(e.getValue());
                    e = e.next();
                }
                endObj();
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

    @Override
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

    @Override
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

    @Override
    public AbstractWriter value(boolean arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(byte[] arg) {
        try {
            if (last != LAST_KEY) {
                switch (last) {
                    case LAST_DONE:
                        throw new IllegalStateException("Nesting error");
                    case LAST_INIT:
                        throw new IllegalStateException("Not expecting byte[] value");
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(double arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(float arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(int arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(long arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            write(arg);
            last = LAST_VAL;
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
        return this;
    }

    @Override
    public AbstractWriter value(String arg) {
        try {
            if (last != LAST_KEY) {
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
            }
            if (arg == null) {
                writeNull();
            } else {
                write(arg);
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
     * Base64 encodes the array and writes the string byte by default.
     */
    protected void write(byte[] arg) throws IOException {
        write(AonBase64.encode(arg));
    }

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
     * Write the value, which will never be null.
     */
    protected abstract void write(CharSequence arg) throws IOException;

    /**
     * Start a new list.
     */
    protected abstract void writeBeginList() throws IOException;

    /**
     * Start a new object.
     */
    protected abstract void writeBeginObj() throws IOException;

    /**
     * End the current list.
     */
    protected abstract void writeEndList() throws IOException;

    /**
     * End the current object.
     */
    protected abstract void writeEndObj() throws IOException;

    /**
     * Write string key of a object entry.
     */
    protected abstract void writeKey(CharSequence arg) throws IOException;

    /**
     * Separate the key from the value in a object.
     */
    protected abstract void writeKeyValueSeparator() throws IOException;

    /**
     * Override point for subclasses which perform use pretty printing, such as json. Does nothing
     * by default.
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

}
