package nl.buildforce.sequoia.jpa.processor.core.database;

import nl.buildforce.sequoia.jpa.processor.core.filter.JPAAggregationOperation;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAArithmeticOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPABooleanOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAComparisonOperator;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAMethodCall;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAUnaryBooleanOperator;
import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

public interface JPAODataDatabaseOperations {

  void setCriterialBuilder(final CriteriaBuilder cb);

  <T extends Number> Expression<T> convert(final JPAArithmeticOperator jpaOperator)
      throws ODataApplicationException;

  Expression<Boolean> convert(final JPABooleanOperator jpaOperator) throws ODataApplicationException;

  <T extends Comparable<T>> Expression<Boolean> convert(final JPAComparisonOperator<T> jpaOperator)
      throws ODataApplicationException;

  <T> Expression<T> convert(final JPAMethodCall jpaFunction) throws ODataApplicationException;

  Expression<Boolean> convert(final JPAUnaryBooleanOperator jpaOperator) throws ODataApplicationException;

  Expression<Long> convert(final JPAAggregationOperation jpaOperator) throws ODataApplicationException;

}