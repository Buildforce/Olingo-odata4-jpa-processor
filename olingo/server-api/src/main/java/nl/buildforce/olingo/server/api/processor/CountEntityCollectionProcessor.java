/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling counting a collection of entities, e.g. an Entity Set.
 */
public interface CountEntityCollectionProcessor extends EntityCollectionProcessor {

  /**
   * Counts entities from persistence and puts serialized content and status into the response.
   * Response content type is <code>text/plain</code> by default.
   * @param request OData request object containing raw HTTP information.
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void countEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo)
      throws ODataApplicationException, ODataLibraryException;

}