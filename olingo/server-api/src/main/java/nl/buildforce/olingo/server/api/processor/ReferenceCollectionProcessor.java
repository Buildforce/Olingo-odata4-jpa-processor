/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling a collection an Entity References.
 */
public interface ReferenceCollectionProcessor extends Processor {

  /**
   * Reads entity references from persistence and put them as serialized content and with
   * according status into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param responseFormat requested content type after content negotiation
   */
  void readReferenceCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
                               ContentType responseFormat);

}