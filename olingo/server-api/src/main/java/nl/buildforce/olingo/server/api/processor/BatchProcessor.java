/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.batch.BatchFacade;
import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;

import java.util.List;

/**
 * Processor interface for handling a single instance of an Entity Type.
 */
public interface BatchProcessor extends Processor {

  /**
   * Process a complete batch request and puts serialized content and status into the response.
   * @param facade BatchFacade which should be used for further batch part handling
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @throws ODataApplicationException
   * @throws ODataLibraryException
   */
  void processBatch(BatchFacade facade, ODataRequest request, ODataResponse response)
      throws ODataApplicationException, ODataLibraryException;

  /**
   * Process a batch change set (containing several batch requests)
   * and puts serialized content and status into the response.
   * @param facade BatchFacade which should be used for further batch part handling
   * @param requests List of ODataRequests which are included in the to be processed change set
   * @throws ODataApplicationException
   * @throws ODataLibraryException
   */
  ODataResponsePart processChangeSet(BatchFacade facade, List<ODataRequest> requests)
      throws ODataApplicationException, ODataLibraryException;
}
