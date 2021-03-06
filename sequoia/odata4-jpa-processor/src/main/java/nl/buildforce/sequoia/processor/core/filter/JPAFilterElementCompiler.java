package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.processor.core.api.JPAODataClaimProvider;
// import nl.buildforce.sequoia.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.processor.core.query.JPAAbstractQuery;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.VisitableExpression;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;

import java.util.List;
import java.util.Optional;

/**
 * Compiles just one Expression. Mainly build for filter on navigation
 *
 */
//TODO handle $it ...
public final class JPAFilterElementCompiler extends JPAAbstractFilter {
  final JPAOperationConverter converter;
  final EntityManager em;
  final OData odata;
  final JPAServiceDocument sd;
  final List<UriResource> uriResourceParts;
  final JPAAbstractQuery parent;
  final List<String> groups;

  public JPAFilterElementCompiler(final OData odata, final JPAServiceDocument sd, final EntityManager em,
      final JPAEntityType jpaEntityType, final JPAOperationConverter converter,
      final List<UriResource> uriResourceParts, final JPAAbstractQuery parent, final VisitableExpression expression,
      final JPAAssociationPath association, final List<String> groups) {

    super(jpaEntityType, expression, association);
    this.converter = converter;
    this.em = em;
    this.odata = odata;
    this.sd = sd;
    this.uriResourceParts = uriResourceParts;
    this.parent = parent;
    this.groups = groups;

  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.processor.core.filter.JPAFilterCompiler#compile()
   */
  @Override
  @SuppressWarnings("unchecked")
  public Expression<Boolean> compile() throws ExpressionVisitException, ODataApplicationException {
    // final int handle = parent.getDebugger().startRuntimeMeasurement("JPAFilterCrossCompiler", "compile");

    final ExpressionVisitor<JPAOperator> visitor = new JPAVisitor(this);

    // parent.getDebugger().stopRuntimeMeasurement(handle);
    return (Expression<Boolean>) expression.accept(visitor).get();
  }

  @Override
  public JPAOperationConverter getConverter() {
    return converter;
  }

  @Override
  public JPAEntityType getJpaEntityType() {
    return jpaEntityType;
  }

  @Override
  public EntityManager getEntityManager() {
    return em;
  }

  @Override
  public OData getOdata() {
    return odata;
  }

  @Override
  public JPAServiceDocument getSd() {
    return sd;
  }

  @Override
  public List<UriResource> getUriResourceParts() {
    return uriResourceParts;
  }

  @Override
  public JPAAbstractQuery getParent() {
    return parent;
  }

  public VisitableExpression getExpressionMember() {
    return expression;
  }

  @Override
  public From<?, ?> getRoot() {
    return parent.getRoot();
  }

  /*@Override
  public JPAServiceDebugger getDebugger() {
    return parent.getDebugger();
  }*/

  @Override
  public Optional<JPAODataClaimProvider> getClaimsProvider() {
    return Optional.empty();
  }

  @Override
  public List<String> getGroups() {
    return groups;
  }

}