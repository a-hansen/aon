Aon
===

* Version: 3.1.0
* JDK 1.5+
* [ISC License](https://en.wikipedia.org/wiki/ISC_license)
* [Javadoc](https://a-hansen.github.io/aon/)


Overview
--------

A JSON compatible data model and parser/generator. This is an alternative to JSONObject 
with the following goals:

* Preserve key insertion order.
* Support additional encodings besides JSON (future).
* Support very large documents.
* Everything is index accessible so the structure can be traversed without object 
creation.

Other features:

* Small, simple and no dependencies.
* Streaming parser/generator.
* [Extremely permissive license](https://en.wikipedia.org/wiki/ISC_license).

Performance
-----------

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

Create primitives with static make methods on Aobj.

```java
import com.comfortanalytics.aon.*;

public static void main(String[] args) {
    Aobj aBool = Aobj.make(true);
    Aobj aDbl = Aobj.make(1d);
    Aobj anInt = Aobj.make(1);
    Aobj aLong = Aobj.make(1l);
    Aobj aStr = Aobj.make("1");
}
```

JSON encoding and decoding is straightforward.

```java
import com.comfortanalytics.aon.*;
import com.comfortanalytics.aon.json.*;

public Amap decode() throws IOException {
    //It can auto-detect zipped documents.
    try (JsonReader reader = new JsonReader(new File("aon.zip"))) {
        return reader.getMap();
    }
}

public void encode(Amap map) throws IOException {
    //If your document is large, zip it.
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
