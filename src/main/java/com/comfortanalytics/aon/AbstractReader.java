package com.comfortanalytics.aon;

import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Basic implementation of Areader.  Subclasses must implement the next() method.
 *
 * @author Aaron Hansen
 * @see #next()
 */
public abstract class AbstractReader implements Areader {

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private Token last = Token.ROOT;
    private BigInteger valBigint;
    private byte[] valBinary;
    private boolean valBoolean;
    private BigDecimal valDecimal;
    private double valDouble;
    private float valFloat;
    private int valInt;
    private long valLong;
    private String valString;

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public BigDecimal getBigDecimal() {
        switch (last) {
            case DECIMAL:
                break;
            case BIGINT:
                return new BigDecimal(valBigint);
            case DOUBLE:
                return BigDecimal.valueOf(valDouble);
            case FLOAT:
                return BigDecimal.valueOf(valFloat);
            case INT:
                return BigDecimal.valueOf(valInt);
            case LONG:
                return BigDecimal.valueOf(valLong);
            case STRING:
                return new BigDecimal(valString);
            default:
                throw new IllegalStateException("Not a big decimal");
        }
        return valDecimal;
    }

    @Override
    public BigInteger getBigInt() {
        switch (last) {
            case DECIMAL:
                return new BigInteger(valDecimal.toString());
            case BIGINT:
                break;
            case DOUBLE:
                return BigInteger.valueOf((long) valDouble);
            case FLOAT:
                return BigInteger.valueOf((long) valFloat);
            case INT:
                return BigInteger.valueOf(valInt);
            case LONG:
                return BigInteger.valueOf(valLong);
            case STRING:
                return new BigInteger(valString);
            default:
                throw new IllegalStateException("Not a big integer");
        }
        return valBigint;
    }

    @Override
    public byte[] getBinary() {
        if (last != Token.BINARY) {
            throw new IllegalStateException("Not binary");
        }
        return valBinary;
    }

    @Override
    public boolean getBoolean() {
        if (last != Token.BOOLEAN) {
            throw new IllegalStateException("Not a boolean");
        }
        return valBoolean;
    }

    @Override
    public double getDouble() {
        switch (last) {
            case DOUBLE:
                break;
            case FLOAT:
                return (double) valFloat;
            case INT:
                return (double) valInt;
            case LONG:
                return (double) valLong;
            default:
                throw new IllegalStateException("Not a double");
        }
        return valDouble;
    }

    @Override
    public float getFloat() {
        switch (last) {
            case DECIMAL:
                return valDecimal.floatValue();
            case BIGINT:
                return valBigint.floatValue();
            case DOUBLE:
                return (float) valDouble;
            case FLOAT:
                break;
            case INT:
                return (float) valInt;
            case LONG:
                return (float) valLong;
            default:
                throw new IllegalStateException("Not a float");
        }
        return valFloat;
    }

    @Override
    public int getInt() {
        switch (last) {
            case DECIMAL:
                return valDecimal.intValue();
            case BIGINT:
                return valBigint.intValue();
            case DOUBLE:
                return (int) valDouble;
            case FLOAT:
                return (int) valFloat;
            case INT:
                break;
            case LONG:
                return (int) valLong;
            default:
                throw new IllegalStateException("Not an int");
        }
        return valInt;
    }

    @Override
    public Alist getList() {
        if (last == Token.ROOT) {
            next();
        }
        if (last != Token.BEGIN_LIST) {
            throw new IllegalStateException("Not a list");
        }
        Alist ret = new Alist();
        while (true) {
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    return ret;
                case END_OBJ:
                    throw new IllegalStateException("Unexpected end of map in list");
                case DECIMAL:
                    ret.add(valDecimal);
                    break;
                case BIGINT:
                    ret.add(valBigint);
                    break;
                case BOOLEAN:
                    ret.add(valBoolean);
                    break;
                case DOUBLE:
                    ret.add(valDouble);
                    break;
                case FLOAT:
                    ret.add(valFloat);
                    break;
                case INT:
                    ret.add(valInt);
                    break;
                case LONG:
                    ret.add(valLong);
                    break;
                case BEGIN_LIST:
                    ret.add(getList());
                    break;
                case BEGIN_OBJ:
                    ret.add(getObj());
                    break;
                case NULL:
                    ret.addNull();
                    break;
                case STRING:
                    ret.add(valString);
                    break;
                default:
                    throw new IllegalStateException("Unexpected token in list: " + last);
            }
        }
    }

    @Override
    public long getLong() {
        switch (last) {
            case DECIMAL:
                return valDecimal.longValue();
            case BIGINT:
                return valBigint.longValue();
            case DOUBLE:
                return (long) valDouble;
            case FLOAT:
                return (long) valFloat;
            case INT:
                return (long) valInt;
            case LONG:
                break;
            default:
                throw new IllegalStateException("Not an int");
        }
        return valLong;
    }

    @Override
    public Aobj getObj() {
        if (last == Token.ROOT) {
            next();
        }
        if (last != Token.BEGIN_OBJ) {
            throw new IllegalStateException("Not a map");
        }
        Aobj ret = new Aobj();
        String key = null;
        while (true) {
            switch (next()) {
                case STRING:
                    key = valString;
                    break;
                case END_OBJ:
                case END_INPUT:
                    return ret;
                default:
                    throw new IllegalStateException("Expecting a string key or map end");
            }
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    throw new IllegalStateException("Unexpected end of list in map");
                case END_OBJ:
                    return ret;
                case DECIMAL:
                    ret.put(key, Adecimal.valueOf(valDecimal));
                    break;
                case BIGINT:
                    ret.put(key, Abigint.valueOf(valBigint));
                    break;
                case BOOLEAN:
                    ret.put(key, Abool.valueOf(valBoolean));
                    break;
                case DOUBLE:
                    ret.put(key, Adouble.valueOf(valDouble));
                    break;
                case FLOAT:
                    ret.put(key, Afloat.valueOf(valFloat));
                    break;
                case INT:
                    ret.put(key, Aint.valueOf(valInt));
                    break;
                case LONG:
                    ret.put(key, Along.valueOf(valLong));
                    break;
                case BEGIN_LIST:
                    ret.put(key, getList());
                    break;
                case BEGIN_OBJ:
                    ret.put(key, getObj());
                    break;
                case NULL:
                    ret.putNull(key);
                    break;
                case STRING:
                    ret.put(key, Astr.valueOf(valString));
                    break;
                default:
                    throw new IllegalStateException("Unexpected token in map: " + last);
            }
        }
    }

    @Override
    public String getString() {
        if (last != Token.STRING) {
            throw new IllegalStateException("Not a string");
        }
        return valString;
    }

    @Override
    public Avalue getValue() {
        if (last == Token.ROOT) {
            next();
        }
        switch (last) {
            case DECIMAL:
                return Adecimal.valueOf(valDecimal);
            case BIGINT:
                return Abigint.valueOf(valBigint);
            case BOOLEAN:
                return Abool.valueOf(valBoolean);
            case DOUBLE:
                return Adouble.valueOf(valDouble);
            case FLOAT:
                return Afloat.valueOf(valFloat);
            case INT:
                return Aint.valueOf(valInt);
            case LONG:
                return Along.valueOf(valLong);
            case BEGIN_LIST:
                return getList();
            case BEGIN_OBJ:
                return getObj();
            case NULL:
                return Anull.NULL;
            case STRING:
                return Astr.valueOf(valString);
        }
        throw new IllegalStateException("Not a value");
    }

    @Override
    public Token last() {
        return last;
    }

    /**
     * Subclasses must override this, then call one of the setXxx methods.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public abstract Token next();

    @Override
    public AbstractReader reset() {
        last = Token.ROOT;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected Methods
    ///////////////////////////////////////////////////////////////////////////

    protected Token setBeginList() {
        return last = Token.BEGIN_LIST;
    }

    protected Token setBeginMap() {
        return last = Token.BEGIN_OBJ;
    }

    protected Token setEndInput() {
        return last = Token.END_INPUT;
    }

    protected Token setEndList() {
        return last = Token.END_LIST;
    }

    protected Token setEndMap() {
        return last = Token.END_OBJ;
    }

    protected Token setNext(BigDecimal arg) {
        if (arg == null) {
            return setNextNull();
        }
        valDecimal = arg;
        return last = Token.DECIMAL;
    }

    protected Token setNext(BigInteger arg) {
        if (arg == null) {
            return setNextNull();
        }
        valBigint = arg;
        return last = Token.BIGINT;
    }

    protected Token setNext(boolean arg) {
        valBoolean = arg;
        return last = Token.BOOLEAN;
    }

    protected Token setNext(byte[] arg) {
        valBinary = arg;
        return last = Token.BINARY;
    }

    protected Token setNext(double arg) {
        valDouble = arg;
        return last = Token.DOUBLE;
    }

    protected Token setNext(float arg) {
        valDouble = arg;
        return last = Token.FLOAT;
    }

    protected Token setNext(int arg) {
        valInt = arg;
        return last = Token.INT;
    }

    protected Token setNext(long arg) {
        valLong = arg;
        return last = Token.LONG;
    }

    protected Token setNext(String arg) {
        if (arg == null) {
            return setNextNull();
        }
        valString = arg;
        return last = Token.STRING;
    }

    protected Token setNextNull() {
        return last = Token.NULL;
    }


}
