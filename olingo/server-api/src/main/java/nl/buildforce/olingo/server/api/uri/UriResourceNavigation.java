/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmNavigationProperty;
import nl.buildforce.olingo.commons.api.edm.EdmType;

/**
 * Used to describe an navigation property used within an resource path
 * For example: http://.../serviceroot/entityset(1)/navProperty
 */
public interface UriResourceNavigation extends UriResourcePartTyped {

  /**
   * @return Navigation property
   */
  EdmNavigationProperty getProperty();

  /**
   * @return Key predicates if used, otherwise an empty list
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
