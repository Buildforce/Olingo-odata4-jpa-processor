package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.sequoia.processor.core.exception.ODataJPASerializerException;
import nl.buildforce.olingo.commons.api.data.Annotatable;
import nl.buildforce.olingo.commons.api.edm.EdmType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;

public interface JPAOperationSerializer extends JPASerializer {
  SerializerResult serialize(final Annotatable result, final EdmType entityType, final ODataRequest request)
      throws SerializerException, ODataJPASerializerException;
}