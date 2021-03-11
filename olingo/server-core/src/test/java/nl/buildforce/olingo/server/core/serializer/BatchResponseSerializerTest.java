/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityIterator;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;
import nl.buildforce.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;
import nl.buildforce.olingo.server.core.deserializer.batch.BatchLineReader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.CONTENT_ID;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class BatchResponseSerializerTest {
  private static final String CRLF = "\r\n";
  private static final String BOUNDARY = "batch_" + UUID.randomUUID().toString();

  @Test
  public void batchResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();
    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    response.setContent(IOUtils.toInputStream("Walter Winter" + CRLF, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(24, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain" + CRLF, body.get(line++));
    assertEquals("Content-Length: 15" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("Walter Winter" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void batchResponseUmlautsUtf8() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    response.setContent(IOUtils.toInputStream("{\"name\":\"Wälter Winter\"}" + CRLF, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(24, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: application/json" + CRLF, body.get(line++));
    assertEquals("Content-Length: 27" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("{\"name\":\"Wälter Winter\"}" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void batchResponseUmlautsUtf8BodyIsoHeader() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, new ContentType(ContentType.TEXT_PLAIN, ContentType.PARAMETER_CHARSET, StandardCharsets.UTF_8.name()).toString());
    response.setContent(IOUtils.toInputStream("Wälter Winter" + CRLF, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    changeSetResponse.setHeader("Custom-Header", new String("äüö".getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.ISO_8859_1));
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(25, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain;charset=utf-8" + CRLF, body.get(line++));
    assertEquals("Content-Length: 16" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("Wälter Winter" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Custom-Header: äüö" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void batchResponseUmlautsUtf8BodyAndHeader() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    response.setContent(IOUtils.toInputStream("{\"name\":\"Wälter Winter\"}" + CRLF, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    changeSetResponse.setHeader("Custom-Header", "äüö");
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    assertEquals(25, body.size());
    // TODO: check: with latest change in BatchResponseSerializer is not possible
    // to set header values with UTF-8 (only iso-8859-1)
    //    assertEquals("Custom-Header: Ã¤Ã¼Ã¶" + CRLF, body.get(19));
    assertEquals("Custom-Header: äüö" + CRLF, body.get(19));
  }

  @Test
  public void batchResponseUmlautsIso() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, new ContentType(ContentType.TEXT_PLAIN, ContentType.PARAMETER_CHARSET, StandardCharsets.ISO_8859_1.name()).toString());
    response.setContent(new ByteArrayInputStream(("Wälter Winter" + CRLF).getBytes(StandardCharsets.ISO_8859_1)));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(24, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain;charset=iso-8859-1" + CRLF, body.get(line++));
    assertEquals("Content-Length: 15" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("Wälter Winter" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void batchResponseWithEndingCRLF() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    response.setContent(IOUtils.toInputStream("Walter Winter",StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    ODataResponse changeSetResponse = new ODataResponse();
    changeSetResponse.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    changeSetResponse.setHeader(CONTENT_ID, "1");
    parts.add(new ODataResponsePart(Collections.singletonList(changeSetResponse), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);
    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(23, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain" + CRLF, body.get(line++));
    assertEquals("Content-Length: 13" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("Walter Winter" + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void response() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    response.setContent(IOUtils.toInputStream("Walter Winter", StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(10, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain" + CRLF, body.get(line++));
    assertEquals("Content-Length: 13" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("Walter Winter" + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void bigResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    String bigData = generateData(10000);
    response.setContent(IOUtils.toInputStream(bigData, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(Collections.singletonList(response), false));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);
    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(10, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain" + CRLF, body.get(line++));
    assertEquals("Content-Length: 10000" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(bigData + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void changeSetResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();

    ODataResponse response = new ODataResponse();
    response.setHeader(CONTENT_ID, "1");
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    parts.add(new ODataResponsePart(Collections.singletonList(response), true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);

    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(14, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 0" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

  @Test
  public void binaryResponse() throws Exception {
    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.APPLICATION_OCTET_STREAM.toString());
    // binary content, not a valid UTF-8 representation of a string
    byte[] content = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
    for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
      content[i - Byte.MIN_VALUE] = (byte) i;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream(Byte.MAX_VALUE - Byte.MIN_VALUE + 1);
    out.write(content);
    response.setContent(new ByteArrayInputStream(out.toByteArray()));

    InputStream batchResponse = new BatchResponseSerializer().serialize(
        Collections.singletonList(new ODataResponsePart(Collections.singletonList(response), false)),
        BOUNDARY);
    assertNotNull(batchResponse);

    String beforeExpected = "--" + BOUNDARY + CRLF
        + "Content-Type: application/http" + CRLF
        + "Content-Transfer-Encoding: binary" + CRLF
        + CRLF
        + "HTTP/1.1 200 OK" + CRLF
        + "Content-Type: application/octet-stream" + CRLF
        + "Content-Length: 256" + CRLF
        + CRLF;
    byte[] beforeContent = new byte[beforeExpected.length()];
    batchResponse.read(beforeContent, 0, beforeExpected.length());
    assertArrayEquals(beforeExpected.getBytes(StandardCharsets.ISO_8859_1), beforeContent);

    byte[] binaryContent = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
    batchResponse.read(binaryContent, 0, binaryContent.length);
    assertArrayEquals(content, binaryContent);

    String afterExpected = CRLF
        + "--" + BOUNDARY + "--" + CRLF;
    byte[] afterContent = new byte[afterExpected.length()];
    batchResponse.read(afterContent, 0, afterExpected.length());
    assertArrayEquals(afterExpected.getBytes(StandardCharsets.ISO_8859_1), afterContent);

    assertEquals(-1, batchResponse.read());
  }

  /**
   * Generates a string with given length containing random upper case characters ([A-Z]).
   * @param len length of the generated string
   * @return random upper case characters ([A-Z])
   */
  public static String generateData(int len) {
    Random random = new Random();
    StringBuilder b = new StringBuilder(len);
    for (int j = 0; j < len; j++) {
      char c = (char) ('A' + random.nextInt('Z' - 'A' + 1));
      b.append(c);
    }
    return b.toString();
  }
  
  @Test
  public void testODataContentResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();
    ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);
    EdmEntityType edmEntityType = mock(EdmEntityType.class);
    EntityIterator entityCollection = new EntityIterator() {
      
      @Override
      public Entity next() {
        return null;
      }
      
      @Override
      public boolean hasNext() {
        return false;
      }
    };  

    SerializerStreamResult serializerResult = OData.newInstance().
        createSerializer(ContentType.APPLICATION_JSON).entityCollectionStreamed(
        serviceMetadata,
        edmEntityType,
        entityCollection,
        EntityCollectionSerializerOptions.with().contextURL
        (ContextURL.with().oDataPath("http://host/svc").build()).build());
    ODataResponse response = new ODataResponse();
    response.setODataContent(serializerResult.getODataContent());
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    parts.add(new ODataResponsePart(response, false));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);

    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(9, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 47" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("{\"@odata.context\":\"../../$metadata\",\"value\":[]}" + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }
  
  @Test
  public void changeSetODataContentResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();
    ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);
    EdmEntityType edmEntityType = mock(EdmEntityType.class);
    EntityIterator entityCollection = new EntityIterator() {
      
      @Override
      public Entity next() {
        return null;
      }
      
      @Override
      public boolean hasNext() {
        return false;
      }
    };  

    SerializerStreamResult serializerResult = OData.newInstance().
        createSerializer(ContentType.APPLICATION_JSON).entityCollectionStreamed(
        serviceMetadata,
        edmEntityType,
        entityCollection,
        EntityCollectionSerializerOptions.with().contextURL
        (ContextURL.with().oDataPath("http://host/svc").build()).build());
    ODataResponse response = new ODataResponse();
    response.setODataContent(serializerResult.getODataContent());
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    response.setHeader(CONTENT_ID, "1");
    parts.add(new ODataResponsePart(response, true));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);

    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(14, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("Content-Type: multipart/mixed; boundary=changeset_"));
    assertEquals(CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals("Content-ID: 1" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 204 No Content" + CRLF, body.get(line++));
    assertEquals("Content-Length: 47" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("{\"@odata.context\":\"../../$metadata\",\"value\":[]}" + CRLF, body.get(line++));
    assertTrue(body.get(line++).startsWith("--changeset_"));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }
  
  @Test
  public void testODataContentWithODataResponse() throws Exception {
    List<ODataResponsePart> parts = new ArrayList<>();
    
    ODataResponse response = new ODataResponse();
    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
    response.setHeader(CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    String bigData = generateData(10000);
    response.setContent(IOUtils.toInputStream(bigData, StandardCharsets.UTF_8));
    parts.add(new ODataResponsePart(response, false));
    
    ServiceMetadata serviceMetadata = mock(ServiceMetadata.class);
    EdmEntityType edmEntityType = mock(EdmEntityType.class);
    EntityIterator entityCollection = new EntityIterator() {
      
      @Override
      public Entity next() {
        return null;
      }
      
      @Override
      public boolean hasNext() {
        return false;
      }
    };  

    SerializerStreamResult serializerResult = OData.newInstance().
        createSerializer(ContentType.APPLICATION_JSON).entityCollectionStreamed(
        serviceMetadata,
        edmEntityType,
        entityCollection,
        EntityCollectionSerializerOptions.with().contextURL
        (ContextURL.with().oDataPath("http://host/svc").build()).build());
    ODataResponse response1 = new ODataResponse();
    response1.setODataContent(serializerResult.getODataContent());
    response1.setStatusCode(HttpStatusCode.OK.getStatusCode());
    parts.add(new ODataResponsePart(response1, false));

    BatchResponseSerializer serializer = new BatchResponseSerializer();
    InputStream content = serializer.serialize(parts, BOUNDARY);

    assertNotNull(content);

    BatchLineReader reader = new BatchLineReader(content);
    List<String> body = reader.toList();
    reader.close();

    int line = 0;
    assertEquals(18, body.size());
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Type: text/plain" + CRLF, body.get(line++));
    assertEquals("Content-Length: 10000" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals(bigData + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + CRLF, body.get(line++));
    assertEquals("Content-Type: application/http" + CRLF, body.get(line++));
    assertEquals("Content-Transfer-Encoding: binary" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("HTTP/1.1 200 OK" + CRLF, body.get(line++));
    assertEquals("Content-Length: 47" + CRLF, body.get(line++));
    assertEquals(CRLF, body.get(line++));
    assertEquals("{\"@odata.context\":\"../../$metadata\",\"value\":[]}" + CRLF, body.get(line++));
    assertEquals("--" + BOUNDARY + "--" + CRLF, body.get(line));
  }

}