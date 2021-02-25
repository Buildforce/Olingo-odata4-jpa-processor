/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import java.util.List;

/**
 * Represents a method expression in the expression tree
 */
public interface Method extends Expression {

  /**
   * @return The used method
   * @see MethodKind
   */
  MethodKind getMethod();

  /**
   * @return The list of expression tree which form the actual method parameters
   */
  List<Expression> getParameters();

}