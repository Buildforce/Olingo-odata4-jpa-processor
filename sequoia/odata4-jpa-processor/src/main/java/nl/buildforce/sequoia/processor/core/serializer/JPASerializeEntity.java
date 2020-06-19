package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.processor.core.query.Util;
import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.data.ContextURL;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.edm.EdmEntitySet;
import nl.buildforce.olingo.commons.api.edm.EdmEntityType;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.commons.api.http.HttpStatusCode;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.ServiceMetadata;
import nl.buildforce.olingo.server.api.serializer.EntitySerializerOptions;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriInfo;

import java.net.URISyntaxException;

final class JPASerializeEntity implements JPAOperationSerializer {
  private final ServiceMetadata serviceMetadata;
  private final UriInfo uriInfo;
  private final UriHelper uriHelper;
  private final ODataSerializer serializer;
  private final ContentType responseFormat;
  private final JPAODataCRUDContextAccess serviceContext;

  JPASerializeEntity(final ServiceMetadata serviceMetadata, final ODataSerializer serializer,
      final UriHelper uriHelper, final UriInfo uriInfo, final ContentType responseFormat,
                     final JPAODataCRUDContextAccess context) {
    this.uriInfo = uriInfo;
    this.serializer = serializer;
    this.serviceMetadata = serviceMetadata;
    this.uriHelper = uriHelper;
    this.responseFormat = responseFormat;
    this.serviceContext = context;
  }

  @Override
  public SerializerResult serialize(final ODataRequest request, final EntityCollection result)
          throws SerializerException, ODataJPASerializerException {

    final EdmEntitySet targetEdmEntitySet = Util.determineTargetEntitySet(uriInfo.getUriResourceParts());

    final EdmEntityType entityType = targetEdmEntitySet.getEntityType();

    return serialize(result, entityType, request);
  }

  @Override
  public SerializerResult serialize(final Annotatable annotatable, final EdmType entityType, final ODataRequest request)
          throws SerializerException, ODataJPASerializerException {

    final EntityCollection result = (EntityCollection) annotatable;
    final String selectList = uriHelper.buildContextURLSelectList((EdmEntityType) entityType, uriInfo.getExpandOption(),
        uriInfo.getSelectOption());
    try {
      final ContextURL contextUrl = ContextURL.with()
          .serviceRoot(buildServiceRoot(request, serviceContext))
          .type(entityType)
          .selectList(selectList)
          .build();

      final EntitySerializerOptions options = EntitySerializerOptions.with()
          .contextURL(contextUrl)
          .select(uriInfo.getSelectOption())
          .expand(uriInfo.getExpandOption())
          .build();

      return serializer.entity(serviceMetadata, (EdmEntityType) entityType, result
          .getEntities()
          .get(0),
          options);
    } catch (final URISyntaxException e) {
      throw new ODataJPASerializerException(e, HttpStatusCode.BAD_REQUEST);
    }
  }

  @Override
  public ContentType getContentType() {
    return responseFormat;
  }
}