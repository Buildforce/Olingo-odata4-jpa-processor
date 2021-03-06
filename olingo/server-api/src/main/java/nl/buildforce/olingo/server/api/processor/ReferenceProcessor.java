/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.uri.UriInfo;

/**
 * Processor interface for handling a single instance of an Entity Reference.
 */
public interface ReferenceProcessor extends Processor {

  /**
   * Reads entity reference from persistence and put it as serialized content and status into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param responseFormat requested content type after content negotiation
   */
  void readReference(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
  ;

  /**
   * Creates entity reference in the persistence and puts content, status, and Location into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param requestFormat content type of body sent with request
   */
  void createReference(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat)
  ;

  /**
   * Update entity reference in the persistence and puts content, status, and Location into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param requestFormat content type of body sent with request
   */
  void updateReference(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat)
  ;

  /**
   * Deletes reference to an entity from persistence and puts the status into the response.
   * Delete on a reference only removes the reference to and not the entity itself
   * (see chapter "11.4.6.2 Remove a Reference to an Entity")
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   */
  void deleteReference(ODataRequest request, ODataResponse response, UriInfo uriInfo);

}