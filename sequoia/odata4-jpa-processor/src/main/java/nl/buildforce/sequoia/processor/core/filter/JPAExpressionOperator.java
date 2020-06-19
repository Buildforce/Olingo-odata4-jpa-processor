package nl.buildforce.sequoia.processor.core.filter;

public interface JPAExpressionOperator extends JPAExpression {

  <E extends Enum<E>> Enum<E> getOperator();

}