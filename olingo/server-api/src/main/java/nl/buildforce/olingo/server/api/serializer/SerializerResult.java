/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import java.io.InputStream;

/**
 * Result type for {@link ODataSerializer} methods
 */
public interface SerializerResult {
  /**
   * Returns the serialized content
   * @return serialized content
   */
  InputStream getContent();
}
