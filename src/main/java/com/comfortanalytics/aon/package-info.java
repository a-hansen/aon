/**
 * <p>
 * Create data structures with Alist and Aobj:
 * <p>
 * <pre>
 * import com.comfortanalytics.aon.*;
 *
 * public static void main(String[] args) {
 *     Aobj map = new Aobj()
 *             .put("boolean", true)
 *             .put("double", 100.1d)
 *             .put("int", 100)
 *             .put("long", 100l)
 *             .put("string", "abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
 *             .putNull("null");
 *     System.out.println("The int value in the map is " + map.getInt("int"));
 *     Alist list = new Alist()
 *             .add(true)
 *             .add(100.1d)
 *             .add(100)
 *             .add(100l)
 *             .add("abcdefghij\r\njklmnopqrs\u0000\u0001\u0002tuvwxyz\r\n")
 *             .addNull();
 *     System.out.println("The int value in the list is " + list.get(2));
 *     Alist complex = new Alist();
 *     complex.addList()
 *            .add(1)
 *            .add(2)
 *            .add(3);
 *     complex.addMap()
 *            .put("a", 1)
 *            .put("b", 2)
 *            .put("c", 3);
 * }
 * </pre>
 * <p>
 * Use implementations of Awriter and Areader for encoding / decoding.  See package
 * com.comfortanalytics.aon.io and com.comfortanalytics.aon.json.
 *
 * @author Aaron Hansen
 */
package com.comfortanalytics.aon;

