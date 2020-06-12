package nl.buildforce.sequoia.jpa.processor.core.api;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;

@Deprecated
public interface JPAODataGetContext {

//  void initDebugger(final String debugFormat);

  /**
   *
   * @param databaseProcessor
   * @throws ODataJPAException
   */
  // void setDatabaseProcessor(final JPAODataDatabaseProcessor databaseProcessor);

  /**
   * @param jpaDebugSupport
   */
  // void setDebugSupport(final DebugSupport jpaDebugSupport);

  /**
   * Allows to provide an Olingo error processor. The error processor allows to enrich an error response. See
   * <a
   * href="http://docs.oasis-open.org/odata/odata-json-format/v4.0/errata03/os/odata-json-format-v4.0-errata03-os-complete.html#_Toc453766668"
   * >JSON Error Response</a> or
   * <a
   * href="http://docs.oasis-open.org/odata/odata-atom-format/v4.0/cs02/odata-atom-format-v4.0-cs02.html#_Toc372792829">Atom
   * Error Response</a>.
   * @param errorProcessor
   */
  // void setErrorProcessor(final ErrorProcessor errorProcessor);

  /**
   *

   * @param postProcessor
   * @throws ODataJPAException
   */
  // void setMetadataPostProcessor(final JPAEdmMetadataPostProcessor postProcessor) throws ODataJPAException;

  /**
   *
   * @param jpaOperationConverter
   * @throws ODataJPAException
   */
  // void setOperationConverter(final JPAODataDatabaseOperations jpaOperationConverter);

  /**
   * Registers the debug support handler.
   * @param references
   */
  // void setReferences(final List<EdmxReference> references);


   /* Name of the top level package to look for
   * <ul>
   * <li> Enumeration Types
   * <li> Java class based Functions
   * </ul>
   * @param packageName
   */
   void setTypePackage(final String... packageName);

  /**
   * Register a provider that is able to decides based on a given query if the server like to return only a sub set of
   * the requested results as well as a $skiptoken.
   * @param provider
   */
  // void setPagingProvider(final JPAODataPagingProvider provider);

}