/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.apply;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

/**
 * Represents a compute expression.
 * @see Compute
 */
public interface ComputeExpression {

  /**
   * Gets the expression to compute.
   * @return an {@link Expression} (but never <code>null</code>)
   */
  Expression getExpression();

  /**
   * Gets the name of the computation result if an alias name has been set.
   * @return an identifier String (but never <code>null</code>)
   */
  String getAlias();
}
