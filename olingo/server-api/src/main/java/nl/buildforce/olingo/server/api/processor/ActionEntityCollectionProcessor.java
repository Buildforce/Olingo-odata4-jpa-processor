/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling an action request with a return type of Entity Collection.
 */
public interface ActionEntityCollectionProcessor extends Processor {
  /**
   * Process an action which has as return type a collection of entities.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param requestFormat content type of body sent with request
   * @param responseFormat requested content type after content negotiation
   */
  void processActionEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
                                     ContentType requestFormat, ContentType responseFormat);

}