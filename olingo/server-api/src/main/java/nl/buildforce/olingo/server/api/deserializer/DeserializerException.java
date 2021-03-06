/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.server.api.deserializer;

import nl.buildforce.olingo.server.api.ODataLibraryException;

/** Exception thrown by deserializers. */
public class DeserializerException extends ODataLibraryException {

  // private static final long serialVersionUID = 6341270437497060906L;

  /** Keys for exception texts in the resource bundle. */
  public enum MessageKeys implements MessageKey {
    NOT_IMPLEMENTED,
    IO_EXCEPTION,
    /** parameter: format */
    UNSUPPORTED_FORMAT,
    JSON_SYNTAX_EXCEPTION,
    /** parameter: propertyName */
    INVALID_NULL_PROPERTY,
    /** parameter: keyName */
    UNKNOWN_CONTENT,
    /** parameter: propertyName */
    INVALID_VALUE_FOR_PROPERTY,
    /** parameter: propertyName */
    INVALID_JSON_TYPE_FOR_PROPERTY,
    VALUE_ARRAY_NOT_PRESENT,
    VALUE_TAG_MUST_BE_AN_ARRAY,
    INVALID_ENTITY,
    /** parameter: navigationPropertyName */
    INVALID_VALUE_FOR_NAVIGATION_PROPERTY,
    DUPLICATE_PROPERTY,
    DUPLICATE_JSON_PROPERTY,
    /** parameters: primitiveTypeName, propertyName */
    UNKNOWN_PRIMITIVE_TYPE,
    /** parameter: navigationPropertyName */
    NAVIGATION_PROPERTY_NOT_FOUND,
    /** parameter: annotationName */
    INVALID_ANNOTATION_TYPE,
    /** parameter: annotationName */
    INVALID_NULL_ANNOTATION,
    /** parameter: binding link */
    INVALID_ENTITY_BINDING_LINK,
    /** parameter: action parameter name */
    INVALID_ACTION_PARAMETER_TYPE,
    /** parameter: parameterName */
    INVALID_NULL_PARAMETER;

    @Override
    public String getKey() {
      return name();
    }
  }

  /**
   * Creates deserializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public DeserializerException(String developmentMessage,
                               MessageKey messageKey, String... parameters) {
    super(developmentMessage, messageKey, parameters);
  }

  /**
   * Creates deserializer exception.
   * @param developmentMessage message text as fallback and for debugging purposes
   * @param cause the cause of this exception
   * @param messageKey one of the {@link MessageKeys} for the exception text in the resource bundle
   * @param parameters parameters for the exception text
   */
  public DeserializerException(String developmentMessage, Throwable cause,
                               MessageKey messageKey, String... parameters) {
    super(developmentMessage, cause, messageKey, parameters);
  }

  @Override
  protected String getBundleName() {
    return DEFAULT_SERVER_BUNDLE_NAME;
  }

}