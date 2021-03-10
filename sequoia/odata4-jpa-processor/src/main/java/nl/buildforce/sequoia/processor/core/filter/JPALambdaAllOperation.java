package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Unary;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Subquery;

final class JPALambdaAllOperation extends JPALambdaOperation {

  JPALambdaAllOperation(final JPAFilterCompilerAccess jpaCompiler, final Member member) {
    super(jpaCompiler, member);
  }

  public Subquery<?> getNotExistsQuery() throws ODataApplicationException {
    return getSubQuery(new NotExpression(determineExpression()));
  }

  @Override
  public Expression<Boolean> get() throws ODataApplicationException {
    final CriteriaBuilder cb = converter.cb;

    return cb.and(cb.exists(getExistsQuery()), cb.not(cb.exists(getNotExistsQuery())));
  }

  @Override
  public String getName() { return "ALL"; }

  private static class NotExpression implements Unary {
    private final nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression expression;

    public NotExpression(final nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression expression) {
      this.expression = expression;
    }

    @Override
    public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
      final T operand = expression.accept(visitor);
      return visitor.visitUnaryOperator(getOperator(), operand);
    }

/*
    @Override
    public nl.buildforce.olingo.server.api.uri.queryoption.expression.Expression getOperand() {
      return expression;
    }
*/

    @Override
    public UnaryOperatorKind getOperator() {
      return UnaryOperatorKind.NOT;
    }
  }

}