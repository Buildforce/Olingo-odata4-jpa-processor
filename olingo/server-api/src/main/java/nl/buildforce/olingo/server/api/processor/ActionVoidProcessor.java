/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling an action request with no return type.
 */
public interface ActionVoidProcessor extends Processor {
  /**
   * Process an action which has no return type.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information about a parsed OData URI
   * @param requestFormat content type of body sent with request
   * @throws ODataApplicationException if the service implementation encounters a failure
   */
  void processActionVoid(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat)
      throws ODataApplicationException;

}