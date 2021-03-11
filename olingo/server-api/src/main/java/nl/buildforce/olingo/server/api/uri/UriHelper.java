/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmStructuredType;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.queryoption.ExpandOption;
import nl.buildforce.olingo.server.api.uri.queryoption.SelectOption;

/**
 * Used for URI-related tasks.
 */
public interface UriHelper {

  /**
   * Builds the select-list part of a {@link nl.buildforce.olingo.commons.api.data.ContextURL ContextURL}.
   * @param type the {@link EdmStructuredType}
   * @param expand the $expand option
   * @param select the $select option
   * @return a String with the select list
   */
  String buildContextURLSelectList(EdmStructuredType type, ExpandOption expand, SelectOption select)
      throws SerializerException;

// --Commented out by Inspection START (''21-03-10 00:11):
//  /**
//   * Builds the key-predicate part of a {@link nl.buildforce.olingo.commons.api.data.ContextURL ContextURL}.
//   * @param keys the keys as a list of {@link UriParameter} instances
//   * @return a String with the key predicate
//   */
//  String buildContextURLKeyPredicate(List<UriParameter> keys) throws SerializerException;
// --Commented out by Inspection STOP (''21-03-10 00:11)

  /**
   * Builds the relative canonical URL for the given entity in the given entity set.
   * @param edmEntitySet the entity set
   * @param entity the entity data
   * @return the relative canonical URL
   */
  String buildCanonicalURL(EdmEntitySet edmEntitySet, Entity entity) throws SerializerException;

  /**
   * Builds the key predicate for the given entity.
   * @param edmEntityType the entity type of the entity
   * @param entity the entity data
   * @return the key predicate
   */
  String buildKeyPredicate(EdmEntityType edmEntityType, Entity entity) throws SerializerException;

// --Commented out by Inspection START (''21-03-06 00:46):
//  /**
//   * Parses a given entity-id. Provides the entity set and key predicates.
//   * A canonical entiy-id to an entity must follow the pattern
//   * <code>[&lt;service root&gt;][&lt;entityContainer&gt;.]&lt;entitySet&gt;(&lt;key&gt;)</code>, i.e.,
//   * it must be a relative or absolute URI consisting of an entity set (qualified
//   * with an entity-container name if not in the default entity container) and a
//   * syntactically valid key that identifies a single entity; example:
//   * <code>http://example.server.com/service.svc/Employees('42')</code>.
//   *
//   * @param edm the edm the entity belongs to
//   * @param entityId URI of the entity-id
//   * @param rawServiceRoot the root URI of the service
//   * @return {@link UriResourceEntitySet} - contains the entity set and the key predicates
//   * @throws DeserializerException in case the entity-id is malformed
//   */
//  UriResourceEntitySet parseEntityId(Edm edm, String entityId, String rawServiceRoot) throws DeserializerException;
// --Commented out by Inspection STOP (''21-03-06 00:46)

}