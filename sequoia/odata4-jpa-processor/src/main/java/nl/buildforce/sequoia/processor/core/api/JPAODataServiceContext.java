package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.metadata.api.JPAEdmMetadataPostProcessor;
import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEdmNameBuilder;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.impl.JPADefaultEdmNameBuilder;
import nl.buildforce.sequoia.processor.core.database.JPADefaultDatabaseProcessor;
import nl.buildforce.sequoia.processor.core.database.JPAODataDatabaseOperations;
import nl.buildforce.sequoia.processor.core.database.JPAODataDatabaseProcessorFactory;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.olingo.commons.api.edmx.EdmxReference;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.processor.ErrorProcessor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public final class JPAODataServiceContext implements /*JPAODataGetContext,*/ JPAODataCRUDContextAccess {
  //@formatter:off
  private final       JPAODataHandler jpaODataGetHandler;
  // --Commented out by Inspection (''20-05-01 19:16):private           JPACUDRequestHandler jpaCUDRequestHandler;

/*
  private                 ErrorProcessor errorProcessor;
  private    JPAEdmMetadataPostProcessor postProcessor;
  private         JPAODataPagingProvider pagingProvider;
*/

  private final  JPAODataDatabaseProcessor databaseProcessor;
  private   Optional<EntityManagerFactory> emf;
  private                   JPAEdmProvider jpaEdm;
  // private                           String mappingPath;
  private final                     String namespace_pUnit;
  private final JPAODataDatabaseOperations operationConverter;
  private                         String[] packageName;
  private final        List<EdmxReference> references = new ArrayList<>();
  //@formatter:on

  //public static Builder with() { return new Builder(); }

    /**
     * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
     * instead
     */
  @Deprecated
  JPAODataServiceContext(JPAODataHandler jpaODataGetHandler) throws ODataJPAFilterException {
    this.jpaODataGetHandler = jpaODataGetHandler;
    namespace_pUnit = jpaODataGetHandler.namespace;
    operationConverter = new JPADefaultDatabaseProcessor();
    try {
      databaseProcessor = new JPAODataDatabaseProcessorFactory().create(this.jpaODataGetHandler.ds);
    } catch (SQLException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  // Used
  public JPAODataServiceContext(final String namespace_pUnit, final DataSource ds, final String... packageName)
  throws ODataJPAException, ODataJPAFilterException {
    jpaODataGetHandler = null;

    try {
      databaseProcessor = new JPAODataDatabaseProcessorFactory().create(ds);

    } catch (  PersistenceException | SQLException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    this.packageName = packageName;
    operationConverter = new JPADefaultDatabaseProcessor();
    this.namespace_pUnit = namespace_pUnit;

    emf = Optional.ofNullable(JPAEntityManagerFactory.getEntityManagerFactory(namespace_pUnit, ds));

    jpaEdm = new JPAEdmProvider(namespace_pUnit, emf.get().getMetamodel(), /*null,*/ packageName);
  }

/*  private JPAODataServiceContext(final Builder builder) throws ODataJPAException, ODataJPAFilterException {
    jpaODataGetHandler = null;

    errorProcessor = builder.errorProcessor;
    pagingProvider = builder.pagingProvider;
    postProcessor = builder.postProcessor;
    mappingPath = builder.mappingPath;
    packageName = builder.packageName;

    operationConverter = builder.operationConverter;
    references = builder.references;
    namespace_pUnit = builder.namespace_pUnit;

    try {
      databaseProcessor = (builder.databaseProcessor == null)
              ? new JPAODataDatabaseProcessorFactory().create(builder.builderDs)
              : builder.databaseProcessor;
    } catch (SQLException | PersistenceException e) {
      throw new ODataJPAFilterException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    emf = (builder.builderEmf.isPresent() || builder.builderDs == null || namespace_pUnit == null)
            ? builder.builderEmf
            :Optional.ofNullable(JPAEntityManagerFactory.getEntityManagerFactory(namespace_pUnit, builder.builderDs));

    jpaEdm = (emf.isPresent()) ?
            new JPAEdmProvider(namespace_pUnit, emf.get().getMetamodel(), postProcessor, packageName)
            : jpaEdm;
  }*/

  @Override
  public JPACUDRequestHandler getCUDRequestHandler() { return null /*jpaCUDRequestHandler*/; }

  @Override
  public JPAODataDatabaseProcessor getDatabaseProcessor() {
    return databaseProcessor;
  }

  @Override
  public JPAEdmProvider getEdmProvider() throws ODataJPAException {
    if (jpaEdm == null && jpaODataGetHandler != null && jpaODataGetHandler.jpaMetamodel != null)
      jpaEdm = new JPAEdmProvider(namespace_pUnit, jpaODataGetHandler.jpaMetamodel, /*null,*/ packageName);
    return jpaEdm;
  }

  public JPAEdmProvider getEdmProvider(final EntityManager em) throws ODataJPAException {
    if (jpaEdm == null) jpaEdm = new JPAEdmProvider(namespace_pUnit, em.getMetamodel(), /*null,*/ packageName);
    return jpaEdm;
  }

  @Override
  public Optional<EntityManagerFactory> getEntityManagerFactory() {
    return emf;
  }

  @Override
  public ErrorProcessor getErrorProcessor() {
    return /*errorProcessor == null ?*/ new JPADefaultErrorProcessor(); //: errorProcessor;
  }

  @Override
  public JPAODataDatabaseOperations getOperationConverter() {
    return operationConverter;
  }

  @Override
  public String[] getPackageName() {
    return packageName;
  }

  @Override
  public JPAODataPagingProvider getPagingProvider() {
    return null /*pagingProvider*/;
  }

  @Override
  public List<EdmxReference> getReferences() {
    return references;
  }

  @Override
  public String getMappingPath() { return null /* mappingPath*/; }

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
 /* @Deprecated
  @Override
  public void setCUDRequestHandler(JPACUDRequestHandler jpaCUDRequestHandler) {
    this.jpaCUDRequestHandler = jpaCUDRequestHandler;
  }*/

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setDatabaseProcessor(final JPAODataDatabaseProcessor databaseProcessor) {
    this.databaseProcessor = databaseProcessor;
  }

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setErrorProcessor(ErrorProcessor errorProcessor) {
    this.errorProcessor = errorProcessor;
  }

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
/*  @Deprecated
  @Override
  public void setMetadataPostProcessor(final JPAEdmMetadataPostProcessor postProcessor) throws ODataJPAException {
    if (jpaODataGetHandler.jpaMetamodel != null)
      jpaEdm = new JPAEdmProvider(jpaODataGetHandler.namespace, jpaODataGetHandler.jpaMetamodel, postProcessor, packageName);
    else
      this.postProcessor = postProcessor;
  }*/

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setOperationConverter(final JPAODataDatabaseOperations jpaOperationConverter) {
    operationConverter = jpaOperationConverter;
  }*/

  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setPagingProvider(final JPAODataPagingProvider provider) {
    pagingProvider = provider;
  }
*/
  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setReferences(final List<EdmxReference> references) {
    this.references = references;
  }
*/
  /**
   * @deprecated will be removed with 1.0.0; use newly created builder (<code>JPAODataServiceContext.with()</code>)
   * instead
   */
  /*@Deprecated
  @Override
  public void setTypePackage(final String... packageName) {
    this.packageName = packageName;
  }
*/
/*  @Deprecated
  public static class Builder {
    // format: OFF
    private      JPAODataDatabaseProcessor databaseProcessor;
    private                     DataSource builderDs;
    private Optional<EntityManagerFactory> builderEmf = Optional.empty();
    private                 ErrorProcessor errorProcessor;

    private                         String mappingPath;
    private                         String namespace_pUnit;
    private     JPAODataDatabaseOperations operationConverter = new JPADefaultDatabaseProcessor();
    private                       String[] packageName = new String[0];

    private         JPAODataPagingProvider pagingProvider;
    private    JPAEdmMetadataPostProcessor postProcessor;
    private            List<EdmxReference> references = new ArrayList<>();
    // format: ON

    public JPAODataCRUDContextAccess build() throws ODataJPAException, ODataJPAFilterException {

      return new JPAODataServiceContext(this);
    }

    *//**
     * A database processor allows database specific implementations for search and odata function with function import
     * that are implemented as database functions.<br>
     * In case no database processor is provided and non could be determined via an data source
     * {@link JPADefaultDatabaseProcessor} is used.
     * @param databaseProcessor
     * @return
     *//*
    public Builder setDatabaseProcessor(final JPAODataDatabaseProcessor databaseProcessor) {
      this.databaseProcessor = databaseProcessor;
      return this;
    }

    *//**
     * The data source is used to create an entity manager factory if not provided, see
     * {@link Builder#setEntityManagerFactory(EntityManagerFactory)}, and to determine the type of
     * database used to select an integrated database processor, in case the database processor was not set via
     * {@link Builder#setDatabaseProcessor(JPAODataDatabaseProcessor)}}.
     * @param ds
     * @return
     *//*
    public Builder setDataSource(final DataSource ds) {
      builderDs = ds;
      return this;
    }

    *//**
     * Allows to provide an Olingo error processor. The error processor allows to enrich an error response. See
     * <a
     * href="http://docs.oasis-open.org/odata/odata-json-format/v4.0/errata03/os/odata-json-format-v4.0-errata03-os-complete.html#_Toc453766668"
     * >JSON Error Response</a> or
     * <a
     * href="http://docs.oasis-open.org/odata/odata-atom-format/v4.0/cs02/odata-atom-format-v4.0-cs02.html#_Toc372792829">Atom
     * Error Response</a>.
     * @param errorProcessor
     *//*
    public Builder setErrorProcessor(final ErrorProcessor errorProcessor) {
      this.errorProcessor = errorProcessor;
      return this;
    }

    *//**
     *
     * @param postProcessor
     * @return
     *//*
    public Builder setMetadataPostProcessor(final JPAEdmMetadataPostProcessor postProcessor) {
      this.postProcessor = postProcessor;
      return this;
    }

    *//**
     *
     * @param jpaOperationConverter
     * @return
     *//*
    public Builder setOperationConverter(final JPAODataDatabaseOperations jpaOperationConverter) {
      operationConverter = jpaOperationConverter;
      return this;
    }

    *//**
     * Register a provider that is able to decides based on a given query if the server like to return only a sub set of
     * the requested results as well as a $skiptoken.
     * @param provider
     *//*
    public Builder setPagingProvider(final JPAODataPagingProvider provider) {
      pagingProvider = provider;
      return this;
    }

    *//**
     * The name of the persistence-unit to be used. It is taken to create a entity manager factory
     * ({@link Builder#setEntityManagerFactory(EntityManagerFactory)}), if not provided and
     * as namespace of the OData service, in case the default name builder shall be used.
     * @param pUnit
     * @return
     *//*
    public Builder setPUnit(final String pUnit) {
      namespace_pUnit = pUnit;
      return this;
    }

    *//**
     *
     * @param references
     * @return
     *//*
    public Builder setReferences(final List<EdmxReference> references) {
      this.references = references;
      return this;
    }

    *//**
     * Name of the top level package to look for
     * <ul>
     * <li> Enumeration Types
     * <li> Java class based Functions
     * </ul>
     * @param packageName
     *//*
    public Builder setTypePackage(final String... packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder setRequestMappingPath(final String mappingPath) {
      this.mappingPath = mappingPath;
      return this;
    }

    *//**
     * Set an externally created entity manager factory.<br>
     * This is necessary e.g. in case a spring based service shall run without a <code>persistence.xml</code>.
     * @param emf
     * @return
     *//*
    public Builder setEntityManagerFactory(final EntityManagerFactory emf) {
      builderEmf = Optional.of(emf);
      return this;
    }

    *//**
     * Set a custom EDM name builder {@link JPAEdmNameBuilder}. If non is provided {@link JPADefaultEdmNameBuilder} is
     * used, which uses the provided persistence-unit name ({@link JPAODataServiceContext.Builder#setPUnit}) as
     * namespace.
     * @param nameBuilder
     * @return
     *//*
    *//*public Builder setEdmNameBuilder(final JPAEdmNameBuilder nameBuilder) {
      this.nameBuilder = nameBuilder;
      return this;
    }*//*
  }*/

/*  static class JPADebugSupportWrapper implements DebugSupport {

    private final DebugSupport debugSupport;
    private JPAServiceDebugger debugger;

    public JPADebugSupportWrapper(final DebugSupport debugSupport) {
      this.debugSupport = debugSupport;
    }

    *//*
     * (non-Javadoc)
     *

     * @see org.apache.olingo.server.api.debug.DebugSupport#createDebugResponse(java.lang.String,
     * org.apache.olingo.server.api.debug.DebugInformation)
     *//*
    @Override
    public ODataResponse createDebugResponse(final String debugFormat, final DebugInformation debugInfo) {
      joinRuntimeInfo(debugInfo);
      return debugSupport.createDebugResponse(debugFormat, debugInfo);
    }

    *//*
     * (non-Javadoc)
     *

     * @see org.apache.olingo.server.api.debug.DebugSupport#init(org.apache.olingo.server.api.OData)
     *//*
    @Override
    public void init(final OData odata) {
      debugSupport.init(odata);
    }

    *//*
     * (non-Javadoc)
     *
     * @see org.apache.olingo.server.api.debug.DebugSupport#isUserAuthorized()
     *//*
    @Override
    public boolean isUserAuthorized() {
      return debugSupport.isUserAuthorized();
    }

    void setDebugger(final JPAServiceDebugger debugger) {
      this.debugger = debugger;
    }

    private void joinRuntimeInfo(final DebugInformation debugInfo) {
      // Olingo create a tree for runtime measurement in DebugTabRuntime.add(final RuntimeMeasurement
      // runtimeMeasurement). The current algorithm (V4.3.0) not working well for batch requests if the own runtime info
      // is just appended (addAll), so insert sorted:
      final List<RuntimeMeasurement> olingoInfo = debugInfo.getRuntimeInformation();
      int startIndex = 0;
      for (RuntimeMeasurement m : debugger.getRuntimeInformation()) {
        for (; startIndex < olingoInfo.size(); startIndex++) {
          if (olingoInfo.get(startIndex).getTimeStarted() > m.getTimeStarted()) {
            break;
          }
        }
        olingoInfo.add(startIndex, m);
        startIndex += 1;
      }
    }
  }*/

}