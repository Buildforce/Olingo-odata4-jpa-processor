/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.uri.queryoption.expression;

import java.util.List;

/**
 * Represents a binary expression node in the expression tree
 * <br>
 * A binary expression node is inserted in the expression tree for any valid
 * ODATA binary operator in {@link BinaryOperatorKind}.
 */
public interface Binary extends Expression {

// --Commented out by Inspection START (''21-03-07 09:25):
//  /**
//   * @return binary operator kind
//   * @see BinaryOperatorKind
//   */
//  BinaryOperatorKind getOperator();
// --Commented out by Inspection STOP (''21-03-07 09:25)

  /**
   * @return Expression sub tree of the left operand
   */
  Expression getLeftOperand();

  /**
   * @return Expression sub tree of the right operand
   */
  Expression getRightOperand();
  
// --Commented out by Inspection START (''21-03-07 09:25):
//  /**
//   *
//   * @return list of expressions of the right operand
//   */
//  List<Expression> getExpressions();
// --Commented out by Inspection STOP (''21-03-07 09:25)

}