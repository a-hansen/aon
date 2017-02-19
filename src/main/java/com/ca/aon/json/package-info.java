/**
 * The JSON binding for Aon.
 * <p>
 * <pre>
 * import com.ca.alog.*;
 * import com.ca.alog.json*;
 * 
 * public Amap deserialize() throws IOException {
 *     //It can detected zipped documents.
 *     return new JsonReader(new File("aon.zip")).getMap();
 * }
 * 
 * public void serialize(Amap map) throws IOException {
 *     //Zipping is easy.
 *     new JsonWriter(new File("aon.zip"), "aon.json")
 *             .value(map)
 *             .close();
 * }
 * </pre>
 *
 * @author Aaron Hansen
 */
package com.ca.aon.json;

