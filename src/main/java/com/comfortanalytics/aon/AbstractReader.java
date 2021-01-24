package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Basic implementation of Areader.  Subclasses must implement the next() method.
 *
 * @author Aaron Hansen
 * @see #next()
 */
@SuppressWarnings("unused")
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
        if (last == Token.DECIMAL) {
            return valDecimal;
        }
        switch (last) {
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
            case NULL:
                return null;
            case STRING:
                return new BigDecimal(valString);
            default:
                throw new IllegalStateException("Not a big decimal");
        }
    }

    @Override
    public BigInteger getBigInt() {
        if (last == Token.BIGINT) {
            return valBigint;
        }
        switch (last) {
            case DECIMAL:
                return new BigInteger(valDecimal.toString());
            case DOUBLE:
                return BigInteger.valueOf((long) valDouble);
            case FLOAT:
                return BigInteger.valueOf((long) valFloat);
            case INT:
                return BigInteger.valueOf(valInt);
            case LONG:
                return BigInteger.valueOf(valLong);
            case NULL:
                return null;
            case STRING:
                return new BigInteger(valString);
            default:
                throw new IllegalStateException("Not a big integer");
        }
    }

    @Override
    public byte[] getBinary() {
        if (last == Token.BINARY) {
            return valBinary;
        }
        if (last == Token.STRING) {
            return AonBase64.decode(valString);
        }
        if (last == Token.NULL) {
            return null;
        }
        throw new IllegalStateException("Not binary");
    }

    @Override
    public boolean getBoolean() {
        if (last == Token.BOOLEAN) {
            return valBoolean;
        }
        if (last == Token.NULL) {
            throw new NullPointerException();
        }
        switch (last) {
            case DECIMAL:
                double d = valDecimal.doubleValue();
                if (d == 0) {
                    return false;
                } else if (d == 1) {
                    return true;
                }
                break;
            case DOUBLE:
                double dbl = valDouble;
                if (dbl == 0) {
                    return false;
                } else if (dbl == 1) {
                    return true;
                }
                break;
            case FLOAT:
                float f = valFloat;
                if (f == 0) {
                    return false;
                } else if (f == 1) {
                    return true;
                }
                break;
            case BIGINT:
            case INT:
            case LONG:
                int i = getInt();
                if (i == 0) {
                    return false;
                } else if (i == 1) {
                    return true;
                }
                break;
            case STRING:
                return Abool.valueOf(valString);
        }
        throw new IllegalStateException("Not a boolean");
    }

    @Override
    public double getDouble() {
        if (last == Token.DOUBLE) {
            return valDouble;
        }
        switch (last) {
            case DECIMAL:
                return valDecimal.doubleValue();
            case BIGINT:
                return valBigint.doubleValue();
            case FLOAT:
                return valFloat;
            case INT:
                return valInt;
            case LONG:
                return (double) valLong;
            case STRING:
                return Double.parseDouble(valString);
            case NULL:
                throw new NullPointerException();
            default:
                throw new IllegalStateException("Not a double");
        }
    }

    @Override
    public float getFloat() {
        if (last == Token.FLOAT) {
            return valFloat;
        }
        switch (last) {
            case DECIMAL:
                return valDecimal.floatValue();
            case BIGINT:
                return valBigint.floatValue();
            case DOUBLE:
                return (float) valDouble;
            case INT:
                return (float) valInt;
            case LONG:
                return (float) valLong;
            case STRING:
                return Float.parseFloat(valString);
            case NULL:
                throw new NullPointerException();
            default:
                throw new IllegalStateException("Not a float");
        }
    }

    @Override
    public int getInt() {
        if (last == Token.INT) {
            return valInt;
        }
        switch (last) {
            case DECIMAL:
                return valDecimal.intValue();
            case BIGINT:
                return valBigint.intValue();
            case DOUBLE:
                return (int) valDouble;
            case FLOAT:
                return (int) valFloat;
            case LONG:
                return (int) valLong;
            case STRING:
                return Integer.parseInt(valString);
            case NULL:
                throw new NullPointerException();
            default:
                throw new IllegalStateException("Not an int");
        }
    }

    @Override
    public Alist getList() {
        if (last == Token.ROOT) {
            next();
        }
        if (last == Token.NULL) {
            return null;
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
                    throw new IllegalStateException("Unexpected end of object in list");
                case DECIMAL:
                    ret.add(valDecimal);
                    break;
                case BIGINT:
                    ret.add(valBigint);
                    break;
                case BINARY:
                    ret.add(valBinary);
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
        if (last == Token.LONG) {
            return valLong;
        }
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
                return valInt;
            case STRING:
                return Long.parseLong(valString);
            case NULL:
                throw new NullPointerException();
            default:
                throw new IllegalStateException("Not a long");
        }
    }

    @Override
    public Aobj getObj() {
        if (last == Token.ROOT) {
            next();
        }
        if (last == Token.NULL) {
            return null;
        }
        if (last != Token.BEGIN_OBJ) {
            throw new IllegalStateException("Not a object");
        }
        Aobj ret = new Aobj();
        String key;
        while (true) {
            switch (next()) {
                case STRING:
                    key = valString;
                    break;
                case END_OBJ:
                case END_INPUT:
                    return ret;
                default:
                    throw new IllegalStateException("Expecting a string key or object end");
            }
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    throw new IllegalStateException("Unexpected end of list in object");
                case END_OBJ:
                    return ret;
                case DECIMAL:
                    ret.put(key, Adecimal.valueOf(valDecimal));
                    break;
                case BIGINT:
                    ret.put(key, Abigint.valueOf(valBigint));
                    break;
                case BINARY:
                    ret.put(key, Abinary.valueOf(valBinary));
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
                    throw new IllegalStateException("Unexpected token in object: " + last);
            }
        }
    }

    @Override
    public String getString() {
        switch (last) {
            case DECIMAL:
                return valDecimal.toString();
            case BIGINT:
                return valBigint.toString();
            case BOOLEAN:
                return String.valueOf(valBoolean);
            case DOUBLE:
                return String.valueOf(valDouble);
            case FLOAT:
                return String.valueOf(valFloat);
            case INT:
                return String.valueOf(valInt);
            case LONG:
                return String.valueOf(valLong);
            case BEGIN_LIST:
                return getList().toString();
            case BEGIN_OBJ:
                return getObj().toString();
            case NULL:
                return null;
            case STRING:
                return valString;
            default:
                throw new IllegalStateException("Not a string");
        }
    }

    @Override
    public Adata getValue() {
        if (last == Token.ROOT) {
            next();
        }
        switch (last) {
            case DECIMAL:
                return Adecimal.valueOf(valDecimal);
            case BIGINT:
                return Abigint.valueOf(valBigint);
            case BINARY:
                return Abinary.valueOf(valBinary);
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

    protected Token setBeginObj() {
        return last = Token.BEGIN_OBJ;
    }

    protected Token setEndInput() {
        return last = Token.END_INPUT;
    }

    protected Token setEndList() {
        return last = Token.END_LIST;
    }

    protected Token setEndObj() {
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
        valFloat = arg;
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
