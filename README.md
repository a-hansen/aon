Aon
===

* Version: 5.0.0
* JDK 1.6+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)

Rationale
---------

A JSON-like encoding that is more compact, supports more data bytes,
preserves the order of object members and supports streaming IO.

#### Compact
Uses techniques of MsgPack and UBJSON to create a hybrid binary
encoding.

#### Ordered Objects
When displaying objects on user interfaces such as property sheets,
order matters.

#### Streaming IO
Prepending lengths into arrays and objects prevents streaming IO.  Use
a database query as an example.  If you want to encode the number of
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
* Does not have data types unsupportable by Java (U64, S32).
* Object member order is preserved.

Aon uses many [UBJSON](http://ubjson.org) concepts, except:

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

**UBJSON** (37 bytes)
```
{ i 0x04 name s i 0x03 aon i 0x0b bestDayEver I 0x012C 0x7409 i 0x04 cool T }
```

**Aon** (32 bytes)
```
{ 0xD004 name 0xD003 aon 0xD00B bestDayEver I 0x012C 0x7409 0xDO04 cool T }
```

Encoding Rules
--------------

#### Object / Map

#### Array / List
* [ &lt;value>... ]

#### Values
A character possibily followed by length and/or data.
* &lt;Character> [len] [data]

Data Types
----------

**Object**
An ordered collection of key value pairs surrounded by curly braces.
* { = Begin
* } = End

**List** (Array)
An ordered collection of values surrounded by brackets.
* [ = Begin
* ] = End

**Boolean**
* T = true
* F = false

**Double**
IEEE 754 floating-point "double format" bit layout (Java Double).
* D <8 byte value> = IEEE 754 floating-point "double format" bit layout (Java Double).

**Float**
IEEE 754 floating-point "single format" bit layout (Java Float).
* F <4 byte value> = IEEE 754 floating-point "single format" bit layout (Java Float).

Signed Integers
* i <1 byte value>
* I <2 byte value>
* j <4 byte value>
* J <8 byte value>

String
* s <1 byte len> <UTF-8 text> = Length is an unsigned byte (max 255)
* S <2 byte len> <UTF-8 text> = Length is an unsigned 2 byte int (max 65535)
* r <4 byte len> <UTF-8 text> = Length is *signed* 4 byte int (max 268435455)

Unsigned Integers
* u <1 byte>
* U <2 byte>
* v <4 byte>

Binary (byte arrays)
* b <1 byte len> <bytes> = Length is an unsigned byte (max 255)
* B <2 byte len> <bytes> = Length is an unsigned 2 byte int (max 65535)
* c <4 byte len> <bytes> = Length is signed 4 byte int (max 268435455)

Big Integer
* n <1 byte len> <text> = Length is an unsigned byte (max 255)
* N <2 byte len> <text> = Length is an unsigned 2 byte int (max 65535)
* o <4 byte len> <text> = Length is signed 4 byte int (max 268435455)

Big Decimal
* g <1 byte len> <text> = Length is an unsigned byte (max 255)
* G <2 byte len> <text> = Length is an unsigned 2 byte int (max 65535)
* h <4 byte len> <text> = Length is signed 4 byte int (max 268435455)

Endianness
----------

Numeric values must be encoded using [Big-Endian](https://en.wikipedia.org/wiki/Endianness) byte order.

MIME Type
---------

application/aon


Java Library
------------

This includes a Java library representing Aon values in memory as well
as encoding and decoding.  It can also encode and decode JSON.

This has a pretty fast JSON encoder/decoder.  With large documents it's slower than Boon,
 Jackson, and Jasoniter, but with small documents it's faster than Boon.  In either case 
 it's faster than Flexjson, Genson, Gson and JSON-Simple.  Testing includes a benchmark 
 for comparing all of those.

Run the benchmark with the gradle wrapper:

```
gradlew benchmark
```

Don't run the benchmark task from within your IDE, it'll probably double print the 
output.  Just run all tests, or AonBenchmark specifically.

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
    System.out.println("The value by index is faster: " + map.getInt(2));
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

Create primitives with static valueOf methods in the corresponding data types.

```java
import com.comfortanalytics.aon.*;

public static void main(String[] args) {
    Abool aBool = Abool.valueOf(true);
    Adouble aDbl = Adouble.valueOf(1d);
    Afloat aFlt = Afloat.valueOf(1f);
    Aint anInt = Aint.valueOf(1);
    Along aLong = Along.valueOf(1l);
    Astr aStr = Astr.valueOf("hello world");
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

JSON encoding and decoding is straightforward.

```java
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.json.*;

public Aobj decode() throws IOException {
    try (JsonReader reader = new JsonReader(new File("data.json"))) {
        return reader.getObj();
    }
}

public void encode(Aobj map) throws IOException {
    new JsonWriter(new File("aon.zip"), "aon.json").value(map).close();
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
