/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

/**
 * Represents a unary expression node in the expression tree
 * <br>
 * A binary expression node is inserted in the expression tree for any valid
 * ODATA unary operator in {@link UnaryOperatorKind}
 */
public interface Unary extends Expression {

  /**
   * @return The used binary operator
   * @see UnaryOperatorKind
   */
  Expression getOperand();

  /**
   * @return Expression sub tree to which the operator applies
   */
  UnaryOperatorKind getOperator();

}