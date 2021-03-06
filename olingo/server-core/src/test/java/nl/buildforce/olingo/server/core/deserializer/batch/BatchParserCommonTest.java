/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import static com.google.common.net.HttpHeaders.ACCEPT;
import static com.google.common.net.HttpHeaders.ACCEPT_LANGUAGE;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.CONTENT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;


import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import org.junit.Assert;
import org.junit.Test;

public class BatchParserCommonTest {

  private static final String CRLF = "\r\n";
  private static final String MULTIPART_MIXED = "multipart/mixed";
  private static final String ACCEPTLANG_EN = ACCEPT_LANGUAGE + ":en-US,en;q=0.7,en-UK;q=0.9";

  @Test
  public void multipleHeaders() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Content-Id: 1" + CRLF,
        "Content-Id: 2" + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> contentIdHeaders = header.getHeaders(CONTENT_ID);
    assertNotNull(contentIdHeaders);
    assertEquals(2, contentIdHeaders.size());
    assertEquals("1", contentIdHeaders.get(0));
    assertEquals("2", contentIdHeaders.get(1));
  }

  @Test
  public void multipleHeadersSameValue() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Content-Id: 1" + CRLF,
        "Content-Id: 1" + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> contentIdHeaders = header.getHeaders(CONTENT_ID);
    assertNotNull(contentIdHeaders);
    assertEquals(1, contentIdHeaders.size());
    assertEquals("1", contentIdHeaders.get(0));
  }

  @Test
  public void headersSeparatedByComma() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Content-Id: 1" + CRLF,
        "Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11" + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> upgradeHeader = header.getHeaders("upgrade");
    assertNotNull(upgradeHeader);
    assertEquals(4, upgradeHeader.size());
    assertEquals("HTTP/2.0", upgradeHeader.get(0));
    assertEquals("SHTTP/1.3", upgradeHeader.get(1));
    assertEquals("IRC/6.9", upgradeHeader.get(2));
    assertEquals("RTA/x11", upgradeHeader.get(3));
  }

  @Test
  public void multipleAcceptHeaders() /*throws Exception*/ {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + CRLF,
        "Accept: text/plain;q=0.3" + CRLF,
        ACCEPTLANG_EN + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> acceptHeader = header.getHeaders(ACCEPT);
    assertNotNull(acceptHeader);
    assertEquals(4, acceptHeader.size());
  }

  @Test
  public void multipleAcceptHeadersSameValue() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Accept: application/atomsvc+xml;q=0.8, application/json;odata=verbose;q=0.5, */*;q=0.1" + CRLF,
        "Accept: application/atomsvc+xml;q=0.8" + CRLF,
        ACCEPTLANG_EN + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> acceptHeader = header.getHeaders(ACCEPT);
    assertNotNull(acceptHeader);
    assertEquals(3, acceptHeader.size());
  }

  @Test
  public void multipleAcceptLanguageHeaders() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        ACCEPTLANG_EN + CRLF,
        "Accept-Language: de-DE;q=0.3" + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> acceptLanguageHeader = header.getHeaders(ACCEPT_LANGUAGE);
    assertNotNull(acceptLanguageHeader);
    assertEquals(4, acceptLanguageHeader.size());
  }

  @Test
  public void multipleAcceptLanguageHeadersSameValue() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        ACCEPTLANG_EN + CRLF,
        "Accept-Language:en-US,en;q=0.7" + CRLF,
        "content-type: Application/http" + CRLF,
        "content-transfer-encoding: Binary" + CRLF));
    assertNotNull(header);

    List<String> acceptLanguageHeader = header.getHeaders(ACCEPT_LANGUAGE);
    assertNotNull(acceptLanguageHeader);
    assertEquals(3, acceptLanguageHeader.size());
  }

  @Test
  public void headersWithSpecialNames() {
    Header header = BatchParserCommon.consumeHeaders(toLineList(
        "Test0123456789: 42" + CRLF,
        "a_b: c/d" + CRLF,
        "!#$%&'*+-.^_`|~: weird" + CRLF));
    assertNotNull(header);
    assertTrue(header.exists("Test0123456789"));
    assertTrue(header.exists("a_b"));
    assertTrue(header.exists("!#$%&'*+-.^_`|~"));
    assertEquals("weird", header.getHeader("!#$%&'*+-.^_`|~"));
  }

  @Test
  public void headerWithWrongName() {
    Header header = BatchParserCommon.consumeHeaders(toLineList("a,b: c/d" + CRLF));
    assertNotNull(header);
    assertFalse(header.iterator().hasNext());
  }

  @Test
  public void boundaryParameter() throws Exception {
    final String boundary = "boundary";
    final String contentType = MULTIPART_MIXED + "; boundary=" + boundary + "  ";
    assertEquals(boundary, BatchParserCommon.getBoundary(contentType, 0));
  }

  @Test
  public void boundaryParameterWithQuotes() throws Exception {
    final String boundary = "batch_1.2+34:2j)0?";
    final String contentType = MULTIPART_MIXED + "; boundary=\"" + boundary + "\"";
    assertEquals(boundary, BatchParserCommon.getBoundary(contentType, 0));
  }

  @Test
  public void boundaryParameterWithSpaces() throws Exception {
    final String boundary = "        boundary";
    final String contentType = MULTIPART_MIXED + "; boundary=\"" + boundary + "\"  ";
    assertEquals(boundary, BatchParserCommon.getBoundary(contentType, 0));
  }

  @Test
  public void invalidContentType() {
    invalidBoundary("multipart;boundary=BOUNDARY", BatchDeserializerException.MessageKeys.INVALID_CONTENT_TYPE);
  }

  @Test
  public void contentTypeCharset() throws Exception {
    final String contentType = MULTIPART_MIXED + "; charset=UTF-8;boundary=" + BatchParserCommon.BOUNDARY;
    String boundary = BatchParserCommon.getBoundary(contentType, 0);
    assertEquals(BatchParserCommon.BOUNDARY, boundary);
  }

  @Test
  public void withoutBoundaryParameter() {
    invalidBoundary(MULTIPART_MIXED, BatchDeserializerException.MessageKeys.MISSING_BOUNDARY_DELIMITER);
  }

  @Test
  public void boundaryParameterWithoutQuote() {
    invalidBoundary(MULTIPART_MIXED + ";boundary=batch_1740-bb:84-2f7f",
        BatchDeserializerException.MessageKeys.INVALID_BOUNDARY);
  }

  @Test
  public void boundaryEmpty() {
    invalidBoundary(MULTIPART_MIXED + ";boundary=\"\"", BatchDeserializerException.MessageKeys.INVALID_BOUNDARY);
  }

  @Test
  public void boundarySpace() {
    invalidBoundary(MULTIPART_MIXED + ";boundary=\" \"", BatchDeserializerException.MessageKeys.INVALID_BOUNDARY);
  }

  @Test
  public void removeEndingCRLF() {
    String line = "Test" + CRLF;
    assertEquals("Test", BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeLastEndingCRLF() {
    String line = "Test" + CRLF + CRLF;
    assertEquals("Test" + CRLF, BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeEndingCRLFWithWS() {
    String line = "Test" + CRLF + "            ";
    assertEquals("Test", BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeEndingCRLFNothingToRemove() {
    String line = "Hallo" + CRLF + "Bla";
    assertEquals(line, BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeEndingCRLFAll() {
    String line = CRLF;
    assertEquals("", BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeEndingCRLFSpace() {
    String line = CRLF + "                      ";
    assertEquals("", BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeLastEndingCRLFWithWS() {
    String line = "Test            " + CRLF;
    assertEquals("Test            ", BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  @Test
  public void removeLastEndingCRLFWithWSLong() {
    String line = "Test            " + CRLF + "Test2    " + CRLF;
    assertEquals("Test            " + CRLF + "Test2    ",
        BatchParserCommon.removeEndingCRLF(new Line(line, 1)).toString());
  }

  private List<Line> toLineList(String... messageRaw) {
    List<Line> lineList = new ArrayList<Line>();
    int counter = 1;

    for (String currentLine : messageRaw) {
      lineList.add(new Line(currentLine, counter++));
    }

    return lineList;
  }

  private void invalidBoundary(String contentType, BatchDeserializerException.MessageKeys messageKey) {
    try {
      BatchParserCommon.getBoundary(contentType, 0);
      Assert.fail("Expected exception not thrown.");
    } catch (BatchDeserializerException e) {
      Assert.assertEquals(messageKey, e.getMessageKey());
    }
  }

}