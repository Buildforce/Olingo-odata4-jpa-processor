package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriInfoResource;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.serializer.JPASerializer;

import jakarta.persistence.EntityManager;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

abstract class JPAAbstractRequestProcessor {

  protected final EntityManager em;
  protected final JPAServiceDocument sd;
  protected final JPAODataCRUDContextAccess sessionContext;
  //private final CriteriaBuilder cb;
  protected final UriInfoResource uriInfo;
  protected final JPASerializer serializer;
  protected final OData odata;
  // protected final JPAServiceDebugger debugger;
  private int successStatusCode = HttpStatusCode.OK.getStatusCode();
  protected final JPAODataRequestContextAccess requestContext;

  public JPAAbstractRequestProcessor(final OData odata,
                                     final JPAODataCRUDContextAccess context,
                                     final JPAODataRequestContextAccess requestContext) throws ODataJPAException {

    this.em = requestContext.getEntityManager();
//    this.cb = em.getCriteriaBuilder();
    this.sessionContext = context;
    this.sd = context.getEdmProvider().getServiceDocument();
    this.uriInfo = requestContext.getUriInfo();
    this.serializer = requestContext.getSerializer();
    this.odata = odata;
//    this.debugger = requestContext.getDebugger();
    this.requestContext = requestContext;
  }

  public int getSuccessStatusCode() {
    return successStatusCode;
  }

  public void setSuccessStatusCode(int successStatusCode) {
    this.successStatusCode = successStatusCode;
  }

  protected final void createSuccessResponse(final ODataResponse response,
                                             final ContentType responseFormat,
                                             final SerializerResult serializerResult) {

    response.setContent(serializerResult.getContent());
    response.setStatusCode(successStatusCode);
    response.setHeader(CONTENT_TYPE, responseFormat.toString());
  }

}