package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.uri.queryoption.expression.Member;

final class JPALambdaAnyOperation extends JPALambdaOperation implements JPAExpressionOperator {

  JPALambdaAnyOperation(final JPAFilterCompilerAccess jpaCompiler, final Member member) {
    super(jpaCompiler, member);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Enum<?> getOperator() {
    return null;
  }

  @Override
  public String getName() {
    return "ANY";
  }

}
