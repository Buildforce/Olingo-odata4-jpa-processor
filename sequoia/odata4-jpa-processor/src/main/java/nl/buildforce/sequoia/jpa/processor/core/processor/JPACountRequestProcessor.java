package nl.buildforce.sequoia.jpa.processor.core.processor;

import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.jpa.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.jpa.processor.core.query.JPAJoinQuery;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

/**
 * <a href=
 * "http://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/part1-protocol/odata-v4.0-errata02-os-part1-protocol-complete.html#_Toc406398314">
 * OData Version 4.0 Part 2 - 11.2.9 Requesting the Number of Items in a Collection</a>
 */
public final class JPACountRequestProcessor extends JPAAbstractGetRequestProcessor {

  public JPACountRequestProcessor(final OData odata, final JPAODataCRUDContextAccess context,
      final JPAODataRequestContextAccess requestContext) throws ODataJPAException {
    super(odata, context, requestContext);
  }

  @Override
  public void retrieveData(final ODataRequest request, final ODataResponse response, final ContentType responseFormat)
          throws ODataJPAException, SerializerException, ODataApplicationException {
    final UriResource uriResource = uriInfo.getUriResourceParts().get(0);

    if (uriResource instanceof UriResourceEntitySet) {
      final EntityCollection result = countEntities(request, uriInfo);
      createSuccesResponse(response, ContentType.TEXT_PLAIN, serializer.serialize(request, result));
    } else {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_RESOURCE_TYPE,
          HttpStatusCode.NOT_IMPLEMENTED, uriResource.getKind().toString());
    }
  }

  protected final EntityCollection countEntities(final ODataRequest request, final UriInfoResource uriInfo)
          throws ODataJPAException, ODataApplicationException {

    JPAJoinQuery query;
    try {
      query = new JPAJoinQuery(odata, sessionContext, request.getAllHeaders(), requestContext);
    } catch (ODataJPAModelException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.QUERY_PREPARATION_ERROR,
          HttpStatusCode.INTERNAL_SERVER_ERROR, e);
    }

    final EntityCollection entityCollection = new EntityCollection();
    entityCollection.setCount(query.countResults().intValue());
    return entityCollection;
  }

}