package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPADataBaseFunction;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEnumerationAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAFilterException;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourceProperty;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

class JPAVisitor implements JPAExpressionVisitor {

  /**
   *
   */
  private final JPAFilterCompilerAccess jpaCompiler;
  private final JPAServiceDebugger debugger;

  /**
   * @param jpaFilterCrossCompiler
   */
  JPAVisitor(final JPAFilterCompilerAccess jpaFilterCrossCompiler) {
    this.jpaCompiler = jpaFilterCrossCompiler;
    this.debugger = jpaCompiler.getDebugger();
  }

  @Override
  public OData getOdata() {
    return jpaCompiler.getOdata();
  }

  @Override
  public From<?, ?> getRoot() {
    return jpaCompiler.getRoot();
  }

  @Override
  public JPAOperator visitAlias(final String aliasName) throws ODataApplicationException {

    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
        HttpStatusCode.NOT_IMPLEMENTED, "Alias");
  }

  @SuppressWarnings("rawtypes")
  @Override
  public JPAOperator visitBinaryOperator(final BinaryOperatorKind operator, final JPAOperator left,
      final JPAOperator right) throws ODataApplicationException {
    int handle = debugger.startRuntimeMeasurement(this, "visitBinaryOperator"); // NOSONAR

    if (operator == BinaryOperatorKind.AND || operator == BinaryOperatorKind.OR) {
      // Connecting operations have to be handled first, as JPANavigationOperation do not need special treatment
      debugger.stopRuntimeMeasurement(handle);
      return new JPABooleanOperatorImp(this.jpaCompiler.getConverter(), operator, (JPAExpression) left,
          (JPAExpression) right);
    }
    if (left instanceof JPANavigationOperation || right instanceof JPANavigationOperation)
      return handleBinaryWithNavigation(operator, left, right);
    if (hasNavigation(left) || hasNavigation(right))
      return new JPANavigationOperation(this.jpaCompiler, operator, left, right);
    if (operator == BinaryOperatorKind.EQ
        || operator == BinaryOperatorKind.NE
        || operator == BinaryOperatorKind.GE
        || operator == BinaryOperatorKind.GT
        || operator == BinaryOperatorKind.LT
        || operator == BinaryOperatorKind.LE
        || operator == BinaryOperatorKind.HAS) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPAComparisonOperatorImp(this.jpaCompiler.getConverter(), operator, left, right);
    }
    if (operator == BinaryOperatorKind.ADD
        || operator == BinaryOperatorKind.SUB
        || operator == BinaryOperatorKind.MUL
        || operator == BinaryOperatorKind.DIV
        || operator == BinaryOperatorKind.MOD) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPAArithmeticOperatorImp(this.jpaCompiler.getConverter(), operator, left, right);
    }
    debugger.stopRuntimeMeasurement(handle);
    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_OPERATOR,
        HttpStatusCode.NOT_IMPLEMENTED, operator.name());
  }

  @Override
  public JPAOperator visitBinaryOperator(BinaryOperatorKind operator, JPAOperator left, List<JPAOperator> right)
      throws ODataApplicationException {
    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_OPERATOR,
        HttpStatusCode.NOT_IMPLEMENTED, operator.name());
  }

  @Override
  public JPAEnumerationOperator visitEnum(final EdmEnumType type, final List<String> enumValues) throws ODataApplicationException {
    final int handle = debugger.startRuntimeMeasurement(this, "visitEnum");
    final JPAEnumerationAttribute jpaEnumerationAttribute = this.jpaCompiler.getSd().getEnumType(type);
    try {
      if (!jpaEnumerationAttribute.isFlags() && enumValues.size() > 1)
        throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
            HttpStatusCode.NOT_IMPLEMENTED, "Collection of Enumerations if not flags");
    } catch (ODataJPAModelException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    debugger.stopRuntimeMeasurement(handle);
    return new JPAEnumerationOperator(this.jpaCompiler.getSd().getEnumType(type), enumValues);
  }

  @Override
  public JPAOperator visitLambdaExpression(final String lambdaFunction, final String lambdaVariable,
      final org.apache.olingo.server.api.uri.queryoption.expression.Expression expression)
      throws ODataApplicationException {

    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
        HttpStatusCode.NOT_IMPLEMENTED, "Lambda Expression");
  }

  @Override
  public JPAOperator visitLambdaReference(final String variableName) throws
          ODataApplicationException {

    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
        HttpStatusCode.NOT_IMPLEMENTED, "Lambda Reference");
  }

  @Override
  public JPAOperator visitLiteral(final Literal literal) {
    final int handle = debugger.startRuntimeMeasurement(this, "visitBinaryOperator");
    debugger.stopRuntimeMeasurement(handle);
    return new JPALiteralOperator(this.jpaCompiler.getOdata(), literal);
  }

  @Override
  public JPAOperator visitMember(final Member member) throws ODataApplicationException {

    final int handle = debugger.startRuntimeMeasurement(this, "visitMember");
    if (getLambdaType(member.getResourcePath()) == UriResourceKind.lambdaAny) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPALambdaAnyOperation(this.jpaCompiler, member);
    } else if (getLambdaType(member.getResourcePath()) == UriResourceKind.lambdaAll) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPALambdaAllOperation(this.jpaCompiler, member);
    } else if (isAggregation(member.getResourcePath())) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPAAggregationOperationImp(jpaCompiler.getRoot(), jpaCompiler.getConverter());
    } else if (isCustomFunction(member.getResourcePath())) {
      final UriResource resource = member.getResourcePath().getUriResourceParts().get(0);
      final JPADataBaseFunction jpaFunction = (JPADataBaseFunction) this.jpaCompiler.getSd().getFunction(
          ((UriResourceFunction) resource).getFunction());
      final List<UriParameter> odataParams = ((UriResourceFunction) resource).getParameters();
      debugger.stopRuntimeMeasurement(handle);
      return new JPAFunctionOperator(this, odataParams, jpaFunction);
    }
    debugger.stopRuntimeMeasurement(handle);
    return new JPAMemberOperator(this.jpaCompiler.getJpaEntityType(), this.jpaCompiler.getRoot(), member, jpaCompiler
        .getAssociation(), this.jpaCompiler.getGroups());
  }

  @Override
  public JPAOperator visitMethodCall(final MethodKind methodCall, final List<JPAOperator> parameters)
      throws ODataApplicationException {

    final int handle = debugger.startRuntimeMeasurement(this, "visitMethodCall");
    if (!parameters.isEmpty()) {
      if (parameters.get(0) instanceof JPANavigationOperation ||
          parameters.size() == 2 && parameters.get(1) instanceof JPANavigationOperation)
        throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
            HttpStatusCode.NOT_IMPLEMENTED, "Nested method calls together with navigation");
      if ((hasNavigation(parameters.get(0)) || parameters.size() == 2 && hasNavigation(parameters.get(1)))) {
        return new JPANavigationOperation(this.jpaCompiler, methodCall, parameters);
      }
    }
    JPAMethodCall method = new JPAMethodCallImp(this.jpaCompiler.getConverter(), methodCall, parameters);
    if (method.get() instanceof Predicate)
      method = new JPAMethodBasedExpression(this.jpaCompiler.getConverter(), methodCall, parameters);
    debugger.stopRuntimeMeasurement(handle);
    return method;
  }

  @Override
  public JPAOperator visitTypeLiteral(final EdmType type) throws ODataApplicationException {
    throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
        HttpStatusCode.NOT_IMPLEMENTED, "Type Literal");
  }

  @Override
  public JPAOperator visitUnaryOperator(final UnaryOperatorKind operator, final JPAOperator operand)
      throws ODataApplicationException {
    final int handle = debugger.startRuntimeMeasurement(this, "visitBinaryOperator");
    if (operator == UnaryOperatorKind.NOT) {
      debugger.stopRuntimeMeasurement(handle);
      return new JPAUnaryBooleanOperatorImp(this.jpaCompiler.getConverter(), operator, (JPAExpressionOperator) operand);
    } else {
      debugger.stopRuntimeMeasurement(handle);
      throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_OPERATOR,
          HttpStatusCode.NOT_IMPLEMENTED, operator.name());
    }
  }

  CriteriaBuilder getCriteriaBuilder() {
    return jpaCompiler.getConverter().cb;
  }

  UriResourceKind getLambdaType(final UriInfoResource member) {
    for (final UriResource r : member.getUriResourceParts()) {
      if (r.getKind() == UriResourceKind.lambdaAny
          || r.getKind() == UriResourceKind.lambdaAll)
        return r.getKind();
    }
    return null;
  }

/*
  JPAServiceDocument getSd() {
    return jpaCompiler.getSd();
  }
*/

  boolean hasNavigation(final JPAOperator operand) {
    if (operand instanceof JPAMemberOperator) {
      final List<UriResource> uriResourceParts = ((JPAMemberOperator) operand).getMember().getResourcePath()
          .getUriResourceParts();
      for (int i = uriResourceParts.size() - 1; i >= 0; i--) {
        if (uriResourceParts.get(i) instanceof UriResourceNavigation
            || (uriResourceParts.get(i) instanceof UriResourceProperty
                && ((UriResourceProperty) uriResourceParts.get(i)).isCollection()))
          return true;
      }
    }
    return false;
  }

  private JPAOperator handleBinaryWithNavigation(final BinaryOperatorKind operator, final JPAOperator left,
      final JPAOperator right) throws ODataJPAFilterException {
    if (left instanceof JPANavigationOperation && right instanceof JPANavigationOperation)
      throw new ODataJPAFilterException(ODataJPAFilterException.MessageKeys.NOT_SUPPORTED_FILTER,
          HttpStatusCode.NOT_IMPLEMENTED, "Binary operations comparing two navigations");

    if (left instanceof JPANavigationOperation) {
      return new JPANavigationOperation(operator, (JPANavigationOperation) left, (JPALiteralOperator) right,
          jpaCompiler);
    }
    return new JPANavigationOperation(operator, (JPANavigationOperation) right, (JPALiteralOperator) left, jpaCompiler);
  }

  private boolean isAggregation(final UriInfoResource resourcePath) {

    return (resourcePath.getUriResourceParts().size() == 1
        && resourcePath.getUriResourceParts().get(0).getKind() == UriResourceKind.count);
  }

  private boolean isCustomFunction(final UriInfoResource resourcePath) {

    return (!resourcePath.getUriResourceParts().isEmpty()
        && resourcePath.getUriResourceParts().get(0) instanceof UriResourceFunction);
  }

}