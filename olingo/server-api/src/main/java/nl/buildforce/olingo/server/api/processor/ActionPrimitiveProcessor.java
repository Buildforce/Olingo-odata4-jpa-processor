/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling an action request with a return type of Primitive.
 */
public interface ActionPrimitiveProcessor extends Processor {
  /**
   * Process an action which has as return type a primitive-type.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information about a parsed OData URI
   * @param requestFormat content type of body sent with request
   * @param responseFormat requested content type after content negotiation
   * @throws ODataApplicationException if the service implementation encounters a failure
   */
  void processActionPrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo,
                              ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException;

}