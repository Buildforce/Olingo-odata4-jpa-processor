/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

/**
 * Thrown for invalid batch payloads.
 */
public class BatchSerializerException extends SerializerException {

  //     private static final long serialVersionUID = 2634433974342796905L;

  public enum MessageKeys implements MessageKey {
    MISSING_CONTENT_ID;

    @Override
    public String getKey() {
      return name();
    }
  }

  public BatchSerializerException(String developmentMessage, MessageKey messageKey, String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}