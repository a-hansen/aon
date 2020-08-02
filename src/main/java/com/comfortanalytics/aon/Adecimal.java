package com.comfortanalytics.aon;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * Decimal values that exceed the min and max value of double (IEEE 754 floating-point
 * "double format" bit layout).
 *
 * @author Aaron Hansen
 */
@SuppressWarnings({"unused"})
public class Adecimal implements Adata {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Adecimal ZERO = new Adecimal(BigDecimal.ZERO);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final BigDecimal value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Adecimal(@Nonnull BigDecimal val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.DECIMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Adata)) {
            return false;
        }
        return value.equals(((Adata) o).toBigDecimal());
    }

    @Nonnull
    @Override
    public BigDecimal get() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean isBigDecimal() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return value;
    }

    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf(value.longValue());
    }

    @Override
    public boolean toBoolean() {
        return value.intValue() != 0;
    }

    @Override
    public double toDouble() {
        return value.doubleValue();
    }

    @Override
    public float toFloat() {
        return value.floatValue();
    }

    @Override
    public int toInt() {
        return value.intValue();
    }

    @Override
    public long toLong() {
        return value.longValue();
    }

    @Override
    public Number toNumber() {
        return value;
    }

    /**
     * Returns Astr
     */
    @Nonnull
    @Override
    public Aprimitive toPrimitive() {
        return Aon.ensureNotNull(Astr.valueOf(toString()));
    }

    @Nonnull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Will convert numbers and strings, otherwise returns null.
     */
    @Override
    public Adecimal valueOf(Aprimitive value) {
        if (Aon.isNull(value)) {
            return null;
        }
        try {
            switch (value.aonType()) {
                case DOUBLE:
                    return valueOf(BigDecimal.valueOf(value.toDouble()));
                case FLOAT:
                    return valueOf(BigDecimal.valueOf(value.toFloat()));
                case INT:
                    return valueOf(BigDecimal.valueOf(value.toInt()));
                case LONG:
                    return valueOf(BigDecimal.valueOf(value.toLong()));
                case STRING:
                    return valueOf(new BigDecimal(value.toString()));
            }
        } catch (Exception ignore) {
        }
        return null;
    }

    public static Adecimal valueOf(BigDecimal arg) {
        if (arg == null) {
            return null;
        }
        if (arg.equals(BigDecimal.ZERO)) {
            return ZERO;
        }
        return new Adecimal(arg);
    }

}
