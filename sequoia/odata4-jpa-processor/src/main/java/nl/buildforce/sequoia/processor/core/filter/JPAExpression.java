package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;

import jakarta.persistence.criteria.Expression;

public interface JPAExpression extends JPAOperator {

  @Override
  Expression<Boolean> get() throws ODataApplicationException;

}