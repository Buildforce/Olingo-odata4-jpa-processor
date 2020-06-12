package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.Expression;

public interface JPAComparisonOperator<T extends Comparable<T>> extends JPAExpressionOperator {

  Expression<T> getLeft() throws ODataApplicationException;

  Object getRight();

  Comparable<T> getRightAsComparable() throws ODataApplicationException;

  Expression<T> getRightAsExpression() throws ODataApplicationException;

}