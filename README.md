Aon
===

* Version: 5.0.0
* JDK 1.6+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)

Overview
--------

Aon is another object notation like JSON but is more compact, has
more data types, and preserves the order of object members.  To be
stream friendly, Aon doesn't encode object or list lengths.

#### Compact
Uses a hybrid binary encoding that is somewhat readable like
[UBJSON](http://www.ubjson.org) and compact like
[MsgPack](http://www.msgpack.org).

#### More Data Types
Big decimal, big integer, binary, boolean, double, float, list, long,
null, object, string and many flavors of signed and unsigned integers.

#### Ordered Objects
Order matters when displaying object members on user interfaces such
as property sheets.

Comparing Formats
-----------------

**JSON** (42 bytes)
```
{"name":"aon","born":20180602,"cool":true}
```

**MsgPack** (26 bytes)
```
0x83 0xA4 name 0xA3 aon 0xA4 born 0xCE 0x01 0x33 0xEE 0x7A 0xA4 cool 0xC3
```

**UBJSON** (32 bytes)
```
{ i 0x04 name s i 0x03 aon i 0x04 born I 0x01 0x33 0xEE 0x7A i 0x04 cool T }
```

**Aon** (26 bytes)
```
{ 0xE4 name 0xE3 aon 0xE4 born j 0x01 0x33 0xEE 0x7A cool T }
```

Format
------

Aon uses a 1 byte indicator and two optional fields (length and data)
for all types:

```
<Indicator> [Length] [Data]
```

* Indicator - A single byte ASCII character or bitmask.  There are
three bitmasks used for compaction; a small negative int (-32 to -1),
a small positive int (0 - 31) and a small string (&lt;= 31 chars).
* Length - An optional positive int specifying the length of the data.
* Data - Bytes representing data such as 32 bit integers or UTF8
strings.

The subsequent Aon format is represented using a pseudo-BNF syntax.

```
<Aon-Graph> ::= <Object> | <List>
<Value> :== <Object> | <List> | <Boolean> | <Double> | <Float> |
         <Null> | <Signed-Int> | <String> | <Unsigned-Int> |
         <Binary> | <Big-Integer> | <Big-Decimal>
```

#### Object
An ordered collection of key value pairs surrounded by curly braces.
```
<Object> ::= "{" <Member-Pair>* "}"
<Member-Pair> ::= <String> <Value>
```
* There can be 0 or more key value pairs.

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
* Signed-int5 can be identified with the bitmask 0xC0.  The value
is stored in the 5 lowest order bits.  To encode, use
((value | 0xC0) & 0xFF).  To decode into a signed 32 bit integer, use
(value | 0xFFFFFFE0).

#### String
Strings require a type byte, a length, and a UTF8 encoded string. If
the length is 31 bytes or less, a single byte can be used for both the
type and the length.
```
<String> ::= <str5> | <str8> | <str16> | <str32>
<str5>  ::= 0xE0 UTF8
<str8>  ::= "s" uint8-length UTF8
<str16> ::= "S" uint16-length UTF8
<str32> ::= "r" int32-length UTF8
```
* Str5 can be identified with the bitmask 0x80.  The value is
stored in the 5 lowest order bits.  To write, use the equation:
(value | 0xE0).  To read, use the equation: (value & 0x1F).
* The length can be 0 for an empty string.
* The str32 length must be a positive signed int.

#### Unsigned Integers
Uses 1 to 5 bytes.  All unsigned ints start with a type byte that
describes the value.  If the value is small enough (0 to 31), a single
byte can be used for both the type and the value.
```
<Unsigned-Int> ::= <unsigned-int5> | <unsigned-int8> | <unsigned-int16> | <unsigned-int32>
<unsigned-int5>  ::= 0x80
<unsigned-int8>  ::= "u" uint8
<unsigned-int16> ::= "U" uint16
<unsigned-int32> ::= "v" uint32
```
* Unsigned-int5 can be identified with the bitmask 0x80.  The value
is stored in the 5 lowest order bits.  To write, use the equation:
(value | 0x80).  To read, use the equation: (value & 0x1F).

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
An integer so large it has to be encoded as a string.
```
<Big-Integer> ::= <bigint8> | <bigint16> | <bigint32>
<bigint>   ::= "n" uint8-length UTF8
<bigint16> ::= "N" uint16-length UTF8
<bigint32> ::= "o" int32-length UTF8
```
* The bigint32 length must be a positive signed int.
* The String should consist of an optional minus sign followed by a
sequence of one or more digits.  It should not contain any any
extraneous characters such as whitespace.

#### Big Decimal
An decimal so large it has to be encoded as a string.
```
<Big-Decimal> ::= <decimal8> | <decimal16> | <decimal32>
<decimal8>  ::= "g" uint8-length UTF8
<decimal16> ::= "G" uint16-length UTF8
<decimal32> ::= "h" int32-length UTF8
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
Big endian

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

```java
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

```java
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.io.*;

public Aobj decode() throws IOException {
    try (AonReader reader = Aon.reader(new File("data.aon"))) {
        return reader.getObj();
    }

    //or Aon.read(new File("data.aon"))
}

public void encode(Aobj obj) throws IOException {
    Aon.writer(new File("data.aon")).value(obj).close();

    //or Aon.write(obj, new File("data.aon"))
}
```

The library also supports JSON encoding and decoding.

```java
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

Streaming IO is supported as well.  The following two methods produce the same result.

```java
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
Aon-JSON as well as many other JSON libs.  The benchmark uses JMH and
takes 10-15 minutes.  At the end of the benchmark are some Aon vs JSON
document size comparisions.

Example benchmark results:

```
Benchmark                                             Mode  Cnt     Score      Error  Units
AonBenchmark.DecodeLargeDoc.Aon                       avgt    4   975.111 ±  328.698  us/op
AonBenchmark.DecodeLargeDoc.AonJson                   avgt    4  2312.974 ±  251.520  us/op
AonBenchmark.DecodeLargeDoc.Flexjson                  avgt    4  8548.223 ± 1313.849  us/op
AonBenchmark.DecodeLargeDoc.Genson                    avgt    4  1615.309 ±   95.464  us/op
AonBenchmark.DecodeLargeDoc.Gson                      avgt    4  2651.733 ±  317.195  us/op
AonBenchmark.DecodeLargeDoc.Jackson                   avgt    4  2133.457 ± 1447.510  us/op
AonBenchmark.DecodeLargeDoc.JsonSimple                avgt    4  4547.263 ±  139.267  us/op
AonBenchmark.DecodeLargeDoc.THE_END_OF_GROUP________  avgt    4     0.001 ±    0.001  us/op
AonBenchmark.DecodeSmallDoc.Aon                       avgt    4     1.716 ±    0.157  us/op
AonBenchmark.DecodeSmallDoc.AonJson                   avgt    4     6.232 ±    0.592  us/op
AonBenchmark.DecodeSmallDoc.Flexjson                  avgt    4    21.293 ±   12.643  us/op
AonBenchmark.DecodeSmallDoc.Genson                    avgt    4     7.449 ±    1.322  us/op
AonBenchmark.DecodeSmallDoc.Gson                      avgt    4     6.496 ±    1.321  us/op
AonBenchmark.DecodeSmallDoc.Jackson                   avgt    4     5.309 ±    0.746  us/op
AonBenchmark.DecodeSmallDoc.JsonSimple                avgt    4    18.020 ±    1.273  us/op
AonBenchmark.DecodeSmallDoc.THE_END_OF_GROUP________  avgt    4     0.001 ±    0.001  us/op
AonBenchmark.EncodeLargeDoc.Aon                       avgt    4   755.315 ±   69.104  us/op
AonBenchmark.EncodeLargeDoc.AonJson                   avgt    4  1581.966 ±  208.914  us/op
AonBenchmark.EncodeLargeDoc.Flexjson                  avgt    4  5076.796 ± 1846.097  us/op
AonBenchmark.EncodeLargeDoc.Genson                    avgt    4  1756.630 ±  177.190  us/op
AonBenchmark.EncodeLargeDoc.Gson                      avgt    4  1835.124 ±  445.181  us/op
AonBenchmark.EncodeLargeDoc.Jackson                   avgt    4   974.625 ±   28.251  us/op
AonBenchmark.EncodeLargeDoc.JsonSimple                avgt    4  8505.397 ± 2905.306  us/op
AonBenchmark.EncodeLargeDoc.THE_END_OF_GROUP________  avgt    4     0.001 ±    0.001  us/op
AonBenchmark.EncodeSmallDoc.Aon                       avgt    4     1.175 ±    0.161  us/op
AonBenchmark.EncodeSmallDoc.AonJson                   avgt    4     2.673 ±    0.283  us/op
AonBenchmark.EncodeSmallDoc.Flexjson                  avgt    4    14.328 ±    6.559  us/op
AonBenchmark.EncodeSmallDoc.Genson                    avgt    4     3.361 ±    0.133  us/op
AonBenchmark.EncodeSmallDoc.Gson                      avgt    4    12.197 ±    9.608  us/op
AonBenchmark.EncodeSmallDoc.Jackson                   avgt    4     1.547 ±    0.118  us/op
AonBenchmark.EncodeSmallDoc.JsonSimple                avgt    4    17.370 ±    8.667  us/op
 AON small doc size: 183
JSON small doc size: 261
 AON large doc size: 111596
JSON large doc size: 159198
```

The benchmark is a Gradle task and can be run as follows:

```
gradlew benchmark
```

History
-------
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
