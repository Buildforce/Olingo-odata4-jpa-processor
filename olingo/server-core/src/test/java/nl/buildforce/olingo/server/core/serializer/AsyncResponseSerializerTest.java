/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import nl.buildforce.olingo.server.api.ODataResponse;
import org.apache.commons.io.IOUtils;
import nl.buildforce.olingo.commons.api.format.ContentType;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import org.junit.Test;

public class AsyncResponseSerializerTest {
  private static final String CRLF = "\r\n";

  @Test
  public void simpleResponse() throws Exception {
    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    response.setHeader(CONTENT_LENGTH, String.valueOf(200));

    response.setContent(IOUtils.toInputStream("Wälter Winter" + CRLF, StandardCharsets.UTF_8));

    AsyncResponseSerializer serializer = new AsyncResponseSerializer();
    InputStream in = serializer.serialize(response);
    String result = IOUtils.toString(in, StandardCharsets.UTF_8);
    assertEquals("HTTP/1.1 200 OK" + CRLF
        + "Content-Type: application/json" + CRLF
        + "Content-Length: 200" + CRLF + CRLF
        + "Wälter Winter" + CRLF, result);
  }

  @Test
  public void biggerResponse() throws Exception {
    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.ACCEPTED.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    response.setHeader(CONTENT_LENGTH, String.valueOf(0));

    String testData = testData(20000);
    response.setContent(IOUtils.toInputStream(testData, StandardCharsets.UTF_8));

    AsyncResponseSerializer serializer = new AsyncResponseSerializer();
    InputStream in = serializer.serialize(response);
    String result = IOUtils.toString(in, StandardCharsets.UTF_8);
    assertEquals("HTTP/1.1 202 Accepted" + CRLF
        + "Content-Type: application/json" + CRLF
        + "Content-Length: 0" + CRLF + CRLF
        + testData, result);
  }

  private String testData(int amount) {
    StringBuilder result = new StringBuilder();
    Random r = new Random();
    for (int i = 0; i < amount; i++) {
      result.append((char) (r.nextInt(26) + 'a'));
    }

    return result.toString();
  }

}