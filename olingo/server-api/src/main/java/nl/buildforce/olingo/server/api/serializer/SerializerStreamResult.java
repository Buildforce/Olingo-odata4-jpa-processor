/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.server.api.ODataContent;

/**
 * Result type for {@link ODataSerializer} methods
 * which supports streaming (write in the future).
 */
public interface SerializerStreamResult {
  /**
   * Returns the content as ODataContent instance.
   * 
   * @return OData response content
   */
  ODataContent getODataContent();
}
