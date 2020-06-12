package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPADescriptionAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAOnConditionItem;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAFilterElementCompiler;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAOperationConverter;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;
import org.apache.olingo.server.api.uri.queryoption.expression.VisitableExpression;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Subquery;
import java.util.*;

public final class JPANavigationFilterQuery extends JPANavigationQuery {
  private final List<UriParameter> keyPredicates;

  public JPANavigationFilterQuery(final OData odata, final JPAServiceDocument sd, final UriResource uriResourceItem,
      final JPAAbstractQuery parent, final EntityManager em, final JPAAssociationPath association,
      final From<?, ?> from, final Optional<JPAODataClaimProvider> claimsProvider) throws ODataApplicationException {

    super(odata, sd, (EdmEntityType) ((UriResourcePartTyped) uriResourceItem).getType(), em, parent, from, association,
        claimsProvider);
    this.keyPredicates = Util.determineKeyPredicates(uriResourceItem);
    this.subQuery = parent.getQuery().subquery(this.jpaEntity.getKeyType());

    this.locale = parent.getLocale();
    this.filterCompiler = null;
    this.aggregationType = null;
    createRoots(association);
  }

  public JPANavigationFilterQuery(final OData odata, final JPAServiceDocument sd, final UriResource uriResourceItem,
      final JPAAbstractQuery parent, final EntityManager em, final JPAAssociationPath association,
      final VisitableExpression expression, final From<?, ?> from,
      final Optional<JPAODataClaimProvider> claimsProvider, final List<String> groups)
      throws ODataApplicationException {

    super(odata, sd, (EdmEntityType) ((UriResourcePartTyped) uriResourceItem).getType(), em, parent, from,
        association, claimsProvider);
    this.keyPredicates = Util.determineKeyPredicates(uriResourceItem);
    this.subQuery = parent.getQuery().subquery(this.jpaEntity.getKeyType());

    this.locale = parent.getLocale();

    this.filterCompiler = new JPAFilterElementCompiler(odata, sd, em, jpaEntity, new JPAOperationConverter(cb,
        getContext().getOperationConverter()), null, this, expression, null, groups);
    this.aggregationType = getAggregationType(this.filterCompiler.getExpressionMember());
    createRoots(association);
    createDescriptionJoin();
  }

  /*
   * (non-Javadoc)
   *

   * @see nl.buildforce.sequoia.jpa.processor.core.query.JPANavigationQuery#getRoot()
   */
  @Override
  public From<?, ?> getRoot() {
    assert queryRoot != null;
    return queryRoot;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * nl.buildforce.sequoia.jpa.processor.core.query.JPANavigationQuery#getSubQueryExists(javax.persistence.criteria.Subquery)
   */

  @Override
  @SuppressWarnings("unchecked")
  public <T> Subquery<T> getSubQueryExists(final Subquery<?> childQuery)
      throws ODataApplicationException {
    final Subquery<T> query = (Subquery<T>) this.subQuery;

    if (this.queryJoinTable != null) {
      if (this.aggregationType != null)
        createSubQueryJoinTableAggregation();
      else
        createSubQueryJoinTable();
    } else {
      createSubQueryAggregation(childQuery, query);
    }
    return query;
  }

  private void createDescriptionJoin() throws ODataApplicationException {
    final HashMap<String, From<?, ?>> joinTables = new HashMap<>();
    generateDescriptionJoin(joinTables, determineAllDescriptionPath(), getRoot());
  }

  private <T> void createSubQueryAggregation(final Subquery<?> childQuery, final Subquery<T> query)
      throws ODataApplicationException {

    List<JPAOnConditionItem> conditionItems = determineJoinColumns();
    createSelectClause(query, queryRoot, conditionItems);
    Expression<Boolean> whereCondition;
    whereCondition = addWhereClause(
        createWhereByAssociation(from, queryRoot, conditionItems),
        createWhereByKey(queryRoot, null, this.keyPredicates, jpaEntity));
    if (childQuery != null) {
      whereCondition = cb.and(whereCondition, cb.exists(childQuery));
    }
    whereCondition = addWhereClause(whereCondition, createProtectionWhereForEntityType(claimsProvider, jpaEntity,
        queryRoot));

    query.where(applyAdditionalFilter(whereCondition));
    handleAggregation(query, queryRoot, conditionItems);
  }

  private Set<JPAPath> determineAllDescriptionPath() throws ODataApplicationException {
    Set<JPAPath> allPath = new HashSet<>();
    if (filterCompiler != null) {
      for (JPAPath path : filterCompiler.getMember()) {
        if (path.getLeaf() instanceof JPADescriptionAttribute)
          allPath.add(path);
      }
    }
    return allPath;
  }

  private List<JPAOnConditionItem> determineJoinColumns() throws ODataJPAQueryException {

    try {
      List<JPAOnConditionItem> conditionItems = association.getJoinColumnsList();
      if (conditionItems.isEmpty())
        throw new ODataJPAQueryException(ODataJPAQueryException.MessageKeys.QUERY_PREPARATION_JOIN_NOT_DEFINED,
            HttpStatusCode.INTERNAL_SERVER_ERROR, association.getTargetType().getExternalName(), association
                .getSourceType().getExternalName());
      return conditionItems;

    } catch (ODataJPAModelException e) {
      throw new ODataJPAQueryException(ODataJPAQueryException.MessageKeys.QUERY_RESULT_NAVI_PROPERTY_UNKNOWN,
          HttpStatusCode.INTERNAL_SERVER_ERROR, e, association.getAlias());
    }
  }

}