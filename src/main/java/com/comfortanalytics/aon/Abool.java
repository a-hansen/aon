package com.comfortanalytics.aon;


import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;

/**
 * Boolean value.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Abool extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Abool FALSE = new Abool(false);
    public static final Abool TRUE = new Abool(true);

    ///////////////////////////////////////////////////////////////////////////
    // Instance Fields
    ///////////////////////////////////////////////////////////////////////////

    private final boolean value;

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Abool(boolean val) {
        value = val;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.BOOLEAN;
    }

    @Override
    public boolean equals(Object arg) {
        return arg == this;
    }

    @Nonnull
    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    /**
     * 0 or 1.
     */
    @Override
    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(toInt());
    }

    /**
     * 0 or 1.
     */
    @Override
    public BigInteger toBigInt() {
        return BigInteger.valueOf(toInt());
    }

    @Override
    public boolean toBoolean() {
        return value;
    }

    /**
     * 0 or 1.
     */
    @Override
    public double toDouble() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public float toFloat() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public int toInt() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public long toLong() {
        if (value) {
            return 1;
        }
        return 0;
    }

    /**
     * 0 or 1.
     */
    @Override
    public Number toNumber() {
        return toInt();
    }

    @Nonnull
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Abool valueOf(boolean arg) {
        if (arg) {
            return TRUE;
        }
        return FALSE;
    }

    /**
     * Attempts to convert the string to a boolean.  Looks for common strings such as 0, 1, true,
     * false, on, off, enabled, disabled, active and inactive.
     *
     * @throws NullPointerException     If the arg is null.
     * @throws IllegalArgumentException If boolean value cannot be determined.
     */
    public static boolean valueOf(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        switch (Character.toLowerCase(str.charAt(0))) {
            case '0':
                if ("0".equals(str)) {
                    return false;
                }
                break;
            case '1':
                if ("1".equals(str)) {
                    return true;
                }
                break;
            case 't':
                if ("true".equalsIgnoreCase(str)) {
                    return true;
                }
                break;
            case 'f':
                if ("false".equalsIgnoreCase(str)) {
                    return false;
                }
                break;
            case 'o':
                if ("on".equalsIgnoreCase(str)) {
                    return true;
                } else if ("off".equalsIgnoreCase(str)) {
                    return false;
                }
                break;
            case 'a':
                if ("active".equalsIgnoreCase(str)) {
                    return true;
                }
                break;
            case 'i':
                if ("inactive".equalsIgnoreCase(str)) {
                    return false;
                }
                break;
            case 'e':
                if ("enabled".equalsIgnoreCase(str)) {
                    return true;
                }
                break;
            case 'd':
                if ("disabled".equalsIgnoreCase(str)) {
                    return false;
                }
                break;
        }
        throw new IllegalArgumentException("Not a boolean: " + str);
    }

}
