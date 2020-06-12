package nl.buildforce.sequoia.jpa.processor.core.filter;

public interface JPAExpressionOperator extends JPAExpression {

  <E extends Enum<E>> Enum<E> getOperator();

}