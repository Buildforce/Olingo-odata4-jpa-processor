/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.util.List;

import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;

public class BatchChangeSetPart extends BatchQueryOperation {
  private BatchQueryOperation request;

  public BatchChangeSetPart(List<Line> message, boolean isStrict) {
    super(message, isStrict);
  }

  @Override
  public BatchChangeSetPart parse() throws BatchDeserializerException {
    headers = BatchParserCommon.consumeHeaders(message);
    BatchParserCommon.consumeBlankLine(message, isStrict);

    request = new BatchQueryOperation(message, isStrict).parse();

    return this;
  }

  public BatchQueryOperation getRequest() {
    return request;
  }

  @Override
  public List<Line> getBody() {
    return request.getBody();
  }

  @Override
  public Line getHttpStatusLine() {
    return request.getHttpStatusLine();
  }
}
