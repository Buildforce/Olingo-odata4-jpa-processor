/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer.batch;

import nl.buildforce.olingo.server.api.deserializer.DeserializerException;

public class BatchDeserializerException extends DeserializerException {
  public enum MessageKeys implements MessageKey {
    /** parameter: line */
    INVALID_BOUNDARY,
    /** parameter: line */
    INVALID_CHANGESET_METHOD,
    /** parameter: line */
    INVALID_CONTENT,
    /** parameter: line */
    INVALID_CONTENT_LENGTH,
    /** parameter: line */
    INVALID_CONTENT_TRANSFER_ENCODING,
    /** parameter: line */
    INVALID_CONTENT_TYPE,
    /** parameters: line, expected content type, actual content type */
    UNEXPECTED_CONTENT_TYPE,
    /** parameter: line */
    INVALID_CONTENT_ID,
    /** parameter: line */
    INVALID_HOST,
    /** parameter: line */
    INVALID_HTTP_VERSION,
    /** parameter: line */
    INVALID_METHOD,
    /** parameter: line */
    INVALID_STATUS_LINE,
    /** parameter: line */
    INVALID_URI,
    /** parameter: line */
    MISSING_BLANK_LINE,
    /** parameter: line */
    MISSING_BOUNDARY_DELIMITER,
    /** parameter: line */
    MISSING_CLOSE_DELIMITER,
    /** parameter: line */
    MISSING_CONTENT_ID,
    /** parameter: line */
    MISSING_CONTENT_TYPE,
    /** parameter: line */
    MISSING_MANDATORY_HEADER,
    /** parameter: line */
    FORBIDDEN_HEADER,
    /** parameter: line */
    INVALID_BASE_URI;

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