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
 * Processor interface for handling a collection of complex-type instances, e.g.,
 * a property of an entity defined as collection of complex-type instances.
 */
public interface ComplexCollectionProcessor extends Processor {

  /**
   * Reads complex-type collection.
   * If it is not available, for example due to permissions, the service responds with 404 Not Found.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param responseFormat requested content type after content negotiation
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void readComplexCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException;

  /**
   * Update (replace) complex-type collection with send data in the persistence and
   * puts content, status, and Location into the response.
   * Update of complex-type collection is equal to a complete replace
   * of the property (see chapter "11.4.9.4 Update a Collection Property").
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @param requestFormat content type of body sent with request
   * @param responseFormat requested content type after content negotiation
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void updateComplexCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
      ContentType requestFormat, ContentType responseFormat) throws ODataApplicationException, ODataLibraryException;

  /**
   * Deletes complex-type collection from an entity and puts the status into the response.
   * Deletion for complex-type collection is equal to
   * set the content to <code>EMPTY</code>.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param uriInfo information of a parsed OData URI
   * @throws ODataApplicationException if the service implementation encounters a failure
   * @throws ODataLibraryException
   */
  void deleteComplexCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo)
      throws ODataApplicationException, ODataLibraryException;
}