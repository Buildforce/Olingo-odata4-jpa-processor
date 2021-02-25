/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.batchhandler;

import java.util.List;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.core.ODataHandlerImpl;
import nl.buildforce.olingo.server.api.batch.BatchFacade;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException;
import nl.buildforce.olingo.server.api.deserializer.batch.BatchDeserializerException.MessageKeys;
import nl.buildforce.olingo.server.api.processor.BatchProcessor;
import nl.buildforce.olingo.server.core.ODataHandlerException;
import nl.buildforce.olingo.server.core.deserializer.batch.BatchParserCommon;

public class BatchHandler {
  private final BatchProcessor batchProcessor;
  private final ODataHandlerImpl oDataHandler;
  private static final String RETURN_MINIMAL = "return=minimal";
  private static final String RETURN_REPRESENTATION = "return=representation";

  public BatchHandler(ODataHandlerImpl oDataHandler, BatchProcessor batchProcessor) {

    this.batchProcessor = batchProcessor;
    this.oDataHandler = oDataHandler;
  }

  public void process(ODataRequest request, ODataResponse response, boolean isStrict)
      throws ODataApplicationException, ODataLibraryException {
    validateRequest(request);
    validatePreferHeader(request);

    BatchFacade operation = new BatchFacadeImpl(oDataHandler, batchProcessor, isStrict);
    batchProcessor.processBatch(operation, request, response);
  }
  
  /** Checks if Prefer header is set with return=minimal or 
   * return=representation for batch requests
   * @param request
   * @throws ODataHandlerException
   */
  private void validatePreferHeader(ODataRequest request) throws ODataHandlerException {
    List<String> returnPreference = request.getHeaders(HttpHeader.PREFER);
    if (null != returnPreference) {
      for (String preference : returnPreference) {
        if (preference.equals(RETURN_MINIMAL) || preference.equals(RETURN_REPRESENTATION)) {
          throw new ODataHandlerException("Prefer Header not supported: " + preference,
              ODataHandlerException.MessageKeys.INVALID_PREFER_HEADER, preference);
        } 
      }
    }
  }

  private void validateRequest(ODataRequest request) throws BatchDeserializerException {
    validateHttpMethod(request);
    validateContentType(request);
  }

  private void validateContentType(ODataRequest request) throws BatchDeserializerException {
    // This method does validation.
    BatchParserCommon.parseContentType(request.getHeader(HttpHeader.CONTENT_TYPE), ContentType.MULTIPART_MIXED, 0);
  }

  private void validateHttpMethod(ODataRequest request) throws BatchDeserializerException {
    if (request.getMethod() != HttpMethod.POST) {
      throw new BatchDeserializerException("Invalid HTTP method", MessageKeys.INVALID_METHOD, "0");
    }
  }
}
