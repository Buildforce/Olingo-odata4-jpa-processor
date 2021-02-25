/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmProperty;

import java.io.InputStream;

/**
 * Deserializer on OData server side.
 */
public interface ODataDeserializer {
  /**
   * Deserializes an entity stream into an {@link nl.buildforce.olingo.commons.api.data.Entity Entity} object.
   * Validates: property types, no double properties, correct json types.
   * Returns a deserialized {@link nl.buildforce.olingo.commons.api.data.Entity Entity} object and an
   * {@link nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption ExpandOption} object.
   * @param stream
   * @param edmEntityType
   * @return {@link DeserializerResult#getEntity()} and {@link DeserializerResult#getExpandTree()}
   * @throws DeserializerException
   */
  DeserializerResult entity(InputStream stream, EdmEntityType edmEntityType) throws DeserializerException;

  /**
   * Deserializes an entity collection stream into an {@link nl.buildforce.olingo.commons.api.data.EntityCollection
   * EntityCollection} object.
   * @param stream
   * @param edmEntityType
   * @return {@link DeserializerResult#getEntityCollection()}
   * @throws DeserializerException
   */
  DeserializerResult entityCollection(InputStream stream, EdmEntityType edmEntityType) throws DeserializerException;

  /**
   * Deserializes an action-parameters stream into a map of key/value pairs.
   * Validates: parameter types, no double parameters, correct json types.
   * @param stream
   * @param edmAction
   * @return {@link DeserializerResult#getActionParameters()}
   * @throws DeserializerException
   */
  DeserializerResult actionParameters(InputStream stream, EdmAction edmAction) throws DeserializerException;

  /**
   * Deserializes the Property or collections of properties (primitive & complex).
   * @param stream
   * @param edmProperty
   * @return {@link DeserializerResult#getProperty()}
   * @throws DeserializerException
   */
  DeserializerResult property(InputStream stream, EdmProperty edmProperty) throws DeserializerException;

  /**
   * Reads entity references from the provided document.
   * @param stream
   * @param keys
   * @return {@link DeserializerResult#getEntityReferences()}
   * @throws DeserializerException
   */
  DeserializerResult entityReferences(InputStream stream) throws DeserializerException;

}