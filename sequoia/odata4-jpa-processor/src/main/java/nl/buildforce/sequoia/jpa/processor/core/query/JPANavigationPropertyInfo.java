package nl.buildforce.sequoia.jpa.processor.core.query;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.processor.core.filter.JPAFilterCompiler;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;

import jakarta.persistence.criteria.From;
import java.util.Collections;
import java.util.List;

public final class JPANavigationPropertyInfo {
  private final JPAServiceDocument sd;
  private final UriResourcePartTyped navigationTarget;
  private JPAAssociationPath associationPath;
  private final List<UriParameter> keyPredicates;
  private From<?, ?> fromClause = null;
  private final UriInfoResource uriInfo;
  private JPAEntityType entityType = null;
  private JPAFilterCompiler filterCompiler = null;

  /**
   *

   * Copy constructor, that does not copy the <i>from</i> clause, so the new JPANavigationPropertyInfo can be used in a
   * new query.
   * @param original
   */
  public JPANavigationPropertyInfo(final JPANavigationPropertyInfo original) {

    this.navigationTarget = original.getUriResource();
    this.associationPath = original.getAssociationPath();
    this.keyPredicates = original.getKeyPredicates();
    this.uriInfo = original.getUriInfo();
    this.sd = original.getServiceDocument();
    this.entityType = this.uriInfo instanceof JPAExpandItem ? ((JPAExpandItem) uriInfo).getEntityType() : null;
  }

  public JPANavigationPropertyInfo(final JPAServiceDocument sd, final JPAAssociationPath associationPath,
      final UriInfoResource uriInfo, final JPAEntityType entityType) {
    this.navigationTarget = null;
    this.associationPath = associationPath;
    this.keyPredicates = Collections.emptyList();
    this.uriInfo = uriInfo;
    this.sd = sd;
    this.entityType = entityType;
  }

  public JPANavigationPropertyInfo(final JPAServiceDocument sd, final UriResourcePartTyped uriResource,
      final JPAAssociationPath associationPath, final UriInfoResource uriInfo) throws ODataApplicationException {

    this.navigationTarget = uriResource;
    this.associationPath = associationPath;
    this.keyPredicates =
            uriResource.isCollection() ? Collections.emptyList() : Util.determineKeyPredicates(uriResource);
    this.uriInfo = uriInfo;
    this.sd = sd;
  }

  public JPAAssociationPath getAssociationPath() { return associationPath; }

  public UriResourcePartTyped getUriResource() { return navigationTarget; }

  /**
   * Set the association path to a other entity.
   * @param associationPath
   */
  public void setAssociationPath(JPAAssociationPath associationPath) {
    assert this.associationPath == null;
    this.associationPath = associationPath;
  }

  JPAEntityType getEntityType() {
    return (entityType == null) ? sd.getEntity(getUriResource().getType()) : entityType;
  }

  JPAFilterCompiler getFilterCompiler() {
    return filterCompiler;
  }

  From<?, ?> getFromClause() { // NOSONAR
    return fromClause;
  }

  List<UriParameter> getKeyPredicates() {
    return keyPredicates;
  }

  UriInfoResource getUriInfo() {
    return uriInfo;
  }

  void setFilterCompiler(JPAFilterCompiler filterCompiler) {
    assert this.filterCompiler == null;
    this.filterCompiler = filterCompiler;
  }

  /**
   * Set the from clause. This is possible only once and can not be changed later.
   * @param from
   */
  void setFromClause(final From<?, ?> from) {
    assert fromClause == null;
    fromClause = from;
  }

  private JPAServiceDocument getServiceDocument() {
    return sd;
  }

}