package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.processor.core.serializer.JPASerializer;
import org.apache.olingo.server.api.uri.UriInfoResource;

import jakarta.persistence.EntityManager;
import java.util.Optional;

public interface JPAODataRequestContextAccess {

  EntityManager getEntityManager();

  UriInfoResource getUriInfo();

  JPASerializer getSerializer();

  JPAODataPage getPage();

  Optional<JPAODataClaimProvider> getClaimsProvider();

  Optional<JPAODataGroupProvider> getGroupsProvider();

  JPACUDRequestHandler getCUDRequestHandler();

  JPAServiceDebugger getDebugger();

  JPAODataTransactionFactory getTransactionFactory();

}