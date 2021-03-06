/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.api.format;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Helper class which is only used within this package.
 * @see AcceptType
 * @see ContentType
 */
final class TypeUtil {

  static final String MEDIA_TYPE_WILDCARD = "*";
  static final String PARAMETER_Q = "q";

  static final char WHITESPACE_CHAR = ' ';
  static final String PARAMETER_SEPARATOR = ";";
  static final String PARAMETER_KEY_VALUE_SEPARATOR = "=";
  static final String TYPE_SUBTYPE_SEPARATOR = "/";
  // static final String TYPE_SUBTYPE_WILDCARD = "*";

  private TypeUtil() { /* static helper class */}

  /** Creates a parameter map with predictable order. */
  static TreeMap<String, String> createParameterMap() {
    return new TreeMap<>(String::compareToIgnoreCase); // was TreeMap<>((s, str) -> s.compareToIgnoreCase(str))
  }

  /**
   * Valid input are <code>;</code> separated <code>key=value</code> pairs
   * without spaces between key and value.
   * <p>
   * See RFC 7231:
   * The type, subtype, and parameter name tokens are case-insensitive.
   * Parameter values might or might not be case-sensitive, depending on
   * the semantics of the parameter name. The presence or absence of a
   * parameter might be significant to the processing of a media-type,
   * depending on its definition within the media type registry.
   * </p>
   *
   * @param parameters as <code>;</code> separated <code>key=value</code> pairs
   * @param parameterMap map to which all parsed parameters are added
   */
  static void parseParameters(String parameters, Map<String, String> parameterMap) {
    if (parameters != null) {
      for (String parameter : parameters.split(TypeUtil.PARAMETER_SEPARATOR)) {
        String[] keyValue = parseParameter(parameter);
        parameterMap.put(keyValue[0], keyValue[1]);
      }
    }
  }

  /**
   * Valid input is one <code>key=value</code> pair without spaces between key and value.
   * <p>
   * See RFC 7231:
   * The type, subtype, and parameter name tokens are case-insensitive.
   * Parameter values might or might not be case-sensitive, depending on
   * the semantics of the parameter name. The presence or absence of a
   * parameter might be significant to the processing of a media-type,
   * depending on its definition within the media type registry.
   * </p>
   *
   * @param parameter as <code>key=value</code> pair
   * @return <code>key</code> as first array value (as lower case) and <code>value</code> as second array value
   */
  static String[] parseParameter(String parameter) {
    if (parameter.isEmpty()) {
      throw new IllegalArgumentException("An empty parameter is not allowed.");
    }
    String[] keyValue = parameter.trim().toLowerCase(Locale.ENGLISH).split(PARAMETER_KEY_VALUE_SEPARATOR);
    if (keyValue.length != 2) {
      throw new IllegalArgumentException("Parameter '" + parameter + "' must have exactly one '"
          + PARAMETER_KEY_VALUE_SEPARATOR + "' that separates the name and the value.");
    }
    validateParameterNameAndValue(keyValue[0], keyValue[1]);
    return keyValue;
  }

  /**
   * Validates that parameter name and parameter value are valid:
   * <ul>
   * <li>not <code>null</code></li>
   * <li>not empty</li>
   * <li>does not contain whitespace characters (name), or does not start with whitespace (value), respectively</li>
   * </ul>
   * @param parameterName  name
   * @param parameterValue value
   * @throws IllegalArgumentException if one of the above requirements is not met
   */
  static void validateParameterNameAndValue(String parameterName, String parameterValue)
      throws IllegalArgumentException {
    if (parameterName == null || parameterName.isEmpty() || parameterName.indexOf(WHITESPACE_CHAR) >= 0) {
      throw new IllegalArgumentException("Illegal parameter name '" + parameterName + "'.");
    }
    if (parameterValue == null || parameterValue.isEmpty()) {
      throw new IllegalArgumentException("Value parameter is NULL or empty.");
    }
    if (Character.isWhitespace(parameterValue.charAt(0))) {
      throw new IllegalArgumentException("Value of parameter '" + parameterName + "' starts with whitespace.");
    }
  }

}