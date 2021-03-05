package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Literal;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;

public final class JPAFilterExpression implements JPAVisitableExpression {
  private final Literal literal;
  private final BinaryOperatorKind operator;
  private final Member member;

  public JPAFilterExpression(final Member member, final Literal literal, final BinaryOperatorKind operator) {
    this.literal = literal;
    this.operator = operator;
    this.member = member;
  }

  @Override
  public <T> T accept(final ExpressionVisitor<T> visitor) throws ODataApplicationException {
    final T left = visitor.visitMember(member);
    final T right = visitor.visitLiteral(literal);
    return visitor.visitBinaryOperator(operator, left, right);
  }

  @Override
  public UriInfoResource getMember() {
    return member.getResourcePath();
  }

  @Override
  public String toString() {
    return "JPAFilterExpression [literal=" + literal
        + ", operator=" + operator + ", member="
        + "[resourcePath="
        + member.getResourcePath().getUriResourceParts()
        + ", startTypeFilter= " + member.getStartTypeFilter()
        + ", type= " + member.getType()
        + "]]";
  }

}