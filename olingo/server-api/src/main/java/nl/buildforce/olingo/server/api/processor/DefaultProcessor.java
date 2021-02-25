/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.processor;

import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpHeader;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ODataServerError;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.etag.ETagHelper;
import nl.buildforce.olingo.server.api.etag.ServiceMetadataETagSupport;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>Processor implementation for handling default cases:
 * <ul><li>request for the metadata document</li>
 * <li>request for the service document</li>
 * <li>error handling</li></ul></p>
 * <p>This implementation is registered in the ODataHandler by default.
 * The default can be replaced by re-registering a custom implementation.</p>
 */
public class DefaultProcessor implements MetadataProcessor, ServiceDocumentProcessor, ErrorProcessor {
    private OData odata;
    private ServiceMetadata serviceMetadata;

    @Override
    public void init(OData odata, ServiceMetadata serviceMetadata) {
        this.odata = odata;
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public void readServiceDocument(ODataRequest request, ODataResponse response, /*UriInfo uriInfo,*/
                                    ContentType requestedContentType) throws ODataApplicationException, ODataLibraryException {
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
                response.setContent(serializer.serviceDocument(serviceMetadata, null).getContent());
                response.setStatusCode(HttpStatusCode.OK.getStatusCode());
                response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toString());
            }
        }
    }

    @Override
    public void readMetadata(ODataRequest request, ODataResponse response, /*UriInfo uriInfo,*/
                             ContentType requestedContentType) throws ODataApplicationException, ODataLibraryException {
        boolean isNotModified = false;
        ServiceMetadataETagSupport eTagSupport = serviceMetadata.getServiceMetadataETagSupport();
        if (eTagSupport != null && eTagSupport.getMetadataETag() != null) {
            // Set application etag at response
            response.setHeader(HttpHeader.ETAG, eTagSupport.getMetadataETag());
            // Check if metadata document has been modified
            ETagHelper eTagHelper = odata.createETagHelper();
            isNotModified = eTagHelper.checkReadPreconditions(eTagSupport.getMetadataETag(), request
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
                response.setContent(serializer.metadataDocument(serviceMetadata).getContent());
                response.setStatusCode(HttpStatusCode.OK.getStatusCode());
                response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toString());
            }
        }
    }

    @Override
    public void processError(ODataRequest request, ODataResponse response,
                             ODataServerError serverError,
                             ContentType requestedContentType) {
        try {
            ODataSerializer serializer = odata.createSerializer(requestedContentType);
            response.setContent(serializer.error(serverError).getContent());
            response.setStatusCode(serverError.getStatusCode());
            response.setHeader(HttpHeader.CONTENT_TYPE, requestedContentType.toString());
        } catch (Exception e) {
            // This should never happen but to be sure we have this catch here to prevent sending a stacktrace to a client.
            String responseContent =
                    "{\"error\":{\"code\":null,\"message\":\"An unexpected exception occurred during error processing\"}}";
            response.setContent(new ByteArrayInputStream(responseContent.getBytes(StandardCharsets.UTF_8)));
            response.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
            response.setHeader(HttpHeader.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        }
    }

}