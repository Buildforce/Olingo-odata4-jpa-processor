package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

public interface JPAArithmeticOperator extends JPAOperator {
  @Override
  Expression<Number> get() throws ODataApplicationException;

  BinaryOperatorKind getOperator();

  Object getRight();

  Expression<Number> getLeft(CriteriaBuilder cb) throws ODataApplicationException;

  Number getRightAsNumber(CriteriaBuilder cb) throws ODataApplicationException;

  Expression<Number> getRightAsExpression() throws ODataApplicationException;

  Expression<Integer> getLeftAsIntExpression() throws ODataApplicationException;

  Expression<Integer> getRightAsIntExpression() throws ODataApplicationException;

}