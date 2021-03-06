package nl.buildforce.sequoia.processor.core.api;

import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.processor.JPAActionRequestProcessor;
import nl.buildforce.sequoia.processor.core.processor.JPACUDRequestProcessor;
import nl.buildforce.sequoia.processor.core.processor.JPAProcessorFactory;
import nl.buildforce.sequoia.processor.core.processor.JPARequestProcessor;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.processor.ActionPrimitiveProcessor;
import nl.buildforce.olingo.server.api.processor.ActionVoidProcessor;
import nl.buildforce.olingo.server.api.processor.ComplexCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ComplexProcessor;
import nl.buildforce.olingo.server.api.processor.CountEntityCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.EntityProcessor;
import nl.buildforce.olingo.server.api.processor.MediaEntityProcessor;
import nl.buildforce.olingo.server.api.processor.PrimitiveCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.PrimitiveValueProcessor;
import nl.buildforce.olingo.server.api.uri.UriInfo;

import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;

public final class JPAODataRequestProcessor
    implements PrimitiveValueProcessor, PrimitiveCollectionProcessor, ComplexProcessor, ComplexCollectionProcessor,
    CountEntityCollectionProcessor, EntityProcessor, MediaEntityProcessor, ActionPrimitiveProcessor,
    ActionVoidProcessor {

  private final JPAODataCRUDContextAccess sessionContext;
  private final JPAODataRequestContextAccess requestContext;
  private JPAProcessorFactory factory;

  public JPAODataRequestProcessor(final JPAODataCRUDContextAccess sessionContext,
      final JPAODataRequestContextAccess requestContext) {
    this.sessionContext = sessionContext;
    this.requestContext = requestContext;
  }

  @Override
  public void init(final OData odata, final ServiceMetadata serviceMetadata) {
    factory = new JPAProcessorFactory(odata, serviceMetadata, sessionContext);
  }

  @Override
  public void countEntityCollection(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException, ODataLibraryException {

    JPARequestProcessor p;
    try {
      p = factory.createProcessor(uriInfo, ContentType.TEXT_PLAIN, request.getAllHeaders(), requestContext);
      p.retrieveData(request, response, ContentType.TEXT_PLAIN);
    } catch (ODataApplicationException | ODataLibraryException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }
  }

  @Override
  public void createEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat) throws ODataApplicationException,
      ODataLibraryException {

    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, responseFormat, requestContext,
          request.getAllHeaders());
      p.createEntity(request, response, requestFormat, responseFormat);
    } catch (ODataApplicationException | ODataLibraryException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }

  }

  @Override
  public void createMediaEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException {

    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_CREATE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public void deleteComplex(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException {
    // Set NULL: .../Organizations('4')/Address

    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, requestContext);
      p.clearFields(request, response);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
          HttpStatusCode.NOT_IMPLEMENTED);
    }
  }

  @Override
  public void deleteEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException {

    try {
      final JPACUDRequestProcessor p = this.factory.createCUDRequestProcessor(uriInfo, requestContext);
      p.deleteEntity(request, response);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }
  }

  @Override
  public void deletePrimitive(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException {
    // Set NULL: .../Organizations('4')/Address/Country
    // https://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752306
    // 11.4.9.2 Set a Value to Null:
    // A successful DELETE request to the edit URL for a structural property, or to the edit URL of the raw value of a
    // primitive property, sets the property to null. The request body is ignored and should be empty. A DELETE request
    // to a non-nullable value MUST fail and the service respond with 400 Bad Request or other appropriate error. The
    // same rules apply whether the target is the value of a regular property or the value of a dynamic property. A
    // missing dynamic property is defined to be the same as a dynamic property with value null. All dynamic properties
    // are nullable.On success, the service MUST respond with 204 No Content and an empty body.
    //
    // Nullable checked by Olingo Core
    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, requestContext);
      p.clearFields(request, response);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
          HttpStatusCode.NOT_IMPLEMENTED);
    }
  }

  @Override
  public void deletePrimitiveValue()
      throws ODataApplicationException {
    // .../Organizations('4')/Address/Country/$value
    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE_VALUE,
        HttpStatusCode.NOT_IMPLEMENTED);

  }

  @Override
  public void deleteMediaEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException {
    // Set NULL: ../$value
    // https://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752305
    // 11.4.8.2 Deleting Stream Values:
    // A successful DELETE request to the edit URL of a stream property
    // attempts to set the property to null and results
    // in an error if the property is non-nullable. Attempting to request a
    // stream property whose value is null results
    // in 204 No Content.
    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  private void uniRead(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
    final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
    try {
      final JPARequestProcessor p = factory.createProcessor(uriInfo, responseFormat, request.getAllHeaders(), requestContext);
      p.retrieveData(request, response, responseFormat);
    } catch (ODataApplicationException | ODataLibraryException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
              HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }
  }

    @Override
  public void readComplex(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readComplexCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
      ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readEntityCollection(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readPrimitiveCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
      ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readPrimitive(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readPrimitiveValue(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void readMediaEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    uniRead(request, response,  uriInfo, responseFormat);
  }

  @Override
  public void updateComplex()
      throws ODataApplicationException {
    // ../Organizations('5')/Address
    // Not supported yet, as PATCH and PUT are allowed here
    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_UPDATE_VALUE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public void updateEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException {

    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, responseFormat, requestContext,
          request.getAllHeaders());
      p.updateEntity(request, response, requestFormat, responseFormat);
    } catch (ODataApplicationException | ODataLibraryException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    } catch (RollbackException e) {
      if (e.getCause() instanceof OptimisticLockException) {
        throw new ODataJPAProcessorException(e.getCause().getCause(), HttpStatusCode.PRECONDITION_FAILED);
      }
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void updatePrimitive(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException {
    // http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752306
    // only PUT ../Organizations('5')/Address/StreetName
    updateEntity(request, response, uriInfo, requestFormat, responseFormat);
  }

  @Override
  public void updatePrimitiveValue()
      throws ODataApplicationException {
    // ../Organizations('5')/Address/StreetName/$value
    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_UPDATE_VALUE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public void updateMediaEntity(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException {

    throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_UPDATE,
        HttpStatusCode.NOT_IMPLEMENTED);
  }

  @Override
  public void updatePrimitiveCollection(final ODataRequest request, final ODataResponse response,
      final UriInfo uriInfo, final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException {

    updateEntity(request, response, uriInfo, requestFormat, responseFormat);
  }

  @Override
  public void deletePrimitiveCollection(final ODataRequest request, final ODataResponse response,
      final UriInfo uriInfo) throws ODataApplicationException {
    // Set NULL: .../Organizations('4')/Comment
    // See deletePrimitive
    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, requestContext);
      p.clearFields(request, response);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
          HttpStatusCode.NOT_IMPLEMENTED);
    }
  }

  @Override
  public void updateComplexCollection(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException, ODataLibraryException {

    updateEntity(request, response, uriInfo, requestFormat, responseFormat);
  }

  @Override
  public void deleteComplexCollection(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo)
      throws ODataApplicationException {
    // Set NULL: .../Persons('4')/InhouseAddress
    // See deletePrimitive
    try {
      final JPACUDRequestProcessor p = factory.createCUDRequestProcessor(uriInfo, requestContext);
      p.clearFields(request, response);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.NOT_SUPPORTED_DELETE,
          HttpStatusCode.NOT_IMPLEMENTED);
    }
  }

  @Override
  public void processActionPrimitive(final ODataRequest request, final ODataResponse response, final UriInfo uriInfo,
      final ContentType requestFormat, final ContentType responseFormat)
      throws ODataApplicationException {

    try {
      final JPAActionRequestProcessor p = this.factory.createActionProcessor(uriInfo, responseFormat, request
          .getAllHeaders(), requestContext);
      p.performAction(request, response, requestFormat);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }
  }

  @Override
  public void processActionVoid(ODataRequest request, ODataResponse response, UriInfo uriInfo,
      ContentType requestFormat) throws ODataApplicationException {
    try {
      final JPAActionRequestProcessor p = this.factory.createActionProcessor(uriInfo, null, request.getAllHeaders(),
          requestContext);
      p.performAction(request, response, requestFormat);
    } catch (ODataApplicationException e) {
      throw e;
    } catch (ODataException e) {
      throw new ODataApplicationException(e.getLocalizedMessage(),
          HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), null, e);
    }
  }

}