/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Used to describe an entity set used within an resource path
 * For example: http://.../serviceroot/entityset(1)
 */
public interface UriResourceEntitySet extends UriResourcePartTyped {

  /**
   * @return Entity set used in the resource path
   */
  EdmEntitySet getEntitySet();

  /**
   * @return Type of the entity set
   */
  EdmEntityType getEntityType();

  /**
   * @return Key predicates if used, otherwise null
   */
  List<UriParameter> getKeyPredicates();

  /**
   * @return Type filter before key predicates if used, otherwise null
   */
  EdmType getTypeFilterOnCollection();

  /**
   * @return Type filter behind key predicates if used, otherwise null
   */
  EdmType getTypeFilterOnEntry();

}