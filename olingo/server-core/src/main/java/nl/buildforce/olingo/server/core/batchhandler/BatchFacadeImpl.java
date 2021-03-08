/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.batchhandler;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataHandler;
import nl.buildforce.olingo.server.api.batch.BatchFacade;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;
import nl.buildforce.olingo.server.api.processor.BatchProcessor;
import nl.buildforce.olingo.server.core.deserializer.batch.BatchParserCommon;

public class BatchFacadeImpl implements BatchFacade {
  private final BatchPartHandler partHandler;

  /**
   * Creates a new BatchFacade.
   * @param oDataHandler   handler
   * @param batchProcessor batch processor
   */
  public BatchFacadeImpl(ODataHandler oDataHandler, BatchProcessor batchProcessor) {
    partHandler = new BatchPartHandler(oDataHandler, batchProcessor, this);
  }

  @Override
  public ODataResponse handleODataRequest(ODataRequest request)
      throws ODataLibraryException {
    return partHandler.handleODataRequest(request);
  }

  @Override
  public ODataResponsePart handleBatchRequest(BatchRequestPart request)
      throws ODataApplicationException, ODataLibraryException {
    return partHandler.handleBatchRequest(request);
  }

  @Override
  public String extractBoundaryFromContentType(String contentType) throws BatchDeserializerException {
    return BatchParserCommon.getBoundary(contentType, 0);
  }

}