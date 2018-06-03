/* ISC License
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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Basic implementation of Areader.  Subclasses must implement the next() method.
 *
 * @author Aaron Hansen
 * @see #next()
 * @see Areader
 */
public abstract class AbstractReader implements Areader {

    // Fields
    // ---------

    private Token last = Token.ROOT;
    protected BigInteger valBigint;
    protected boolean valBoolean;
    protected BigDecimal valDecimal;
    protected double valDouble;
    protected float valFloat;
    protected int valInt;
    protected long valLong;
    protected String valString;

    // Public Methods
    // --------------

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

    public boolean getBoolean() {
        if (last != Token.BOOLEAN) {
            throw new IllegalStateException("Not a boolean");
        }
        return valBoolean;
    }

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
                case END_MAP:
                    throw new IllegalStateException("Unexpected end of map in list");
                case KEY:
                    throw new IllegalStateException("Unexpected key in list");
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
                case BEGIN_MAP:
                    ret.add(getMap());
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

    public Amap getMap() {
        if (last == Token.ROOT) {
            next();
        }
        if (last != Token.BEGIN_MAP) {
            throw new IllegalStateException("Not a map");
        }
        Amap ret = new Amap();
        String key = null;
        while (true) {
            switch (next()) {
                case KEY:
                    key = valString;
                    break;
                case END_MAP:
                case END_INPUT:
                    return ret;
                default:
                    throw new IllegalStateException("Expecting a key or map end");
            }
            switch (next()) {
                case END_INPUT:
                    throw new IllegalStateException("Unexpected end of input");
                case END_LIST:
                    throw new IllegalStateException("Unexpected end of list in map");
                case END_MAP:
                    return ret;
                case KEY:
                    throw new IllegalStateException("Unexpected key in map");
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
                case BEGIN_MAP:
                    ret.put(key, getMap());
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

    public Aobj getObj() {
        if (last == Token.ROOT) {
            next();
        }
        switch (last) {
            case KEY:
                return Astr.valueOf(valString);
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
            case BEGIN_MAP:
                return getMap();
            case NULL:
                return Anull.NULL;
            case STRING:
                return Astr.valueOf(valString);
        }
        throw new IllegalStateException("Not a value");
    }

    public String getString() {
        if ((last != Token.STRING) && (last != Token.KEY)) {
            throw new IllegalStateException("Not a string");
        }
        return valString;
    }

    public Token last() {
        return last;
    }

    /**
     * Subclasses must override this, then call one of the setXxx methods.
     * <p>
     * {@inheritDoc}
     */
    public abstract Token next();

    public AbstractReader reset() {
        last = Token.ROOT;
        return this;
    }

    protected Token setBeginList() {
        return last = Token.BEGIN_LIST;
    }

    protected Token setBeginMap() {
        return last = Token.BEGIN_MAP;
    }

    protected Token setEndInput() {
        return last = Token.END_INPUT;
    }

    protected Token setEndList() {
        return last = Token.END_LIST;
    }

    protected Token setEndMap() {
        return last = Token.END_MAP;
    }

    /**
     * Call setNextValue(String) before calling this.
     *
     * @see #setNextValue(String)
     */
    protected Token setNextKey() {
        return last = Token.KEY;
    }

    protected Token setNextValue(BigDecimal arg) {
        if (arg == null) {
            return setNextValueNull();
        }
        valDecimal = arg;
        return last = Token.DECIMAL;
    }

    protected Token setNextValue(BigInteger arg) {
        if (arg == null) {
            return setNextValueNull();
        }
        valBigint = arg;
        return last = Token.BIGINT;
    }

    protected Token setNextValue(boolean arg) {
        valBoolean = arg;
        return last = Token.BOOLEAN;
    }

    protected Token setNextValue(double arg) {
        valDouble = arg;
        return last = Token.DOUBLE;
    }

    protected Token setNextValue(float arg) {
        valDouble = arg;
        return last = Token.FLOAT;
    }

    protected Token setNextValue(int arg) {
        valInt = arg;
        return last = Token.INT;
    }

    /**
     * If this is a key, call this, followed by setNextKey()
     *
     * @see #setNextKey()
     */
    protected Token setNextValue(long arg) {
        valLong = arg;
        return last = Token.LONG;
    }

    protected Token setNextValue(String arg) {
        if (arg == null) {
            return setNextValueNull();
        }
        valString = arg;
        return last = Token.STRING;
    }

    protected Token setNextValueNull() {
        return last = Token.NULL;
    }


}
