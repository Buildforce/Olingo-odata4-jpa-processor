package nl.buildforce.sequoia.processor.core.serializer;

import nl.buildforce.olingo.server.api.serializer.SerializerResult;

import java.io.InputStream;

final class JPAValueSerializerResult implements SerializerResult {
  /**
   *

   */
  private final InputStream result;

  public JPAValueSerializerResult(final InputStream inputStream) {
    this.result = inputStream;
  }

  @Override
  public InputStream getContent() {
    return result;
  }
}