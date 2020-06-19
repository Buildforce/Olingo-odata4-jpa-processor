package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Literal;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.VisitableExpression;

public class JPABinaryExpression implements VisitableExpression {

  private final BinaryOperatorKind operator;
  private final VisitableExpression expression;
  private final Literal literal;

  public JPABinaryExpression(final VisitableExpression expression, final Literal literal,
      final BinaryOperatorKind operator) {
    this.operator = operator;
    this.expression = expression;
    this.literal = literal;
  }

  @Override
  public <T> T accept(ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
    T localLeft = this.expression.accept(visitor);
    T localRight = this.literal.accept(visitor);
    return visitor.visitBinaryOperator(operator, localLeft, localRight);
  }

}