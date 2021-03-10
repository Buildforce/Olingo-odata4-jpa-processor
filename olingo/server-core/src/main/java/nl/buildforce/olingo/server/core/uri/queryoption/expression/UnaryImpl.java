/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Unary;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

public class UnaryImpl implements Unary {

  private final UnaryOperatorKind operator;
  private final Expression expression;
  private final EdmType type;

  public UnaryImpl(UnaryOperatorKind operator, Expression expression, EdmType type) {
    this.operator = operator;
    this.expression = expression;
    this.type = type;
  }

  @Override
  public UnaryOperatorKind getOperator() {
    return operator;
  }

/*
  @Override
  public Expression getOperand() {
    return expression;
  }
*/

  public EdmType getType() {
    return type;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    T operand = expression.accept(visitor);
    return visitor.visitUnaryOperator(operator, operand);
  }

  @Override
  public String toString() {
    return "{" + operator.name() + " " + expression + '}';
  }

}