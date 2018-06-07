package com.comfortanalytics.aon.json;

/**
 * @author Aaron Hansen
 */
interface JsonConstants {

    /**
     * How Double.NaN is encoded: "\\u001BNaN"
     */
    String DBL_NAN = "\u001BNaN";

    /**
     * How Double.NEGATIVE_INFINITY is encoded: "\\u001B-Infinity"
     */
    String DBL_NEG_INF = "\u001B-Infinity";

    /**
     * How Double.POSITIVE_INFINITY is encoded: "\\u001BInfinity"
     */
    String DBL_POS_INF = "\u001BInfinity";

}
