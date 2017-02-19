Alog
====

* Date: Feb 19, 2017
* Version: 1.0.0
* Author: Aaron hansen


Overview
--------

A JSON compatible data model with some key design goals:

* Preserve key order.
* Support additional encodings besides JSON.
* Support very large documents.
* Everything is index accessible so the structure can be traversed without object 
creation.

This has a fast JSON encoder/decoder, but because key order preservation, it'll 
never be the fastest.  It's slower than Jackson, on-par with Genson, and faster than 
Gson and Flex-json.  The tests include a benchmark for comparing all of those.

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

JSON serialization is simple.

```java
import com.ca.alog.*;
import com.ca.alog.json*;

public Amap deserialize() throws IOException {
    //Notice it can detected zipped documents.
    return new JsonReader(new File("aon.zip")).getMap();
}

public void serialize(Amap map) throws IOException {
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
_1.0.0 - 2017-2-19_
  - Hello World.
