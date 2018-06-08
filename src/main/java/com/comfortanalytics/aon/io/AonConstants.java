package com.comfortanalytics.aon.io;

import java.nio.charset.Charset;

/**
 * @author Aaron Hansen
 */
public interface AonConstants {

    char DOUBLE = 'D';
    char FLOAT = 'd';
    char FALSE = 'F';
    char TRUE = 'T';
    char NULL = 'Z';

    char BIGINT8 = 'n';
    char BIGINT16 = 'N';
    char BIGINT32 = 'o';

    char BIN8 = 'b';
    char BIN16 = 'B';
    char BIN32 = 'c';

    char DEC8 = 'g';
    char DEC16 = 'G';
    char DEC32 = 'h';

    char I8 = 'i';
    char I16 = 'I';
    char I32 = 'j';
    char I64 = 'J';

    char LIST_END = ']';
    char LIST_START = '[';

    int MAX_I8 = Byte.MAX_VALUE;
    int MAX_I16 = Short.MAX_VALUE;
    int MAX_I32 = Integer.MAX_VALUE;

    int MAX_U8 = 0xFF;
    int MAX_U16 = 0xFFFF;
    long MAX_U32 = 0xFFFFFFFF;

    int MIN_I8 = Byte.MIN_VALUE;
    int MIN_I16 = Short.MIN_VALUE;
    int MIN_I32 = Integer.MIN_VALUE;

    char OBJ_END = '}';
    char OBJ_START = '{';

    char STR8 = 's';
    char STR16 = 'S';
    char STR32 = 'r';

    char U8 = 'u';
    char U16 = 'U';
    char U32 = 'v';

    Charset UTF8 = Charset.forName("UTF-8");

}
