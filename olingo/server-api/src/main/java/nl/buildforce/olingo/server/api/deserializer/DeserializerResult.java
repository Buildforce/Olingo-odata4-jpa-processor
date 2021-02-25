/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer;

import java.net.URI;
import java.util.List;
import java.util.Map;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Parameter;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;

/**
 * Result type for {@link ODataDeserializer} methods
 */
public interface DeserializerResult {
  /**
   * Returns an entity.
   * @return an {@link Entity} or null
   */
  Entity getEntity();

  /**
   * Returns an entity collection.
   * @return an {@link EntityCollection} or null
   */
  EntityCollection getEntityCollection();

  /**
   * Returns the ExpandOptions for the deserialized entity.
   * @return an {@link ExpandOption} or null
   */
  ExpandOption getExpandTree();

  /**
   * Returns the deserialized action parameters of an {@link Entity} as key/value pairs.
   * @return the action parameters
   */
  Map<String, Parameter> getActionParameters();

  /**
   * Returns a Property or collections of properties (primitive & complex).
   * @return {@link Property} or collections of properties (primitive & complex) or null
   */
  Property getProperty();

  /**
   * Returns the entity references from the provided document.
   * @return a collection of entity references
   */
  List<URI> getEntityReferences();
}
