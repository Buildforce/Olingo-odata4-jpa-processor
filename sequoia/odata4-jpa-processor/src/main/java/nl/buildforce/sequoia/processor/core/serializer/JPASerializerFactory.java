package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.OData;
import nl.buildforce.olingo.server.api.ODataApplicationException;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceKind;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class JPASerializerFactory {
  private final ServiceMetadata serviceMetadata;
  private final OData odata;
  private final UriHelper uriHelper;
  private final JPAODataCRUDContextAccess serviceContext;

  public JPASerializerFactory(final OData odata, final ServiceMetadata serviceMetadata,
      final JPAODataCRUDContextAccess serviceContext) {
    this.odata = odata;
    this.serviceMetadata = serviceMetadata;
    this.uriHelper = odata.createUriHelper();
    this.serviceContext = serviceContext;
  }

  public JPASerializer createCUDSerializer(final ContentType responseFormat, final UriInfo uriInfo,
      final Optional<List<String>> responseVersion) throws SerializerException {
    final ODataSerializer serializer = odata.createSerializer(responseFormat,
        responseVersion.orElse(Collections.emptyList()));
    return new JPASerializeCreate(serviceMetadata, serializer, uriInfo, serviceContext);
  }

  public JPASerializer createSerializer(final ContentType responseFormat, final UriInfo uriInfo,
      final Optional<List<String>> responseVersion) throws ODataApplicationException, SerializerException {

    // Assumption: Type of last resource path item rules the type of the response
    final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
    final UriResource lastItem = resourceParts.get(resourceParts.size() - 1);
    final boolean isCollection = determineIsCollection(lastItem);

    return createSerializer(responseFormat, uriInfo, lastItem.getKind(), isCollection, responseVersion);
  }

  private boolean determineIsCollection(final UriResource lastItem) {
    if (lastItem instanceof UriResourcePartTyped)
      return ((UriResourcePartTyped) lastItem).isCollection();
    return false;
  }

  public ServiceMetadata getServiceMetadata() {
    return serviceMetadata;
  }

  JPASerializer createSerializer(final ContentType responseFormat, final UriInfo uriInfo, final EdmTypeKind edmTypeKind,
      final boolean isCollection, final Optional<List<String>> responseVersion) throws SerializerException,
          ODataJPASerializerException {

    final ODataSerializer serializer = odata.createSerializer(responseFormat,
        responseVersion.orElse(Collections.emptyList()));
    switch (edmTypeKind) {
      case ENTITY:
        if (isCollection)
          return new JPASerializeEntityCollection(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
        else
          return new JPASerializeEntity(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
      case COMPLEX:
        if (isCollection)
          return new JPASerializeComplexCollection(serviceMetadata, serializer, responseFormat, serviceContext);
        else
          return new JPASerializeComplex(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
      case PRIMITIVE:
        if (isCollection)
          return new JPASerializePrimitiveCollection(serviceMetadata, serializer, responseFormat, serviceContext);
        else
          return new JPASerializePrimitive(serviceMetadata, serializer, uriInfo, responseFormat, serviceContext);
      default:
        throw new ODataJPASerializerException(ODataJPASerializerException.MessageKeys.NOT_SUPPORTED_RESOURCE_TYPE,
            HttpStatusCode.NOT_IMPLEMENTED, edmTypeKind.toString());
    }
  }

  private JPASerializer createSerializerPrimitivePropertyRequest(final ContentType responseFormat,
                                                                 final UriInfo uriInfo, final Optional<List<String>> responseVersion) throws SerializerException {

    final ODataSerializer serializer = odata.createSerializer(responseFormat,
            responseVersion.orElse(Collections.emptyList()));
    return new JPASerializePrimitive(serviceMetadata, serializer, uriInfo, responseFormat, serviceContext);
  }

  JPASerializer createSerializer(final ContentType responseFormat, final UriInfo uriInfo,
      final UriResourceKind uriResourceKind, boolean isCollection, final Optional<List<String>> responseVersion)
      throws SerializerException, ODataJPASerializerException {

    return switch (uriResourceKind) {
      case entitySet, navigationProperty -> createSerializerCollectionRequest(responseFormat, uriInfo, isCollection, responseVersion);
      case complexProperty -> createSerializerComplexPropertyRequest(responseFormat, uriInfo, responseVersion);
      case primitiveProperty -> createSerializerPrimitivePropertyRequest(responseFormat, uriInfo, responseVersion);
      case action, function -> new JPASerializeFunction(uriInfo, responseFormat, this, responseVersion);
      case count -> new JPASerializeCount(odata.createFixedFormatSerializer());
      case value -> new JPASerializeValue(serviceMetadata, odata.createFixedFormatSerializer(), uriInfo);
      default -> throw new ODataJPASerializerException(ODataJPASerializerException.MessageKeys.NOT_SUPPORTED_RESOURCE_TYPE,
              HttpStatusCode.NOT_IMPLEMENTED, uriResourceKind.toString());
    };
  }

  private JPASerializer createSerializerComplexPropertyRequest(final ContentType responseFormat, final UriInfo uriInfo,
      final Optional<List<String>> responseVersion) throws SerializerException {

    final ODataSerializer serializer = odata.createSerializer(responseFormat,
        responseVersion.orElse(Collections.emptyList()));
    return new JPASerializeComplex(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
  }

  private JPASerializer createSerializerCollectionRequest(final ContentType responseFormat, final UriInfo uriInfo,
      boolean isCollection, final Optional<List<String>> responseVersion) throws SerializerException {

    final ODataSerializer serializer = odata.createSerializer(responseFormat,
        responseVersion.orElse(Collections.emptyList()));
    if (isCollection)
      return new JPASerializeEntityCollection(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
    else
      return new JPASerializeEntity(serviceMetadata, serializer, uriHelper, uriInfo, responseFormat, serviceContext);
  }

}