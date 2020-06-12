package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAOperationResultParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAParameter;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.impl.JPATypeConverter;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import java.util.List;

/**
 * Handle OData Functions that are implemented e.g. as user defined database functions. This will be mapped
 * to JPA criteria builder function().
 *
 * @author Oliver Grande
 *
 */
final class JPAFunctionOperator implements JPAOperator {
  private final JPADataBaseFunction jpaFunction;
  private final JPAVisitor visitor;
  private final List<UriParameter> uriParams;

  public JPAFunctionOperator(final JPAVisitor jpaVisitor, final List<UriParameter> uriParams,
      final JPADataBaseFunction jpaFunction) {

    this.uriParams = uriParams;
    this.visitor = jpaVisitor;
    this.jpaFunction = jpaFunction;
  }

  @Override
  public Expression<?> get() throws ODataApplicationException {

    if (jpaFunction.getResultParameter().isCollection()) {
      throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FUNCTION_COLLECTION,
          HttpStatusCode.NOT_IMPLEMENTED);
    }

    if (!JPATypeConverter.isScalarType(
        jpaFunction.getResultParameter().getType())) {
      throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FUNCTION_NOT_SCALAR,
          HttpStatusCode.NOT_IMPLEMENTED);
    }

    final CriteriaBuilder cb = visitor.getCriteriaBuilder();
    List<JPAParameter> parameters;
    try {
      parameters = jpaFunction.getParameter();
    } catch (ODataJPAModelException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    final Expression<?>[] jpaParameter = new Expression<?>[parameters.size()];
    for (int i = 0; i < parameters.size(); i++) {
      // a. $it/Area b. Area c. 10000
      final UriParameter p = findUriParameter(parameters.get(i));

      if (p != null && p.getText() != null) {
        final JPALiteralOperator operator = new JPALiteralOperator(visitor.getOdata(), new ParameterLiteral(p
                .getText()));
        jpaParameter[i] = cb.literal(operator.get(parameters.get(i)));
      } else {
        try {
          jpaParameter[i] = (Expression<?>) p.getExpression().accept(visitor).get();
        } catch (ExpressionVisitException e) {
          throw new ODataJPAFilterException(e, HttpStatusCode.NOT_IMPLEMENTED);
        }
      }
    }
    return cb.function(jpaFunction.getDBName(), jpaFunction.getResultParameter().getType(), jpaParameter);
  }

  private UriParameter findUriParameter(final JPAParameter jpaFunctionParam) {
    for (final UriParameter uriParam : uriParams) {
      if (uriParam.getName().equals(jpaFunctionParam.getName())) {
        return uriParam;
      }
    }
    return null;
  }

  public JPAOperationResultParameter getReturnType() {
    return jpaFunction.getResultParameter();
  }

  @Override
  public String getName() {
    return jpaFunction.getDBName();
  }

  private static class ParameterLiteral implements Literal {

    public ParameterLiteral(final String text) {
      this.text = text;
    }

    private final String text;

    @Override
    public <T> T accept(final ExpressionVisitor<T> visitor) {
      return null;
    }

    @Override
    public String getText() {
      return text;
    }

    @Override
    public EdmType getType() {
      return null;
    }
  }

}