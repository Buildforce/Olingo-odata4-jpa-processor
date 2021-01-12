package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.etag.ETagHelper;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;
import nl.buildforce.olingo.server.api.processor.DefaultProcessor;
import nl.buildforce.olingo.server.api.processor.ServiceDocumentProcessor;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;

public class JPAODataServiceDocumentProcessor implements ServiceDocumentProcessor {

  private OData odata;
  private ServiceMetadata serviceMetadata;
  private final JPAODataCRUDContextAccess serviceContext;

  public JPAODataServiceDocumentProcessor(final JPAODataCRUDContextAccess serviceContext) {
    this.serviceContext = serviceContext;
  }

  @Override
  public void init(OData odata, ServiceMetadata serviceMetadata) {
    this.odata = odata;
    this.serviceMetadata = serviceMetadata;
  }

  /**
   * This is a copy from @see
   * {@link DefaultProcessor#readServiceDocument(ODataRequest, ODataResponse, ContentType)}
   * 
   */
  @Override
  public void readServiceDocument(final ODataRequest request, final ODataResponse response, /*final UriInfo uriInfo,*/
      final ContentType requestedContentType) throws ODataLibraryException {
    String uri = serviceContext.useAbsoluteContextURL() ? request.getRawBaseUri() : null;
    boolean isNotModified = false;
    ServiceMetadataETagSupport eTagSupport = serviceMetadata.getServiceMetadataETagSupport();
    if (eTagSupport != null && eTagSupport.getServiceDocumentETag() != null) {
      // Set application etag at response
      response.setHeader(HttpHeader.ETAG, eTagSupport.getServiceDocumentETag());
      // Check if service document has been modified
      ETagHelper eTagHelper = odata.createETagHelper();
      isNotModified = eTagHelper.checkReadPreconditions(eTagSupport.getServiceDocumentETag(), request
          .getHeaders(HttpHeader.IF_MATCH), request.getHeaders(HttpHeader.IF_NONE_MATCH));
    }

    // Send the correct response
    if (isNotModified) {
      response.setStatusCode(HttpStatusCode.NOT_MODIFIED.getStatusCode());
    } else {
      // HTTP HEAD requires no payload but a 200 OK response
      if (HttpMethod.HEAD == request.getMethod()) {
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
      } else {
        ODataSerializer serializer = odata.createSerializer(requestedContentType);
        response.setContent(serializer.serviceDocument(serviceMetadata, uri).getContent());
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toContentTypeString());
      }
    }
  }
}