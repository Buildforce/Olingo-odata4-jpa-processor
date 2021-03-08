/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.uri.queryoption.expression;

import java.util.ArrayList;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Binary;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression;

public class BinaryImpl implements Binary {

  private final Expression left;
  private final BinaryOperatorKind operator;
  private final Expression right;
  private final EdmType type;
  private final List<Expression> expressions;

  public BinaryImpl(Expression left, BinaryOperatorKind operator, Expression right,
                    EdmType type) {
    this.left = left;
    this.operator = operator;
    this.right = right;
    this.type = type;
    expressions = null;
  }
  
  public BinaryImpl(Expression left, BinaryOperatorKind operator, List<Expression> right,
                    EdmType type) {
    this.left = left;
    this.operator = operator;
    this.right = null;
    this.type = type;
    expressions = right;
  }

/*
  @Override
  public BinaryOperatorKind getOperator() {
    return operator;
  }

  @Override
  public List<Expression> getExpressions() {
    return expressions;
  }
*/

  @Override
  public Expression getLeftOperand() {
    return left;
  }

  @Override
  public Expression getRightOperand() {
    return right;
  }

  public EdmType getType() {
    return type;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    T localLeft = left.accept(visitor);
    if (right != null) {
      T localRight = right.accept(visitor);
      return visitor.visitBinaryOperator(operator, localLeft, localRight);
    } else if (expressions != null) {
      List<T> expressions = new ArrayList<>();
      for (Expression expression : this.expressions) {
        expressions.add(expression.accept(visitor));
      }
      return visitor.visitBinaryOperator(operator, localLeft, expressions);
    }
    return null;
  }

  @Override
  public String toString() {
    return "{" + left + " " + operator.name() + " " + (null != right ? right : expressions) + '}';
  }

}