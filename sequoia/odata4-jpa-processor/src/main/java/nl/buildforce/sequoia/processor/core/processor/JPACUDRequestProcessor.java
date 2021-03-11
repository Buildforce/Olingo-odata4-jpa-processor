package nl.buildforce.sequoia.processor.core.processor;

import nl.buildforce.olingo.commons.api.data.Entity;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Link;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.ex.ODataException;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpMethod;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ODataLibraryException;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ODataResponse;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.prefer.Preferences.Return;
import nl.buildforce.olingo.server.api.prefer.Preferences;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriParameter;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceComplexProperty;
import nl.buildforce.olingo.server.api.uri.UriResourceEntitySet;
import nl.buildforce.olingo.server.api.uri.UriResourceProperty;
import nl.buildforce.olingo.server.api.uri.UriResourceValue;

import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAssociationPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAAttribute;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAElement;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAEntityType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAPath;
import nl.buildforce.sequoia.metadata.core.edm.mapper.api.JPAStructuredType;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAException;
import nl.buildforce.sequoia.metadata.core.edm.mapper.exception.ODataJPAModelException;
import nl.buildforce.sequoia.processor.core.api.JPACUDRequestHandler;
import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataRequestContextAccess;
import nl.buildforce.sequoia.processor.core.api.JPAODataTransactionFactory.JPAODataTransaction;
import nl.buildforce.sequoia.processor.core.converter.JPATupleChildConverter;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAInvocationTargetException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPAProcessorException;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.processor.core.modify.JPAConversionHelper;
import nl.buildforce.sequoia.processor.core.modify.JPACreateResultFactory;
import nl.buildforce.sequoia.processor.core.modify.JPAUpdateResult;
import nl.buildforce.sequoia.processor.core.query.EdmEntitySetInfo;
import nl.buildforce.sequoia.processor.core.query.ExpressionUtil;
import nl.buildforce.sequoia.processor.core.query.Util;

import jakarta.persistence.EntityManager;
import nl.buildforce.sequoia.processor.core.converter.JPAExpandResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.net.HttpHeaders.LOCATION;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.ODATA_ENTITY_ID;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.PREFER;
import static nl.buildforce.olingo.commons.api.http.HttpHeader.PREFERENCE_APPLIED;
import static nl.buildforce.olingo.server.core.ODataDispatcher.RETURN_MINIMAL;

public final class JPACUDRequestProcessor extends JPAAbstractRequestProcessor {

  private static final String DEBUG_CREATE_ENTITY = "createEntity";
  private static final String DEBUG_UPDATE_ENTITY = "updateEntity";
  private final ServiceMetadata serviceMetadata;
  private final JPAConversionHelper helper;

  public JPACUDRequestProcessor(final OData odata, final ServiceMetadata serviceMetadata,
      final JPAODataCRUDContextAccess sessionContext, final JPAODataRequestContextAccess requestContext,
      JPAConversionHelper cudHelper) throws ODataJPAException {

    super(odata, sessionContext, requestContext);
    this.serviceMetadata = serviceMetadata;
    this.helper = cudHelper;
  }

  public void clearFields(final ODataRequest request, ODataResponse response) throws ODataJPAProcessException {

    //final int handle = debugger.startRuntimeMeasurement(this, "clearFields");
    final JPACUDRequestHandler handler = requestContext.getCUDRequestHandler();
    final EdmEntitySetInfo edmEntitySetInfo = Util.determineTargetEntitySetAndKeys(uriInfo.getUriResourceParts());

    final JPARequestEntity requestEntity = createRequestEntity(edmEntitySetInfo, uriInfo.getUriResourceParts(), request
        .getAllHeaders());

    JPAODataTransaction ownTransaction = null;
    final boolean foreignTransaction = requestContext.getTransactionFactory().hasActiveTransaction();

    if (!foreignTransaction)
      ownTransaction = requestContext.getTransactionFactory().createTransaction();
    try {
      // final int updateHandle = debugger.startRuntimeMeasurement(handler, DEBUG_UPDATE_ENTITY);
      handler.updateEntity(requestEntity, em, determineHttpVerb(request, uriInfo.getUriResourceParts()));
      if (!foreignTransaction)
        handler.validateChanges(em);
    } catch (ODataJPAProcessException e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw e;
    } catch (Exception e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    if (!foreignTransaction)
      ownTransaction.commit();
    // // debugger.stopRuntimeMeasurement(handle);
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
  }

  public void createEntity(final ODataRequest request, final ODataResponse response, final ContentType requestFormat,
      final ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

    // final int handle = debugger.startRuntimeMeasurement(this, DEBUG_CREATE_ENTITY);
    final JPACUDRequestHandler handler = requestContext.getCUDRequestHandler();

    final EdmEntitySetInfo edmEntitySetInfo = Util.determineModifyEntitySetAndKeys(uriInfo.getUriResourceParts());
    final Entity odataEntity = helper.convertInputStream(odata, request, requestFormat, uriInfo.getUriResourceParts());

    final JPARequestEntity requestEntity = createRequestEntity(edmEntitySetInfo, odataEntity, request.getAllHeaders());

    // Create entity
    Object result;
    JPAODataTransaction ownTransaction = null;

    final boolean foreignTransaction = requestContext.getTransactionFactory().hasActiveTransaction();
    if (!foreignTransaction)
      ownTransaction = requestContext.getTransactionFactory().createTransaction();
    try {
      // final int createHandle = debugger.startRuntimeMeasurement(handler, DEBUG_CREATE_ENTITY);
      result = handler.createEntity(requestEntity, em);
      if (!foreignTransaction)
        handler.validateChanges(em);
    } catch (ODataJPAProcessException e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw e;
    } catch (Exception e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    if (result != null && result.getClass() != requestEntity.getEntityType().getTypeClass()
        && !(result instanceof Map<?, ?>)) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.WRONG_RETURN_TYPE, HttpStatusCode.INTERNAL_SERVER_ERROR, result
          .getClass().toString(), requestEntity.getEntityType().getTypeClass().toString());
    }

    if (!foreignTransaction)
      ownTransaction.commit();

    createCreateResponse(request, response, responseFormat, requestEntity, edmEntitySetInfo, result);
    // // debugger.stopRuntimeMeasurement(handle);
  }

  /*
   * 4.4 Addressing References between Entities
   * DELETE http://host/service/Categories(1)/Products/$ref?$id=../../Products(0)
   * DELETE http://host/service/Products(0)/Category/$ref
   */
  public void deleteEntity(final ODataRequest request, final ODataResponse response) throws ODataJPAProcessException {
    // final int handle = debugger.startRuntimeMeasurement(this, "deleteEntity");
    final JPACUDRequestHandler handler = requestContext.getCUDRequestHandler();
    final JPAEntityType et;
    final Map<String, Object> jpaKeyPredicates = new HashMap<>();

    // 1. Retrieve the entity set which belongs to the requested entity
    List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
    // Note: only in our example we can assume that the first segment is the EntitySet
    UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
    EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

    // 2. Convert Key from URL to JPA
    try {
      et = sessionContext.getEdmProvider().getServiceDocument().getEntity(edmEntitySet.getName());
      List<UriParameter> uriKeyPredicates = uriResourceEntitySet.getKeyPredicates();
      for (UriParameter uriParam : uriKeyPredicates) {
        JPAAttribute attribute = et.getPath(uriParam.getName()).getLeaf();
        jpaKeyPredicates.put(attribute.getInternalName(), ExpressionUtil.convertValueOnAttribute(odata, attribute,
            uriParam.getText(), true));
      }
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
    final JPARequestEntity requestEntity = createRequestEntity(et, jpaKeyPredicates, request.getAllHeaders());

    // 3. Perform Delete
    JPAODataTransaction ownTransaction = null;
    final boolean foreignTransaction = requestContext.getTransactionFactory().hasActiveTransaction();
    if (!foreignTransaction)
      ownTransaction = requestContext.getTransactionFactory().createTransaction();
    try {
      // final int deleteHandle = debugger.startRuntimeMeasurement(handler, "deleteEntity");
      handler.deleteEntity(requestEntity, em);
      if (!foreignTransaction)
        handler.validateChanges(em);
    } catch (ODataJPAProcessException e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw e;
    } catch (Throwable e) { // NOSONAR
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    if (!foreignTransaction)
      ownTransaction.commit();

    // 4. configure the response object
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    // // debugger.stopRuntimeMeasurement(handle);
  }

  public void updateEntity(final ODataRequest request, final ODataResponse response, final ContentType requestFormat,
      final ContentType responseFormat) throws ODataJPAProcessException, ODataLibraryException {

    // final int handle = debugger.startRuntimeMeasurement(this, DEBUG_UPDATE_ENTITY);
    final JPACUDRequestHandler handler = requestContext.getCUDRequestHandler();
    final EdmEntitySetInfo edmEntitySetInfo = Util.determineModifyEntitySetAndKeys(uriInfo.getUriResourceParts());
    final Entity odataEntity = helper.convertInputStream(odata, request, requestFormat, uriInfo.getUriResourceParts());

    // http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752300
    // 11.4.3 Update an Entity
    // ...
    // The entity MUST NOT contain related entities as inline content. It MAY contain binding information for
    // navigation properties. For single-valued navigation properties this replaces the relationship. For
    // collection-valued navigation properties this adds to the relationship.
    // TODO navigation properties this replaces the relationship
    final JPARequestEntity requestEntity = createRequestEntity(edmEntitySetInfo, odataEntity, request.getAllHeaders());

    // Update entity
    JPAUpdateResult updateResult;

    JPAODataTransaction ownTransaction = null;
    final boolean foreignTransaction = requestContext.getTransactionFactory().hasActiveTransaction();
    if (!foreignTransaction)
      ownTransaction = requestContext.getTransactionFactory().createTransaction();
    try {
      // http://docs.oasis-open.org/odata/odata/v4.0/errata03/os/complete/part1-protocol/odata-v4.0-errata03-os-part1-protocol-complete.html#_Toc453752300
      // 11.4.3 Update an Entity
      // Services SHOULD support PATCH as the preferred means of updating an entity. ... .Services MAY additionally
      // support PUT, but should be aware of the potential for data-loss in round-tripping properties that the client
      // may not know about in advance, such as open or added properties, or properties not specified in metadata.
      // 11.4.4 Upsert an Entity
      // To ensure that an update request is not treated as an insert, the client MAY specify an If-Match header in the
      // update request. The service MUST NOT treat an update request containing an If-Match header as an insert.
      // A PUT or PATCH request MUST NOT be treated as an update if an If-None-Match header is specified with a value of
      // "*".
      // final int updateHandle = debugger.startRuntimeMeasurement(handler, DEBUG_UPDATE_ENTITY);
      updateResult = handler.updateEntity(requestEntity, em, determineHttpVerb(request, uriInfo.getUriResourceParts()));
      if (!foreignTransaction)
        handler.validateChanges(em);
    } catch (ODataJPAProcessException e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw e;
    } catch (Throwable e) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    if (updateResult == null) {
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.RETURN_NULL, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
    if (updateResult.getModifiedEntity() != null && !requestEntity.getEntityType().getTypeClass().isInstance(
        updateResult.getModifiedEntity())) {
      // This shall tolerate that e.g. EclipseLink return at least in case of InheritanceType.TABLE_PER_CLASS an
      // instance of a sub class even so the super class was requested.
      if (!foreignTransaction)
        ownTransaction.rollback();
      throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.WRONG_RETURN_TYPE, HttpStatusCode.INTERNAL_SERVER_ERROR,
          updateResult.getModifiedEntity().getClass().toString(), requestEntity.getEntityType().getTypeClass()
              .toString());
    }
    if (!foreignTransaction) ownTransaction.commit();

    if (updateResult.wasCreate()) {
      createCreateResponse(request, response, responseFormat, requestEntity.getEntityType(),
          edmEntitySetInfo.getEdmEntitySet(), updateResult.getModifiedEntity());
    } else
      createUpdateResponse(request, response, responseFormat, requestEntity, edmEntitySetInfo, updateResult);
    // // debugger.stopRuntimeMeasurement(handle);
  }

  private HttpMethod determineHttpVerb(final ODataRequest request, List<UriResource> resourceParts) {
    final HttpMethod originalMethod = request.getMethod();
    final HttpMethod targetMethod;
    final int noResourceParts = resourceParts.size();
    if (originalMethod == HttpMethod.PUT && resourceParts.get(noResourceParts - 1) instanceof UriResourceProperty) {
      targetMethod = HttpMethod.PATCH;
    } else {
      targetMethod = originalMethod;
    }
    return targetMethod;
  }

  final JPARequestEntity createRequestEntity(final EdmEntitySet edmEntitySet, final Entity odataEntity,
      final Map<String, List<String>> headers) throws ODataJPAProcessorException {

    try {
      final JPAEntityType et = sessionContext.getEdmProvider().getServiceDocument().getEntity(edmEntitySet.getName());
      return createRequestEntity(et, odataEntity, new HashMap<>(0), headers, null);
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  final JPARequestEntity createRequestEntity(final EdmEntitySetInfo edmEntitySetInfo, final Entity odataEntity,
      final Map<String, List<String>> headers) throws ODataJPAProcessorException {

    try {
      final JPAEntityType et = sessionContext.getEdmProvider().getServiceDocument().getEntity(edmEntitySetInfo
          .getName());
      final Map<String, Object> keys = helper.convertUriKeys(odata, et, edmEntitySetInfo.getKeyPredicates());
      final JPARequestEntityImpl requestEntity = (JPARequestEntityImpl) createRequestEntity(et, odataEntity, keys,
          headers, et.getAssociationPath(edmEntitySetInfo.getNavigationPath()));
      requestEntity.setBeforeImage(createBeforeImage(requestEntity, em));
      return requestEntity;
    } catch (ODataException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  /**
   * Converts the deserialized request into the internal (JPA) format, which shall be provided to the hook method
   * @param et
   * @param odataEntity
   * @param keys
   * @param headers
   * @param jpaAssociationPath
   * @return
   * @throws ODataJPAProcessorException
   */
  final JPARequestEntity createRequestEntity(
    final JPAEntityType et,
    final Entity odataEntity,
    final Map<String, Object> keys,
    final Map<String, List<String>> headers,
    final JPAAssociationPath jpaAssociationPath
  ) throws ODataJPAProcessorException {

    try {
      if (jpaAssociationPath == null) {
        final Map<String, Object> jpaAttributes = helper.convertProperties(odata, et, odataEntity.getProperties());
        final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities = createInlineEntities(et, odataEntity,
            headers);
        final Map<JPAAssociationPath, List<JPARequestLink>> relationLinks = createRelationLinks(et, odataEntity);
        return new JPARequestEntityImpl(et, jpaAttributes, relatedEntities, relationLinks, keys, headers,
            requestContext);
      } else {
        // Handle requests like POST
        // .../AdministrativeDivisions(DivisionCode='DE6',CodeID='NUTS1',CodePublisher='Eurostat')/Children
        final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities = createInlineEntities(odataEntity,
            jpaAssociationPath, headers);
        return new JPARequestEntityImpl(et, Collections.emptyMap(), relatedEntities, Collections.emptyMap(), keys,
            headers, requestContext);
      }

    } catch (ODataException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  /**
   * Create an RequestEntity instance for delete requests
   * @param et
   * @param keys
   * @param headers
   * @return
   */
  final JPARequestEntity createRequestEntity(final JPAEntityType et, final Map<String, Object> keys,
      final Map<String, List<String>> headers) {

    final Map<String, Object> jpaAttributes = new HashMap<>(0);
    final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities = new HashMap<>(0);
    final Map<JPAAssociationPath, List<JPARequestLink>> relationLinks = new HashMap<>(0);

    return new JPARequestEntityImpl(et, jpaAttributes, relatedEntities, relationLinks, keys, headers, requestContext);
  }

  private Entity convertEntity(JPAEntityType et, Object result, Map<String, List<String>> headers)
      throws ODataJPAProcessorException {

    try {
      final JPATupleChildConverter converter = new JPATupleChildConverter(sd, odata.createUriHelper(), serviceMetadata);
      final JPACreateResultFactory factory = new JPACreateResultFactory(converter);//
      return converter.getResult(factory.getJPACreateResult(et, result, headers), Collections.emptySet())
          .get(JPAExpandResult.ROOT_RESULT_KEY).getEntities().get(0);
    } catch (ODataJPAModelException | ODataApplicationException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

  }

  private Map<String, Object> convertUriPath(JPAEntityType et, final List<UriResource> resourcePaths)
      throws ODataJPAModelException {

    final Map<String, Object> jpaAttributes = new HashMap<>();
    Map<String, Object> currentMap = jpaAttributes;
    JPAStructuredType st = et;
    int lastIndex;

    if (resourcePaths.get(resourcePaths.size() - 1) instanceof UriResourceValue)
      lastIndex = resourcePaths.size() - 1;
    else
      lastIndex = resourcePaths.size();

    for (int i = 1; i < lastIndex; i++) {
      final UriResourceProperty uriResourceProperty = (UriResourceProperty) resourcePaths.get(i);
      if (uriResourceProperty instanceof UriResourceComplexProperty && i < resourcePaths.size() - 1) {
        final Map<String, Object> jpaEmbedded = new HashMap<>();
        final JPAPath path = st.getPath(uriResourceProperty.getProperty().getName());
        final String internalName = path.getPath().get(0).getInternalName();

        currentMap.put(internalName, jpaEmbedded);

        currentMap = jpaEmbedded;
        st = st.getAttribute(internalName).getStructuredType();
      } else {
        currentMap.put(st.getPath(uriResourceProperty.getProperty().getName()).getLeaf().getInternalName(), null);
      }
    }
    return jpaAttributes;
  }

  private Optional<Object> createBeforeImage(final JPARequestEntity requestEntity, final EntityManager em)
      throws ODataJPAProcessorException, ODataJPAInvocationTargetException {

    if (!requestEntity.getKeys().isEmpty()) {
      final Object key = requestEntity.getModifyUtil().createPrimaryKey(requestEntity.getEntityType(), requestEntity
          .getKeys(), requestEntity.getEntityType());
      final Optional<Object> beforeImage = Optional.ofNullable(em.find(requestEntity.getEntityType().getTypeClass(),
          key));
      beforeImage.ifPresent(em::detach);
      return beforeImage;
    }
    return Optional.empty();
  }

  private void createCreateResponse(final ODataRequest request, final ODataResponse response,
      final ContentType responseFormat, final JPAEntityType et, EdmEntitySet edmEntitySet, final Object result)
      throws SerializerException, ODataJPAProcessorException, ODataJPASerializerException {

    // http://docs.oasis-open.org/odata/odata/v4.0/odata-v4.0-part1-protocol.html
    // Create response:
    // 8.3.2 Header Location
    // The Location header MUST be returned in the response from a Create Entity or Create Media Entity request to
    // specify the edit URL, or for read-only entities the read URL, of the created entity, and in responses returning
    // 202 Accepted to specify the URL that the client can use to request the status of an asynchronous request.
    //
    // 8.3.3 Header OData-EntityId
    // A response to a create or upsert operation that returns 204 No Content MUST include an OData-EntityId response
    // header. The value of the header is the entity-id of the entity that was acted on by the request. The syntax of
    // the OData-EntityId header is specified in [OData-ABNF].
    //
    // 8.2.8.7 Preference return=representation and return=minimal states:
    // A preference of return=minimal requests that the service invoke the request but does not return content in the
    // response. The service MAY apply this preference by returning 204 No Content in which case it MAY include a
    // Preference-Applied response header containing the return=minimal preference.
    // A preference of return=representation requests that the service invokes the request and returns the modified
    // resource. The service MAY apply this preference by returning the representation of the successfully modified
    // resource in the body of the response, formatted according to the rules specified for the requested format. In
    // this case the service MAY include a Preference-Applied response header containing the return=representation
    // preference.
    //
    // 11.4.1.5 Returning Results from Data Modification Requests
    // Clients can request whether created or modified resources are returned from create, update, and upsert operations
    // using the return preference header. In the absence of such a header, services SHOULD return the created or
    // modified content unless the resource is a stream property value.
    // When returning content other than for an update to a media entity stream, services MUST return the same content
    // as a subsequent request to retrieve the same resource. For updating media entity streams, the content of a
    // non-empty response body MUST be the updated media entity.
    //
    // 11.4.2 Create an Entity
    // Upon successful completion, the response MUST contain a Location header that contains the edit URL or read URL of
    // the created entity.
    //
    setSuccessStatusCode(HttpStatusCode.CREATED.getStatusCode());
    Preferences prefer = odata.createPreferences(request.getHeaders(PREFER));
    // TODO Stream properties

    String location = helper.convertKeyToLocal(odata, request, edmEntitySet, et, result);
    if (prefer.getReturn() == Return.MINIMAL) {
      createMinimalCreateResponse(response, location);
    } else {
      Entity createdEntity = convertEntity(et, result, request.getAllHeaders());
      EntityCollection entities = new EntityCollection();
      entities.getEntities().add(createdEntity);
      createSuccessResponse(response, responseFormat, serializer.serialize(request, entities));
      response.setHeader(LOCATION, location);
    }
  }

  private void createCreateResponse(final ODataRequest request, final ODataResponse response,
      final ContentType responseFormat, final JPARequestEntity requestEntity, final EdmEntitySetInfo edmEntitySet,
      final Object result) throws SerializerException, ODataJPAProcessorException, ODataJPASerializerException {

    if (!requestEntity.getKeys().isEmpty()) {
      // .../AdministrativeDivisions(DivisionCode='DE5',CodeID='NUTS1',CodePublisher='Eurostat')/Children
      // As of now only one related entity can be created
      try {
        final JPAAssociationPath path = requestEntity.getEntityType().getAssociationPath(edmEntitySet
            .getNavigationPath());

        final JPARequestEntity linkedEntity = requestEntity.getRelatedEntities().get(path).get(0);
        final Object linkedResult = getLinkedResult(result, path, requestEntity.getBeforeImage());
        createCreateResponse(request, response, responseFormat, linkedEntity.getEntityType(), edmEntitySet
            .getTargetEdmEntitySet(), linkedResult);
      } catch (ODataJPAModelException e) {
        throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
    } else {
      createCreateResponse(request, response, responseFormat, requestEntity.getEntityType(), edmEntitySet
          .getEdmEntitySet(), result);
    }
  }

  private Map<JPAAssociationPath, List<JPARequestEntity>> createInlineEntities(final Entity odataEntity,
      final JPAAssociationPath path, final Map<String, List<String>> headers) throws ODataJPAProcessorException {

    final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities = new HashMap<>(1);
    final List<JPARequestEntity> inlineEntities = new ArrayList<>();

    inlineEntities.add(createRequestEntity((JPAEntityType) path.getTargetType(), odataEntity, new HashMap<>(0), headers, null));

    relatedEntities.put(path, inlineEntities);

    return relatedEntities;
  }

  private Map<JPAAssociationPath, List<JPARequestEntity>> createInlineEntities(JPAEntityType et, Entity odataEntity,
      Map<String, List<String>> headers) throws ODataJPAModelException, ODataJPAProcessorException {

    final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities = new HashMap<>();

    for (JPAAssociationPath path : et.getAssociationPathList()) {
      List<Property> stProperties = odataEntity.getProperties();
      Property p = null;
      for (JPAElement pathItem : path.getPath()) {
        if (pathItem == path.getLeaf()) { // We have reached the target and can process further
          final Link navigationLink = p != null ? p.asComplex().getNavigationLink(pathItem.getExternalName())
              : odataEntity.getNavigationLink(pathItem.getExternalName());
          createInlineEntities((JPAEntityType) path.getTargetType(), headers, relatedEntities, navigationLink, path);
        }
        p = findProperty(pathItem.getExternalName(), stProperties);
        if (p == null) break;
        if (p.isComplex()) {
          stProperties = p.asComplex().getValue();
        }
      }
    }
    return relatedEntities;
  }

  private void createInlineEntities(JPAEntityType st, Map<String, List<String>> headers,
      final Map<JPAAssociationPath, List<JPARequestEntity>> relatedEntities, Link navigationLink,
      JPAAssociationPath path) throws ODataJPAProcessorException {

    if (navigationLink == null) return;
    final List<JPARequestEntity> inlineEntities = new ArrayList<>();
    if (path.getLeaf().isCollection()) {
      for (Entity e : navigationLink.getInlineEntitySet().getEntities()) {
        inlineEntities.add(createRequestEntity(st, e, new HashMap<>(0), headers, null));
      }
      relatedEntities.put(path, inlineEntities);
    } else {
      inlineEntities.add(createRequestEntity(st, navigationLink.getInlineEntity(),
              new HashMap<>(0), headers, null));
      relatedEntities.put(path, inlineEntities);
    }
  }

  private void createMinimalCreateResponse(final ODataResponse response, String location) {
    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    response.setHeader(PREFERENCE_APPLIED, RETURN_MINIMAL);
    response.setHeader(LOCATION, location);
    response.setHeader(ODATA_ENTITY_ID, location);
  }

  private Map<JPAAssociationPath, List<JPARequestLink>> createRelationLinks(JPAEntityType et, Entity odataEntity)
      throws ODataJPAModelException {

    final Map<JPAAssociationPath, List<JPARequestLink>> relationLinks =
        new HashMap<>();
    for (Link binding : odataEntity.getNavigationBindings()) {
      final List<JPARequestLink> bindingLinks = new ArrayList<>();
      JPAAssociationPath path = et.getAssociationPath(binding.getTitle());
      if (path.getLeaf().isCollection()) {
        for (String bindingLink : binding.getBindingLinks()) {
          final JPARequestLink requestLink = new JPARequestLinkImpl(path, bindingLink, helper);
          bindingLinks.add(requestLink);
        }
      } else {
        final JPARequestLink requestLink = new JPARequestLinkImpl(path, binding.getBindingLink(), helper);
        bindingLinks.add(requestLink);
      }
      relationLinks.put(path, bindingLinks);
    }
    return relationLinks;
  }

  private JPARequestEntity createRequestEntity(EdmEntitySetInfo edmEntitySetInfo, List<UriResource> resourceParts,
      Map<String, List<String>> headers) throws ODataJPAProcessorException {

    try {
      final JPAEntityType et = sessionContext.getEdmProvider().getServiceDocument().getEntity(edmEntitySetInfo
          .getEdmEntitySet().getName());
      final Map<String, Object> keys = helper.convertUriKeys(odata, et, edmEntitySetInfo.getKeyPredicates());
      final Map<String, Object> jpaAttributes = convertUriPath(et, resourceParts);

      return new JPARequestEntityImpl(et, jpaAttributes, Collections.emptyMap(), Collections.emptyMap(), keys, headers,
          requestContext);

    } catch (ODataException e) {
      throw new ODataJPAProcessorException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  private void createUpdateResponse(final ODataRequest request, final ODataResponse response,
      final ContentType responseFormat, final JPARequestEntity requestEntity, final EdmEntitySetInfo edmEntitySetInfo,
      final JPAUpdateResult updateResult)
      throws SerializerException, ODataJPAProcessorException, ODataJPASerializerException {

    // http://docs.oasis-open.org/odata/odata/v4.0/odata-v4.0-part1-protocol.html

    // 8.2.8.7 Preference return=representation and return=minimal states:
    // A preference of return=minimal requests that the service invoke the request but does not return content in the
    // response. The service MAY apply this preference by returning 204 No Content in which case it MAY include a
    // Preference-Applied response header containing the return=minimal preference.
    // A preference of return=representation requests that the service invokes the request and returns the modified
    // resource. The service MAY apply this preference by returning the representation of the successfully modified
    // resource in the body of the response, formatted according to the rules specified for the requested format. In
    // this case the service MAY include a Preference-Applied response header containing the return=representation
    // preference.
    //
    // 11.4.1.5 Returning Results from Data Modification Requests
    // Clients can request whether created or modified resources are returned from create, update, and upsert operations
    // using the return preference header. In the absence of such a header, services SHOULD return the created or
    // modified content unless the resource is a stream property value.
    // When returning content other than for an update to a media entity stream, services MUST return the same content
    // as a subsequent request to retrieve the same resource. For updating media entity streams, the content of a
    // non-empty response body MUST be the updated media entity.
    //
    this.setSuccessStatusCode(HttpStatusCode.OK.getStatusCode());
    Preferences prefer = odata.createPreferences(request.getHeaders(PREFER));
    // TODO Stream properties
    if (updateResult == null || prefer.getReturn() == Return.MINIMAL) {
      response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
      response.setHeader(PREFERENCE_APPLIED, RETURN_MINIMAL);
    } else {
      if (updateResult.getModifiedEntity() == null)
        throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.RETURN_MISSING_ENTITY, HttpStatusCode.INTERNAL_SERVER_ERROR);

      Entity updatedEntity;
      JPAAssociationPath path;
      try {
        path = requestEntity.getEntityType().getAssociationPath(edmEntitySetInfo
            .getNavigationPath());
      } catch (ODataJPAModelException e) {
        throw new ODataJPAProcessorException(e, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
      if (path != null) {
        // PATCH .../Organizations('1')/AdministrativeInformation/Updated/User
        final JPARequestEntity linkedEntity = requestEntity.getRelatedEntities().get(path).get(0);
        final Object linkedResult = getLinkedResult(updateResult.getModifiedEntity(), path, Optional.empty());
        updatedEntity = convertEntity(linkedEntity.getEntityType(), linkedResult, request.getAllHeaders());
      } else {
        updatedEntity = convertEntity(requestEntity.getEntityType(), updateResult.getModifiedEntity(), request
            .getAllHeaders());
      }
      EntityCollection entities = new EntityCollection();
      entities.getEntities().add(updatedEntity);
      createSuccessResponse(response, responseFormat, serializer.serialize(request, entities));
    }
  }

  private Property findProperty(final String name, final List<Property> properties) {

    for (Property property : properties) {
      if (name.equals(property.getName())) {
        return property;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private Object getLinkedResult(final Object result, final JPAAssociationPath path, final Optional<Object> beforeImage)
      throws ODataJPAProcessorException {

    if (result instanceof Map<?, ?>) {
      return getLinkedMapBasedResult((Map<String, Object>) result, path);
    } else {
      if (beforeImage.isPresent() && beforeImage.get().equals(result)) {
        return getLinkedInstanceBasedResultByDelta(result, path, beforeImage);
      } else {
        return getLinkedInstanceBasedResultByIndex(result, path);
      }
    }
  }

  /*
   * The method compares before image with the current state of a collection. It is expected that exactly one entry has
   * been created. Up to now no contract checks are performed, which may change in the future.
   */
  Object getLinkedInstanceBasedResultByDelta(final Object result, final JPAAssociationPath path,
      final Optional<Object> beforeImage) throws ODataJPAProcessorException {
    if (beforeImage.isPresent()) {
      if (em.contains(beforeImage.get())) {
        throw new ODataJPAProcessorException(ODataJPAProcessorException.MessageKeys.BEFORE_IMAGE_MERGED, HttpStatusCode.INTERNAL_SERVER_ERROR);
      }
      if (!path.getLeaf().isCollection())
        return getLinkedInstanceBasedResultByIndex(result, path);

      Object value = result;
      Object before = beforeImage.get();
      for (JPAElement pathItem : path.getPath()) {
        final Map<String, Object> valueGetterMap = helper.buildGetterMap(value);
        value = valueGetterMap.get(pathItem.getInternalName());
        // We are not able to use the buffered Getter Map for the before image as well, as the buffer uses a HashMap and
        // before image and result have the same key and therefore are equal and have likely the same hash value.
        final Map<String, Object> beforeGetterMap = helper.determineGetter(before);
        before = beforeGetterMap.get(pathItem.getInternalName());
      }
      if (value != null && !((Collection<?>) value).isEmpty()) {
        for (final Object element : ((Collection<?>) value)) {
          if (!((Collection<?>) before).contains(element))
            return element;
        }
      }
      return null;
    }
    return null;
  }

  private Object getLinkedInstanceBasedResultByIndex(final Object result, final JPAAssociationPath path)
      throws ODataJPAProcessorException {

    Object value = result;
    for (JPAElement pathItem : path.getPath()) {
      final Map<String, Object> embeddedGetterMap = helper.buildGetterMap(value);
      value = embeddedGetterMap.get(pathItem.getInternalName());
    }
    if (path.getLeaf().isCollection() && value != null) {
      if (((Collection<?>) value).isEmpty())
        value = null;
      else
        value = ((Collection<?>) value).toArray()[0];
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  private Object getLinkedMapBasedResult(final Map<String, Object> result, final JPAAssociationPath path) {
    Map<String, Object> target = result;
    for (JPAElement pathItem : path.getPath())
      target = (Map<String, Object>) target.get(pathItem.getInternalName());
    return target;
  }

}