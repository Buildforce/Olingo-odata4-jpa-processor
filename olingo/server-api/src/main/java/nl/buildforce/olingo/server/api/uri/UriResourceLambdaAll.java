/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Used to describe an all lambda expression used within an resource path
 * For example: http://.../serviceroot/entityset/all(...)
 */
public interface UriResourceLambdaAll extends UriResourcePartTyped {

  /**
   * @return Name of the lambda variable
   */
  String getLambdaVariable();

  /**
   * @return Lambda expression
   */
  Expression getExpression();
}
