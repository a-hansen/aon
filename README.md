Aon
===

* Version: 5.0.0
* JDK 1.6+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)

Rationale
---------

A JSON-like encoding that is more compact, supports more data types,
preserves the order of object members and is stream friendly.

#### Compact
Borrows techniques from MsgPack and UBJSON to create a hybrid binary
encoding.

#### More Data Types
Byte arrays, floats, doubles, big integers and big decimals are all
native data types.

#### Ordered Objects
When displaying objects on user interfaces such as property sheets,
order matters.

#### Streaming IO
Prepending lengths into lists and objects prevents streaming IO.  Take
database queries for example.  If you want to encode the number of
result rows, you either need to execute a count query first (2 queries)
or you have to load all result rows in memory.

Comparisons
-----------

Aon is (another object notation) like JSON, except:

* It is more compact.
* Supports more data types.
* Object member order is preserved.

Aon was influenced by [MsgPack](http://msgpack.org) compaction, except:

* It is stream friendly.
* Only has data types that can be supported by Java (no U64 or S32).
* Object member order is preserved.

Aon uses many [UBJSON](http://ubjson.org) concepts as well, except:

* It is more compact.
* Object member order is preserved.

**JSON** (49 bytes)
```
{"name":"aon","bestDayEver":19690505,"cool":true}
```

**MsgPack** ( bytes)
```
0x83 0xA7 name 0xC3 aon 0xA6 bestDayEver 0x0000 0x000000 0x00 cool 0x00
```

**UBJSON** (39 bytes)
```
{ i 0x04 name s i 0x03 aon i 0x0B bestDayEver I 0x01 0x2C 0x74 0x09 i 0x04 cool T }
```

**Aon** (35 bytes)
```
{ 0xE4 name 0xE3 aon 0xEB bestDayEver j 0x01 0x2C 0x74 0x09 0xDO 0xE4 cool T }
```

Format
------

Aon uses Big Endian byte order.

The Aon format is represented here using a pseudo-BNF syntax.

```
aon ::= <Object> | <List>
<Value> :== <Object> | <List> | <Boolean> | <Double> | <Float> | <Null> |
         <Signed-Integer> | <String> | <Unsigned-Integer> | <Binary> |
         <Big-Integer> | <Big-Decimal>
```

#### Object
An ordered collection of key value pairs surrounded by curly braces.
```
<Object> ::= "{" <Key-Value-Pair>* "}"
<Key-Value-Pair> ::= <String> <Value>
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
* IEEE 754 floating-point "double format" bit layout (Java Double).

#### Float
```
<Float> ::= "D" byte[4]
```
* IEEE 754 floating-point "single format" bit layout (Java Float).

#### Null
```
<Null> ::= "Z"
```

#### Signed Integer
```
<Signed-Integer> ::= <tiny-int> | <1-byte-int> | <2-byte-int> | <4-byte-int> | <8-byte-int>
<tiny-int>   ::= 0xC0
<1-byte-int> ::= "i" int8
<2-byte-int> ::= "I" int16
<4-byte-int> ::= "j" int32
<8-byte-int> ::= "J" int64
```
* The tiny int can be identified with the bitmask 0xC0.  The length
of the string is the low order signed 5 bits (byte & ~0xC0).  The
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
of the string is the lowest order unsigned 5 bits (byte & ~0xE0).  The
max length of a tiny string is 31 bytes.
* The large string length must be a positive signed int
* All strings must be UT8 encoded

#### Unsigned Integers
```
<Unsigned-Integer> ::= <tiny-uint> | <1-byte-uint> | <2-byte-uint> | <4-byte-uint>
<tiny-uint>   ::= 0x80
<1-byte-uint> ::= "i" uint8
<2-byte-uint> ::= "I" uint16
<4-byte-uint> ::= "j" uint32
```
* The tiny uint can be identified with the bitmask 0x80.  The length
of the string is the lowest order unsigned 5 bits (byte & ~0x80).  The
max value of a tiny uint is 31.

#### Binary (byte array)
```
<Binary> ::= <1-byte-bin> | <2-byte-bin> | <4-byte-bin>
<1-byte-bin> ::= "b" uint8-length bytes
<2-byte-bin> ::= "I" uint16-length bytes
<4-byte-bin> ::= "j" int32-length bytes
```

#### Big Integer
```
<Big-Integer> ::= <small-bigint> | <med-bigint> | <large-bigint>
<small-bigint> ::= "n" uint8-length UTF8
<med-string>   ::= "N" uint16-length UTF8
<large-string> ::= "o" int16-length UTF8
```
* The string format is the same as java.math.BigInteger.toString().
* "The String representation consists of an optional minus sign
followed by a sequence of one or more decimal digits. The String may
not contain any extraneous characters (whitespace, for example)."

#### Big Decimal
```
<Big-Decimal> ::= <small-decimal> | <med-decimal> | <large-decimal>
<small-decimal> ::= "g" uint8-length UTF8
<med-decimal>   ::= "G" uint16-length UTF8
<large-decimal> ::= "h" int16-length UTF8
```
* The string format is the same as java.math.BigDecimal.toString().
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
            .put("boolean", true)
            .put("double", 100.1d)
            .put("int", 100)
            .put("long", 100l)
            .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
            .putNull("null");
    System.out.println("The int value in the obj is " + map.getInt("int"));
    Alist list = new Alist()
            .add(true)
            .add(100.1d)
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
    out.beginObj().key("a").value(1).key("b").value(2).key("c").value(3).endObj();
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
