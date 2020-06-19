package nl.buildforce.sequoia.processor.core.serializer;

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
import nl.buildforce.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import nl.buildforce.olingo.server.api.serializer.ODataSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriHelper;
import nl.buildforce.olingo.server.api.uri.UriInfo;

import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.sequoia.processor.core.query.Util;

import java.net.URISyntaxException;

final class JPASerializeEntityCollection implements JPAOperationSerializer {
  private final ServiceMetadata serviceMetadata;
  private final UriInfo uriInfo;
  private final UriHelper uriHelper;
  private final ODataSerializer serializer;
  private final ContentType responseFormat;
  private final JPAODataCRUDContextAccess serviceContext;

  JPASerializeEntityCollection(final ServiceMetadata serviceMetadata, final ODataSerializer serializer,
      final UriHelper uriHelper, final UriInfo uriInfo, final ContentType responseFormat,
                               final JPAODataCRUDContextAccess serviceContext) {
    this.uriInfo = uriInfo;
    this.serializer = serializer;
    this.serviceMetadata = serviceMetadata;
    this.uriHelper = uriHelper;
    this.responseFormat = responseFormat;
    this.serviceContext = serviceContext;
  }

  @Override
  public SerializerResult serialize(final ODataRequest request, final EntityCollection result)
          throws SerializerException, ODataJPASerializerException {

    final EdmEntitySet targetEdmEntitySet = Util.determineTargetEntitySet(uriInfo.getUriResourceParts());

    final String selectList = uriHelper.buildContextURLSelectList(targetEdmEntitySet.getEntityType(),
        uriInfo.getExpandOption(), uriInfo.getSelectOption());

    ContextURL contextUrl;
    try {
      contextUrl = ContextURL.with()
          .serviceRoot(buildServiceRoot(request, serviceContext))
          .entitySet(targetEdmEntitySet)
          .selectList(selectList)
          .build();
    } catch (URISyntaxException e) {
      throw new ODataJPASerializerException(e, HttpStatusCode.BAD_REQUEST);
    }

    final String id = request.getRawBaseUri() + "/" + targetEdmEntitySet.getEntityType().getName();
    final EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with()
        .contextURL(contextUrl)
        .id(id)
        .count(uriInfo.getCountOption())
        .select(uriInfo.getSelectOption())
        .expand(uriInfo.getExpandOption())
        .build();

    return serializer.entityCollection(this.serviceMetadata, targetEdmEntitySet.getEntityType(), result, opts);

  }

  @Override
  public SerializerResult serialize(final Annotatable annotatable, final EdmType entityType, final ODataRequest request)
          throws SerializerException, ODataJPASerializerException {

    final EntityCollection result = (EntityCollection) annotatable;
    final String selectList = uriHelper.buildContextURLSelectList((EdmEntityType) entityType, uriInfo.getExpandOption(),
        uriInfo.getSelectOption());

    ContextURL contextUrl;
    try {
      contextUrl = ContextURL.with()
          .serviceRoot(buildServiceRoot(request, serviceContext))
          .asCollection()
          .type(entityType)
          .selectList(selectList)
          .build();
    } catch (URISyntaxException e) {
      throw new ODataJPASerializerException(e, HttpStatusCode.BAD_REQUEST);
    }

    final EntityCollectionSerializerOptions options = EntityCollectionSerializerOptions.with()
        .contextURL(contextUrl)
        .select(uriInfo.getSelectOption())
        .expand(uriInfo.getExpandOption())
        .build();

    return serializer.entityCollection(serviceMetadata, (EdmEntityType) entityType, result, options);
  }

  @Override
  public ContentType getContentType() {
    return responseFormat;
  }
}