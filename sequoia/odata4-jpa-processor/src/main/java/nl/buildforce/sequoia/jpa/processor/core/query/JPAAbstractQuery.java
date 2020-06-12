package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPADescriptionAttribute;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAElement;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAProtectionInfo;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAClaimsPair;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataGroupProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAServiceDebugger;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.debug.RuntimeMeasurement;
import org.apache.olingo.server.api.uri.UriParameter;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.AbstractQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException.MessageKeys.MISSING_CLAIM;
import static nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException.MessageKeys.MISSING_CLAIMS_PROVIDER;
import static nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException.MessageKeys.QUERY_RESULT_ENTITY_TYPE_ERROR;
import static nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAQueryException.MessageKeys.WILDCARD_UPPER_NOT_SUPPORTED;

public abstract class JPAAbstractQuery {
  protected Locale locale;
  protected final CriteriaBuilder cb;
  protected final EntityManager em;
  protected final JPAEntityType jpaEntity;
  protected final JPAServiceDocument sd;
  protected final OData odata;
  protected final Optional<JPAODataClaimProvider> claimsProvider;
  protected JPAServiceDebugger debugger = new EmptyDebugger();
  protected List<String> groups = Collections.emptyList();

  public JPAAbstractQuery(final OData odata, final JPAServiceDocument sd, final JPAEntityType jpaEntityType,
      final EntityManager em, final Optional<JPAODataClaimProvider> claimsProvider) {
    this.em = em;
    this.cb = em.getCriteriaBuilder();
    this.sd = sd;
    this.jpaEntity = jpaEntityType;
    this.odata = odata;
    this.claimsProvider = claimsProvider;
  }

  public JPAAbstractQuery(final OData odata, final JPAServiceDocument sd, final JPAEntityType jpaEntityType,
      final EntityManager em, final JPAServiceDebugger debugger, final Optional<JPAODataClaimProvider> claimsProvider) {
    this.em = em;
    this.cb = em.getCriteriaBuilder();
    this.sd = sd;
    this.jpaEntity = jpaEntityType;
    this.debugger = debugger;
    this.odata = odata;
    this.claimsProvider = claimsProvider;
  }

  public JPAAbstractQuery(final OData odata, final JPAServiceDocument sd, final EdmEntityType edmEntityType,
      final EntityManager em, final Optional<JPAODataClaimProvider> claimsProvider) {
    this.em = em;
    this.cb = em.getCriteriaBuilder();
    this.sd = sd;
    this.jpaEntity = sd.getEntity(edmEntityType);
    this.odata = odata;
    this.claimsProvider = claimsProvider;
  }

  public JPAAbstractQuery(final OData odata,
                          final JPAServiceDocument sd,
                          final JPAEntityType jpaEntityType,
                          final JPAODataRequestContextAccess requestContext) {
    final Optional<JPAODataGroupProvider> groupsProvider = requestContext.getGroupsProvider();

      this.jpaEntity = jpaEntityType;
      this.odata = odata;
      this.sd = sd;
    em = requestContext.getEntityManager();
    cb = em.getCriteriaBuilder();
    debugger = requestContext.getDebugger();
    claimsProvider = requestContext.getClaimsProvider();
    groups = groupsProvider.isPresent() ? groupsProvider.get().getGroups() : Collections.emptyList();
  }

  protected Expression<Boolean> createWhereByKey(final From<?, ?> root,
      final Expression<Boolean> whereCondition, final List<UriParameter> keyPredicates,
      JPAEntityType et) throws ODataApplicationException {
    // .../Organizations('3')
    // .../BusinessPartnerRoles(BusinessPartnerID='6',RoleCategory='C')
    Expression<Boolean> compoundCondition = whereCondition;

    if (keyPredicates != null) {
      for (final UriParameter keyPredicate : keyPredicates) {
        Expression<Boolean> equalCondition;
        try {
          equalCondition = ExpressionUtil.createEQExpression(odata, cb, root, et, keyPredicate);
        } catch (ODataJPAModelException e) {
          throw new ODataJPAQueryException(e, HttpStatusCode.BAD_REQUEST);
        }
        if (compoundCondition == null)
          compoundCondition = equalCondition;
        else
          compoundCondition = cb.and(compoundCondition, equalCondition);
      }
    }
    return compoundCondition;
  }

  public abstract From<?, ?> getRoot();

  public abstract AbstractQuery<?> getQuery();

  public JPAServiceDebugger getDebugger() {
    return debugger;
  }

  protected abstract Locale getLocale();

  protected void generateDescriptionJoin(final HashMap<String, From<?, ?>> joinTables, final Set<JPAPath> pathSet,
      final From<?, ?> target) {

    for (final JPAPath descriptionFieldPath : pathSet) {
      final JPADescriptionAttribute descriptionField = ((JPADescriptionAttribute) descriptionFieldPath.getLeaf());
      Join<?, ?> join = createJoinFromPath(descriptionFieldPath.getAlias(), descriptionFieldPath.getPath(), target,
          JoinType.LEFT);
      if (descriptionField.isLocationJoin())
        join.on(createOnCondition(join, descriptionField, getLocale().toString()));
      else
        join.on(createOnCondition(join, descriptionField, getLocale().getLanguage()));
      joinTables.put(descriptionField.getInternalName(), join);
    }
  }

  protected <T, S> Join<T, S> createJoinFromPath(final String alias, final List<JPAElement> pathList,
      final From<T, S> root, final JoinType finalJoinType) {

    Join<T, S> join = null;
    JoinType jt;
    for (int i = 0; i < pathList.size(); i++) {
      if (i == pathList.size() - 1) jt = finalJoinType; else jt = JoinType.INNER;
      if (i == 0) {
        join = root.join(pathList.get(i).getInternalName(), jt);
        join.alias(alias);
      } else {
        join = join.join(pathList.get(i).getInternalName(), jt);
        join.alias(pathList.get(i).getExternalName());
      }
    }
    return join;
  }

  private Expression<Boolean> createOnCondition(Join<?, ?> join, JPADescriptionAttribute descriptionField,
      String localValue) {

    Expression<Boolean> result = cb.equal(determineLocalePath(join, descriptionField.getLocaleFieldName()), localValue);
    for (JPAPath value : descriptionField.getFixedValueAssignment().keySet()) {
      result = cb.and(result,
          cb.equal(determineLocalePath(join, value), descriptionField.getFixedValueAssignment().get(value)));
    }
    return result;
  }

  private Expression<?> determineLocalePath(final Join<?, ?> join,
      final JPAPath jpaPath) {
    Path<?> p = join;
    for (final JPAElement pathElement : jpaPath.getPath()) {
      p = p.get(pathElement.getInternalName());
    }
    return p;
  }

  abstract JPAODataCRUDContextAccess getContext();

  protected Expression<Boolean> addWhereClause(
      Expression<Boolean> whereCondition,
      final Expression<Boolean> additionalExpression) {

    if (additionalExpression != null) {
      if (whereCondition == null)
        whereCondition = additionalExpression;
      else
        whereCondition = cb.and(whereCondition, additionalExpression);
    }
    return whereCondition;
  }

  protected Expression<Boolean> orWhereClause(
      Expression<Boolean> whereCondition,
      final Expression<Boolean> additionalExpression) {

    if (additionalExpression != null) {
      if (whereCondition == null)
        whereCondition = additionalExpression;
      else
        whereCondition = cb.or(whereCondition, additionalExpression);
    }
    return whereCondition;
  }

  @SuppressWarnings({ "unchecked" })
  private <Y extends Comparable<? super Y>> Predicate createBetween(final JPAClaimsPair<?> value, final Path<?> p) {
    return cb.between((Expression<? extends Y>) p, (Y) value.min, (Y) value.max);
  }

  @SuppressWarnings("unchecked")
  private Expression<Boolean> createProtectionWhereForAttribute(
      final List<JPAClaimsPair<?>> values, final Path<?> p, final boolean wildcardsSupported)
      throws ODataJPAQueryException {

    Expression<Boolean> attriRestriction = null;
    for (final JPAClaimsPair<?> value : values) { // for each given claim value
      if (value.hasUpperBoundary)
        if (wildcardsSupported && ((String) value.min).matches(".*[*%+_].*"))
          throw new ODataJPAQueryException(WILDCARD_UPPER_NOT_SUPPORTED, HttpStatusCode.INTERNAL_SERVER_ERROR);
        else
          attriRestriction = orWhereClause(attriRestriction, createBetween(value, p));
      else {
        if (wildcardsSupported && ((String) value.min).matches(".*[*%+_].*"))
          attriRestriction = orWhereClause(attriRestriction, cb.like((Path<String>) p,
              ((String) value.min).replace('*', '%').replace('+', '_')));
        else
          attriRestriction = orWhereClause(attriRestriction, cb.equal(p, value.min));
      }
    }
    return attriRestriction;
  }

  protected Expression<Boolean> createProtectionWhereForEntityType(
      final Optional<JPAODataClaimProvider> claimsProvider, final JPAEntityType et, final From<?, ?> from)
      throws ODataJPAQueryException {
    try {
      Expression<Boolean> restriction = null;
      final Map<String, From<?, ?>> dummyJoinTables = new HashMap<>(1);
      for (final JPAProtectionInfo protection : et.getProtections()) { // look for protected attributes
        final List<JPAClaimsPair<?>> values = claimsProvider.get().get(protection.getClaimName()); // NOSONAR
        if (values.isEmpty())
          throw new ODataJPAQueryException(MISSING_CLAIM, HttpStatusCode.FORBIDDEN);
        final Path<?> p = ExpressionUtil.convertToCriteriaPath(dummyJoinTables, from, protection.getPath().getPath());
        restriction = addWhereClause(restriction, createProtectionWhereForAttribute(values, p, protection
            .supportsWildcards()));
      }
      return restriction;
    } catch (NoSuchElementException e) {
      throw new ODataJPAQueryException(MISSING_CLAIMS_PROVIDER, HttpStatusCode.FORBIDDEN);
    } catch (ODataJPAModelException e) {
      throw new ODataJPAQueryException(QUERY_RESULT_ENTITY_TYPE_ERROR, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  // TODO clean-up
  private static class EmptyDebugger implements JPAServiceDebugger {

    @Override
    public int startRuntimeMeasurement(final Object instance, String methodName) {
      return 0;
    }

    @Override
    public void stopRuntimeMeasurement(int handle) {
      // not needed
    }

    @Override
    public Collection<RuntimeMeasurement> getRuntimeInformation() {
      return new ArrayList<>();
    }

  }

}