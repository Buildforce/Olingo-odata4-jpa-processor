package nl.buildforce.sequoia.jpa.processor.core.serializer;

import nl.buildforce.sequoia.jpa.processor.core.exception.ODataJPASerializerException;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;

public interface JPAOperationSerializer extends JPASerializer {
  SerializerResult serialize(final Annotatable result, final EdmType entityType, final ODataRequest request)
      throws SerializerException, ODataJPASerializerException;
}