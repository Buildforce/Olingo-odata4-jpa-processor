/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.serializer.SerializerException;

public class AsyncResponseSerializer {
  private static final int BUFFER_SIZE = 8192;
  private static final String COLON = ":";
  private static final String SP = " ";
  private static final String CRLF = "\r\n";
  private static final String HEADER_CHARSET_NAME = "ISO-8859-1";
  private static final String HTTP_VERSION = "HTTP/1.1";

  public InputStream serialize(ODataResponse response) throws SerializerException {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      appendStatusLine(response, buffer);
      appendResponseHeader(response, buffer);
      append(CRLF, buffer);
      appendBody(response, buffer);

      buffer.flush();
      return new ByteArrayInputStream(buffer.toByteArray(), 0, buffer.size());
    } catch (IOException e) {
      throw new SerializerException("Exception occurred during serialization of asynchronous response.",
          e, SerializerException.MessageKeys.IO_EXCEPTION);
    }
  }

  private void appendResponseHeader(ODataResponse response,
                                    ByteArrayOutputStream buffer) throws IOException {
    Map<String, List<String>> header = response.getAllHeaders();

    for (Map.Entry<String, List<String>> entry : header.entrySet()) {
      appendHeader(entry.getKey(), entry.getValue(), buffer);
    }
  }

  private void appendHeader(String name, List<String> values, ByteArrayOutputStream buffer)
      throws IOException {
    for (String value : values) {
      append(name + COLON + SP + value + CRLF, buffer);
    }
  }

  private void appendStatusLine(ODataResponse response, ByteArrayOutputStream buffer)
      throws IOException {
    HttpStatusCode status = HttpStatusCode.fromStatusCode(response.getStatusCode());
    append(HTTP_VERSION + SP + response.getStatusCode() + SP + status + CRLF, buffer);
  }

  private void appendBody(ODataResponse response, ByteArrayOutputStream buffer) throws IOException {
    InputStream input = response.getContent();
    if (input != null) {
      ByteBuffer inBuffer = ByteBuffer.allocate(BUFFER_SIZE);
      try (ReadableByteChannel ic = Channels.newChannel(input)) {
        try (WritableByteChannel oc = Channels.newChannel(buffer)) {
          while (ic.read(inBuffer) > 0) {
            inBuffer.flip();
            oc.write(inBuffer);
            inBuffer.rewind();
          }
        }
      }
    }
  }

  private void append(String value, ByteArrayOutputStream buffer) throws IOException {
    try {
      buffer.write(value.getBytes(HEADER_CHARSET_NAME));
    } catch (UnsupportedEncodingException e) {
      throw new IOException("Default header charset with name '" + HEADER_CHARSET_NAME +
          "' is not available.", e);
    }
  }
}