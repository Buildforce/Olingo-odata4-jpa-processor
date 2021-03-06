/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.util.LinkedList;
import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

public class BatchBodyPart /*implements BatchPart*/ {
  private final String boundary;
  private final boolean isStrict;
  private final List<Line> remainingMessage = new LinkedList<>();

  private Header headers;
  private boolean isChangeSet;
  private List<BatchQueryOperation> requests;

  public BatchBodyPart(List<Line> message, String boundary, boolean isStrict) {
    this.boundary = boundary;
    this.isStrict = isStrict;
    remainingMessage.addAll(message);
  }

  public BatchBodyPart parse() throws BatchDeserializerException {
    headers = BatchParserCommon.consumeHeaders(remainingMessage);
    BatchParserCommon.consumeBlankLine(remainingMessage, isStrict);
    isChangeSet = isChangeSet(headers);
    requests = consumeRequest(remainingMessage);

    return this;
  }

  private boolean isChangeSet(Header headers) throws BatchDeserializerException {
    List<String> contentTypes = headers.getHeaders(CONTENT_TYPE);

    if (contentTypes.isEmpty()) {
      throw new BatchDeserializerException("Missing content type",
          BatchDeserializerException.MessageKeys.MISSING_CONTENT_TYPE,
          Integer.toString(headers.getLineNumber()));
    }

    for (String contentType : contentTypes) {
      if (isContentTypeMultiPartMixed(contentType)) {
        return true;
      }
    }
    return false;
  }

  private List<BatchQueryOperation> consumeRequest(List<Line> remainingMessage)
      throws BatchDeserializerException {
    return isChangeSet ? consumeChangeSet(remainingMessage) : consumeQueryOperation(remainingMessage);
  }

  private List<BatchQueryOperation> consumeChangeSet(List<Line> remainingMessage)
      throws BatchDeserializerException {
    List<List<Line>> changeRequests = splitChangeSet(remainingMessage);
    List<BatchQueryOperation> requestList = new LinkedList<>();

    for (List<Line> changeRequest : changeRequests) {
      requestList.add(new BatchChangeSetPart(changeRequest, isStrict).parse());
    }

    return requestList;
  }

  private List<List<Line>> splitChangeSet(List<Line> remainingMessage) throws BatchDeserializerException {

    HeaderField contentTypeField = headers.getHeaderField(CONTENT_TYPE);
    String changeSetBoundary = BatchParserCommon.getBoundary(contentTypeField.getValue(),
        contentTypeField.getLineNumber());
    validateChangeSetBoundary(changeSetBoundary, headers);

    return BatchParserCommon.splitMessageByBoundary(remainingMessage, changeSetBoundary);
  }

  private void validateChangeSetBoundary(String changeSetBoundary, Header header)
      throws BatchDeserializerException {
    if (changeSetBoundary.equals(boundary)) {
      throw new BatchDeserializerException("Change set boundary is equals to batch request boundary",
          BatchDeserializerException.MessageKeys.INVALID_BOUNDARY,
          Integer.toString(header.getHeaderField(CONTENT_TYPE).getLineNumber()));
    }
  }

  private List<BatchQueryOperation> consumeQueryOperation(List<Line> remainingMessage)
      throws BatchDeserializerException {
    List<BatchQueryOperation> requestList = new LinkedList<>();
    requestList.add(new BatchQueryOperation(remainingMessage, isStrict).parse());

    return requestList;
  }

  private boolean isContentTypeMultiPartMixed(String contentType) throws BatchDeserializerException {
    ContentType type;
    try {
        type = new ContentType(contentType);
      } catch (IllegalArgumentException e) {
        throw new BatchDeserializerException("Invalid content type.", e,
                BatchDeserializerException.MessageKeys.INVALID_CONTENT_TYPE, Integer.toString(0));
      }
      return type.isCompatible(ContentType.MULTIPART_MIXED);
    }

  // @Override
  public Header getHeaders() {
    return headers;
  }

/*
  @Override
  public boolean isStrict() {
    return isStrict;
  }
*/

  public boolean isChangeSet() {
    return isChangeSet;
  }

  public List<BatchQueryOperation> getRequests() {
    return requests;
  }

}