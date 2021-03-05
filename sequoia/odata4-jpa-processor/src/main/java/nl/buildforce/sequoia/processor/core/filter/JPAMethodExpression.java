package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Literal;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.MethodKind;

import java.util.ArrayList;
import java.util.List;

public final class JPAMethodExpression implements JPAVisitableExpression {
  private final MethodKind methodCall;
  private final Member member;
  private final Literal literal;

  public JPAMethodExpression(final Member member, final JPALiteralOperator operand, final MethodKind methodCall) {

    this.methodCall = methodCall;
    this.member = member;
    this.literal = operand != null ? operand.getLiteral() : null;
  }

  @Override
  public <T> T accept(final ExpressionVisitor<T> visitor) throws ODataApplicationException {
    final List<T> parameters = new ArrayList<>(2);

    parameters.add(visitor.visitMember(member));
    if (literal != null)
      parameters.add(visitor.visitLiteral(literal));
    return visitor.visitMethodCall(methodCall, parameters);
  }

  @Override
  public UriInfoResource getMember() {
    return member.getResourcePath();
  }

}