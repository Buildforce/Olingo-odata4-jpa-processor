package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.query.JPAAbstractQuery;
import nl.buildforce.sequoia.jpa.processor.core.query.JPANavigationPropertyInfo;
import nl.buildforce.sequoia.jpa.processor.core.query.Util;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceComplexProperty;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;
import org.apache.olingo.server.api.uri.UriResourceProperty;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

abstract class JPAExistsOperation implements JPAOperator {

  protected final JPAOperationConverter converter;
  protected final List<UriResource> uriResourceParts;
  protected final JPAAbstractQuery root;
  protected final JPAServiceDocument sd;
  protected final EntityManager em;
  protected final OData odata;
  protected final From<?, ?> from;
  protected final Optional<JPAODataClaimProvider> claimsProvider;
  protected final List<String> groups;

  JPAExistsOperation(final JPAFilterCompilerAccess jpaCompiler) {

    this.uriResourceParts = jpaCompiler.getUriResourceParts();
    this.root             = jpaCompiler.getParent();
    this.sd               = jpaCompiler.getSd();
    this.em               = jpaCompiler.getEntityManager();
    this.converter        = jpaCompiler.getConverter();
    this.odata            = jpaCompiler.getOdata();
    this.from             = jpaCompiler.getRoot();
    this.claimsProvider   = jpaCompiler.getClaimsProvider();
    this.groups           = jpaCompiler.getGroups();
  }

  @Override
  public Expression<Boolean> get() throws ODataApplicationException {
    return converter.cb.exists(getExistsQuery());
  }

  abstract Subquery<?> getExistsQuery() throws ODataApplicationException;

  protected List<JPANavigationPropertyInfo> determineAssociations(final JPAServiceDocument sd,
      final List<UriResource> resourceParts) throws ODataApplicationException {
    final List<JPANavigationPropertyInfo> pathList = new ArrayList<>();

    StringBuilder associationName = null;
    UriResourcePartTyped navigation = null;
    if (Util.hasNavigation(resourceParts)) {
      for (int i = resourceParts.size() - 1; i >= 0; i--) {
        final UriResource resourcePart = resourceParts.get(i);
        if (resourcePart instanceof UriResourceNavigation) {
          if (navigation != null)
            pathList.add(new JPANavigationPropertyInfo(sd, navigation, Util.determineAssociationPath(sd,
                ((UriResourcePartTyped) resourceParts.get(i)), associationName), null));
          navigation = (UriResourceNavigation) resourceParts.get(i);
          associationName = new StringBuilder();
          associationName.insert(0, ((UriResourceNavigation) navigation).getProperty().getName());
        }
        if (navigation != null) {
          if (resourceParts.get(i) instanceof UriResourceComplexProperty) {
            associationName.insert(0, JPAPath.PATH_SEPARATOR);
            associationName.insert(0, ((UriResourceComplexProperty) resourceParts.get(i)).getProperty().getName());
          }
          if (resourcePart instanceof UriResourceEntitySet)
            pathList.add(new JPANavigationPropertyInfo(sd, navigation, Util.determineAssociationPath(sd,
                ((UriResourcePartTyped) resourceParts.get(i)), associationName), null));
        }
      }
    } else if (resourceParts != null && hasCollection(resourceParts)) {
      for (int i = resourceParts.size() - 1; i >= 0; i--) {
        final UriResource resourcePart = resourceParts.get(i);
        if (isCollection(resourcePart)) {
          navigation = (UriResourcePartTyped) resourceParts.get(i);
          associationName = new StringBuilder();
          associationName.insert(0, ((UriResourceProperty) navigation).getProperty().getName());
        } else if (navigation != null) {
          if (resourceParts.get(i) instanceof UriResourceComplexProperty) {
            associationName.insert(0, JPAPath.PATH_SEPARATOR);
            associationName.insert(0, ((UriResourceComplexProperty) resourceParts.get(i)).getProperty().getName());
          }
          if (resourcePart instanceof UriResourceEntitySet)
            pathList.add(new JPANavigationPropertyInfo(sd, navigation, Util.determineAssociationPath(sd,
                ((UriResourcePartTyped) resourceParts.get(i)), associationName), null));
        }
      }
    }
    return pathList;
  }

  public boolean hasCollection(final List<UriResource> resourceParts) {
    if (resourceParts != null) {
      for (int i = resourceParts.size() - 1; i >= 0; i--) {
        if (isCollection(resourceParts.get(i)))
          return true;
      }
    }
    return false;
  }

  public boolean isCollection(UriResource resourcePart) {

    return (resourcePart instanceof UriResourceProperty && ((UriResourceProperty) resourcePart).isCollection());
  }

}