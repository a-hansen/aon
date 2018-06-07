/**
 * Aon format encoder and decoder.
 * <p>
 * <pre>
 * import com.comfortanalytics.aon.*;
 * import com.comfortanalytics.aon.io*;
 *
 * public Aobj decode() throws IOException {
 *     return new AonReader(new File("file.aon")).getObj();
 * }
 *
 * public void encode(Aobj map) throws IOException {
 *     new AonWriter(new File("file.aon")).value(map).close();
 * }
 * </pre>
 *
 * @author Aaron Hansen
 */
package com.comfortanalytics.aon.io;

