Aon
===

* Version: 5.0.0
* JDK 1.6+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)

Overview
--------

Aon is a JSON-like encoding that is more compact, supports more data
types, and preserves the order of object members.

#### Compact
Borrows techniques from MsgPack and UBJSON to create a hybrid binary
encoding.

#### More Data Types
Big decimal, Big integer, binary, boolean, double, float, int, list,
long, null, object and string.

#### Ordered Objects
Order matters when displaying objects on user interfaces such as
property sheets.

Comparisons to Other Formats
----------------------------

Aon is like JSON, except:

* It is more compact.
* Supports more data types.
* Object member order is preserved.

Aon was influenced by [MsgPack](http://msgpack.org) compaction, except:

* It is streaming IO friendly, lists and objects do not enocde a size.
* Only has data types that can be supported by Java (no U64 or S32).
* Object member order is preserved.

Aon uses many [UBJSON](http://ubjson.org) concepts as well, except:

* It is more compact.
* Binary is a native data byte rather than a list of ints.
* Object member order is preserved.

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
{ i 0x04 name s i 0x03 aon i 0x0B born I 0x01 0x33 0xEE 0x7A i 0x04 cool T }
```

**Aon** (27 bytes)
```
{ 0xE4 name 0xE3 aon 0xEB born j 0x01 0x33 0xEE 0x82 0x7A cool T }
```

Format
------

Aon uses Big Endian byte order and is represented here using a
pseudo-BNF syntax.

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
An ordered collection of values surrounded by brackets.
```
<List> ::= "[" <Value>* "]"
```
* There can be 0 or more values in a list.

#### Boolean
```
<Boolean> ::= "T" | "F"
```
* T = true
* F = false

#### Double
```
<Double> ::= "D" byte[8]
```
* IEEE 754 floating-point "double format" bit layout (64 bits).

#### Float
```
<Float> ::= "D" byte[4]
```
* IEEE 754 floating-point "single format" bit layout (32 bits).

#### Null
```
<Null> ::= "Z"
```

#### Signed Integer
```
<Signed-Int> ::= <tiny-int> | <1-byte-int> | <2-byte-int> | <4-byte-int> | <8-byte-int>
<tiny-int>   ::= 0xC0
<1-byte-int> ::= "i" int8
<2-byte-int> ::= "I" int16
<4-byte-int> ::= "j" int32
<8-byte-int> ::= "J" int64
```
* The tiny int can be identified with the bitmask 0xC0.  The length
of the string is the signed lowest order 5 bits (byte & ~0xC0).  The
value range for a tiny int is -16 to 15.

#### String
```
<String> ::= <tiny-string> | <small-string> | <med-string> | <large-string>
<tiny-string>  ::= 0xE0 UTF8
<small-string> ::= "s" uint8 UTF8
<med-string>   ::= "S" uint16 UTF8
<large-string> ::= "t" int32 UTF8
```
* The tiny string can be identified with the bitmask 0xE0.  The length
of the string is the unsigned lowest order 5 bits (byte & ~0xE0).  The
max length of a tiny string is 31 bytes.
* The large string length must be a positive signed int

#### Unsigned Integers
```
<Unsigned-Int> ::= <uint5> | <uint8> | <uint16> | <uint32>
<uint5>  ::= 0x80
<uint8>  ::= "i" uint8
<uint16> ::= "I" uint16
<uint32> ::= "j" uint32
```
* The uint5 can be identified with the bitmask 0x80.  The length
of the string is the unsigned lowest order 5 bits (byte & ~0x80).  The
max value of a tiny uint is 31.

#### Binary (byte array)
```
<Binary> ::= <bin8> | <bin16> | <bin32>
<bin8>   ::= "b" uint8-length bytes
<bin16>  ::= "I" uint16-length bytes
<bin32>  ::= "j" int32-length bytes
```

#### Big Integer
```
<Big-Integer> ::= <bigint8> | <bigint16> | <bigint32>
<bigint>   ::= "n" uint8-length UTF8
<bigint16> ::= "N" uint16-length UTF8
<bigint32> ::= "o" int16-length UTF8
```
* The string format is the same as java.math.BigInteger string
constructor and toString method.
* "The String representation consists of an optional minus sign
followed by a sequence of one or more decimal digits. The String may
not contain any extraneous characters (whitespace, for example)."

#### Big Decimal
```
<Big-Decimal> ::= <decimal8> | <decimal16> | <decimal32>
<decimal8>  ::= "g" uint8-length UTF8
<decimal16> ::= "G" uint16-length UTF8
<decimal32> ::= "h" int16-length UTF8
```
* The string format is the same as java.math.BigDecimal string
constructor and toString method.
* "The string representation consists of an optional sign, '+'
( '\u002B') or '-' ('\u002D'), followed by a sequence of zero or more
decimal digits ("the integer"), optionally followed by a fraction,
optionally followed by an exponent. The fraction consists of a decimal
point followed by zero or more decimal digits. The string must contain
at least one digit in either the integer or the fraction. The number
formed by the sign, the integer and the fraction is referred to as the
significand. The exponent consists of the character 'e' ('\u0065') or
'E' ('\u0045') followed by one or more decimal digits."

MIME Type
---------

application/aon


Java Library
------------

This includes a Java library representing Aon values in memory as well
as encoding and decoding.  It can also encode and decode JSON.

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

Streaming io is supported as well.  The following two methods produce the same result.

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
