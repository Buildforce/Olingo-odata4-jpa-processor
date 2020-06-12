package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.Unary;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

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
    private final org.apache.olingo.server.api.uri.queryoption.expression.Expression expression;

    public NotExpression(final org.apache.olingo.server.api.uri.queryoption.expression.Expression expression) {
      this.expression = expression;
    }

    @Override
    public <T> T accept(final ExpressionVisitor<T> visitor) throws ExpressionVisitException, ODataApplicationException {
      final T operand = expression.accept(visitor);
      return visitor.visitUnaryOperator(getOperator(), operand);
    }

    @Override
    public org.apache.olingo.server.api.uri.queryoption.expression.Expression getOperand() {
      return expression;
    }

    @Override
    public UnaryOperatorKind getOperator() {
      return UnaryOperatorKind.NOT;
    }
  }

}