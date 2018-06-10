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
stream friendly Aon doesn't encode object or list lengths.

#### Compact
Borrows techniques from MsgPack and UBJSON to create a hybrid binary
encoding.

#### More Data Types
Big decimal, big integer, binary, boolean, double, float, list, long,
null, object, string and many flavors of signed and unsigned integers.

#### Ordered Objects
Order matters when displaying object members on user interfaces such
as property sheets.

Comparisons to Other Formats
----------------------------

Aon is like JSON, except:

* Aon is more compact.
* Aon has data types.
* Aon preserves object member order.

Aon was influenced by [MsgPack](http://msgpack.org) compaction, except:

* Aon is streaming IO friendly; lists and objects do not encode their
size.
* Aon only has data types that can be supported by Java (no UINT64 or
unsigned 32 bit lengths for datatypes that are backed by arrays).
* Aon preserves object member order.

Aon uses many [UBJSON](http://ubjson.org) concepts as well, except:

* Aon is more compact.
* Aon preserves object member order.

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

The Aon format is represented here using a pseudo-BNF syntax.

```
<Document> ::= <Object> | <List>
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
Booleans require only a single byte.
```
<Boolean> ::= "T" | "F"
```
* T = true
* F = false

#### Double
Requires 9 bytes, the letter 'D' followed by 8 bytes.
```
<Double> ::= "D" byte[8]
```
* IEEE 754 floating-point "double format" bit layout (64 bits).

#### Float
Requires 5 bytes, the letter 'd' followed by 4 bytes.
```
<Float> ::= "d" byte[4]
```
* IEEE 754 floating-point "single format" bit layout (32 bits).

#### Null
Requires a single byte, the letter 'Z'.
```
<Null> ::= "Z"
```

#### Signed Integer
Uses 1 to 5 bytes, depending on the value.
```
<Signed-Int> ::= <signed-int5> | <signed-int8> | <signed-int16> | <signed-int32> | <signed-int64>
<signed-int5>  ::= 0xC0
<signed-int8>  ::= "i" int8
<signed-int16> ::= "I" int16
<signed-int32> ::= "j" int32
<signed-int64> ::= "J" int64
```
* The tiny int can be identified with the bitmask 0xC0.  The value
is the 5 signed low order bits.  The value range for a tiny int
is -16 to 15.

#### String
Strings require a type indicator and a length in addition to the UTF8
encoded string.  If the string is small enough, a single byte can be
used as the type indicator as well as its length.  Larger strings
require additional bytes for the length.
```
<String> ::= <str5> | <str8> | <str16> | <str32>
<str5>  ::= 0xE0 UTF8
<str8>  ::= "s" uint8 UTF8
<str16> ::= "S" uint16 UTF8
<str32> ::= "t" int32 UTF8
```
* The str5 can be identified with the bitmask 0xE0.  The length of the
string is the 5 unsigned low order bits.  The max length of a str5 is
31 bytes.
* The str32 length must be a positive signed int.

#### Unsigned Integers
Uses 1 to 5 bytes, depending on the value.
```
<Unsigned-Int> ::= <unsigned-int5> | <unsigned-int8> | <unsigned-int16> | <unsigned-int32>
<unsigned-int5>  ::= 0x80
<unsigned-int8>  ::= "i" uint8
<unsigned-int16> ::= "I" uint16
<unsigned-int32> ::= "j" uint32
```
* The uint5 can be identified with the bitmask 0x80.  The value is the
5 unsigned low order bits.  The max value of a tiny uint is 31.

#### Binary (byte array)
Requires 2 to 5 bytes in addition to the byte array.
```
<Binary> ::= <bin8> | <bin16> | <bin32>
<bin8>   ::= "b" uint8-length bytes
<bin16>  ::= "I" uint16-length bytes
<bin32>  ::= "j" int32-length bytes
```
* The bin32 length must be a positive signed int.

#### Big Integer
An integer so large has to be encoded as a string.
```
<Big-Integer> ::= <bigint8> | <bigint16> | <bigint32>
<bigint>   ::= "n" uint8-length UTF8
<bigint16> ::= "N" uint16-length UTF8
<bigint32> ::= "o" int32-length UTF8
```
* The bigint32 length must be a positive signed int.
* "The String representation consists of an optional minus sign
followed by a sequence of one or more decimal digits. The String may
not contain any extraneous characters (whitespace, for example)."

#### Big Decimal
An decimal so large it has to be encoded as a string.
```
<Big-Decimal> ::= <decimal8> | <decimal16> | <decimal32>
<decimal8>  ::= "g" uint8-length UTF8
<decimal16> ::= "G" uint16-length UTF8
<decimal32> ::= "h" int32-length UTF8
```
* The decimal32 length must be a positive signed int.
* "The string representation consists of an optional sign, '+'
( '\u002B') or '-' ('\u002D'), followed by a sequence of zero or more
decimal digits ("the integer"), optionally followed by a fraction,
optionally followed by an exponent. The fraction consists of a decimal
point followed by zero or more decimal digits. The string must contain
at least one digit in either the integer or the fraction. The number
formed by the sign, the integer and the fraction is referred to as the
significand. The exponent consists of the character 'e' ('\u0065') or
'E' ('\u0045') followed by one or more decimal digits."

Endianness
----------
Big endian

Java Library
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
    System.out.println("The int value in the list is " + list.get(2));
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
    try (AonReader reader = new AonReader(new File("data.aon"))) {
        return reader.getObj();
    }
}

public void encode(Aobj map) throws IOException {
    new AonWriter(new File("data.aon")).value(map).close();
}
```

The library also supports JSON encoding and decoding.

```java
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.json.*;

public Aobj decode() throws IOException {
    try (JsonReader reader = new JsonReader(new File("data.json"))) {
        return reader.getObj();
    }
}

public void encode(Aobj map) throws IOException {
    new JsonWriter(new File("data.json")).value(map).close();
}
```

Streaming IO is supported as well.  The following two methods produce the same result.

```java
import com.comfortanalytics.aon.*;

public void notStreaming(Awriter out) {
    out.value(new Aobj().put("a",1).put("b",2).put("c",3));
}

public void streaming(Awriter out) {
    out.beginObj()
            .key("a").value(1)
            .key("b").value(3)
            .key("c").value(3)
            .endObj();
}
```

Benchmark
---------

There is a benchmark that compares native Aon encoding with Aon-JSON
as well as many other JSON libs.  The benchmark uses JMH and takes
10-15 minutes.  At the end of the benchmark are some Aon vs JSON
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
  - Added Jsoniter to the benchmark, wow fast!
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
