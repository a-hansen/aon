package com.comfortanalytics.aon;

import javax.annotation.Nonnull;

/**
 * There is a single object representing null, the NULL constant in this class.
 *
 * @author Aaron Hansen
 */
@SuppressWarnings("unused")
public class Anull extends Aprimitive {

    ///////////////////////////////////////////////////////////////////////////
    // Class Fields
    ///////////////////////////////////////////////////////////////////////////

    public static final Anull NULL = new Anull();

    ///////////////////////////////////////////////////////////////////////////
    // Constructors
    ///////////////////////////////////////////////////////////////////////////

    private Anull() {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Methods
    ///////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public Atype aonType() {
        return Atype.NULL;
    }

    /**
     * True of the arg == this.
     */
    @Override
    public boolean equals(Object arg) {
        return arg == this;
    }

    @Nonnull
    @Override
    public Object get() {
        return null;
    }

    /**
     * True
     */
    @Override
    public boolean isNull() {
        return true;
    }

    @Nonnull
    @Override
    public String toString() {
        return "null";
    }


}
