package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import jakarta.persistence.criteria.Expression;

public interface JPAUnaryBooleanOperator extends JPAExpressionOperator {

  @Override
  Expression<Boolean> get() throws ODataApplicationException;

  Expression<Boolean> getLeft() throws ODataApplicationException;

  @SuppressWarnings("unchecked")
  @Override
  UnaryOperatorKind getOperator();

}