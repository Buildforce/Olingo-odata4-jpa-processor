/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.batchhandler;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataHandler;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.batch.BatchFacade;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;
import nl.buildforce.olingo.server.api.processor.BatchProcessor;
import nl.buildforce.olingo.server.core.batchhandler.referenceRewriting.BatchReferenceRewriter;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.CONTENT_ID;

public class BatchPartHandler {
  private final ODataHandler oDataHandler;
  private final BatchProcessor batchProcessor;
  private final BatchFacade batchFacade;
  private final BatchReferenceRewriter rewriter;

  public BatchPartHandler(ODataHandler oDataHandler, BatchProcessor processor,
                          BatchFacade batchFacade) {
    this.oDataHandler = oDataHandler;
    batchProcessor = processor;
    this.batchFacade = batchFacade;
    rewriter = new BatchReferenceRewriter();
  }

  public ODataResponse handleODataRequest(ODataRequest request) throws BatchDeserializerException {
    return handle(request, true);
  }

  public ODataResponsePart handleBatchRequest(BatchRequestPart request)
      throws ODataApplicationException, ODataLibraryException {
    if (request.isChangeSet()) {
      return handleChangeSet(request);
    } else {
      ODataResponse response = handle(request.getRequests().get(0), false);

      return new ODataResponsePart(response, false);
    }
  }

  public ODataResponse handle(ODataRequest request, boolean isChangeSet)
      throws BatchDeserializerException {
    ODataResponse response;

    if (isChangeSet) {
      rewriter.replaceReference(request);

      response = oDataHandler.process(request);

      rewriter.addMapping(request, response);
    } else {
      response = oDataHandler.process(request);
    }

    // Add content id to response
    String contentId = request.getHeader(CONTENT_ID);
    if (contentId != null) {
      response.setHeader(CONTENT_ID, contentId);
    }

    return response;
  }

  private ODataResponsePart handleChangeSet(BatchRequestPart request) throws ODataApplicationException,
  ODataLibraryException {
    return batchProcessor.processChangeSet(batchFacade, request.getRequests());
  }

}