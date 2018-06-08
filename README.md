Aon
===

* Version: 5.0.0
* JDK 1.6+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)


Objectives
----------

Aon is another object notation like JSON, except:
* It is binary.
* Supports more data types.
* Object member order is preserved.

Aon also borrows heavily from [MsgPack](http://msgpack.org) and
[UBJSON](http://ubjson.org).  MsgPack is great except it is not
stream friendly, it requires array and object sizes up front.  UBJSON
is just plain clever so Aon borrows it's readability aspects.

Comparisons
-----------

**JSON** (27 bytes)
```
{ "compact" : true, "schema" : 0 }
```

**MsgPack** (18 bytes)
```
0x82 0xA7 compact 0xC3 0xA6 schema 0x00
```

**Aon** (22 bytes)
```
{ s 0x07 compact T s 0x06 schema i 0x00 }
```

Encoding Rules
--------------

#### Object / Map
An ordered collection of key value pairs surrounded by curly braces.
* { &lt;&lt;string> &lt;value>>... }

#### Array / List
An ordered collection of values surrounded by brackets.
* [ &lt;value>... ]

#### Values
A character possibily followed by length and/or data.
* &lt;Character> [len] [data]

Data Types
----------

Array / List
* [ = Begin
* ] = End

Object / Map
* { = Begin
* } = End

Boolean
* T = true
* F = false

Double
* D <8 byte value> = IEEE 754 floating-point "double format" bit layout (Java Double).

Float
* F <4 byte value> = IEEE 754 floating-point "single format" bit layout (Java Float).

Signed Integers
* i <1 byte value>
* I <2 byte value>
* j <4 byte value>
* J <8 byte value>

String
* s <1 byte len> <UTF-8 text> = The length is an unsigned byte (max 255)
* S <2 byte len> <UTF-8 text> = The length is an unsigned 2 byte int (max 65535)
* r <4 byte len> <UTF-8 text> = The length is *signed* 4 byte int (max 268435455)

Unsigned Integers
* u <1 byte>
* U <2 byte>
* v <4 byte>

Binary (byte arrays)
* b <1 byte len> <bytes> = The length is an unsigned byte (max 255)
* B <2 byte len> <bytes> = The length is an unsigned 2 byte int (max 65535)
* c <4 byte len> <bytes> = The length is signed 4 byte int (max 268435455)

Big Integer
* n <1 byte len> <text> = The length is an unsigned byte (max 255)
* N <2 byte len> <text> = The length is an unsigned 2 byte int (max 65535)
* o <4 byte len> <text> = The length is signed 4 byte int (max 268435455)

Big Decimal
* g <1 byte len> <text> = The length is an unsigned byte (max 255)
* G <2 byte len> <text> = The length is an unsigned 2 byte int (max 65535)
* h <4 byte len> <text> = The length is signed 4 byte int (max 268435455)

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

Create data structures with Alist and Amap.

```java
import com.comfortanalytics.aon.*;

public static void main(String[] args) {
    Amap map = new Amap()
            .put("boolean", true)
            .put("double", 100.1d)
            .put("int", 100)
            .put("long", 100l)
            .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
            .putNull("null");
    System.out.println("The int value in the map is " + map.getInt("int"));
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
    complex.addMap()
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

JSON encoding and decoding is straightforward.

```java
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.json.*;

public Amap decode() throws IOException {
    try (JsonReader reader = new JsonReader(new File("aon.zip"))) {
        return reader.getMap();
    }
}

public void encode(Amap map) throws IOException {
    new JsonWriter(new File("aon.zip"), "aon.json")
            .value(map)
            .close();
}
```

Streaming io is supported as well.  The following two methods produce the same result.

```java
import com.comfortanalytics.aon.*;

public void first(Awriter out) {
    out.value(new Amap().put("a",1).put("b",2).put("c",3));
}

public void second(Awriter out) {
    out.newMap().key("a").value(1).key("b").value(2).key("c").value(3).endMap();
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
