/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.util.List;

import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;

public class BatchQueryOperation implements BatchPart {

  protected final boolean isStrict;
  protected Line httpStatusLine;
  protected Header headers;
  protected List<Line> body;
  // protected int bodySize;
  protected final List<Line> message;

  public BatchQueryOperation(List<Line> message, boolean isStrict) {
    this.isStrict = isStrict;
    this.message = message;
  }

  public BatchQueryOperation parse() throws BatchDeserializerException {
    httpStatusLine = consumeHttpStatusLine(message);
    headers = BatchParserCommon.consumeHeaders(message);
    BatchParserCommon.consumeBlankLine(message, isStrict);
    body = message;

    return this;
  }

  protected Line consumeHttpStatusLine(List<Line> message) throws BatchDeserializerException {
    if (!message.isEmpty() && !"".equals(message.get(0).toString().trim())) {
      Line method = message.get(0);
      message.remove(0);

      return method;
    } else {
      int line = (!message.isEmpty()) ? message.get(0).getLineNumber() : 0;
      throw new BatchDeserializerException("Missing http request line",
          BatchDeserializerException.MessageKeys.INVALID_STATUS_LINE, "" + line);
    }
  }

  public Line getHttpStatusLine() {
    return httpStatusLine;
  }

  public List<Line> getBody() {
    return body;
  }

/*
  public int getBodySize() {
    return bodySize;
  }
*/

  @Override
  public Header getHeaders() {
    return headers;
  }

/*
  @Override
  public boolean isStrict() {
    return isStrict;
  }
*/

}