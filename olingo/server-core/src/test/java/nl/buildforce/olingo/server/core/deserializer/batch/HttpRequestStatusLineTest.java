/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException.MessageKeys;
import org.junit.Test;

public class HttpRequestStatusLineTest {

  private static final String HTTP_VERSION = "HTTP/1.1";
  private static final String SPACE = " ";
  private final String baseUri = "http://localhost/odata";
  private final String serviceResolutionUri = "";

  @Test
  public void absolute() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("http://localhost/odata/ESAllPrim?$top=2");
    assertEquals("$top=2", line.getRawQueryPath());
    assertEquals("/ESAllPrim", line.getRawODataPath());
    assertEquals(baseUri + "/ESAllPrim?$top=2", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void absoluteWithRelativePath() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("http://localhost/odata/../../ESAllPrim?$top=2");
    assertEquals("/../../ESAllPrim", line.getRawODataPath());
    assertEquals("$top=2", line.getRawQueryPath());
    assertEquals(baseUri + "/../../ESAllPrim?$top=2", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void absolutePath() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("/odata/ESAllPrim");
    assertEquals("/ESAllPrim", line.getRawODataPath());
    assertEquals(baseUri + "/ESAllPrim", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void relativeWithDots() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("../../ESAllPrim?$top=2");
    assertEquals("/../../ESAllPrim", line.getRawODataPath());
    assertEquals("$top=2", line.getRawQueryPath());
    assertEquals(baseUri + "/../../ESAllPrim?$top=2", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void relative() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("ESAllPrim?$top=2");
    assertEquals("$top=2", line.getRawQueryPath());
    assertEquals("/ESAllPrim", line.getRawODataPath());
    assertEquals(baseUri + "/ESAllPrim?$top=2", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void relativeMultipleSegments() throws BatchDeserializerException {
    HttpRequestStatusLine line = parse("ESKeyNav(1)/NavPropertyETTwoKeyNavOne/PropertyComp");
    assertEquals("", line.getRawQueryPath());
    assertEquals("/ESKeyNav(1)/NavPropertyETTwoKeyNavOne/PropertyComp", line.getRawODataPath());
    assertEquals(baseUri + "/ESKeyNav(1)/NavPropertyETTwoKeyNavOne/PropertyComp", line.getRawRequestUri());
    assertEquals(baseUri, line.getRawBaseUri());
    assertEquals(serviceResolutionUri, line.getRawServiceResolutionUri());
  }

  @Test
  public void otherBaseUri() {
    parseFail("http://otherhost/odata/ESAllPrim", MessageKeys.INVALID_BASE_URI);
  }

  @Test
  public void invalidRelative() {
    parseFail("/ESAllPrim", MessageKeys.INVALID_URI);
  }

  private HttpRequestStatusLine parse(String uri) throws BatchDeserializerException {
    Line statusline = new Line(HttpMethod.GET.name() + SPACE + uri + SPACE + HTTP_VERSION, 0);
    return new HttpRequestStatusLine(statusline, baseUri, serviceResolutionUri);
  }

  private void parseFail(String uri, MessageKeys messageKey) {
    try {
      parse(uri);
      fail("Expected exception");
    } catch (BatchDeserializerException e) {
      assertEquals(messageKey, e.getMessageKey());
    }
  }

}