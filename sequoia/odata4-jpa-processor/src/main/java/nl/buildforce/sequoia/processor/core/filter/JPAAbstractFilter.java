package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAQueryException;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.VisitableExpression;

import java.util.Collections;
import java.util.List;

public abstract class JPAAbstractFilter implements JPAFilterCompiler, JPAFilterCompilerAccess {
  final JPAEntityType jpaEntityType;
  final VisitableExpression expression;
  final JPAAssociationPath association;

  public JPAAbstractFilter(final JPAEntityType jpaEntityType, final VisitableExpression expression) {
    this(jpaEntityType, expression, null);
  }

  public JPAAbstractFilter(final JPAEntityType jpaEntityType, final UriInfoResource uriResource,
      final JPAAssociationPath association) {
    this.jpaEntityType = jpaEntityType;
    if (uriResource != null && uriResource.getFilterOption() != null) {
      this.expression = uriResource.getFilterOption().getExpression();
    } else {
      this.expression = null;
    }
    this.association = association;
  }

  public JPAAbstractFilter(final JPAEntityType jpaEntityType, final VisitableExpression expression,
      final JPAAssociationPath association) {
    this.jpaEntityType = jpaEntityType;
    this.expression = expression;
    this.association = association;
  }

  @Override
  public List<JPAPath> getMember() throws ODataApplicationException {
    final JPAMemberVisitor visitor = new JPAMemberVisitor(jpaEntityType);
    if (expression != null) {
      try {
        expression.accept(visitor);
      } catch (ExpressionVisitException e) {
        throw new ODataJPAQueryException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
      return Collections.unmodifiableList(visitor.get());
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public JPAAssociationPath getAssociation() {
    return association;
  }

}