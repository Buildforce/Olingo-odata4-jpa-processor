/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// import nl.buildforce.olingo.server.api.debug.DebugSupport;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;
import nl.buildforce.olingo.server.api.serializer.CustomContentTypeSupport;

/**
 * Handles HTTP requests as OData requests.
 */
public interface ODataHttpHandler extends ODataHandler {

  /**
   * <p>Processes a HttpServletRequest as an OData request.</p>
   * <p>This includes URI parsing, content negotiation, dispatching the request
   * to a specific custom processor implementation for handling data and
   * creating the serialized content for the response object.</p>
   * @param request - must be a HTTP OData request
   * @param response - HTTP OData response
   */
  void process(HttpServletRequest request, HttpServletResponse response);
  
  /**
   * Sets the split parameter which is used for service resolution.
   * @param split the number of path segments reserved for service resolution; default is 0
   */
  //void setSplit(int split);


  /**
   * Registers the debug support handler.
   * @param debugSupport handler to register
   */
  // void register(DebugSupport debugSupport);

  /**
   * Registers a service implementation for modifying the standard list of supported
   * content types.
   * @see CustomContentTypeSupport
   */
  // void register(CustomContentTypeSupport customContentTypeSupport);

  /**
   * Registers support for concurrency control for certain entity sets.
   * @param customETagSupport handler to register
   */
  void register(CustomETagSupport customETagSupport);

}