/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException.MessageKeys;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.CONTENT_ID;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.TE;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.EXPECT;
import static com.google.common.net.HttpHeaders.FROM;
import static com.google.common.net.HttpHeaders.MAX_FORWARDS;
import static com.google.common.net.HttpHeaders.RANGE;
import static com.google.common.net.HttpHeaders.WWW_AUTHENTICATE;

public class BatchRequestTransformator {
  private final String baseUri;
  private final String rawServiceResolutionUri;

  public BatchRequestTransformator(String baseUri, String serviceResolutionUri) {
    this.baseUri = baseUri;
    rawServiceResolutionUri = serviceResolutionUri;
  }

  public List<BatchRequestPart> transform(BatchBodyPart bodyPart) throws BatchDeserializerException {
    List<ODataRequest> requests = new LinkedList<>();
    List<BatchRequestPart> resultList = new ArrayList<>();

    validateHeaders(bodyPart.getHeaders(), bodyPart.isChangeSet());

    for (BatchQueryOperation queryOperation : bodyPart.getRequests()) {
      requests.add(processQueryOperation(bodyPart, baseUri, queryOperation));
    }

    resultList.add(new BatchRequestPart(bodyPart.isChangeSet(), requests));
    return resultList;
  }

  private ODataRequest processQueryOperation(BatchBodyPart bodyPart, String baseUri,
                                             BatchQueryOperation queryOperation) throws BatchDeserializerException {
    if (bodyPart.isChangeSet()) {
      BatchQueryOperation encapsulatedQueryOperation = ((BatchChangeSetPart) queryOperation).getRequest();
      handleContentId(queryOperation, encapsulatedQueryOperation);
      validateHeaders(queryOperation.getHeaders(), false);

      return createRequest(encapsulatedQueryOperation, baseUri, bodyPart.isChangeSet());
    } else {
      return createRequest(queryOperation, baseUri, bodyPart.isChangeSet());
    }
  }

  private void handleContentId(BatchQueryOperation changeRequestPart, BatchQueryOperation request)
      throws BatchDeserializerException {
    HeaderField contentIdChangeRequestPart = getContentId(changeRequestPart);
    HeaderField contentIdRequest = getContentId(request);

    if (contentIdChangeRequestPart == null && contentIdRequest == null) {
      throw new BatchDeserializerException("Missing content id", MessageKeys.MISSING_CONTENT_ID,
          Integer.toString(changeRequestPart.getHeaders().getLineNumber()));
    } else if (contentIdChangeRequestPart != null) {
      request.getHeaders().replaceHeaderField(contentIdChangeRequestPart);
    }
  }

  private HeaderField getContentId(BatchQueryOperation queryOperation) throws BatchDeserializerException {
    HeaderField contentIdHeader = queryOperation.getHeaders().getHeaderField(CONTENT_ID);

    if (contentIdHeader != null) {
      if (contentIdHeader.getValues().size() == 1) {
        return contentIdHeader;
      } else {
        throw new BatchDeserializerException("Invalid Content-ID header", MessageKeys.INVALID_CONTENT_ID,
            Integer.toString(contentIdHeader.getLineNumber()));
      }
    }

    return null;
  }

  private ODataRequest createRequest(BatchQueryOperation operation, String baseUri,
                                     boolean isChangeSet) throws BatchDeserializerException {
    HttpRequestStatusLine statusLine =
        new HttpRequestStatusLine(operation.getHttpStatusLine(), baseUri, rawServiceResolutionUri);
    statusLine.validateHttpMethod(isChangeSet);
    BatchTransformatorCommon.validateHost(operation.getHeaders(), baseUri);

    validateBody(statusLine, operation);
    Charset charset = getCharset(operation);
    InputStream bodyStream = getBodyStream(operation, statusLine, charset);

    validateForbiddenHeader(operation);

    ODataRequest request = new ODataRequest();
    request.setBody(bodyStream);
    request.setMethod(statusLine.getMethod());
    request.setRawBaseUri(statusLine.getRawBaseUri());
    request.setRawODataPath(statusLine.getRawODataPath());
    request.setRawQueryPath(statusLine.getRawQueryPath());
    request.setRawRequestUri(statusLine.getRawRequestUri());
    request.setRawServiceResolutionUri(statusLine.getRawServiceResolutionUri());

    for (HeaderField field : operation.getHeaders()) {
      request.addHeader(field.getFieldName(), field.getValues());
    }

    return request;
  }

  private Charset getCharset(BatchQueryOperation operation) {
    ContentType contentType = ContentType.parse(operation.getHeaders().getHeader(CONTENT_TYPE));
    if (contentType != null) {
      String charsetValue = contentType.getParameter(ContentType.PARAMETER_CHARSET);
      if (charsetValue == null) {
        if (contentType.isCompatible(ContentType.APPLICATION_JSON) || contentType.getSubtype().contains("xml")) {
          return StandardCharsets.UTF_8;
        }
      } else {
        return Charset.forName(charsetValue);
      }
    }
    return StandardCharsets.ISO_8859_1;
  }

  private void validateForbiddenHeader(BatchQueryOperation operation) throws BatchDeserializerException {
    Header header = operation.getHeaders();

    if (header.exists(WWW_AUTHENTICATE) || header.exists(AUTHORIZATION)
        || header.exists(EXPECT) || header.exists(FROM) || header.exists(MAX_FORWARDS)
        || header.exists(RANGE) || header.exists(TE)) {
      throw new BatchDeserializerException("Forbidden header", MessageKeys.FORBIDDEN_HEADER,
          Integer.toString(header.getLineNumber()));
    }
  }

  private InputStream getBodyStream(BatchQueryOperation operation, HttpRequestStatusLine statusLine,
                                    Charset charset) throws BatchDeserializerException {
    if (statusLine.getMethod().equals(HttpMethod.GET)) {
      return new ByteArrayInputStream(new byte[0]);
    } else {
      int contentLength = BatchTransformatorCommon.getContentLength(operation.getHeaders());

      if (contentLength == -1) {
        return BatchParserCommon.convertLineListToInputStream(operation.getBody(), charset);
      } else {
        return BatchParserCommon.convertLineListToInputStream(operation.getBody(), charset, contentLength);
      }
    }
  }

  private void validateBody(HttpRequestStatusLine statusLine, BatchQueryOperation operation)
      throws BatchDeserializerException {
    if (statusLine.getMethod().equals(HttpMethod.GET) && isInvalidGetRequestBody(operation)) {
      throw new BatchDeserializerException("Invalid request line", MessageKeys.INVALID_CONTENT,
          Integer.toString(statusLine.getLineNumber()));
    }
  }

  private boolean isInvalidGetRequestBody(BatchQueryOperation operation) {
    return operation.getBody().size() > 1
        || operation.getBody().size() == 1 && !operation.getBody().get(0).toString().trim().isEmpty();
  }

  private void validateHeaders(Header headers, boolean isChangeSet) throws BatchDeserializerException {
    if (isChangeSet) {
      BatchTransformatorCommon.validateContentType(headers, ContentType.MULTIPART_MIXED);
    } else {
      BatchTransformatorCommon.validateContentTransferEncoding(headers);
      BatchTransformatorCommon.validateContentType(headers, ContentType.APPLICATION_HTTP);
    }
  }

}