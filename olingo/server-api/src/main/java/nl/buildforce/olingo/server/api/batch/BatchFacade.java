/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/package nl.buildforce.olingo.server.api.batch;

 import nl.buildforce.olingo.server.api.ODataApplicationException;
 import nl.buildforce.olingo.server.api.ODataLibraryException;
 import nl.buildforce.olingo.server.api.ODataRequest;
 import nl.buildforce.olingo.server.api.ODataResponse;
 import nl.buildforce.olingo.server.api.deserializer.batch.BatchRequestPart;
 import nl.buildforce.olingo.server.api.deserializer.batch.ODataResponsePart;

 /**
  * <p>Provides methods to process {@link ODataRequest}s and {@link BatchRequestPart}s
  * in the context of a batch request.</p>
  *
  * <p>Within a {@link nl.buildforce.olingo.server.api.processor.BatchProcessor BatchProcessor} implementation
 * BatchRequestParts should be passed to {@link #handleBatchRequest(BatchRequestPart)}.
  * Only if the BatchRequestPart represents a change set, the request will be delegated to
  * {@link nl.buildforce.olingo.server.api.processor.BatchProcessor#processChangeSet(BatchFacade, java.util.List)}.
  * Otherwise the requests will be directly executed.</p>
  *
  * <p>The processor implementation could use {@link #handleODataRequest(ODataRequest)} to process
  * requests in a change set.</p>
  */
 public interface BatchFacade {
   /**
    * Executes an ODataRequest, which must be a part of a change set.
    * Each request must have a Content-Id header field, which holds an identifier
    * that is unique in the whole batch request.
    * @param request ODataRequest to process
    * @return corresponding ODataResponse to the given request
    * @throws ODataLibraryException
    */
   ODataResponse handleODataRequest(ODataRequest request) throws ODataLibraryException;

   /**
    * Handles a BatchRequestPart.
    * @param request Request to process
    * @return corresponding {@link ODataResponsePart}
    * @throws ODataApplicationException
    * @throws ODataLibraryException
    */
   ODataResponsePart handleBatchRequest(BatchRequestPart request)
       throws ODataApplicationException, ODataLibraryException;

   /**
    * Extracts the boundary of a multipart/mixed header.
    * See RFC 2046#5.1
    * @param contentType Content Type
    * @return boundary
    */
   String extractBoundaryFromContentType(String contentType)
       throws ODataLibraryException;

 }