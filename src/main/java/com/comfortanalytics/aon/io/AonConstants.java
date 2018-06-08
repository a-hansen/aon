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

    int BIN5 = 0x8;
    char BIN8 = 'b';
    char BIN16 = 'B';
    char BIN32 = 'c';

    char DEC8 = 'g';
    char DEC16 = 'G';
    char DEC32 = 'h';

    int I5 = 0xC0;
    char I8 = 'i';
    char I16 = 'I';
    char I32 = 'j';
    char I64 = 'J';

    char LIST_START = '[';
    char LIST_END = ']';

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

    int S5 = 0xE0;
    char S8 = 's';
    char S16 = 'S';
    char S32 = 'r';

    int U5 = 0x80;
    char U8 = 'u';
    char U16 = 'U';
    char U32 = 'v';

    Charset UTF8 = Charset.forName("UTF-8");

    // 8, 0x8, 1000
    //12, 0xC, 1100
    //14, 0xE, 1110

    //10, 0xA, 1010
    //11, 0xB, 1011

    //13, 0xD, 1101
    //13, 0xD, 1101
    //15, 0xF, 1111

    //I5 U5 S5

}
