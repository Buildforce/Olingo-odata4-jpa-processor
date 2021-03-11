/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.net.URI;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException.MessageKeys;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.HOST;

public class BatchTransformatorCommon {

  private BatchTransformatorCommon() {
    // Private Utility Constructor
  }

  public static void validateContentType(Header headers, ContentType expected)
      throws BatchDeserializerException {
    List<String> contentTypes = headers.getHeaders(CONTENT_TYPE);

    if (contentTypes.isEmpty()) {
      throw new BatchDeserializerException("Missing content type", MessageKeys.MISSING_CONTENT_TYPE,
          Integer.toString(headers.getLineNumber()));
    }
    BatchParserCommon.parseContentType(contentTypes.get(0), expected, headers.getLineNumber());
  }

  public static void validateContentTransferEncoding(Header headers) throws BatchDeserializerException {
    HeaderField contentTransferField = headers.getHeaderField(BatchParserCommon.CONTENT_TRANSFER_ENCODING);

    if (contentTransferField != null) {
      List<String> contentTransferValues = contentTransferField.getValues();
      if (contentTransferValues.size() > 1
          || !BatchParserCommon.BINARY_ENCODING.equalsIgnoreCase(contentTransferValues.get(0))) {
        throw new BatchDeserializerException("Invalid Content-Transfer-Encoding header",
            MessageKeys.INVALID_CONTENT_TRANSFER_ENCODING, Integer.toString(headers.getLineNumber()));
      }
    }
  }

  public static int getContentLength(Header headers) throws BatchDeserializerException {
    HeaderField contentLengthField = headers.getHeaderField(CONTENT_LENGTH);

    if (contentLengthField != null && contentLengthField.getValues().size() == 1) {
      try {
        int contentLength = Integer.parseInt(contentLengthField.getValues().get(0));

        if (contentLength < 0) {
          throw new BatchDeserializerException("Invalid content length", MessageKeys.INVALID_CONTENT_LENGTH,
              Integer.toString(contentLengthField.getLineNumber()));
        }

        return contentLength;
      } catch (NumberFormatException e) {
        throw new BatchDeserializerException("Invalid content length", e, MessageKeys.INVALID_CONTENT_LENGTH,
            Integer.toString(contentLengthField.getLineNumber()));
      }
    }

    return -1;
  }

  public static void validateHost(Header headers, String baseUri) throws BatchDeserializerException {
    HeaderField hostField = headers.getHeaderField(HOST);

    if (hostField != null &&
        (hostField.getValues().size() > 1
            || !URI.create(baseUri).getAuthority().equalsIgnoreCase(hostField.getValues().get(0).trim()))) {
      throw new BatchDeserializerException("Invalid Host header",
          MessageKeys.INVALID_HOST, Integer.toString(headers.getLineNumber()));
    }
  }

}