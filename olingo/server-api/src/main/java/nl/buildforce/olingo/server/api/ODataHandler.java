/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import nl.buildforce.olingo.server.api.processor.Processor;

/**
 * <p>Handles requests as OData requests.</p>
 *
 * <p>This includes URI parsing, content negotiation, dispatching the request
 * to a specific custom processor implementation for handling data and
 * creating the serialized content for the response object.</p>
 */
public interface ODataHandler {

  /**
   * <p>Processes an OData request.</p>
   * <p>This includes URI parsing, content negotiation, dispatching the request
   * to a specific custom processor implementation for handling data and
   * creating the serialized content for the response object.</p>
   * @param request the OData request
   * @return OData response
   */
  ODataResponse process(ODataRequest request);

  /**
   * <p>Registers additional custom processor implementations for handling OData requests.</p>
   * <p>If request processing requires a processor that is not registered then a
   * "not implemented" exception will happen.</p>
   */
  void register(Processor processor);

  /**
   * <p>Registers additional extensions for handling OData requests.</p>
   * <p>This method is used for registration of all possible extensions
   * and provide the extensibility for further extensions and
   * different ODataHandler implementations/extensions.</p>
   */
  void register(OlingoExtension extension);
}
