package nl.buildforce.sequoia.processor.core.filter;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.*;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.processor.core.query.Util;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.uri.queryoption.expression.Member;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;

import java.util.List;
import java.util.Set;

import static nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException.MessageKeys.NOT_ALLOWED_MEMBER;

public class JPAMemberOperator implements JPAOperator {
  private final Member member;
  private final JPAEntityType jpaEntityType;
  private final From<?, ?> root;
  private final JPAAssociationPath association;

  JPAMemberOperator(final JPAEntityType jpaEntityType, final From<?, ?> parent,
      final Member member, final JPAAssociationPath association, final List<String> list)
      throws ODataApplicationException {

    this.member = member;
    this.jpaEntityType = jpaEntityType;
    this.root = parent;
    this.association = association;
    checkGroup(determineAttributePath(), list);
  }

  public JPAAttribute determineAttribute() throws ODataApplicationException {
    final JPAPath path = determineAttributePath();
    return path == null ? null : path.getLeaf();
  }

  @Override
  public Path<?> get() throws ODataApplicationException {
    final JPAPath selectItemPath = determineAttributePath();
    return selectItemPath == null ? null : determineCriteriaPath(selectItemPath);
  }

  public Member getMember() {
    return member;
  }

  @Override
  public String getName() {
    return member.toString();
  }

  private JPAPath determineAttributePath() throws ODataApplicationException {
    final String attributePath = Util.determinePropertyNavigationPath(member.getResourcePath().getUriResourceParts());
    JPAPath selectItemPath;

    try {
      selectItemPath = jpaEntityType.getPath(attributePath);
      if (selectItemPath == null && association != null) {
        selectItemPath = jpaEntityType.getPath(attributePath.isEmpty() ? association.getAlias() : association.getAlias()
            + JPAPath.PATH_SEPARATOR + attributePath);
      }
    } catch (ODataJPAModelException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    return selectItemPath;
  }

  private Path<?> determineCriteriaPath(final JPAPath selectItemPath) throws ODataJPAFilterException {
    Path<?> p = root;

    for (final JPAElement jpaPathElement : selectItemPath.getPath()) {
      if (jpaPathElement instanceof JPADescriptionAttribute) {
        p = determineDescriptionCriteriaPath(selectItemPath, p, jpaPathElement);
      } else if (jpaPathElement instanceof JPACollectionAttribute) {
        if (!((JPACollectionAttribute) jpaPathElement).isComplex()) try {
          p = p.get(((JPACollectionAttribute) jpaPathElement).getTargetAttribute().getInternalName());
        } catch (ODataJPAModelException e) {
          throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
        }
      } else {
        p = p.get(jpaPathElement.getInternalName());
      }
    }
    return p;
  }

  private Path<?> determineDescriptionCriteriaPath(final JPAPath selectItemPath, Path<?> p,
      final JPAElement jpaPathElement) {
    final Set<?> allJoins = root.getJoins();

    for (Object allJoin : allJoins) {
      Join<?, ?> join = (Join<?, ?>) allJoin;
      if (join.getAlias() != null && join.getAlias().equals(selectItemPath.getAlias())) {
        final Set<?> subJoins = join.getJoins();
        for (final Object sub : subJoins) {
          // e.g. "Organizations?$filter=Address/RegionName eq 'Kalifornien'
          // see createFromClause in JPAExecutableQuery
          if (((Join<?, ?>) sub).getAlias() != null &&
                  ((Join<?, ?>) sub).getAlias().equals(jpaPathElement.getExternalName())) {
            join = (Join<?, ?>) sub;
          }
        }
        p = join.get(((JPADescriptionAttribute) jpaPathElement).getDescriptionAttribute().getInternalName());
        break;
      }
    }
    return p;
  }

  private void checkGroup(final JPAPath path, final List<String> groups) throws ODataJPAFilterException {
    JPAPath orgPath = path;

    if (association != null && association.getPath() != null) {
      final JPAAttribute st = ((JPAAttribute) this.association.getPath().get(0));
      if (st.isComplex()) {
        try {
          orgPath = st.getStructuredType().getPath(path.getLeaf().getExternalName());
        } catch (ODataJPAModelException e) {
          // Ignore exception and use path
        }
      }
    }
    if (orgPath != null && !orgPath.isPartOfGroups(groups))
      throw new ODataJPAFilterException(NOT_ALLOWED_MEMBER, HttpStatusCode.FORBIDDEN, orgPath.getAlias());
  }

}