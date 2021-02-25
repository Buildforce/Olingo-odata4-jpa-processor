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
 * Processor interface for handling an instance of a primitive type, e.g., a primitive property of an entity.
 */
public interface PrimitiveProcessor extends Processor {

  /**
   * Reads primitive-type instance.
   * If its value is <code>null</code>, the service responds with 204 No Content.
   * If it is not available, for example due to permissions, the service responds with 404 Not Found.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param responseFormat requested content type after content negotiation
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void readPrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException;

  /**
   * Update primitive-type instance with send data in the persistence and
   * puts content, status, and Location into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param requestFormat content type of body sent with request
   * @param responseFormat requested content type after content negotiation
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void updatePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo,
      ContentType requestFormat, ContentType responseFormat)
          throws ODataApplicationException, ODataLibraryException;

  /**
   * Deletes primitive-type value from an entity and puts the status into the response.
   * Deletion for primitive-type values is equal to
   * set the value to <code>NULL</code> (see chapter "11.4.9.2 Set a Value to Null")
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void deletePrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo)
      throws ODataApplicationException, ODataLibraryException;

}