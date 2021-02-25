/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

/**
 * Represents a member expression node in the expression tree. This expression is used to describe access paths
 * to properties and other EDM elements.
 */
public interface Member extends Expression {

  /**
   * @return UriInfoResource object describing the whole path used to access an data value
   * (this includes for example the usage of $root and $it inside the URI)
   */
  UriInfoResource getResourcePath();

  /**
   * @return Type
   */
  EdmType getType();

  /**
   * @return The used type filter ahead of the path
   */
  EdmType getStartTypeFilter();

  /**
   * @return true if the accessed data is a collection, otherwise false
   */
  boolean isCollection();

}