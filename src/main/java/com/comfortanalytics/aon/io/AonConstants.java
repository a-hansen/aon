package com.comfortanalytics.aon.io;

import java.nio.charset.Charset;

public interface AonConstants {

    char BIGINT16 = 'N';
    char BIGINT32 = 'o';
    char BIGINT8 = 'n';
    char BIN16 = 'B';
    char BIN32 = 'c';
    char BIN8 = 'b';
    char DEC16 = 'G';
    char DEC32 = 'h';
    char DEC8 = 'g';
    char DOUBLE = 'D';
    char FALSE = 'F';
    char FLOAT = 'd';
    char I16 = 'I';
    char I32 = 'j';
    char I64 = 'J';
    char I8 = 'i';
    char LIST_END = ']';
    char LIST_START = '[';
    int MAX_I16 = Short.MAX_VALUE;
    int MAX_I32 = Integer.MAX_VALUE;
    int MAX_I8 = Byte.MAX_VALUE;
    int MAX_U16 = 0xFFFF;
    long MAX_U32 = 0xFFFFFFFF;
    int MAX_U8 = 0xFF;
    int MIN_I16 = Short.MIN_VALUE;
    int MIN_I32 = Integer.MIN_VALUE;
    int MIN_I8 = Byte.MIN_VALUE;
    char NULL = 'Z';
    char OBJ_END = '}';
    char OBJ_START = '{';
    char STR16 = 'S';
    char STR32 = 'r';
    char STR8 = 's';
    char TRUE = 'T';
    char U16 = 'U';
    char U32 = 'v';
    char U8 = 'u';
    Charset UTF8 = Charset.forName("UTF-8");

}
