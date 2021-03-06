/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.serializer;

import nl.buildforce.olingo.server.api.ODataLibraryException;

/** Exception thrown by the {@link ODataSerializer}. */
public class SerializerException extends ODataLibraryException {

  //     private static final long serialVersionUID = 5358683245923127425L;

  /** Keys for exception texts in the resource bundle. */
  public enum MessageKeys implements MessageKey {
    NULL_METADATA_OR_EDM,
    NOT_IMPLEMENTED,
    /** parameter: format */
    UNSUPPORTED_FORMAT,
    //JSON_METADATA,
    IO_EXCEPTION,
    NULL_INPUT,
    NO_CONTEXT_URL,
    /** parameter: property name */
    UNSUPPORTED_PROPERTY_TYPE,
    /** parameter: property name */
    INCONSISTENT_PROPERTY_TYPE,
    /** parameter: property name */
    MISSING_PROPERTY,
    /** parameter: Delta property name */
    MISSING_DELTA_PROPERTY,
    /** parameter: - */
    MISSING_ID,
    /** parameters: property name, property value */
    WRONG_PROPERTY_VALUE,
    /** parameters: primitive-type name, value */
    WRONG_PRIMITIVE_VALUE,
    UNKNOWN_TYPE,
    WRONG_BASE_TYPE,
    UNSUPPORTED_OPERATION_TYPE,
    NULL_PROPERTY,
    /** parameter: encoding-name */
    //UNSUPPORTED_ENCODING
    ;

    @Override
    public String getKey() {
      return name();
    }
  }

  /**
   * Creates serializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public SerializerException(String developmentMessage,
                             MessageKey messageKey, String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  /**
   * Creates serializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param cause the cause of this exception
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public SerializerException(String developmentMessage, Throwable cause,
                             MessageKey messageKey, String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}