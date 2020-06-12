package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.jpa.processor.core.query.Util;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriResourceKind;
import org.apache.olingo.server.api.uri.queryoption.expression.*;

import java.util.ArrayList;
import java.util.List;

final class JPAMemberVisitor implements ExpressionVisitor<JPAPath> {
  private final ArrayList<JPAPath> pathList = new ArrayList<>();
  private final JPAEntityType jpaEntityType;

  public JPAMemberVisitor(final JPAEntityType jpaEntityType) {
    this.jpaEntityType = jpaEntityType;
  }

  public List<JPAPath> get() {
    return pathList;
  }

  @Override
  public JPAPath visitBinaryOperator(final BinaryOperatorKind operator, final JPAPath left, final JPAPath right) {
    return null;
  }

  @Override
  public JPAPath visitBinaryOperator(BinaryOperatorKind operator, JPAPath left, List<JPAPath> right) {
    return null;
  }

  @Override
  public JPAPath visitUnaryOperator(final UnaryOperatorKind operator, final JPAPath operand) {
    return null;
  }

  @Override
  public JPAPath visitMethodCall(final MethodKind methodCall, final List<JPAPath> parameters) {
    return null;
  }

  @Override
  public JPAPath visitLambdaExpression(final String lambdaFunction, final String lambdaVariable,
      final org.apache.olingo.server.api.uri.queryoption.expression.Expression expression) {
    return null;
  }

  @Override
  public JPAPath visitLiteral(final Literal literal) {
    return null;
  }

  @Override
  public JPAPath visitMember(final Member member) throws ODataApplicationException {
    final UriResourceKind uriResourceKind = member.getResourcePath().getUriResourceParts().get(0).getKind();

    if (uriResourceKind == UriResourceKind.primitiveProperty || uriResourceKind == UriResourceKind.complexProperty) {
      if (!Util.hasNavigation(member.getResourcePath().getUriResourceParts())) {
        final String path = Util.determinePropertyNavigationPath(member.getResourcePath().getUriResourceParts());
        JPAPath selectItemPath;
        try {
          selectItemPath = jpaEntityType.getPath(path);
        } catch (ODataJPAModelException e) {
          throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
        }
        if (selectItemPath != null) {
          pathList.add(selectItemPath);
          return selectItemPath;
        }
      }
    }
    return null;
  }

  @Override
  public JPAPath visitAlias(final String aliasName) {
    return null;
  }

  @Override
  public JPAPath visitTypeLiteral(final EdmType type) {
    return null;
  }

  @Override
  public JPAPath visitLambdaReference(final String variableName) {
    return null;
  }

  @Override
  public JPAPath visitEnum(final EdmEnumType type, final List<String> enumValues) {
    return null;
  }

}