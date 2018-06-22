/**
 * The JSON binding for Aon.
 * <p>
 * <pre>
 * import com.comfortanalytics.aon.*;
 * import com.comfortanalytics.aon.json*;
 *
 * public Aobj decode() throws IOException {
 *     //It can detected zipped documents.
 *     return new JsonReader(new File("aon.json")).getObj();
 * }
 *
 * public void encode(Aobj obj) throws IOException {
 *     new JsonWriter(new File("aon.json"))
 *             .value(obj)
 *             .close();
 * }
 * </pre>
 *
 * @author Aaron Hansen
 */
package com.comfortanalytics.aon.json;

