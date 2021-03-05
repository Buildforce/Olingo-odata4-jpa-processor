/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;

/**
 * Processor interface for handling the metadata document.
 */
public interface MetadataProcessor extends Processor {

  /**
   * Reads data from persistence and puts serialized content and status into the response.
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param responseFormat requested content type after content negotiation
   * @throws ODataLibraryException
   */
  void readMetadata(ODataRequest request, ODataResponse response, ContentType responseFormat) throws ODataLibraryException;

}