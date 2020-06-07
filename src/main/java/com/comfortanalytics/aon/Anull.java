package com.comfortanalytics.aon;

/**
 * There is a single Aobj representing null, the NULL constant in this class.
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

    /**
     * True
     */
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "null";
    }


}
