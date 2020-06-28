Aon
===
[![](https://jitpack.io/v/a-hansen/aon.svg)](https://jitpack.io/#a-hansen/aon)
* JDK 1.8+

Overview
--------

Aon is:

* Another JSON-like object notation.
* A streaming parser generator for the JSON and Aon formats.
* An [API](#java-library) like JSONObject for dealing with data in memory.

The Aon format is like JSON except is it more compact, has more data
types, and preserves the order of object members.  To be stream
friendly, Aon doesn't encode object or list lengths.

#### Compact
Uses a binary encoding that borrows techniques from
[UBJSON](http://www.ubjson.org) and [MsgPack](http://www.msgpack.org).

#### More Data Types
Big decimal, big integer, binary, boolean, double, float, list, long,
null, object, string and many flavors of signed and unsigned integers.

#### Ordered Objects
Order matters when displaying object members on user interfaces such
as property sheets.

#### Stream Friendly
Some formats encode object and array lengths at the start of the
structure.  It can make parsing more efficient, but streaming very
difficult.

#### Java Friendly
All data types are supported by Java.  For example, it isn't
possible to have a string or byte array whose length is specified as
an unsigned 32 bit int.

Dependency Management
---------------------

Maven
```
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>

<dependency>
  <groupId>com.comfortanalytics</groupId>
  <artifactId>aon</artifactId>
  <version>n.n.n</version>
  <type>pom</type>
</dependency>
```

Gradle
```
repositories {
    jcenter()
}
dependencies {
    implementation 'com.comfortanalytics:aon:+'
}
```

Comparing Formats
-----------------

**JSON** (42 bytes)
```
{"name":"aon","born":20180602,"cool":true}
```

**UBJSON** (32 bytes)
```
{ i 0x04 name s i 0x03 aon i 0x04 born I 0x01 0x33 0xEE 0x7A i 0x04 cool T }
```

**Aon** (27 bytes)
```
{ 0xA4 name 0xA3 aon 0xA4 born j 0x01 0x33 0xEE 0x7A 0xA4 cool T }
```

**MsgPack** (26 bytes)
```
0x83 0xA4 name 0xA3 aon 0xA4 born 0xCE 0x01 0x33 0xEE 0x7A 0xA4 cool 0xC3
```

Format
------

Aon uses a 1 byte prefix and two optional fields (length and data)
for all types:

```
<Prefix> [Length] [Data]
```

* Prefix - A single byte ASCII character or bitmask.  There are
three bitmasks used for compaction; a small negative int (-32 to -1),
a small positive int (0 - 31) and a small string (&lt;= 31 chars).
* Length - An optional positive int specifying the length of the data.
* Data - Optional bytes representing data such as 32 bit integers or
UTF8 strings.

The subsequent Aon format is represented using a pseudo-BNF syntax.
There is a [cheat sheet](#type-cheat-sheet) towards the end of this document.


```
<Document> ::= <Object> | <List>
<Value> :== <Object> | <List> | <Boolean> | <Double> | <Float> |
         <Null> | <Signed-Int> | <String> | <Unsigned-Int> |
         <Binary> | <Big-Integer> | <Big-Decimal>
```

#### Object
A collection of key value pairs surrounded by curly braces. Objects
must preserve the order addition and encoding.  Object implementations
must provide a mechanism to iterate memebers in order.
```
<Object> ::= "{" <Member-Pair>* "}"
<Member-Pair> ::= <String> <Value>
```
* There can be 0 or more key value pairs.
* The string key must be unique among all members of the same object.

#### List
An array of values surrounded by brackets.
```
<List> ::= "[" <Value>* "]"
```
* There can be 0 or more values in a list.

#### Boolean
A single byte character, 'T' for true, or 'F' for false.
```
<Boolean> ::= "T" | "F"
```

#### Double
Requires 9 bytes, the letter 'D' followed by 8 bytes in the IEEE 754
floating-point "double format" bit layout.
```
<Double> ::= "D" byte[8]
```

#### Float
Requires 5 bytes, the letter 'd' followed by 4 bytes in the IEEE 754
floating-point "single format" bit layout.
```
<Float> ::= "d" byte[4]
```

#### Null
A single byte, the letter 'Z'.
```
<Null> ::= "Z"
```

#### Signed Integer
Uses 1 to 5 bytes.  All signed ints start with a type byte that
describes the value.  If the value is small enough (-32 to -1), a
single byte can be used for both the type and the value.  If the
signed int is in the range 0 to 31, use unsigned-int5.
```
<Signed-Int> ::= <signed-int5> | <signed-int8> | <signed-int16> | <signed-int32> | <signed-int64>
<signed-int5>  ::= 0xC0
<signed-int8>  ::= "i" int8
<signed-int16> ::= "I" int16
<signed-int32> ::= "j" int32
<signed-int64> ::= "J" int64
```
* Signed-int5 can be identified with the bitmask 0xC0.  The value is
stored in the 5 lowest order bits, without a sign bit.
* To encode: (value & 0x1F) | 0xC0
* To decode (into 32 bit int): (read() & 0x1F) | 0xFFFFFFE0

#### String
Strings require a type byte, a length, and a UTF8 encoded string. If
the length is 31 bytes or less, a single byte can be used for both the
type and the length.
```
<String> ::= <str5> | <str8> | <str16> | <str32>
<str5>  ::= 0xA0 UTF8
<str8>  ::= "s" uint8-length UTF8
<str16> ::= "S" uint16-length UTF8
<str32> ::= "r" int32-length UTF8
```
* Str5 can be identified with the bitmask 0xA0.  The value is
stored in the 5 lowest order bits.
* To encode: value | 0xA0
* To decode: read() & 0x1F
* The length can be 0 for an empty string with no data field.
* The str32 length must be a positive signed int.

#### Unsigned Integers
Uses 1 to 5 bytes.  All unsigned ints start with a type byte that
describes the value.  If the value is small enough (0 to 31), a single
byte can be used for both the type and the value.
```
<Unsigned-Int> ::= <unsigned-int5> | <unsigned-int8> | <unsigned-int16> | <unsigned-int32>
<unsigned-int5>  ::= 0xE0
<unsigned-int8>  ::= "u" uint8
<unsigned-int16> ::= "U" uint16
<unsigned-int32> ::= "v" uint32
```
* Unsigned-int5 can be identified with the bitmask 0xE0.  The value
is stored in the 5 lowest order bits.
* To encode: value | 0xE0
* To decode: read() & 0x1F

#### Binary (byte array)
Requires 2 to 5 bytes in addition to the byte array.
```
<Binary> ::= <bin8> | <bin16> | <bin32>
<bin8>   ::= "b" uint8-length bytes
<bin16>  ::= "B" uint16-length bytes
<bin32>  ::= "c" int32-length bytes
```
* The bin32 length must be a positive signed int.

#### Big Integer
An integer so large it has to be encoded as a byte array.
```
<Big-Integer> ::= <bigint8> | <bigint16> | <bigint32>
<bigint>   ::= "n" uint8-length bytes
<bigint16> ::= "N" uint16-length bytes
<bigint32> ::= "o" int32-length bytes
```
* The bigint32 length must be a positive signed int.
* The array represents the two's-complement represention of the big
integer.  The array must be in big endian byte order, with the most
significant byte in the zeroth element (left most byte).  The array
must include at least one sign bit.

#### Big Decimal
An decimal so large it has to be encoded as a string.
```
<Big-Decimal> ::= <bigdec8> | <bigdec16> | <bigdec32>
<bigdec8>  ::= "g" uint8-length UTF8
<bigdec16> ::= "G" uint16-length UTF8
<bigdec32> ::= "h" int32-length UTF8
```
* The decimal32 length must be a positive signed int.
* The string should consist of an optional sign, '+' or '-',
followed by a sequence of zero or more digits ("the integer"),
optionally followed by a fraction, optionally followed by an exponent.
The fraction consists of a decimal point followed by zero or more
digits. The string must contain at least one digit in either the
integer or the fraction. The exponent consists of the character 'e'
or 'E' followed by one or more digits.

Endianness
----------
All numeric values must be written in **big endian** order.

MIME Type
---------
application/aon

Java Library
------------

This includes a Java library for representing Aon values in memory as
well as encoding and decoding.  It can also encode and decode JSON.

Usage
-----

Create data structures with Alist and Aobj.

```
import com.comfortanalytics.aon.*;

public static void main(String[] args) {
    Aobj obj = new Aobj()
            .put("binary", new byte[5])
            .put("boolean", true)
            .put("double", 100.1d)
            .put("float", 100.1f)
            .put("int", 100)
            .put("long", 100l)
            .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
            .putNull("null");
    System.out.println("The int value in the obj is " + map.getInt("int"));
    Alist list = new Alist()
            .add("binary", new byte[5])
            .add(true)
            .add(100.1d)
            .add(100.1f)
            .add(100)
            .add(100l)
            .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
            .addNull();
    System.out.println("The int value in the list is " + list.get(4));
    Alist complex = new Alist();
    complex.addList()
           .add(1)
           .add(2)
           .add(3);
    complex.addObj()
           .put("a", 1)
           .put("b", 2)
           .put("c", 3);
}
```

Aon encoding and decoding is straightforward.

```
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.io.*;

public Aobj decode() throws IOException {
    try (AonReader reader = Aon.reader(new File("data.aon"))) {
        return reader.getObj();
    }
}

public void encode(Aobj obj) throws IOException {
    Aon.writer(new File("data.aon")).value(obj).close();
}
```

The library also supports JSON encoding and decoding.

```
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.json.*;

public Aobj decode() throws IOException {
    try (JsonReader reader = Aon.jsonReader(new File("data.json"))) {
        return reader.getObj();
    }
}

public void encode(Aobj map) throws IOException {
    Aon.jsonWriter(new File("data.json")).value(map).close();
}
```

Streaming IO is supported as well.  The following two methods produce
the same result.

```
import com.comfortanalytics.aon.*;

public void streaming(Awriter out) {
    out.beginObj()
            .key("a").value(1)
            .key("b").value(3)
            .key("c").value(3)
            .endObj();
}

public void notStreaming(Awriter out) {
    out.value(new Aobj().put("a",1).put("b",2).put("c",3));
}
```

Benchmark
---------

There is a benchmark test class that compares native Aon encoding with
Aon-JSON as well as other popular JSON libs.  The benchmark uses JMH and
takes @45 minutes.

There are 4 categories of tests (large/small docs, encode/decode).  The results are 
sorted from fastest to slowest in each category.  If you compute the average placement 
of the four categories you find the overall performance from fastest to slowest:

1. Aon - Not exactly fair because it isn't JSON.
2. Jackson
3. Aon-JSON
4. Gson
5. Genson
6. JsonSimple

```
Benchmark                               Mode  Cnt      Score      Error  Units
AonBenchmark.DecodeLargeDoc.Aon         avgt    9   1321.429 �  195.275  us/op
AonBenchmark.DecodeLargeDoc.Genson      avgt    9   2034.332 �   90.413  us/op
AonBenchmark.DecodeLargeDoc.Jackson     avgt    9   2255.877 �   95.578  us/op
AonBenchmark.DecodeLargeDoc.AonJson     avgt    9   2346.974 �  157.704  us/op
AonBenchmark.DecodeLargeDoc.Gson        avgt    9   3222.755 �  175.402  us/op
AonBenchmark.DecodeLargeDoc.JsonSimple  avgt    9   4719.093 �   59.491  us/op

AonBenchmark.DecodeSmallDoc.Aon         avgt    9      2.035 �    0.035  us/op
AonBenchmark.DecodeSmallDoc.Gson        avgt    9      7.619 �    0.079  us/op
AonBenchmark.DecodeSmallDoc.Jackson     avgt    9      9.702 �    0.286  us/op
AonBenchmark.DecodeSmallDoc.AonJson     avgt    9      9.847 �    0.271  us/op
AonBenchmark.DecodeSmallDoc.JsonSimple  avgt    9     23.580 �    2.149  us/op
AonBenchmark.DecodeSmallDoc.Genson      avgt    9     66.161 �    1.848  us/op

AonBenchmark.EncodeLargeDoc.Jackson     avgt    9    826.832 �   44.415  us/op
AonBenchmark.EncodeLargeDoc.Aon         avgt    9    953.173 �  196.065  us/op
AonBenchmark.EncodeLargeDoc.AonJson     avgt    9   1602.833 �   57.043  us/op
AonBenchmark.EncodeLargeDoc.Genson      avgt    9   1993.407 �   76.821  us/op
AonBenchmark.EncodeLargeDoc.Gson        avgt    9  25703.711 � 2121.290  us/op
AonBenchmark.EncodeLargeDoc.JsonSimple  avgt    9  29252.399 � 1997.614  us/op

AonBenchmark.EncodeSmallDoc.Aon         avgt    9      1.318 �    0.085  us/op
AonBenchmark.EncodeSmallDoc.AonJson     avgt    9      3.839 �    0.100  us/op
AonBenchmark.EncodeSmallDoc.Jackson     avgt    9      3.866 �    0.104  us/op
AonBenchmark.EncodeSmallDoc.Gson        avgt    9     46.116 �    4.343  us/op
AonBenchmark.EncodeSmallDoc.JsonSimple  avgt    9     46.765 �    4.882  us/op
AonBenchmark.EncodeSmallDoc.Genson      avgt    9     53.422 �    1.913  us/op

Document sizes in bytes:
 AON small doc: 172
JSON small doc: 254
 AON large doc: 106000
JSON large doc: 150804
```

To run the benchmark, use the jmh gradle task:

```
gradlew jmh
```


Type Cheat Sheet
----------------

|Type     |Prefix    |Length     |Data      |Comment|
|---------|----------|-----------|----------|-------|
|boolean  | T _or_ F
|double   | D
|float    | d
|list     | [ _or_ ]
|null     | Z
|object   | { _or_ }
| | | | |
|bigdec8  | g        | uint8  | Length bytes     | UTF8
|bigdec16 | G        | uint16 | Length bytes     | UTF8
|bigdec32 | h        | int32  | Length bytes     | UTF8
| | | | |
|bigint8  | n        | uint8  | Length bytes     | Signed
|bigint16 | N        | uint16 | Length bytes     | Signed
|bigint32 | o        | int32  | Length bytes     | Signed, len must be a positive signed int
| | | | |
|bin8     | b        | uint8  | Length bytes     |
|bin16    | B        | uint16 | Length bytes     |
|bin32    | c        | int32  | Length bytes     | Length must be a positive signed int
| | | | |
|int5     | 0xC0     |        |                  | 32 bit value = (prefix & 0x1F) \| 0xFFFFFFE0
|int8     | i        |        | 1 signed byte    |
|int16    | I        |        | 2 signed bytes   |
|int32    | j        |        | 4 signed bytes   |
|int64    | J        |        | 8 signed bytes   |
| | | | |
|str5     | 0xA0     |        | Prefix bytes     | Data len = prefix & 0x1F
|str8     | s        | uint8  | Length bytes     | UTF8
|str16    | S        | uint16 | Length bytes     | UTF8
|str32    | r        | int32  | Length bytes     | UTF8, len must be postitive signed int
| | | | |
|uint5    | 0xE0     |        |                  | Value = prefix & 0x1F
|uint8    | u        |        | 1 unsigned byte  |
|uint16   | U        |        | 2 unsigned bytes |
|uint32   | v        |        | 4 unsigned bytes |

History
-------
_6.0.0_
  - Moved to Java 1.8
  - Model changes revolving around the addition of Aprimitive and AIvalue
  - Reworked jmh for the benchmark
  - Intellij Analyzer fixes
  - Switch to JUnit
  
_5.0.1_
  - Fix reading numbers.
  - TestNG
  - jcenter
  
_5.0.0_
  - New native Aon encoding format, major rewrite.

_4.0.1_
  - Fixed NPE in Amap.put(String,String).
  - Fix zip encoding.

_4.0.0_
  - Added AbstractReader and AbstractWriter, json now uses these.
  - Minor parenting fixes.
  
_3.1.0_
  - Now compatible with jdk 1.5
  - Removed idea and findbugs from the gradle script.
  
_3.0.0_
  - Split JsonAppender from JsonWriter for performance reasons.
  - Better performance!
  - Addressed some FindBugs issues.

_2.0.0_
  - Package change.

_1.1.0_
  - Made JsonWriter Appendable.
  - Added parenting to groups.
  - Added JSON-Simple to the benchmark.
  - Changed how benchmark results are printed.
  
_1.0.1_
  - Minor performance improvement in JsonWriter.
  
_1.0.0_
  - Hello World.
