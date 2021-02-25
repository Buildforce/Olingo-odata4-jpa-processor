/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import java.io.InputStream;

import nl.buildforce.olingo.server.api.serializer.SerializerResult;

public class SerializerResultImpl implements SerializerResult {
  private InputStream content;

  @Override
  public InputStream getContent() {
    return content;
  }

  public static SerializerResultBuilder with() {
    return new SerializerResultBuilder();
  }

  public static class SerializerResultBuilder {
    private final SerializerResultImpl result = new SerializerResultImpl();

    public SerializerResultBuilder content(InputStream input) {
      result.content = input;
      return this;
    }

    public SerializerResult build() {
      return result;
    }
  }
}