package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.olingo.commons.api.data.EntityCollection;
import nl.buildforce.olingo.commons.api.format.ContentType;
import nl.buildforce.olingo.server.api.ODataRequest;
import nl.buildforce.olingo.server.api.serializer.FixedFormatSerializer;
import nl.buildforce.olingo.server.api.serializer.SerializerException;
import nl.buildforce.olingo.server.api.serializer.SerializerResult;

final class JPASerializeCount implements JPASerializer {
  private final FixedFormatSerializer serializer;

  JPASerializeCount(final FixedFormatSerializer serializer) {
    this.serializer = serializer;
  }

  @Override
  public SerializerResult serialize(final ODataRequest request, final EntityCollection result) {
    return new JPAValueSerializerResult(serializer.count(result.getCount()));
  }

  @Override
  public ContentType getContentType() {
    return ContentType.TEXT_PLAIN;
  }

}