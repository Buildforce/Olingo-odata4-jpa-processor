package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.commons.api.edm.constants.EdmTypeKind;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;
import nl.buildforce.olingo.server.api.uri.UriInfo;
import nl.buildforce.olingo.server.api.uri.UriResource;
import nl.buildforce.olingo.server.api.uri.UriResourceAction;
import nl.buildforce.olingo.server.api.uri.UriResourceFunction;
import nl.buildforce.olingo.server.api.uri.UriResourcePartTyped;

import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;

import java.util.List;
import java.util.Optional;

final class JPASerializeFunction implements JPAOperationSerializer {
  private final JPAOperationSerializer serializer;

  public JPASerializeFunction(final UriInfo uriInfo, final ContentType responseFormat,
                              final JPASerializerFactory jpaSerializerFactory, Optional<List<String>> responseVersion)
      throws ODataJPASerializerException, SerializerException {

    this.serializer = (JPAOperationSerializer) createSerializer(jpaSerializerFactory, responseFormat, uriInfo,
        responseVersion);
  }

  @Override
  public SerializerResult serialize(final ODataRequest request, final EntityCollection result)
      throws SerializerException, ODataJPASerializerException {
    return serializer.serialize(request, result);
  }

  @Override
  public SerializerResult serialize(final Annotatable annotatable, final EdmType entityType, final ODataRequest request)
      throws SerializerException, ODataJPASerializerException {
    return serializer.serialize(annotatable, entityType, request);
  }

/*
  JPASerializer getSerializer() {
    return serializer;
  }
*/

  private JPASerializer createSerializer(final JPASerializerFactory jpaSerializerFactory,
      final ContentType responseFormat, final UriInfo uriInfo, final Optional<List<String>> responseVersion)
      throws ODataJPASerializerException, SerializerException {

    final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
    final UriResourcePartTyped operation = (UriResourcePartTyped) resourceParts.get(resourceParts.size() - 1);
    final EdmTypeKind edmTypeKind = determineReturnEdmTypeKind(operation);
    return jpaSerializerFactory.createSerializer(responseFormat, uriInfo, edmTypeKind, operation.isCollection(),
        responseVersion);
  }

  private EdmTypeKind determineReturnEdmTypeKind(final UriResourcePartTyped operation) {
    if (operation instanceof UriResourceFunction)
      return ((UriResourceFunction) operation).getFunction().getReturnType().getType().getKind();
    else
      return ((UriResourceAction) operation).getAction().getReturnType().getType().getKind();
  }

  @Override
  public ContentType getContentType() {
    return this.serializer.getContentType();
  }

}