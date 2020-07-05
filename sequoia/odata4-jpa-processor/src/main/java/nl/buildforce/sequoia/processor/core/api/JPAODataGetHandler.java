package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.metadata.api.JPAEdmProvider;
import nl.buildforce.sequoia.metadata.api.JPAEntityManagerFactory;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAFilterException;
import nl.buildforce.sequoia.processor.core.processor.JPAODataRequestContextImpl;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataHttpHandler;
// import nl.buildforce.olingo.server.api.debug.DebugSupport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.util.Optional;

// import org.apache.olingo.server.core.ODataHttpHandlerImpl.REQUESTMAPPING

public class JPAODataGetHandler {
  public static final String REQUESTMAPPING_ATTRIBUTE = "requestMapping";
  public final Optional<EntityManagerFactory> emf;
  private final JPAODataServiceContext serviceContext;
  private final JPAODataRequestContextImpl requestContext;
  final OData odata;
  @Deprecated
  final DataSource ds;
  @Deprecated
  final String namespace;
  @Deprecated
  final Metamodel jpaMetamodel;

  /**
   * @deprecated (Will be removed with 1.0.0, use service context builder, <code>JPAODataServiceContext.with()</code>
   * instead
   * @param pUnit
   * @throws ODataJPAException
   */
  @Deprecated
  public JPAODataGetHandler(final String pUnit) throws ODataJPAFilterException {// NOSONAR
    this(pUnit, null);
  }

  /**
   * @deprecated (Will be removed with 1.0.0, use service context builder, <code>JPAODataServiceContext.with()</code>
   * instead
   * @param pUnit
   * @param ds
   * @throws ODataJPAFilterException
   */
  @Deprecated
  public JPAODataGetHandler(final String pUnit, final DataSource ds) throws ODataJPAFilterException {
    namespace = pUnit;
    this.ds = ds;
    emf = ds == null ? Optional.empty() : Optional.ofNullable(JPAEntityManagerFactory.getEntityManagerFactory(pUnit, ds));
    jpaMetamodel = emf.map(EntityManagerFactory::getMetamodel).orElse(null);
    serviceContext = new JPAODataServiceContext(this);
    requestContext = new JPAODataRequestContextImpl();
    odata = OData.newInstance();
  }

  public JPAODataGetHandler(final JPAODataCRUDContextAccess serviceContext) {
    this(serviceContext, OData.newInstance());
  }

  /**
   * Give the option to inject the odata helper e.g. for testing
   * @param serviceContext
   * @param odata
   */
  JPAODataGetHandler(final JPAODataCRUDContextAccess serviceContext, final OData odata) {
    namespace = null;
    ds = null;
    emf = serviceContext.getEntityManagerFactory();
    jpaMetamodel = null;
    this.serviceContext = (JPAODataServiceContext) serviceContext;
    requestContext = new JPAODataRequestContextImpl();
    this.odata = odata;
  }

  public JPAODataGetContext getJPAODataContext() {
    return serviceContext;
  }

  public JPAODataRequestContext getJPAODataRequestContext() {
    return requestContext;
  }

  public void process(final HttpServletRequest request, final HttpServletResponse response) throws ODataJPAModelException {
    try {
      if (emf.isPresent() && requestContext.getEntityManager() == null) {
        final EntityManager em = emf.get().createEntityManager();
        try {
          requestContext.setEntityManager(em);
          processInternal(request, response);
        } finally {
          em.close();
        }
      } else {
        processInternal(request, response);
      }
    } catch (RuntimeException | ODataJPAException e ) {
      throw new ODataJPAModelException(ODataJPAModelException.MessageKeys.PROCESSINTERNAL_FAILED);
    }
  }

  /**
   * @deprecated (Will be removed with 1.0.0, parameter <code>em</code> not longer supported,
   * use Request Context (<code>getJPAODataRequestContext</code>) instead)
   * @param request
   * @param response
   * @param em
   */
  @Deprecated
  public void process(final HttpServletRequest request, final HttpServletResponse response, final EntityManager em)
          throws ODataJPAModelException {

    requestContext.setEntityManager(em);
    process(request, response);
  }

  /**
   * @deprecated (Will be removed with 1.0.0, parameter <code>claims</code> and <code>em</code> not longer supported,
   * use Request Context (<code>getJPAODataRequestContext</code>) instead)
   * @param request
   * @param response
   * @param claims
   * @param em
   */
  @Deprecated
  public void process(final HttpServletRequest request, final HttpServletResponse response,
      final JPAODataClaimProvider claims, final EntityManager em) throws ODataJPAModelException {

    requestContext.setClaimsProvider(claims);
    requestContext.setEntityManager(em);
    process(request, response);
  }

  private void processInternal(final HttpServletRequest request, final HttpServletResponse response)
      throws ODataJPAException {

    final JPAEdmProvider jpaEdm = serviceContext.getEdmProvider() == null
            ? serviceContext.getEdmProvider(requestContext.getEntityManager())
            : serviceContext.getEdmProvider();

    final ODataHttpHandler handler = odata.createHandler(odata.createServiceMetadata(jpaEdm, jpaEdm.getReferences()));
    serviceContext.getEdmProvider().setRequestLocales(request.getLocales());
    // requestContext.setDebugFormat(request.getParameter(DebugSupport.ODATA_DEBUG_QUERY_PARAMETER));
    setCUDHandler();
    final HttpServletRequest mappedRequest = prepareRequestMapping(request, serviceContext.getMappingPath());
    // handler.register(requestContext.getDebugSupport());
    handler.register(new JPAODataRequestProcessor(serviceContext, requestContext));
    handler.register(new JPAODataBatchProcessor(requestContext));
    handler.register(serviceContext.getEdmProvider().getServiceDocument());
    handler.register(serviceContext.getErrorProcessor());
    handler.register(new JPAODataServiceDocumentProcessor(serviceContext));
    handler.process(mappedRequest, response);
  }

  private void setCUDHandler() {
    if (serviceContext.getCUDRequestHandler() != null && requestContext.getCUDRequestHandler() == null)
      requestContext.setCUDRequestHandler(serviceContext.getCUDRequestHandler());
  }

  private HttpServletRequest prepareRequestMapping(final HttpServletRequest req, final String requestPath) {
    if (requestPath != null && !requestPath.isEmpty()) {
      HttpServletRequestWrapper request = new HttpServletRequestWrapper(req);
      request.setAttribute(REQUESTMAPPING_ATTRIBUTE, requestPath);
      return request;
    } else {
      return req;
    }
  }

}