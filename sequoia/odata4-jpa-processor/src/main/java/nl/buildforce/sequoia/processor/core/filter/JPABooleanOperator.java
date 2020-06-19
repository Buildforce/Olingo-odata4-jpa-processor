package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.Expression;

public interface JPABooleanOperator extends JPAExpressionOperator {

  Expression<Boolean> getLeft() throws ODataApplicationException;

  Expression<Boolean> getRight() throws ODataApplicationException;

}