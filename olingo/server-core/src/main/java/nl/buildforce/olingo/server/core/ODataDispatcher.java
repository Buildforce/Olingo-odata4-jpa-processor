/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core;

import java.io.IOException;
import java.util.List;

import nl.buildforce.olingo.commons.api.edm.EdmAction;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmFunction;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import nl.buildforce.olingo.commons.api.edm.EdmReturnType;
import nl.buildforce.olingo.commons.api.edm.EdmSingleton;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.etag.CustomETagSupport;
import nl.buildforce.olingo.server.api.etag.PreconditionException;
import nl.buildforce.olingo.server.api.processor.ActionComplexCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ActionComplexProcessor;
import nl.buildforce.olingo.server.api.processor.ActionEntityCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ActionEntityProcessor;
import nl.buildforce.olingo.server.api.processor.ActionPrimitiveCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ActionPrimitiveProcessor;
import nl.buildforce.olingo.server.api.processor.ActionVoidProcessor;
import nl.buildforce.olingo.server.api.processor.BatchProcessor;
import nl.buildforce.olingo.server.api.processor.ComplexCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ComplexProcessor;
import nl.buildforce.olingo.server.api.processor.CountComplexCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.CountEntityCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.CountPrimitiveCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.EntityCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.EntityProcessor;
import nl.buildforce.olingo.server.api.processor.MediaEntityProcessor;
import nl.buildforce.olingo.server.api.processor.MetadataProcessor;
import nl.buildforce.olingo.server.api.processor.PrimitiveCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.PrimitiveProcessor;
import nl.buildforce.olingo.server.api.processor.PrimitiveValueProcessor;
import nl.buildforce.olingo.server.api.processor.ReferenceCollectionProcessor;
import nl.buildforce.olingo.server.api.processor.ReferenceProcessor;
import nl.buildforce.olingo.server.api.processor.ServiceDocumentProcessor;
import nl.buildforce.olingo.server.api.serializer.RepresentationType;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriResourceNavigation;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;
import nl.buildforce.olingo.server.api.uri.UriResourcePrimitiveProperty;
import nl.buildforce.olingo.server.api.uri.UriResourceProperty;
import nl.buildforce.olingo.server.api.uri.UriResourceSingleton;
import nl.buildforce.olingo.server.core.batchhandler.BatchHandler;
import nl.buildforce.olingo.server.core.etag.PreconditionsValidator;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.PREFER;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static com.google.common.net.HttpHeaders.IF_NONE_MATCH;
import static com.google.common.net.HttpHeaders.IF_MATCH;

public class ODataDispatcher {

  private static final String NOT_IMPLEMENTED_MESSAGE = "not implemented";
  private final UriInfo uriInfo;
  private final ODataHandlerImpl handler;
  public static final String RETURN_MINIMAL = "return=minimal";
  public static final String RETURN_REPRESENTATION = "return=representation";
  private static final String EDMSTREAM = "Edm.Stream";

  public ODataDispatcher(UriInfo uriInfo, ODataHandlerImpl handler) {
    this.uriInfo = uriInfo;
    this.handler = handler;
  }

  public void dispatch(ODataRequest request, ODataResponse response) throws ODataApplicationException,
          ODataLibraryException {
    switch (uriInfo.getKind()) {
      case metadata -> {
        checkMethods(request.getMethod(), HttpMethod.GET, HttpMethod.HEAD);
        ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, handler.getCustomContentTypeSupport(), RepresentationType.METADATA);
        handler.selectProcessor(MetadataProcessor.class)
                .readMetadata(request, response, /*uriInfo,*/ requestedContentType);
      }
      case service -> {
        checkMethods(request.getMethod(), HttpMethod.GET, HttpMethod.HEAD);
        if ("".equals(request.getRawODataPath())) {
          handler.selectProcessor(RedirectProcessor.class)
                  .redirect(request, response);
        } else {
          ContentType serviceContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, handler.getCustomContentTypeSupport(), RepresentationType.SERVICE);
          handler.selectProcessor(ServiceDocumentProcessor.class)
                  .readServiceDocument(request, response, /*uriInfo,*/ serviceContentType);
        }
      }
      case resource, entityId -> handleResourceDispatching(request, response);
      case batch -> {
        checkMethod(request.getMethod(), HttpMethod.POST);
        new BatchHandler(handler, handler.selectProcessor(BatchProcessor.class))
                .process(request, response);
      }
      default -> throw new ODataHandlerException(NOT_IMPLEMENTED_MESSAGE,
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }

  private void handleResourceDispatching(ODataRequest request, ODataResponse response)
      throws ODataApplicationException, ODataLibraryException {

    int lastPathSegmentIndex = uriInfo.getUriResourceParts().size() - 1;
    UriResource lastPathSegment = uriInfo.getUriResourceParts().get(lastPathSegmentIndex);

    switch (lastPathSegment.getKind()) {
      case action -> {
        checkMethod(request.getMethod(), HttpMethod.POST);
        handleActionDispatching(request, response, (UriResourceAction) lastPathSegment);
      }
      case function -> {
        checkMethod(request.getMethod(), HttpMethod.GET);
        handleFunctionDispatching(request, response, (UriResourceFunction) lastPathSegment);
      }
      case entitySet, navigationProperty -> handleEntityDispatching(request, response,
              ((UriResourcePartTyped) lastPathSegment).isCollection(), isEntityOrNavigationMedia(lastPathSegment));
      case singleton -> handleSingleEntityDispatching(request, response, isSingletonMedia(lastPathSegment), true);
      case count -> {
        checkMethod(request.getMethod(), HttpMethod.GET);
        handleCountDispatching(request, response, lastPathSegmentIndex);
      }
      case primitiveProperty -> handlePrimitiveDispatching(request, response,
              ((UriResourceProperty) lastPathSegment).isCollection());
      case complexProperty -> handleComplexDispatching(request, response,
              ((UriResourceProperty) lastPathSegment).isCollection());
      case value -> handleValueDispatching(request, response, lastPathSegmentIndex);
      case ref -> handleReferenceDispatching(request, response, lastPathSegmentIndex);
      default -> throw new ODataHandlerException(NOT_IMPLEMENTED_MESSAGE,
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }

  private void handleFunctionDispatching(ODataRequest request, ODataResponse response,
                                         UriResourceFunction uriResourceFunction) throws ODataApplicationException, ODataLibraryException {
    EdmFunction function = uriResourceFunction.getFunction();
    if (function == null) {
      function = uriResourceFunction.getFunctionImport().getUnboundFunctions().get(0);
    }
    EdmReturnType returnType = function.getReturnType();
    switch (returnType.getType().getKind()) {
      case ENTITY -> handleEntityDispatching(request, response,
              returnType.isCollection() && uriResourceFunction.getKeyPredicates().isEmpty(),
              false);
      case PRIMITIVE -> handlePrimitiveDispatching(request, response, returnType.isCollection());
      case COMPLEX -> handleComplexDispatching(request, response, returnType.isCollection());
      default -> throw new ODataHandlerException(NOT_IMPLEMENTED_MESSAGE,
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }

  private void handleActionDispatching(ODataRequest request, ODataResponse response,
                                       UriResourceAction uriResourceAction) throws ODataApplicationException, ODataLibraryException {
    EdmAction action = uriResourceAction.getAction();
    if (action.isBound()) {
      // Only bound actions can have ETag control for the binding parameter.
      validatePreconditions(request, false);
    }
    ContentType requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
        RepresentationType.ACTION_PARAMETERS, false);
    EdmReturnType returnType = action.getReturnType();
    if (returnType == null) {
      handler.selectProcessor(ActionVoidProcessor.class)
          .processActionVoid(request, response, uriInfo, requestFormat);
    } else {
      boolean isCollection = returnType.isCollection();
      ContentType responseFormat;
      switch (returnType.getType().getKind()) {
        case ENTITY -> {
          responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, handler.getCustomContentTypeSupport(),
                  isCollection ? RepresentationType.COLLECTION_ENTITY : RepresentationType.ENTITY);
          if (isCollection) {
            handler.selectProcessor(ActionEntityCollectionProcessor.class)
                    .processActionEntityCollection(request, response, uriInfo, requestFormat, responseFormat);
          } else {
            handler.selectProcessor(ActionEntityProcessor.class)
                    .processActionEntity(request, response, uriInfo, requestFormat, responseFormat);
          }
        }
        case PRIMITIVE -> {
          responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, handler.getCustomContentTypeSupport(),
                  isCollection ? RepresentationType.COLLECTION_PRIMITIVE : RepresentationType.PRIMITIVE);
          if (isCollection) {
            handler.selectProcessor(ActionPrimitiveCollectionProcessor.class)
                    .processActionPrimitiveCollection(request, response, uriInfo, requestFormat, responseFormat);
          } else {
            handler.selectProcessor(ActionPrimitiveProcessor.class)
                    .processActionPrimitive(request, response, uriInfo, requestFormat, responseFormat);
          }
        }
        case COMPLEX -> {
          responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, handler.getCustomContentTypeSupport(),
                  isCollection ? RepresentationType.COLLECTION_COMPLEX : RepresentationType.COMPLEX);
          if (isCollection) {
            handler.selectProcessor(ActionComplexCollectionProcessor.class)
                    .processActionComplexCollection(request, response, uriInfo, requestFormat, responseFormat);
          } else {
            handler.selectProcessor(ActionComplexProcessor.class)
                    .processActionComplex(request, response, uriInfo, requestFormat, responseFormat);
          }
        }
        default -> throw new ODataHandlerException(NOT_IMPLEMENTED_MESSAGE,
                ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
      }
    }
  }

  private void handleReferenceDispatching(ODataRequest request, ODataResponse response,
                                          int lastPathSegmentIndex) throws ODataLibraryException {
    HttpMethod httpMethod = request.getMethod();
    boolean isCollection = ((UriResourcePartTyped) uriInfo.getUriResourceParts()
        .get(lastPathSegmentIndex - 1))
            .isCollection();

    if (isCollection && httpMethod == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.COLLECTION_REFERENCE);
      handler.selectProcessor(ReferenceCollectionProcessor.class)
          .readReferenceCollection(request, response, uriInfo, responseFormat);

    } else if (isCollection && httpMethod == HttpMethod.POST) {
      ContentType requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
          RepresentationType.REFERENCE, true);
      handler.selectProcessor(ReferenceProcessor.class)
          .createReference(request, response, uriInfo, requestFormat);

    } else if (!isCollection && httpMethod == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.REFERENCE);
      handler.selectProcessor(ReferenceProcessor.class).readReference(request, response, uriInfo, responseFormat);

    } else if (!isCollection && (httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.PATCH)) {
      ContentType requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
          RepresentationType.REFERENCE, true);
      handler.selectProcessor(ReferenceProcessor.class)
          .updateReference(request, response, uriInfo, requestFormat);

    } else if (httpMethod == HttpMethod.DELETE) {
      validatePreferHeader(request);
      handler.selectProcessor(ReferenceProcessor.class)
          .deleteReference(request, response, uriInfo);

    } else {
      throwMethodNotAllowed(httpMethod);
    }
  }

  private void handleValueDispatching(ODataRequest request, ODataResponse response,
                                      int lastPathSegmentIndex) throws ODataApplicationException, ODataLibraryException {
    // The URI Parser already checked if $value is allowed here so we only have to dispatch to the correct processor
    UriResource resource = uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1);
    if (resource instanceof UriResourceProperty
        || resource instanceof UriResourceFunction
            && ((UriResourceFunction) resource).getType().getKind() == EdmTypeKind.PRIMITIVE) {
      handlePrimitiveValueDispatching(request, response, resource);
    } else {
      handleMediaValueDispatching(request, response, resource);
    }
  }

  private void handleMediaValueDispatching(ODataRequest request, ODataResponse response,
                                           UriResource resource) throws
          ODataApplicationException, ODataLibraryException {
    HttpMethod method = request.getMethod();
    validatePreferHeader(request);
    if (method == HttpMethod.GET) {
      // This can be a GET on an EntitySet, Navigation or Function
      ContentType requestedContentType = ContentNegotiator.
          doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.MEDIA);
      handler.selectProcessor(MediaEntityProcessor.class)
          .readMediaEntity(request, response, uriInfo, requestedContentType);
      // PUT and DELETE can only be called on EntitySets or Navigation properties which are media resources
    } else if (method == HttpMethod.PUT && (isEntityOrNavigationMedia(resource) 
        || isSingletonMedia(resource))) {
      validatePreconditions(request, true);
      ContentType requestFormat = ContentType.parse(request.getHeader(CONTENT_TYPE));
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.ENTITY);
      handler.selectProcessor(MediaEntityProcessor.class)
          .updateMediaEntity(request, response, uriInfo, requestFormat, responseFormat);
    } else if (method == HttpMethod.DELETE && isEntityOrNavigationMedia(resource)) {
      validatePreconditions(request, true);
      handler.selectProcessor(MediaEntityProcessor.class)
          .deleteMediaEntity(request, response, uriInfo);
    } else {
      throwMethodNotAllowed(method);
    }
  }
  
  private void handlePrimitiveValueDispatching(ODataRequest request, ODataResponse response,
                                               UriResource resource) throws
          ODataApplicationException, ODataLibraryException {
    HttpMethod method = request.getMethod();
    EdmType type = resource instanceof UriResourceProperty ?
        ((UriResourceProperty) resource).getType() : ((UriResourceFunction) resource).getType();
    RepresentationType valueRepresentationType =
        type == EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary) ?
            RepresentationType.BINARY : RepresentationType.VALUE;
    if (method == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType requestedContentType = ContentNegotiator.
          doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), valueRepresentationType);

      handler.selectProcessor(PrimitiveValueProcessor.class)
          .readPrimitiveValue(request, response, uriInfo, requestedContentType);
    } else if (method == HttpMethod.PUT && resource instanceof UriResourceProperty) {
      validatePreconditions(request, false);
      ContentType requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
          valueRepresentationType, true);
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), valueRepresentationType);
      handler.selectProcessor(PrimitiveValueProcessor.class).updatePrimitiveValue();
    } else if (method == HttpMethod.DELETE && resource instanceof UriResourceProperty) {
      validatePreferHeader(request);
      validatePreconditions(request, false);
      handler.selectProcessor(PrimitiveValueProcessor.class)
          .deletePrimitiveValue();
    } else {
      throwMethodNotAllowed(method);
    }
  }
  
  private void handleComplexDispatching(ODataRequest request, ODataResponse response,
                                        boolean isCollection) throws ODataApplicationException, ODataLibraryException {
    HttpMethod method = request.getMethod();
    RepresentationType complexRepresentationType = isCollection ? RepresentationType.COLLECTION_COMPLEX
        : RepresentationType.COMPLEX;
    if (method == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), complexRepresentationType);
      if (isCollection) {
        handler.selectProcessor(ComplexCollectionProcessor.class)
            .readComplexCollection(request, response, uriInfo, requestedContentType);
      } else {
        handler.selectProcessor(ComplexProcessor.class)
            .readComplex(request, response, uriInfo, requestedContentType);
      }
    } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
      validatePreconditions(request, false);
      ContentType requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
          complexRepresentationType, true);
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), complexRepresentationType);
      if (isCollection) {
        handler.selectProcessor(ComplexCollectionProcessor.class)
            .updateComplexCollection(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        handler.selectProcessor(ComplexProcessor.class).updateComplex();
      }
    } else if (method == HttpMethod.DELETE) {
      validatePreferHeader(request);
      validatePreconditions(request, false);
      if (isCollection) {
        handler.selectProcessor(ComplexCollectionProcessor.class)
            .deleteComplexCollection(request, response, uriInfo);
      } else {
        handler.selectProcessor(ComplexProcessor.class)
            .deleteComplex(request, response, uriInfo);
      }
    } else {
      throwMethodNotAllowed(method);
    }
  }

  private void handlePrimitiveDispatching(ODataRequest request, ODataResponse response,
                                          boolean isCollection) throws ODataApplicationException, ODataLibraryException {
    HttpMethod method = request.getMethod();
    RepresentationType representationType = isCollection ? RepresentationType.COLLECTION_PRIMITIVE
        : RepresentationType.PRIMITIVE;
    if (method == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), representationType);
      if (isCollection) {
        handler.selectProcessor(PrimitiveCollectionProcessor.class)
            .readPrimitiveCollection(request, response, uriInfo, requestedContentType);
      } else {
        handler.selectProcessor(PrimitiveProcessor.class)
            .readPrimitive(request, response, uriInfo, requestedContentType);
      }
    } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
      validatePreconditions(request, false);
      ContentType requestFormat = null;
      List<UriResource> uriResources = uriInfo.getUriResourceParts();
      UriResource uriResource = uriResources.get(uriResources.size() - 1);
      if (uriResource instanceof UriResourcePrimitiveProperty &&
    		  ((UriResourcePrimitiveProperty)uriResource).getType()
    		  .getFullQualifiedName().getFullQualifiedNameAsString().equalsIgnoreCase(EDMSTREAM)) {
    	 requestFormat = ContentType.parse(request.getHeader(CONTENT_TYPE));
      } else {
    	  requestFormat = getSupportedContentType(request.getHeader(CONTENT_TYPE),
    	          representationType, true);
      }
      ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), representationType);
      if (isCollection) {
        handler.selectProcessor(PrimitiveCollectionProcessor.class)
            .updatePrimitiveCollection(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        handler.selectProcessor(PrimitiveProcessor.class)
            .updatePrimitive(request, response, uriInfo, requestFormat, responseFormat);
      }
    } else if (method == HttpMethod.DELETE) {
      validatePreferHeader(request);
      validatePreconditions(request, false);
      if (isCollection) {
        handler.selectProcessor(PrimitiveCollectionProcessor.class)
            .deletePrimitiveCollection(request, response, uriInfo);
      } else {
        handler.selectProcessor(PrimitiveProcessor.class)
            .deletePrimitive(request, response, uriInfo);
      }
    } else {
      throwMethodNotAllowed(method);
    }
  }

  private void handleCountDispatching(ODataRequest request, ODataResponse response,
                                      int lastPathSegmentIndex) throws ODataApplicationException, ODataLibraryException {
    validatePreferHeader(request);
    UriResource resource = uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1);
    if (resource instanceof UriResourceEntitySet
        || resource instanceof UriResourceNavigation
        || resource instanceof UriResourceFunction
            && ((UriResourceFunction) resource).getType().getKind() == EdmTypeKind.ENTITY) {
      handler.selectProcessor(CountEntityCollectionProcessor.class)
          .countEntityCollection(request, response, uriInfo);
    } else if (resource instanceof UriResourcePrimitiveProperty
        || resource instanceof UriResourceFunction
            && ((UriResourceFunction) resource).getType().getKind() == EdmTypeKind.PRIMITIVE) {
      handler.selectProcessor(CountPrimitiveCollectionProcessor.class)
          .countPrimitiveCollection(request, response, uriInfo);
    } else {
      handler.selectProcessor(CountComplexCollectionProcessor.class)
          .countComplexCollection(request, response, uriInfo);
    }
  }

  private void handleEntityDispatching(ODataRequest request, ODataResponse response,
                                       boolean isCollection, boolean isMedia) throws ODataApplicationException, ODataLibraryException {
    if (isCollection) {
      handleEntityCollectionDispatching(request, response, isMedia);
      } else {
        handleSingleEntityDispatching(request, response, isMedia, false);
      }
  }

  
  private void handleEntityCollectionDispatching(ODataRequest request, ODataResponse response,
                                                 boolean isMedia
      ) throws ODataApplicationException, ODataLibraryException {
    HttpMethod method = request.getMethod();
    if (method == HttpMethod.GET) {
      validatePreferHeader(request);
      ContentType requestedContentType = ContentNegotiator.
          doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.COLLECTION_ENTITY);
      handler.selectProcessor(EntityCollectionProcessor.class)
          .readEntityCollection(request, response, uriInfo, requestedContentType);
    } else if (method == HttpMethod.POST) {
      ContentType responseFormat = ContentNegotiator.
          doContentNegotiation(uriInfo.getFormatOption(),
          request, handler.getCustomContentTypeSupport(), RepresentationType.ENTITY);
      if (isMedia) {
        validatePreferHeader(request);
        ContentType requestFormat = ContentType.parse(
            request.getHeader(CONTENT_TYPE));
        handler.selectProcessor(MediaEntityProcessor.class)
            .createMediaEntity(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        try {
         ContentType requestFormat = (request.getHeader(CONTENT_TYPE) == null &&
             (request.getBody() == null || request.getBody().available() == 0)) ?
            getSupportedContentType(
            request.getHeader(CONTENT_TYPE),
            RepresentationType.ENTITY, false) : getSupportedContentType(
                request.getHeader(CONTENT_TYPE),
                RepresentationType.ENTITY, true);
            handler.selectProcessor(EntityProcessor.class)
            .createEntity(request, response, uriInfo, requestFormat, responseFormat);
        } catch (IOException e) {
          throw new ODataHandlerException("There is problem in the payload.",
              ODataHandlerException.MessageKeys.INVALID_PAYLOAD);
        }
      }
    } else {
      throwMethodNotAllowed(method);
    }
  }

  /**Checks if Prefer header is set with return=minimal or 
   * return=representation for GET and DELETE requests
   * @param request
   * @throws ODataHandlerException
   */
  private void validatePreferHeader(ODataRequest request) throws ODataHandlerException {
    List<String> returnPreference = request.getHeaders(PREFER);
    if (null != returnPreference) {
      for (String preference : returnPreference) {
        if (preference.equals(RETURN_MINIMAL) || preference.equals(RETURN_REPRESENTATION)) {
          throw new ODataHandlerException("Prefer Header not supported: " + preference,
              ODataHandlerException.MessageKeys.INVALID_PREFER_HEADER, preference);
        } 
      }
    }
  }
  
  private boolean isSingletonMedia(UriResource pathSegment) {
   return pathSegment instanceof UriResourceSingleton
       && ((UriResourceSingleton) pathSegment).getEntityType().hasStream();
  }

  
   
  private void handleSingleEntityDispatching(ODataRequest request, ODataResponse response,
                                             boolean isMedia, boolean isSingleton) throws
          ODataApplicationException,
        ODataLibraryException {
      HttpMethod method = request.getMethod();
      if (method == HttpMethod.GET) {
        validatePreferHeader(request);
        ContentType requestedContentType = ContentNegotiator.
            doContentNegotiation(uriInfo.getFormatOption(),
            request, handler.getCustomContentTypeSupport(), RepresentationType.ENTITY);
        handler.selectProcessor(EntityProcessor.class)
            .readEntity(request, response, uriInfo, requestedContentType);
      } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
        if (isMedia) {
          validatePreferHeader(request);
        }
        validatePreconditions(request, false);
        ContentType requestFormat = getSupportedContentType(
            request.getHeader(CONTENT_TYPE),
            RepresentationType.ENTITY, true);
        ContentType responseFormat = ContentNegotiator.
            doContentNegotiation(uriInfo.getFormatOption(),
            request, handler.getCustomContentTypeSupport(), RepresentationType.ENTITY);
        handler.selectProcessor(EntityProcessor.class)
            .updateEntity(request, response, uriInfo, requestFormat, responseFormat);
      } else if (method == HttpMethod.DELETE && !isSingleton) {
        validateIsSingleton(method);
        validatePreconditions(request, false);
        validatePreferHeader(request);

        EntityProcessor processor = isMedia ? handler.selectProcessor(MediaEntityProcessor.class) : handler.selectProcessor(EntityProcessor.class);
        processor.deleteEntity(request, response, uriInfo);
      } else {
        throwMethodNotAllowed(method);
      }
    }

  /*Delete method is not allowed for Entities navigating to Singleton*/ 
  private void validateIsSingleton(HttpMethod method) throws ODataHandlerException {
    int lastPathSegmentIndex = uriInfo.getUriResourceParts().size() - 1;
    UriResource pathSegment = uriInfo.getUriResourceParts().get(lastPathSegmentIndex);
    if (pathSegment instanceof UriResourceNavigation
        && uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1) instanceof UriResourceEntitySet
        && ((UriResourceEntitySet) uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1)).getEntitySet()
            .getRelatedBindingTarget(
                pathSegment.getSegmentValue()) instanceof EdmSingleton) {
      throwMethodNotAllowed(method);
    }
  }


  private void validatePreconditions(ODataRequest request, boolean isMediaValue)
      throws PreconditionException {
    // If needed perform preconditions validation.
    CustomETagSupport eTagSupport = handler.getCustomETagSupport();
    if (eTagSupport != null
        && new PreconditionsValidator(uriInfo).mustValidatePreconditions(eTagSupport, isMediaValue)
        && request.getHeader(IF_MATCH) == null
        && request.getHeader(IF_NONE_MATCH) == null) {
      throw new PreconditionException("Expected an if-match or if-none-match header.",
          PreconditionException.MessageKeys.MISSING_HEADER);
    }
  }

  private void checkMethod(HttpMethod requestMethod, HttpMethod allowedMethod)
      throws ODataHandlerException {
    if (requestMethod != allowedMethod) {
      throwMethodNotAllowed(requestMethod);
    }
  }

  private void checkMethods(HttpMethod requestMethod, HttpMethod... allowedMethods)
      throws ODataHandlerException {
    //Check if the request method is one of the allowed ones
    for (HttpMethod allowedMethod : allowedMethods) {
      if (requestMethod == allowedMethod) {
        return;
      }
    }
    //request method does not match any allowed method
    throwMethodNotAllowed(requestMethod);
  }

  private void throwMethodNotAllowed(HttpMethod httpMethod) throws ODataHandlerException {
    throw new ODataHandlerException("HTTP method " + httpMethod + " is not allowed.",
        ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, httpMethod.toString());
  }

  private ContentType getSupportedContentType(String contentTypeHeader,
                                              RepresentationType representationType, boolean mustNotBeNull)
      throws ODataHandlerException, ContentNegotiatorException {
    if (contentTypeHeader == null) {
      if (mustNotBeNull) {
        throw new ODataHandlerException("ContentTypeHeader parameter is null",
            ODataHandlerException.MessageKeys.MISSING_CONTENT_TYPE);
      }
      return ContentType.APPLICATION_JSON;
    }
    ContentType contentType;
    try {
      contentType = new ContentType(contentTypeHeader);
    } catch (IllegalArgumentException e) {
      throw new ODataHandlerException("Illegal content type.", e,
          ODataHandlerException.MessageKeys.INVALID_CONTENT_TYPE, contentTypeHeader);
    }
    ContentNegotiator.checkSupport(contentType, handler.getCustomContentTypeSupport(), representationType);
    return contentType;
  }

  private boolean isEntityOrNavigationMedia(UriResource pathSegment) {
    // This method MUST NOT check if the resource is of type function since these are handled differently
    return pathSegment instanceof UriResourceEntitySet
        && ((UriResourceEntitySet) pathSegment).getEntityType().hasStream()
        || pathSegment instanceof UriResourceNavigation
            && ((EdmEntityType) ((UriResourceNavigation) pathSegment).getType()).hasStream();
  }

}