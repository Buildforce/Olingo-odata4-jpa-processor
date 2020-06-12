package nl.buildforce.sequoia.jpa.processor.core.serializer;

import org.apache.olingo.server.api.serializer.SerializerResult;

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