/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataServerError;

/**
 * Processor which is called if any error/exception occurs inside the library or another processor.
 */
public interface ErrorProcessor extends Processor {

  /**
   * Processes an error/exception. MUST NOT throw an exception!
   *
   * @param request OData request object containing raw HTTP information
   * @param response OData response object for collecting response data
   * @param serverError the server error
   * @param responseFormat requested content type after content negotiation
   */
  void processError(ODataRequest request, ODataResponse response, ODataServerError serverError,
                    ContentType responseFormat);
}
