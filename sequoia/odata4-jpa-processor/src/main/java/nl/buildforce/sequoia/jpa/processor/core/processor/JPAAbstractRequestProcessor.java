package nl.buildforce.sequoia.jpa.processor.core.processor;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.api.JPAServiceDocument;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAServiceDebugger;
import nl.buildforce.sequoia.jpa.processor.core.serializer.JPASerializer;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfoResource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;

abstract class JPAAbstractRequestProcessor {

  protected final EntityManager em;
  protected final JPAServiceDocument sd;
  protected final JPAODataCRUDContextAccess sessionContext;
  protected final CriteriaBuilder cb;
  protected final UriInfoResource uriInfo;
  protected final JPASerializer serializer;
  protected final OData odata;
  protected final JPAServiceDebugger debugger;
  protected int successStatusCode = HttpStatusCode.OK.getStatusCode();
  protected final JPAODataRequestContextAccess requestContext;

  public JPAAbstractRequestProcessor(final OData odata, final JPAODataCRUDContextAccess context,
      final JPAODataRequestContextAccess requestContext) throws ODataJPAException {

    this.em = requestContext.getEntityManager();
    this.cb = em.getCriteriaBuilder();
    this.sessionContext = context;
    this.sd = context.getEdmProvider().getServiceDocument();
    this.uriInfo = requestContext.getUriInfo();
    this.serializer = requestContext.getSerializer();
    this.odata = odata;
    this.debugger = requestContext.getDebugger();
    this.requestContext = requestContext;
  }

  protected final void createSuccesResponse(final ODataResponse response, final ContentType responseFormat,
                                            final SerializerResult serializerResult) {

    response.setContent(serializerResult.getContent());
    response.setStatusCode(successStatusCode);
    response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
  }

}