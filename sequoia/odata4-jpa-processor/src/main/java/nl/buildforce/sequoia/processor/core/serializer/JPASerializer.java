package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.sequoia.processor.core.api.JPAODataCRUDContextAccess;
import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;

import java.net.URI;
import java.net.URISyntaxException;

public interface JPASerializer {

  SerializerResult serialize(final ODataRequest request, final EntityCollection result)
      throws SerializerException, ODataJPASerializerException;

  ContentType getContentType();

  default URI buildServiceRoot(final ODataRequest request, final JPAODataCRUDContextAccess serviceContext)
          throws URISyntaxException {
    if (serviceContext.useAbsoluteContextURL()) {
      final String serviceRoot = request.getRawBaseUri();
      if (serviceRoot == null)
        return null;
      return new URI(serviceRoot.endsWith("/") ? serviceRoot : (serviceRoot + "/"));
    }
    return null;
  }
}