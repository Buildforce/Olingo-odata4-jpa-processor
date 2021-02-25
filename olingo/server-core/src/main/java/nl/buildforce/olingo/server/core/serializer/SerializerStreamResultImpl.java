/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.core.serializer;

import nl.buildforce.olingo.server.api.ODataContent;
import nl.buildforce.olingo.server.api.serializer.SerializerStreamResult;

public class SerializerStreamResultImpl implements SerializerStreamResult {
  private ODataContent oDataContent;

  @Override
  public ODataContent getODataContent() {
    return oDataContent;
  }

  public static SerializerResultBuilder with() {
    return new SerializerResultBuilder();
  }

  public static class SerializerResultBuilder {
    private final SerializerStreamResultImpl result = new SerializerStreamResultImpl();

    public SerializerResultBuilder content(ODataContent content) {
      result.oDataContent = content;
      return this;
    }

    public SerializerStreamResult build() {
      return result;
    }
  }
}