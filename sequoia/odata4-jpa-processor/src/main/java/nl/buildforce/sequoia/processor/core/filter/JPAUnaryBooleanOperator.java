package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import jakarta.persistence.criteria.Expression;

public interface JPAUnaryBooleanOperator extends JPAExpressionOperator {

  @Override
  Expression<Boolean> get() throws ODataApplicationException;

  Expression<Boolean> getLeft() throws ODataApplicationException;

  @SuppressWarnings("unchecked")
  @Override
  UnaryOperatorKind getOperator();

}