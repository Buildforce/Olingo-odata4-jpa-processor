package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.Expression;

public interface JPABooleanOperator extends JPAExpressionOperator {

  Expression<Boolean> getLeft() throws ODataApplicationException;

  Expression<Boolean> getRight() throws ODataApplicationException;

}