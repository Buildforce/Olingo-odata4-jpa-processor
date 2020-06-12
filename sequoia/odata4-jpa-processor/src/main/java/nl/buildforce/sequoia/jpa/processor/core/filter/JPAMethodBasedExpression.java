package nl.buildforce.sequoia.jpa.processor.core.filter;

import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;

import jakarta.persistence.criteria.Expression;
import java.util.List;

final class JPAMethodBasedExpression extends JPAMethodCallImp implements JPAExpression {

  public JPAMethodBasedExpression(JPAOperationConverter converter, MethodKind methodCall,
      List<JPAOperator> parameters) {
    super(converter, methodCall, parameters);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Expression<Boolean> get() throws ODataApplicationException {
    return (Expression<Boolean>) super.get();
  }

}