/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer.batch;

import nl.buildforce.olingo.server.api.deserializer.DeserializerException;

public class BatchDeserializerException extends DeserializerException {
  public enum MessageKeys implements MessageKey {
    /** parameter: line */
    FORBIDDEN_HEADER,
    INVALID_BASE_URI,
    INVALID_BOUNDARY,
    INVALID_CHANGESET_METHOD,
    INVALID_CONTENT,
    INVALID_CONTENT_ID,
    INVALID_CONTENT_LENGTH,
    INVALID_CONTENT_TRANSFER_ENCODING,
    INVALID_CONTENT_TYPE,
    INVALID_HOST,
    INVALID_HTTP_VERSION,
    INVALID_METHOD,
    INVALID_STATUS_LINE,
    INVALID_URI,
    MISSING_BLANK_LINE,
    MISSING_BOUNDARY_DELIMITER,
    MISSING_CLOSE_DELIMITER,
    MISSING_CONTENT_ID,
    MISSING_CONTENT_TYPE,
    // MISSING_MANDATORY_HEADER,
    /** parameters: line, expected content type, actual content type */
    UNEXPECTED_CONTENT_TYPE;

    @Override
    public String getKey() {
      return name();
    }
  }

  //     private static final long serialVersionUID = -907752788975531134L;

  /**
   * Creates batch deserializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public BatchDeserializerException(String developmentMessage, MessageKey messageKey,
                                    String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  /**
   * Creates batch deserializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param cause the cause of this exception
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public BatchDeserializerException(String developmentMessage, Throwable cause,
                                    MessageKey messageKey, String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

}