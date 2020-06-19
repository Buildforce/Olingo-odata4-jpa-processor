package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.data.Property;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.PrimitiveSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;

import java.net.URISyntaxException;

final class JPASerializePrimitiveCollection implements JPAOperationSerializer {
  private final ServiceMetadata serviceMetadata;
  private final ODataSerializer serializer;
  private final ContentType responseFormat;
  private final JPAODataCRUDContextAccess serviceContext;

  JPASerializePrimitiveCollection(final ServiceMetadata serviceMetadata, final ODataSerializer serializer,
      final ContentType responseFormat, final JPAODataCRUDContextAccess context) {

    this.serializer = serializer;
    this.serviceMetadata = serviceMetadata;
    this.responseFormat = responseFormat;
    this.serviceContext = context;
  }

  @Override
  public ContentType getContentType() {
    return responseFormat;
  }

  @Override
  public SerializerResult serialize(final Annotatable result, final EdmType primitiveType, final ODataRequest request)
      throws SerializerException, ODataJPASerializerException {

    try {
      final ContextURL contextUrl = ContextURL.with()
          .serviceRoot(buildServiceRoot(request, serviceContext))
          .asCollection()
          .build();
      final PrimitiveSerializerOptions options = PrimitiveSerializerOptions.with().contextURL(contextUrl).build();

      return serializer.primitiveCollection(serviceMetadata, (EdmPrimitiveType) primitiveType, (Property) result,
          options);
    } catch (final URISyntaxException e) {
      throw new ODataJPASerializerException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  @Override
  public SerializerResult serialize(final ODataRequest request, final EntityCollection result) {
    return null;
  }

}