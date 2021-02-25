/* Copyright Buildƒorce Digital i.o. 2021
 * Licensed under the EUPL-1.2-or-later
*/
package nl.buildforce.olingo.commons.core.edm.primitivetype;

import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveType;
import nl.buildforce.olingo.commons.api.edm.EdmPrimitiveTypeException;
import nl.buildforce.olingo.commons.api.edm.FullQualifiedName;

/**
 * Abstract implementation of the EDM primitive-type interface.
 */
abstract class AbstractPrimitiveType implements EdmPrimitiveType {

  protected String uriPrefix = "";

  protected String uriSuffix = "";

  @Override
  public FullQualifiedName getFullQualifiedName() {
    return new FullQualifiedName(getNamespace(), getName());
  }

  @Override
  public boolean isCompatible(EdmPrimitiveType primitiveType) {
    return equals(primitiveType);
  }

  @Override
  public boolean validate(String value,
                          Boolean isNullable, Integer maxLength, Integer precision, Integer scale,
                          Boolean isUnicode) {

    try {
      valueOfString(value, isNullable, maxLength, precision, scale, isUnicode, getDefaultType());
      return true;
    } catch (EdmPrimitiveTypeException e) {
      return false;
    }
  }

  @Override
  public final <T> T valueOfString(String value,
                                   Boolean isNullable, Integer maxLength, Integer precision,
                                   Integer scale, Boolean isUnicode, Class<T> returnType)
          throws EdmPrimitiveTypeException {

    if (value == null) {
      if (isNullable != null && !isNullable) {
        throw new EdmPrimitiveTypeException("The literal 'null' is not allowed.");
      }
      return null;
    }
    return internalValueOfString(value, isNullable, maxLength, precision, scale, isUnicode, returnType);
  }

  protected abstract <T> T internalValueOfString(String value,
      Boolean isNullable, Integer maxLength, Integer precision, Integer scale, Boolean isUnicode,
      Class<T> returnType) throws EdmPrimitiveTypeException;

  @Override
  public final String valueToString(Object value,
                                    Boolean isNullable, Integer maxLength, Integer precision,
                                    Integer scale, Boolean isUnicode) throws EdmPrimitiveTypeException {
    if (value == null) {
      if (isNullable != null && !isNullable) {
        throw new EdmPrimitiveTypeException("The value NULL is not allowed.");
      }
      return null;
    }
    return internalValueToString(value, isNullable, maxLength, precision, scale, isUnicode);
  }

  protected abstract <T> String internalValueToString(T value,
                                                      Boolean isNullable,
                                                      Integer maxLength,
                                                      Integer precision,
                                                      Integer scale,
                                                      Boolean isUnicode) throws EdmPrimitiveTypeException;

  @Override
  public String toUriLiteral(String literal) {
    return literal == null ? null :
      uriPrefix.isEmpty() && uriSuffix.isEmpty() ? literal : uriPrefix + literal + uriSuffix;
  }

  @Override
  public String fromUriLiteral(String literal) throws EdmPrimitiveTypeException {
    if (literal == null) {
      return null;
    } else if (uriPrefix.isEmpty() && uriSuffix.isEmpty()) {
      return literal;
    } else if (literal.length() >= uriPrefix.length() + uriSuffix.length()
        && literal.startsWith(uriPrefix) && literal.endsWith(uriSuffix)) {

      return literal.substring(uriPrefix.length(), literal.length() - uriSuffix.length());
    } else {
      throw new EdmPrimitiveTypeException("The literal '" + literal + "' has illegal content.");
    }
  }

  @Override
  public String toString() {
    return getFullQualifiedName().getFullQualifiedNameAsString();
  }
}
