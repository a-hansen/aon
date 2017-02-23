Aon
===

* Date: Feb 23, 2017
* Version: 2.0.0
* [Javadocs](https://a-hansen.github.io/aon/)


Overview
--------

A JSON compatible data model and parser/generator.  It is not for Java object 
serialization.

Key design goals:

* Preserve key order.
* Support additional encodings besides JSON.
* Support very large documents.
* Everything is index accessible so the structure can be traversed without object 
creation.

Other features:

* Small and simple.
* Streaming parser/generator.
* Built-in support for zipped documents.
* Extremely permissive [license](https://en.wikipedia.org/wiki/ISC_license).

This has a pretty fast JSON encoder/decoder.  It's slower than Boon and Jackson, 
on-par with Genson, and faster than Gson, Flexjson and JSON-Simple.  The tests 
include a benchmark for comparing all of those.

Requirements
------------

Java 1.7 or higher is required.

Usage
-----

Create data structures with Alist and Amap.

```java
import com.ca.aon.*;

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

Create primitives with static make methods on Aobj.

```java
import com.ca.aon.*;

public static void main(String[] args) {
    Aobj aBool = Aobj.make(true);
    Aobj aDbl = Aobj.make(1d);
    Aobj anInt = Aobj.make(1);
    Aobj aLong = Aobj.make(1l);
    Aobj aStr = Aobj.make("1");
}
```

JSON encoding and decoding is pretty simple.

```java
import com.ca.alog.*;
import com.ca.alog.json.*;

public Amap decode() throws IOException {
    //Notice it can auto-detect zipped documents.
    JsonReader reader = new JsonReader(new File("aon.zip"));
    Amap map = reader.getMap();
    reader.close();
    return map;
}

public void encode(Amap map) throws IOException {
    //Zipping is easy.
    new JsonWriter(new File("aon.zip"), "aon.json")
            .value(map)
            .close();
}
```

Streaming io is supported as well.  The following two methods produce the same result.

```java
import com.ca.alog.*;

public void first(Awriter out) {
    out.value(new Amap().put("a",1).put("b",2).put("c",3));
}

public void second(Awriter out) {
    out.newMap().key("a").value(1).key("b").value(2).key("c").value(3).endMap();
}
```

History
-------
_2.0.0 - 2017-2-23_
  - Package change.

_1.1.0 - 2017-2-21_
  - Made JsonWriter Appendable.
  - Added parenting to groups.
  - Added JSON-Simple to the benchmark.
  - Changed how benchmark results are printed.
  
_1.0.1 - 2017-2-20_
  - Minor performance improvement in JsonWriter.
  
_1.0.0 - 2017-2-19_
  - Hello World.
