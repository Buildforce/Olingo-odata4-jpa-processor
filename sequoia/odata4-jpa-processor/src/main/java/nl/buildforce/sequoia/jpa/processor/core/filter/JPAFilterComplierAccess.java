package nl.buildforce.sequoia.jpa.processor.core.filter;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataClaimProvider;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.jpa.processor.core.query.JPAAbstractQuery;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.uri.UriResource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.From;
import java.util.List;
import java.util.Optional;

interface JPAFilterCompilerAccess {

  JPAAbstractQuery getParent();

  List<UriResource> getUriResourceParts();

  JPAServiceDocument getSd();

  OData getOdata();

  EntityManager getEntityManager();

  JPAEntityType getJpaEntityType();

  JPAOperationConverter getConverter();

  From<?, ?> getRoot();

  JPAServiceDebugger getDebugger();

  JPAAssociationPath getAssociation();

  Optional<JPAODataClaimProvider> getClaimsProvider();

  List<String> getGroups();

}