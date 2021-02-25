/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import java.util.regex.Pattern;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;

/**
 * Implementation of the EDM primitive type String.
 */
public final class EdmString extends SingletonPrimitiveType {

  private static final Pattern PATTERN_ASCII = Pattern.compile("\\p{ASCII}*");

  private static final EdmString INSTANCE = new EdmString();

  {
    uriPrefix = "'";
    uriSuffix = "'";
  }

  public static EdmString getInstance() {
    return INSTANCE;
  }

  @Override
  public Class<?> getDefaultType() {
    return String.class;
  }

  @Override
  protected <T> T internalValueOfString(String value,
                                        Boolean isNullable, Integer maxLength, Integer precision,
                                        Integer scale, Boolean isUnicode, Class<T> returnType) throws EdmPrimitiveTypeException {

    if (isUnicode != null && !isUnicode && !PATTERN_ASCII.matcher(value).matches()
        || maxLength != null && maxLength < value.length()) {
      throw new EdmPrimitiveTypeException("The literal '" + value + "' does not match the facets' constraints.");
    }

    if (returnType.isAssignableFrom(String.class)) {
      return returnType.cast(value);
    } else {
      throw new EdmPrimitiveTypeException("The value type " + returnType + " is not supported.");
    }
  }

  @Override
  protected <T> String internalValueToString(T value,
                                             Boolean isNullable, Integer maxLength, Integer precision,
                                             Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {

    String result = value instanceof String ? (String) value : String.valueOf(value);

    if (isUnicode != null && !isUnicode && !PATTERN_ASCII.matcher(result).matches()
        || maxLength != null && maxLength < result.length()) {
      throw new EdmPrimitiveTypeException("The value '" + value + "' does not match the facets' constraints.");
    }

    return result;
  }

  @Override
  public String toUriLiteral(String literal) {
    if (literal == null) {
      return null;
    }

    int length = literal.length();

    StringBuilder uriLiteral = new StringBuilder(length + 2);
    uriLiteral.append(uriPrefix);
    for (int i = 0; i < length; i++) {
      char c = literal.charAt(i);
      if (c == '\'') {
        uriLiteral.append(c);
      }
      uriLiteral.append(c);
    }
    uriLiteral.append(uriSuffix);
    return uriLiteral.toString();
  }

  @Override
  public String fromUriLiteral(String literal) throws EdmPrimitiveTypeException {
    return literal == null ? null : super.fromUriLiteral(literal).replace("''", "'");
  }
}
