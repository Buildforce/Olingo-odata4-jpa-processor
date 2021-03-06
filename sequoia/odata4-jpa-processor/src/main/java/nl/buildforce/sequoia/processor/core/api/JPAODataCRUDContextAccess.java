package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.database.JPAODataDatabaseOperations;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.server.api.processor.ErrorProcessor;

import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface JPAODataCRUDContextAccess {

  JPAODataDatabaseProcessor getDatabaseProcessor();

  JPAEdmProvider getEdmProvider() throws ODataJPAException;

  JPAODataDatabaseOperations getOperationConverter();

  List<EdmxReference> getReferences();

  /**
   * @deprecated (will be removed with 1.0.0; use request context)
   * @return
   */
  @Deprecated
  JPACUDRequestHandler getCUDRequestHandler();

  /**
   * Returns a list of packages that may contain Enumerations of Java implemented OData operations
   * @return
   */
  String[] getPackageName();

  /**
   * If server side paging shall be supported <code>getPagingProvider</code> returns an implementation of a paging
   * provider. Details about the OData specification can be found under <a
   * href="https://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Server-Driven_Paging">OData
   * Version 4.0 Part 1 - 11.2.5.7 Server-Driven Paging</a>
   * @return
   */
  JPAODataPagingProvider getPagingProvider();

  default Optional<EntityManagerFactory> getEntityManagerFactory() {
    return Optional.empty();
  }

  default ErrorProcessor getErrorProcessor() {
    return null;
  }

  default String getMappingPath() {
    return "";
  }

  default boolean useAbsoluteContextURL() {
    return false;
  }

}