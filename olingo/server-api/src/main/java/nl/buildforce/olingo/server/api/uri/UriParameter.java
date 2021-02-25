/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a function parameter or key predicate when used in the URI.
 */
public interface UriParameter {

  /**
   * @return Alias name if the parameter's value is an alias, otherwise null
   */
  String getAlias();

  /**
   * @return Text of the parameter's value
   */
  String getText();

  /**
   * @return Expression if the parameter's value is an expression, otherwise null
   */
  Expression getExpression();

  /**
   * @return Name of the parameter
   */
  String getName();

  /**
   * @return Name of the referenced property when referential constraints are used
   */
  String getReferencedProperty();
}
