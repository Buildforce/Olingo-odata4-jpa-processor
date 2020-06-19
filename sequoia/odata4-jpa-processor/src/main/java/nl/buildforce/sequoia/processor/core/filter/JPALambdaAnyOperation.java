package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;

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
