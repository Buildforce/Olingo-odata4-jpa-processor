/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.deserializer.batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import nl.buildforce.olingo.commons.api.ex.ODataRuntimeException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchOptions;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;

public class BatchParser {

  private BatchOptions options;

  public List<BatchRequestPart> parseBatchRequest(InputStream content, String boundary,
                                                  BatchOptions options)
      throws BatchDeserializerException {
    this.options = options;

    BatchRequestTransformator transformator = new BatchRequestTransformator(options.getRawBaseUri(),
        options.getRawServiceResolutionUri());
    return parse(content, boundary, transformator);
  }

  private List<BatchRequestPart> parse(InputStream in, String boundary,
                                       BatchRequestTransformator transformator)
      throws BatchDeserializerException {
    try {
      return parseBatch(in, boundary, transformator);
    } catch (IOException e) {
      throw new ODataRuntimeException(e);
    }
  }

  private List<BatchRequestPart> parseBatch(InputStream in, String boundary,
                                            BatchRequestTransformator transformator) throws IOException, BatchDeserializerException {
    List<BatchRequestPart> resultList = new LinkedList<>();
    List<List<Line>> bodyPartStrings = splitBodyParts(in, boundary);

    for (List<Line> bodyPartString : bodyPartStrings) {
      BatchBodyPart bodyPart = new BatchBodyPart(bodyPartString, boundary, options.isStrict()).parse();
      resultList.addAll(transformator.transform(bodyPart));
    }

    return resultList;
  }

  private List<List<Line>> splitBodyParts(InputStream in, String boundary) throws IOException,
      BatchDeserializerException {
    BatchLineReader reader = new BatchLineReader(in);
    List<Line> message = reader.toLineList();
    reader.close();

    return BatchParserCommon.splitMessageByBoundary(message, boundary);
  }
}
